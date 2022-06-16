package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedData {

	private static List<Map<String, String>> conferenceData;
	private static List<Map<String, String>> teamData;
	private static List<Map<String, String>> playerData;

	private static List<Map<String, String>> data;
	private static Map<String, List<Map<String, String>>> doubleMappedData;

	private static List<Map<String, String>> forESMappedData; // to ES
	private static Map<String, List<Map<String, String>>> forESDoubleMappedData; // to ES

	public static List<Map<String, String>> getData() {
		if (data == null) {
			data = new ArrayList<>();
		}
		return data;
	}

	public static void setData(List<Map<String, String>> data) {
		MappedData.data = data;
	}

	public static List<Map<String, String>> getConferenceData() {
		if (conferenceData == null) {
			conferenceData = new ArrayList<>();
		}
		return conferenceData;
	}

	public static void setConferenceData(List<Map<String, String>> conferenceData) {
		MappedData.conferenceData = conferenceData;
	}

	public static List<Map<String, String>> getTeamData() {
		if (teamData == null) {
			teamData = new ArrayList<>();
		}
		return teamData;
	}

	public static void setTeamData(List<Map<String, String>> teamData) {
		MappedData.teamData = teamData;
	}

	public static List<Map<String, String>> getForESMappedData() {
		if (forESMappedData == null) {
			forESMappedData = new ArrayList<>();
		}
		return forESMappedData;
	}

	public static void setForESMappedData(List<Map<String, String>> forESMappedData) {
		MappedData.forESMappedData = forESMappedData;
	}

	public static List<Map<String, String>> getPlayerData() {
		if (playerData == null) {
			playerData = new ArrayList<>();
		}
		return playerData;
	}

	public static void setPlayerData(List<Map<String, String>> playerData) {
		MappedData.playerData = playerData;
	}

	public static Map<String, List<Map<String, String>>> getDoubleMappedData() {
		if (doubleMappedData == null) {
			doubleMappedData = new HashMap<String, List<Map<String, String>>>();
		}
		return doubleMappedData;
	}

	public static void setDoubleMappedData(Map<String, List<Map<String, String>>> doubleMappedData) {
		MappedData.doubleMappedData = doubleMappedData;
	}

	public static Map<String, List<Map<String, String>>> getForESDoubleMappedData() {
		if (forESDoubleMappedData == null) {
			forESDoubleMappedData = new HashMap<String, List<Map<String, String>>>();
		}
		return forESDoubleMappedData;
	}

	public static void setForESDoubleMappedData(Map<String, List<Map<String, String>>> forESDoubleMappedData) {
		MappedData.forESDoubleMappedData = forESDoubleMappedData;
	}

}
