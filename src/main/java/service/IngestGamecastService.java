package service;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import data.JsonData;
import data.MappedData;
import util.ConfigUtils;
import util.JsonUtils;

public class IngestGamecastService extends RawDataIngestService {
	private static Logger log = Logger.getLogger(IngestGamecastService.class);

	public static void go() throws Exception {

		try {
			JsonData.setJsonArray(new JsonArray());
			MappedData.setGamecastData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), /**/
					ConfigUtils.getProperty("files.gamecast.input"), /**/
					true));

			mapTeamConferenceKeys(MappedData.getGamecastData(), MappedData.getMappedGamecastData());

			JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(MappedData.getMappedGamecastData()));

			generateDocumentFiles(ConfigUtils.getProperty("directory.gamecast.document"), ConfigUtils.getProperty("gamecast.document.json.file.name"), JsonData.getJsonArray());

		} catch (Exception e) {
			throw e;
		}
	}

//	private static void mapKeys() throws Exception {
//
//		try {
//			for (Map<String, String> map : MappedData.getGamecastData()) {
//				// log.info("----------------------------------------------------------------------------------------------");
//				Map<String, String> lookupMap = valueLookup(map, MappedData.getConferenceData(), "homeTeamConferenceId");
//				if (lookupMap != null) {
//					map.put("homeTeamConferenceShortName", lookupMap.get("shortName"));
//					map.put("homeTeamConferenceLongName", lookupMap.get("longName"));
//				} else {
//					// log.warn(map.get("_source") + " -> Cannot locate home team conference map
//					// for: ");
//					// map.forEach((k, v) -> log.info(k + " -> " + v));
//					// log.info(map.get("homeTeamConferenceId"));
//					map.put("homeTeamConferenceShortName", "");
//					map.put("homeTeamConferenceLongName", "");
//				}
//
//				lookupMap = valueLookup(map, MappedData.getConferenceData(), "roadTeamConferenceId");
//				if (lookupMap != null) {
//					map.put("roadTeamConferenceShortName", lookupMap.get("shortName"));
//					map.put("roadTeamConferenceLongName", lookupMap.get("longName"));
//				} else {
//					// log.warn(map.get("_source") + " -> Cannot locate road team conference map
//					// for: ");
//					// map.forEach((k, v) -> log.info(k + " -> " + v));
//					// log.info(map.get("roadTeamConferenceId"));
//					map.put("roadTeamConferenceShortName", "");
//					map.put("roadTeamConferenceLongName", "");
//				}
//
//				lookupMap = valueLookup(map, MappedData.getTeamData(), "homeTeamId");
//				if (lookupMap != null) {
//					map.put("homeTeamName", lookupMap.get("teamName"));
//				} else {
//					// log.warn(map.get("_source") + " -> Cannot locate home team map for: ");
//					// map.forEach((k, v) -> log.info(k + " -> " + v));
//					// log.info(map.get("homeTeamId"));
//					map.put("homeTeamName", "");
//				}
//
//				lookupMap = valueLookup(map, MappedData.getTeamData(), "roadTeamId");
//				if (lookupMap != null) {
//					map.put("roadTeamName", lookupMap.get("teamName"));
//				} else {
//					// log.warn(map.get("_source") + " -> Cannot locate road team map for: ");
//					// map.forEach((k, v) -> log.info(k + " -> " + v));
//					// log.info(map.get("roadTeamId"));
//					map.put("roadTeamName", "");
//				}
//
//				MappedData.getMappedGamecastData().add(map);
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//	}

}
