package vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gamestat extends Base {

	private Integer playerId;
	private String playerFirstName;
	private String playerLastName;
	private String playerName;
	private Integer playerMinutes;
	private Integer playerFgAttempted;
	private Integer playerFgMade;
	private Integer playerFg3Attempted;
	private Integer playerFg3Made;
	private Integer playerFtAttempted;
	private Integer playerFtMade;
	private Integer playerOffensiveRebounds;
	private Integer playerDefensiveRebounds;
	private Integer playerTotalRebounds;
	private Integer playerAssists;
	private Integer playerSteals;
	private Integer playerBlocks;
	private Integer playerTurnovers;
	private Integer playerFouls;
	private Integer playerPointsScored;

	public Gamestat() {
		super();
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
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

	public Integer getPlayerMinutes() {
		return playerMinutes;
	}

	public void setPlayerMinutes(Integer playerMinutes) {
		this.playerMinutes = playerMinutes;
	}

	public Integer getPlayerFgAttempted() {
		return playerFgAttempted;
	}

	public void setPlayerFgAttempted(Integer playerFgAttempted) {
		this.playerFgAttempted = playerFgAttempted;
	}

	public Integer getPlayerFgMade() {
		return playerFgMade;
	}

	public void setPlayerFgMade(Integer playerFgMade) {
		this.playerFgMade = playerFgMade;
	}

	public Integer getPlayerFg3Attempted() {
		return playerFg3Attempted;
	}

	public void setPlayerFg3Attempted(Integer playerFg3Attempted) {
		this.playerFg3Attempted = playerFg3Attempted;
	}

	public Integer getPlayerFg3Made() {
		return playerFg3Made;
	}

	public void setPlayerFg3Made(Integer playerFg3Made) {
		this.playerFg3Made = playerFg3Made;
	}

	public Integer getPlayerFtAttempted() {
		return playerFtAttempted;
	}

	public void setPlayerFtAttempted(Integer playerFtAttempted) {
		this.playerFtAttempted = playerFtAttempted;
	}

	public Integer getPlayerFtMade() {
		return playerFtMade;
	}

	public void setPlayerFtMade(Integer playerFtMade) {
		this.playerFtMade = playerFtMade;
	}

	public Integer getPlayerOffensiveRebounds() {
		return playerOffensiveRebounds;
	}

	public void setPlayerOffensiveRebounds(Integer playerOffensiveRebounds) {
		this.playerOffensiveRebounds = playerOffensiveRebounds;
	}

	public Integer getPlayerDefensiveRebounds() {
		return playerDefensiveRebounds;
	}

	public void setPlayerDefensiveRebounds(Integer playerDefensiveRebounds) {
		this.playerDefensiveRebounds = playerDefensiveRebounds;
	}

	public Integer getPlayerTotalRebounds() {
		return playerTotalRebounds;
	}

	public void setPlayerTotalRebounds(Integer playerTotalRebounds) {
		this.playerTotalRebounds = playerTotalRebounds;
	}

	public Integer getPlayerAssists() {
		return playerAssists;
	}

	public void setPlayerAssists(Integer playerAssists) {
		this.playerAssists = playerAssists;
	}

	public Integer getPlayerSteals() {
		return playerSteals;
	}

	public void setPlayerSteals(Integer playerSteals) {
		this.playerSteals = playerSteals;
	}

	public Integer getPlayerBlocks() {
		return playerBlocks;
	}

	public void setPlayerBlocks(Integer playerBlocks) {
		this.playerBlocks = playerBlocks;
	}

	public Integer getPlayerTurnovers() {
		return playerTurnovers;
	}

	public void setPlayerTurnovers(Integer playerTurnovers) {
		this.playerTurnovers = playerTurnovers;
	}

	public Integer getPlayerFouls() {
		return playerFouls;
	}

	public void setPlayerFouls(Integer playerFouls) {
		this.playerFouls = playerFouls;
	}

	public Integer getPlayerPointsScored() {
		return playerPointsScored;
	}

	public void setPlayerPointsScored(Integer playerPointsScored) {
		this.playerPointsScored = playerPointsScored;
	}

}
