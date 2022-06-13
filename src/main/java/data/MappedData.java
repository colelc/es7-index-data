package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedData {

	private static List<Map<String, String>> gamecastData;
	private static Map<String, List<Map<String, String>>> playbyplayData;

	private static List<Map<String, String>> conferenceData;
	private static List<Map<String, String>> teamData;
	private static List<Map<String, String>> playerData;

	private static List<Map<String, String>> mappedGamecastData; // to ES
	private static Map<String, List<Map<String, String>>> mappedPlaybyplayData; // to ES

	public static List<Map<String, String>> getGamecastData() {
		if (gamecastData == null) {
			gamecastData = new ArrayList<>();
		}
		return gamecastData;
	}

	public static void setGamecastData(List<Map<String, String>> gamecastData) {
		MappedData.gamecastData = gamecastData;
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

	public static List<Map<String, String>> getMappedGamecastData() {
		if (mappedGamecastData == null) {
			mappedGamecastData = new ArrayList<>();
		}
		return mappedGamecastData;
	}

	public static void setMappedGamecastData(List<Map<String, String>> mappedGamecastData) {
		MappedData.mappedGamecastData = mappedGamecastData;
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

	public static Map<String, List<Map<String, String>>> getPlaybyplayData() {
		if (playbyplayData == null) {
			// List<Map<String, String>> temp = new ArrayList<>();
			playbyplayData = new HashMap<String, List<Map<String, String>>>();
		}
		return playbyplayData;
	}

	public static void setPlaybyplayData(Map<String, List<Map<String, String>>> playbyplayData) {
		MappedData.playbyplayData = playbyplayData;
	}

	public static Map<String, List<Map<String, String>>> getMappedPlaybyplayData() {
		if (mappedPlaybyplayData == null) {
			mappedPlaybyplayData = new HashMap<String, List<Map<String, String>>>();
		}
		return mappedPlaybyplayData;
	}

	public static void setMappedPlaybyplayData(Map<String, List<Map<String, String>>> mappedPlaybyplayData) {
		MappedData.mappedPlaybyplayData = mappedPlaybyplayData;
	}

}
