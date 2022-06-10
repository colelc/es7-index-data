package data;

import com.google.gson.JsonArray;

public class JsonData {
	private static String jsonArrayAsString;
	private static JsonArray jsonArray;

	public static String getJsonArrayAsString() {
		return jsonArrayAsString;
	}

	public static void setJsonArrayAsString(String jsonArrayAsString) {
		JsonData.jsonArrayAsString = jsonArrayAsString;
	}

	public static JsonArray getJsonArray() {
		return jsonArray;
	}

	public static void setJsonArray(JsonArray jsonArray) {
		JsonData.jsonArray = jsonArray;
	}
}
