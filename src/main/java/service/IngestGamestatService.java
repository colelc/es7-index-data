package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import data.JsonData;
import data.MappedData;
import util.ConfigUtils;
import util.JsonUtils;

public class IngestGamestatService extends RawDataIngestService {
	private static Logger log = Logger.getLogger(IngestGamestatService.class);

	public static void go() throws Exception {

		try {
			JsonData.setJsonArray(new JsonArray());
			MappedData.setData(ingestRawData(ConfigUtils.getProperty("directory.raw.input"), /**/
					ConfigUtils.getProperty("files.gamestat.input"), /**/
					true));

			mapTeamConferenceKeys(MappedData.getData(), MappedData.getForESMappedData());
			List<Map<String, String>> mapped = new ArrayList<>();
			doPlayerMapping(MappedData.getForESMappedData(), mapped);

//			JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(MappedData.getForESMappedData()));
			JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(mapped));

			generateDocumentFile(ConfigUtils.getProperty("directory.gamestat.document"), ConfigUtils.getProperty("gamestat.document.json.file.name"), JsonData.getJsonArray(), 0);
		} catch (Exception e) {
			throw e;
		}
	}

}
