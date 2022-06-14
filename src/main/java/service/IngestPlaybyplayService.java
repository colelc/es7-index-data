package service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import data.JsonData;
import data.MappedData;
import util.ConfigUtils;
import util.FileUtils;
import util.JsonUtils;

public class IngestPlaybyplayService extends RawDataIngestService {
	private static Logger log = Logger.getLogger(IngestGamecastService.class);

	public static void go() throws Exception {

		try {
			JsonData.setJsonArray(new JsonArray());
			MappedData.setPlaybyplayData(ingestData(ConfigUtils.getProperty("directory.raw.input"), /**/
					ConfigUtils.getProperty("files.playbyplay.input"), /**/
					true));

			mapTeamConferenceKeysForPlaybyplay(MappedData.getPlaybyplayData(), MappedData.getMappedPlaybyplayData());
			mapPlayerKeysForPlaybyplay();

			int ix = 0;
			for (String key : MappedData.getMappedPlaybyplayData().keySet()) {
				List<Map<String, String>> data = MappedData.getMappedPlaybyplayData().get(key);
				JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(data));
				generateDocumentFile(ConfigUtils.getProperty("directory.playbyplay.document"), /**/
						ConfigUtils.getProperty("playbyplay.document.json.file.name"), /**/
						JsonData.getJsonArray(), /**/
						ix++);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected static Map<String, List<Map<String, String>>> ingestData(String directory, String targetFileName, boolean includeGameday) throws Exception {

		log.info("PROCESSING FOR " + directory + " -> " + targetFileName + "*");
		Map<String, List<Map<String, String>>> data = new HashMap<>();

		try {
			Set<String> files = FileUtils.getFileListFromDirectory(directory, targetFileName);

			files.forEach(file -> {
				try {
					// log.info("Raw data ingest: " + directory + File.separator + file);
					Reader in = new FileReader(directory + File.separator + file);
					Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

					for (CSVRecord record : records) {
						String curr = null;
						Map<String, String> map = new HashMap<>();
						map.put("sourceFile", file);
						if (includeGameday) {
							map.put("gameDay", file.substring(file.lastIndexOf("_") + 1));
						}

						for (String kv : record) {
							if (kv.contains("[")) {
								String[] tokens = kv.replace("[", "").replace("]", "").split("=");
								if (tokens.length == 2) {
									map.put(tokens[0], tokens[1].replace("%", ""));
									curr = tokens[0];
								} else {
									map.put(tokens[0], "");
								}
							} else {
								String value = map.get(curr);
								map.put(curr, value + " | " + kv.trim());
							}
						}

						// map.forEach((k, v) -> log.info(k + " -> " + v));
						// log.info(map.toString());

						String playerId = map.get("playerId");
						if (playerId == null) {
							log.warn("no value for player id ... skipping");
							continue;
						}

						String ix = playerId.substring(playerId.length() - 2);

						List<Map<String, String>> sublist = data.get(ix);
						if (sublist == null) {
							sublist = new ArrayList<>();
							sublist.add(map);
							data.put(ix, sublist);
							// log.info(ix + " -> " + playerId);
						} else {
							sublist.add(map);
						}
					}
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					return;
				}
			});
		} catch (Exception e) {
			throw e;
		}

//		int total = 0;
//		for (String key : data.keySet()) {
//			List<Map<String, String>> tmp = data.get(key);
//			log.info(key + " has " + tmp.size());
//			total += tmp.size();
//		}
//		log.info("Grand total: " + total);
		return data;
	}

	protected static void mapTeamConferenceKeysForPlaybyplay(Map<String, List<Map<String, String>>> dataIn, Map<String, List<Map<String, String>>> dataOut) throws Exception {

		try {
			for (String key : dataIn.keySet()) {
				List<Map<String, String>> submap = dataIn.get(key);
				for (Map<String, String> map : submap) {
					// log.info("----------------------------------------------------------------------------------------------");
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

					List<Map<String, String>> sublist = dataOut.get(key);
					if (sublist == null) {
						sublist = new ArrayList<>();
						sublist.add(map);
						dataOut.put(key, sublist);
					} else {
						sublist.add(map);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		int total = 0;
		for (String key : dataOut.keySet()) {
			List<Map<String, String>> tmp = dataOut.get(key);
			// log.info(key + " has " + tmp.size());
			total += tmp.size();
		}
		log.info("Grand total: " + total);

	}

//	protected static void generateDocumentFiles(String directory, String fileName, JsonArray json) throws Exception {
//
//		int ix = 0;
//
//		json.forEach(jsonElement -> {
//			try (PrintWriter out = new PrintWriter(new FileWriter(directory + File.separator + fileName + String.valueOf(ix++) + ".json"))) {
//				if (jsonElement.isJsonObject()) {
//					out.write(jsonElement.getAsJsonObject().toString());
//				}
//			} catch (Exception e) {
//				log.error(e.getMessage());
//				e.printStackTrace();
//				System.exit(99);
//			}
//
//		});
//
//	}

	private static void mapPlayerKeysForPlaybyplay() throws Exception {
		int total = 0;
		Map<String, List<Map<String, String>>> data = new HashMap<>();

		try {
			for (Iterator<String> it = MappedData.getMappedPlaybyplayData().keySet().iterator(); it.hasNext();) {
				String key = it.next();
				List<Map<String, String>> submaps = MappedData.getMappedPlaybyplayData().get(key);

				for (Iterator<Map<String, String>> itx = submaps.iterator(); itx.hasNext();) {
					// for (Map<String, String> map : submaps) {
					Map<String, String> map = itx.next();
					++total;
					if (total % 10000 == 0) {
						log.info("Processed so far: " + total);
					}

					Map<String, String> lookupMap = valueLookup(map, MappedData.getPlayerData(), "playerId");
					if (lookupMap != null) {
						populate(map, lookupMap);
					} else {
						init(map);
					}
					// MappedData.getMappedPlaybyplayData().get(key).add(map);
					List<Map<String, String>> sublist = data.get(key);
					if (sublist == null) {
						sublist = new ArrayList<>();
						sublist.add(map);
						data.put(key, sublist);
					} else {
						sublist.add(map);
					}

				}
			}

			MappedData.getMappedPlaybyplayData().clear();
			MappedData.getMappedPlaybyplayData().putAll(data);
		} catch (Exception e) {
			throw e;
		}
	}

	private static void populate(Map<String, String> map, Map<String, String> lookupMap) {
		map.put("playerName", lookupMap.get("playerName"));
		map.put("playerFirstName", lookupMap.get("playerFirstName"));
		map.put("playerLastName", lookupMap.get("playerLastName"));
		map.put("uniformNumber", lookupMap.get("uniformNumber"));
		map.put("playerUrl", lookupMap.get("playerUrl"));
		map.put("heightCm", lookupMap.get("heightCm"));
		map.put("homeState", lookupMap.get("homeState"));
		map.put("heightInches", lookupMap.get("heightInches"));
		map.put("homeCity", lookupMap.get("homeCity"));
		map.put("position", lookupMap.get("position"));
		map.put("heightFeet", lookupMap.get("heightFeet"));
		map.put("classYear", lookupMap.get("classYear"));
	}

	private static void init(Map<String, String> map) {
		map.put("playerName", "");
		map.put("playerFirstName", "");
		map.put("playerLastName", "");
		map.put("uniformNumber", "");
		map.put("playerUrl", "");
		map.put("heightCm", "");
		map.put("homeState", "");
		map.put("heightInches", "");
		map.put("homeCity", "");
		map.put("position", "");
		map.put("heightFeet", "");
		map.put("classYear", "");
	}

}
