package service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import data.MappedData;
import util.ConfigUtils;
import util.FileUtils;

public class RawDataIngestService {
	private static Logger log = Logger.getLogger(RawDataIngestService.class);
	private static int ix = 0;
	private static int numFiles = 0;
	private static int numRecordsInFile = 0;
	private static int totalRecords = 0;

	static {
		try {
			MappedData.setConferenceData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), ConfigUtils.getProperty("files.conference.input"), false));
			MappedData.setTeamData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), ConfigUtils.getProperty("files.team.input"), false));
			MappedData.setPlayerData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), ConfigUtils.getProperty("files.player.input"), false));
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			System.exit(99);
		}
	}

	protected static List<Map<String, String>> ingestRawData(String directory, String targetFileName, boolean includeGameday) throws Exception {

		log.info("PROCESSING FOR " + directory + " -> " + targetFileName + "*");
		List<Map<String, String>> data = new ArrayList<>();
		try {
			Set<String> files = FileUtils.getFileListFromDirectory(directory, targetFileName);

			files.forEach(file -> {
				try {
					log.info("Raw data ingest: " + directory + File.separator + file);
					Reader in = new FileReader(directory + File.separator + file);
					Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

					++numFiles;
					numRecordsInFile = 0;

					for (CSVRecord record : records) {
						Map<String, String> map = new HashMap<>(processCSVRecord(record, includeGameday, file));

						// map.forEach((k, v) -> log.info(k + " -> " + v));
						// log.info(map.toString());
						data.add(map);
						numRecordsInFile++;
					}

					totalRecords += numRecordsInFile;
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return;
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return;
				}
			});
		} catch (Exception e) {
			throw e;
		}

		log.info("PROCESSING FOR " + directory + " -> " + targetFileName + "*");
		log.info("File Count: " + String.valueOf(numFiles) /**/
				+ " -> Total Records: " + String.valueOf(totalRecords));

		return data;
	}

	protected static Map<String, String> processCSVRecord(CSVRecord record, boolean includeGameday, String file) throws Exception {

		Map<String, String> map = new HashMap<>();
		try {
			String curr = null;

			map.put("sourceFile", file);
			if (includeGameday) {
				map.put("gameDay", file.substring(file.lastIndexOf("_") + 1));
			}

			for (String kv : record) {
				if (kv.contains("[")) {
					String[] tokens = kv.replace("[", "").replace("]", "").split("=");
					if (tokens.length == 2) {
						map.put(tokens[0], tokens[1].replace("%", "").replace("--", "0"));
						curr = tokens[0];
					} else {
						map.put(tokens[0], "");
					}
				} else {
					String value = map.get(curr);
					map.put(curr, value + " | " + kv.trim());
				}
			}
		} catch (Exception e) {
			throw e;
		}

		return map;
	}

	protected static void mapTeamConferenceKeys(List<Map<String, String>> dataIn, List<Map<String, String>> dataOut) throws Exception {

		try {
			for (Map<String, String> map : dataIn) {
				doMapping(map);
				dataOut.add(map);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected static void doMapping(Map<String, String> map) throws Exception {

		try {
			Map<String, String> lookupMap = valueLookup(map, MappedData.getConferenceData(), "homeTeamConferenceId");

			if (lookupMap != null) {
				map.put("homeTeamConferenceShortName", lookupMap.get("shortName"));
				map.put("homeTeamConferenceLongName", lookupMap.get("longName"));
			} else {
				// log.warn(map.get("_source") + " -> Cannot locate home team conference map
				// for: ");
				// map.forEach((k, v) -> log.info(k + " -> " + v));
				// log.info(map.get("homeTeamConferenceId"));
				map.put("homeTeamConferenceShortName", "");
				map.put("homeTeamConferenceLongName", "");
			}

			lookupMap = valueLookup(map, MappedData.getConferenceData(), "roadTeamConferenceId");
			if (lookupMap != null) {
				map.put("roadTeamConferenceShortName", lookupMap.get("shortName"));
				map.put("roadTeamConferenceLongName", lookupMap.get("longName"));
			} else {
				// log.warn(map.get("_source") + " -> Cannot locate road team conference map
				// for: ");
				// map.forEach((k, v) -> log.info(k + " -> " + v));
				// log.info(map.get("roadTeamConferenceId"));
				map.put("roadTeamConferenceShortName", "");
				map.put("roadTeamConferenceLongName", "");
			}

			lookupMap = valueLookup(map, MappedData.getTeamData(), "homeTeamId");
			if (lookupMap != null) {
				map.put("homeTeamName", lookupMap.get("teamName"));
			} else {
				// log.warn(map.get("_source") + " -> Cannot locate home team map for: ");
				// map.forEach((k, v) -> log.info(k + " -> " + v));
				// log.info(map.get("homeTeamId"));
				map.put("homeTeamName", "");
			}

			lookupMap = valueLookup(map, MappedData.getTeamData(), "roadTeamId");
			if (lookupMap != null) {
				map.put("roadTeamName", lookupMap.get("teamName"));
			} else {
				// log.warn(map.get("_source") + " -> Cannot locate road team map for: ");
				// map.forEach((k, v) -> log.info(k + " -> " + v));
				// log.info(map.get("roadTeamId"));
				map.put("roadTeamName", "");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected static void mapPlayerKeys() throws Exception {

		Map<String, List<Map<String, String>>> data = new HashMap<>();

		try {
			for (Iterator<String> it = MappedData.getForESDoubleMappedData().keySet().iterator(); it.hasNext();) {
				String key = it.next();
				List<Map<String, String>> submaps = MappedData.getForESDoubleMappedData().get(key);
				List<Map<String, String>> mapped = new ArrayList<>();
				doPlayerMapping(submaps, mapped);
				data.put(key, mapped);
			}

			MappedData.getForESDoubleMappedData().clear();
			MappedData.getForESDoubleMappedData().putAll(data);
		} catch (Exception e) {
			throw e;
		}
	}

	protected static void doPlayerMapping(List<Map<String, String>> submaps, List<Map<String, String>> mapped) throws Exception {

		try {
			List<Map<String, String>> playerMap = MappedData.getPlayerData();
			for (Iterator<Map<String, String>> itx = submaps.iterator(); itx.hasNext();) {
				Map<String, String> map = itx.next();

				Map<String, String> lookupMap = valueLookup(map, playerMap, "playerId");
				if (lookupMap != null) {
					populate(map, lookupMap);
				} else {
					init(map);
				}
				mapped.add(map);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected static void populate(Map<String, String> map, Map<String, String> lookupMap) {
		map.put("playerName", lookupMap.get("playerName"));
		map.put("playerFirstName", lookupMap.get("playerFirstName"));
		map.put("playerLastName", lookupMap.get("playerLastName"));
//		map.put("uniformNumber", lookupMap.get("uniformNumber"));
//		map.put("playerUrl", lookupMap.get("playerUrl"));
//		map.put("heightCm", lookupMap.get("heightCm"));
//		map.put("homeState", lookupMap.get("homeState"));
//		map.put("heightInches", lookupMap.get("heightInches"));
//		map.put("homeCity", lookupMap.get("homeCity"));
//		map.put("position", lookupMap.get("position"));
//		map.put("heightFeet", lookupMap.get("heightFeet"));
//		map.put("classYear", lookupMap.get("classYear"));
	}

	protected static void init(Map<String, String> map) {
		map.put("playerName", "");
		map.put("playerFirstName", "");
		map.put("playerLastName", "");
//		map.put("uniformNumber", "");
//		map.put("playerUrl", "");
//		map.put("heightCm", "");
//		map.put("homeState", "");
//		map.put("heightInches", "");
//		map.put("homeCity", "");
//		map.put("position", "");
//		map.put("heightFeet", "");
//		map.put("classYear", "");
	}

	protected static void generateDocumentFiles(String directory, String fileName, JsonArray json) throws Exception {

		ix = 0;

		json.forEach(jsonElement -> {
			try (PrintWriter out = new PrintWriter(new FileWriter(directory + File.separator + fileName + String.valueOf(ix++) + ".json"))) {
				if (jsonElement.isJsonObject()) {
					out.write(jsonElement.getAsJsonObject().toString());
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				System.exit(99);
			}

		});

	}

	protected static void generateDocumentFile(String directory, String fileName, JsonArray json, int index) throws Exception {

		try (PrintWriter out = new PrintWriter(new FileWriter(directory + File.separator + fileName + "_" + String.valueOf(index) + ".json"))) {

			json.forEach(jsonElement -> {
				if (jsonElement.isJsonObject()) {
					out.write(jsonElement.getAsJsonObject().toString() + "\n");
				}

			});
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			System.exit(99);
		}

	}

	protected static Map<String, String> valueLookup(Map<String, String> inMap, List<Map<String, String>> lookupList, String lookupKey) throws Exception {

		if (lookupKey == null || lookupKey.trim().length() == 0) {
			log.warn("No lookupKey is available for " + inMap.toString());
		}

		try {
			String id = inMap.get(lookupKey);
			Optional<Map<String, String>> opt = lookupList.stream().filter(f -> f.values().contains(id)).findFirst();
			if (opt.isPresent()) {
				return opt.get();
			}
			return null;

		} catch (Exception e) {
			throw e;
		}

	}

	protected static String getRandomNumberInRangeAsString(int min, int max) {
		Random r = new Random();
		int someInt = r.nextInt((max - min) + 1) + min;
		return String.valueOf(someInt);
	}

}
