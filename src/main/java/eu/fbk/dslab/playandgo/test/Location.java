package eu.fbk.dslab.playandgo.test;

public class Location {
	private long timestamp;
	private double latitude;
	private double longitude;
	private String tripId;
	
	public Location() {}
	
	public Location(double lat, double lng, long timestamp, String tripId) {
		this.timestamp = timestamp;
		this.latitude = lat;
		this.longitude = lng;
		this.tripId = tripId;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
}
