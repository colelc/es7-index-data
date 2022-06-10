package service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import data.JsonData;
import data.MappedData;
import util.ConfigUtils;
import util.FileUtils;
import util.JsonUtils;

public class RawDataIngestService {
	private static Logger log = Logger.getLogger(RawDataIngestService.class);
	private static int numFiles = 0;
	private static int numRecordsInFile = 0;
	private static int totalRecords = 0;
	private static int totalMisses = 0;

	public static void go() throws Exception {

		try (PrintWriter out = new PrintWriter(new FileWriter(ConfigUtils.getGamecastJsonOutputFile()))) {
			MappedData.setGamecastData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), ConfigUtils.getProperty("files.gamecast.input")));
			MappedData.setConferenceData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), ConfigUtils.getProperty("files.conference.input")));
			MappedData.setTeamData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), ConfigUtils.getProperty("files.team.input")));
			mapKeys();

			log.info("Could not map: " + String.valueOf(totalMisses));
			JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(MappedData.getMappedGamecastData()));

			JsonData.getJsonArray().forEach(jsonElement -> {
				if (jsonElement.isJsonObject()) {
					out.write(jsonElement.getAsJsonObject().toString() + "\n");
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	private static void mapKeys() throws Exception {

		try {
			for (Map<String, String> map : MappedData.getGamecastData()) {
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
					++totalMisses;
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
					++totalMisses;
				}

				lookupMap = valueLookup(map, MappedData.getTeamData(), "homeTeamId");
				if (lookupMap != null) {
					map.put("homeTeamName", lookupMap.get("teamName"));
				} else {
					// log.warn(map.get("_source") + " -> Cannot locate home team map for: ");
					// map.forEach((k, v) -> log.info(k + " -> " + v));
					// log.info(map.get("homeTeamId"));
					map.put("homeTeamName", "");
					++totalMisses;
				}

				lookupMap = valueLookup(map, MappedData.getTeamData(), "roadTeamId");
				if (lookupMap != null) {
					map.put("roadTeamName", lookupMap.get("teamName"));
				} else {
					// log.warn(map.get("_source") + " -> Cannot locate road team map for: ");
					// map.forEach((k, v) -> log.info(k + " -> " + v));
					// log.info(map.get("roadTeamId"));
					map.put("roadTeamName", "");
					++totalMisses;
				}

				MappedData.getMappedGamecastData().add(map);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private static Map<String, String> valueLookup(Map<String, String> inMap, List<Map<String, String>> lookupList, String lookupKey) throws Exception {

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

	private static List<Map<String, String>> ingestRawData(String directory, String targetFileName) throws Exception {

		log.info("PROCESSING FOR " + directory + " -> " + targetFileName + "*");
		List<Map<String, String>> data = new ArrayList<>();
		try {
			Set<String> files = FileUtils.getFileListFromDirectory(directory, targetFileName);

			numFiles = 0;
			numRecordsInFile = 0;
			totalRecords = 0;

			files.forEach(file -> {
				try {
					// log.info("Raw data ingest: " + directory + File.separator + file);
					Reader in = new FileReader(directory + File.separator + file);
					Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
					++numFiles;

					numRecordsInFile = 0;
					for (CSVRecord record : records) {
						String curr = null;
						Map<String, String> map = new HashMap<>();
						map.put("sourceFile", file);
						map.put("gameDay", file.substring(file.lastIndexOf("_") + 1));

						for (String kv : record) {
							if (kv.contains("[")) {
								String[] tokens = kv.replace("[", "").replace("]", "").split("=");
								if (tokens.length == 2) {
									map.put(tokens[0], tokens[1]);
									curr = tokens[0];
								} else {
									map.put(tokens[0], "");
								}
							} else {
								String value = map.get(curr);
								map.put(curr, value + "|" + kv.trim());
							}
						}

						// map.forEach((k, v) -> log.info(k + " -> " + v));
						data.add(map);
						numRecordsInFile++;
					}

					totalRecords += numRecordsInFile;
//					log.info("File Count: " + String.valueOf(numFiles) /**/
//							+ "  Record count: " + String.valueOf(numRecordsInFile) /**/
//							+ " -> Total Records: " + String.valueOf(totalRecords));
				} catch (IOException e) {
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
}
