package vo;

public class Gamecast extends Base {
	private String networkCoverage;// LHN,
	private Integer venueCapacity;// 16540,
	private String venueName;// Frank Erwin Center,
	private Integer gameAttendance;// 2585,
	private String venueState;// TX,
	private Integer gamePercentageFull;// 16 ("%" omitted)
	private String gameTimeUTC;// 01 00,
	private String referees;// Lisa Jones|Brenda Pantoja|Michael McConnell,
	private String status;// Final,
	private String venueCity;// Austin,

	public Gamecast() {
		super();
	}

	public String getNetworkCoverage() {
		return networkCoverage;
	}

	public void setNetworkCoverage(String networkCoverage) {
		this.networkCoverage = networkCoverage;
	}

	public Integer getVenueCapacity() {
		return venueCapacity;
	}

	public void setVenueCapacity(Integer venueCapacity) {
		this.venueCapacity = venueCapacity;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public Integer getGameAttendance() {
		return gameAttendance;
	}

	public void setGameAttendance(Integer gameAttendance) {
		this.gameAttendance = gameAttendance;
	}

	public String getVenueState() {
		return venueState;
	}

	public void setVenueState(String venueState) {
		this.venueState = venueState;
	}

	public Integer getGamePercentageFull() {
		return gamePercentageFull;
	}

	public void setGamePercentageFull(Integer gamePercentageFull) {
		this.gamePercentageFull = gamePercentageFull;
	}

	public String getGameTimeUTC() {
		return gameTimeUTC;
	}

	public void setGameTimeUTC(String gameTimeUTC) {
		this.gameTimeUTC = gameTimeUTC;
	}

	public String getReferees() {
		return referees;
	}

	public void setReferees(String referees) {
		this.referees = referees;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVenueCity() {
		return venueCity;
	}

	public void setVenueCity(String venueCity) {
		this.venueCity = venueCity;
	}

}
