package root.demo.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LocationDTO implements Serializable {

	private String lat;
	private String lon;

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public LocationDTO(String lat, String lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

}
