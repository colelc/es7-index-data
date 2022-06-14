package service;

import org.apache.log4j.Logger;

import data.JsonData;
import data.MappedData;
import util.ConfigUtils;
import util.JsonUtils;

public class IngestPlayerService extends RawDataIngestService {
	private static Logger log = Logger.getLogger(IngestPlayerService.class);

	public static void go() throws Exception {

		try {
			JsonData.setJsonArray(JsonUtils.listOfMapsToJsonArray(MappedData.getPlayerData()));

			generateDocumentFile(ConfigUtils.getProperty("directory.player.document"), ConfigUtils.getProperty("player.document.json.file.name"), JsonData.getJsonArray(), 0);
		} catch (Exception e) {
			throw e;
		}
	}

}
