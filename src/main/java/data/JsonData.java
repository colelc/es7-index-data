package data;

import com.google.gson.JsonArray;

public class JsonData {
	// private static Map<String, JsonArray> playByPlayMapOfJsonArray;
	private static JsonArray jsonArray;

	public static JsonArray getJsonArray() {
		return jsonArray;
	}

	public static void setJsonArray(JsonArray jsonArray) {
		JsonData.jsonArray = jsonArray;
	}

//	public static Map<String, JsonArray> getPlayByPlayMapOfJsonArray() {
//		if (playByPlayMapOfJsonArray == null) {
//			playByPlayMapOfJsonArray = new HashMap<>();
//		}
//		return playByPlayMapOfJsonArray;
//	}
//
//	public static void setPlayByPlayMapOfJsonArray(Map<String, JsonArray> playByPlayMapOfJsonArray) {
//		JsonData.playByPlayMapOfJsonArray = playByPlayMapOfJsonArray;
//	}

}
