package service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
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

	public static void go(String run) throws Exception {
		if (!ConfigUtils.execute(run, "IngestPlaybyplayService")) {
			return;
		}

		try {
			JsonData.setJsonArray(new JsonArray());

			MappedData.setDoubleMappedData(ingestData(ConfigUtils.getProperty("directory.raw.input"), /**/
					ConfigUtils.getProperty("files.playbyplay.input"), /**/
					true));

			mapTeamConferenceKeysForPlaybyplay(MappedData.getDoubleMappedData(), MappedData.getForESDoubleMappedData());
			mapPlayerKeys();

			int ix = 0;
			for (String key : MappedData.getForESDoubleMappedData().keySet()) {
				List<Map<String, String>> data = MappedData.getForESDoubleMappedData().get(key);
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
						Map<String, String> map = new HashMap<>(processCSVRecord(record, includeGameday, file));

						// map.forEach((k, v) -> log.info(k + " -> " + v));
						// log.info(map.toString());

						String playerId = map.get("playerId");
						if (playerId == null) {
							log.warn("no value for player id ... skipping");
							continue;
						}

						String ix = playerId.substring(playerId.length() - 2);
						// String ix = getRandomNumberInRangeAsString(0, 14);

						List<Map<String, String>> sublist = data.get(ix);
						if (sublist == null) {
							sublist = new ArrayList<>();
							sublist.add(map);
							data.put(ix, sublist);
							log.info(ix + " -> " + playerId);
						} else {
							sublist.add(map);
						}
					}
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
					doMapping(map);

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

}
