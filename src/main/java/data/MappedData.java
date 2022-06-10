package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MappedData {

	private static List<Map<String, String>> gamecastData;
	private static List<Map<String, String>> conferenceData;
	private static List<Map<String, String>> teamData;

	private static List<Map<String, String>> mappedGamecastData;

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

}
