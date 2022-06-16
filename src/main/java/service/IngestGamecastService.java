package service;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import data.JsonData;
import data.MappedData;
import util.ConfigUtils;
import util.JsonUtils;

public class IngestGamecastService extends RawDataIngestService {
	private static Logger log = Logger.getLogger(IngestGamecastService.class);

	public static void go(String run) throws Exception {
		if (!ConfigUtils.execute(run, "IngestGamecastService")) {
			return;
		}

		try {
			JsonData.setJsonArray(new JsonArray());
			MappedData.setData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), /**/
					ConfigUtils.getProperty("files.gamecast.input"), /**/
					true));

			mapTeamConferenceKeys(MappedData.getData(), MappedData.getForESMappedData());

			JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(MappedData.getForESMappedData()));

			generateDocumentFile(ConfigUtils.getProperty("directory.gamecast.document"), ConfigUtils.getProperty("gamecast.document.json.file.name"), JsonData.getJsonArray(), 0);

		} catch (Exception e) {
			throw e;
		}
	}

}
