package vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Playbyplay {
	private String gameDay;
	private String gameId;
	private Integer homeTeamConferenceId;
	private String homeTeamConferenceLongName;
	private String homeTeamConferenceShortName;
	private String id;
	private String play;
	private String playerFirstName;
	private String playerLastName;
	private String playerName;
	private Integer roadTeamConferenceId;
	private String roadTeamConferenceLongName;
	private String roadTeamConferenceShortName;
	private Integer roadTeamId;
	private Integer seconds;
	private String sourceFile;
	private Integer homeTeamId;
	private String homeTeamName;
	private String playerId;
	private String roadTeamName;

	public String getGameDay() {
		return gameDay;
	}

	public void setGameDay(String gameDay) {
		this.gameDay = gameDay;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Integer getHomeTeamConferenceId() {
		return homeTeamConferenceId;
	}

	public void setHomeTeamConferenceId(Integer homeTeamConferenceId) {
		this.homeTeamConferenceId = homeTeamConferenceId;
	}

	public String getHomeTeamConferenceLongName() {
		return homeTeamConferenceLongName;
	}

	public void setHomeTeamConferenceLongName(String homeTeamConferenceLongName) {
		this.homeTeamConferenceLongName = homeTeamConferenceLongName;
	}

	public String getHomeTeamConferenceShortName() {
		return homeTeamConferenceShortName;
	}

	public void setHomeTeamConferenceShortName(String homeTeamConferenceShortName) {
		this.homeTeamConferenceShortName = homeTeamConferenceShortName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlay() {
		return play;
	}

	public void setPlay(String play) {
		this.play = play;
	}

	public String getPlayerFirstName() {
		return playerFirstName;
	}

	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}

	public String getPlayerLastName() {
		return playerLastName;
	}

	public void setPlayerLastName(String playerLastName) {
		this.playerLastName = playerLastName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getRoadTeamConferenceId() {
		return roadTeamConferenceId;
	}

	public void setRoadTeamConferenceId(Integer roadTeamConferenceId) {
		this.roadTeamConferenceId = roadTeamConferenceId;
	}

	public String getRoadTeamConferenceLongName() {
		return roadTeamConferenceLongName;
	}

	public void setRoadTeamConferenceLongName(String roadTeamConferenceLongName) {
		this.roadTeamConferenceLongName = roadTeamConferenceLongName;
	}

	public String getRoadTeamConferenceShortName() {
		return roadTeamConferenceShortName;
	}

	public void setRoadTeamConferenceShortName(String roadTeamConferenceShortName) {
		this.roadTeamConferenceShortName = roadTeamConferenceShortName;
	}

	public Integer getRoadTeamId() {
		return roadTeamId;
	}

	public void setRoadTeamId(Integer roadTeamId) {
		this.roadTeamId = roadTeamId;
	}

	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public Integer getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(Integer homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getRoadTeamName() {
		return roadTeamName;
	}

	public void setRoadTeamName(String roadTeamName) {
		this.roadTeamName = roadTeamName;
	}

}
