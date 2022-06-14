package util;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class JsonUtils {
	private static Logger log = Logger.getLogger(JsonUtils.class);

	public static String listOfMapsToJsonString(List<Map<String, String>> listOfMaps) throws Exception {
		try {
			return new Gson().toJson(listOfMapsToJsonArray(listOfMaps));
		} catch (Exception e) {
			throw e;
		}
	}

	public static JsonArray listOfMapsToJsonArray(List<Map<String, String>> listOfMaps) throws Exception {
		try {
			Gson gson = new Gson();
			JsonArray jsonArray = gson.toJsonTree(listOfMaps).getAsJsonArray();
			return jsonArray;
		} catch (Exception e) {
			throw e;
		}
	}

//	public static JsonArray mapOfListOfMapsToJsonArray(Map<String, List<Map<String, String>>> map) throws Exception {
//		JsonArray retValue = new JsonArray();
//		try {
//			for (String key : map.keySet()) {
//				List<Map<String, String>> list = map.get(key);
//				Gson gson = new Gson();
//				JsonArray jsonArray = gson.toJsonTree(list).getAsJsonArray();
//				retValue.addAll(jsonArray);
//			}
//
//			return retValue;
//		} catch (Exception e) {
//			throw e;
//		}
//	}

}
