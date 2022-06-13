package data;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;

public class JsonData {
	private static Map<String, JsonArray> playByPlayMapOfJsonArray;
	private static JsonArray gamecastJsonArray;

	public static JsonArray getGamecastJsonArray() {
		return gamecastJsonArray;
	}

	public static void setGamecastJsonArray(JsonArray gamecastJsonArray) {
		JsonData.gamecastJsonArray = gamecastJsonArray;
	}

	public static Map<String, JsonArray> getPlayByPlayMapOfJsonArray() {
		if (playByPlayMapOfJsonArray == null) {
			playByPlayMapOfJsonArray = new HashMap<>();
		}
		return playByPlayMapOfJsonArray;
	}

	public static void setPlayByPlayMapOfJsonArray(Map<String, JsonArray> playByPlayMapOfJsonArray) {
		JsonData.playByPlayMapOfJsonArray = playByPlayMapOfJsonArray;
	}

}
