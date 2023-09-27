package main.java.com.bdreiss.trackmyattack.GeoData;

/**
 * Interface representing a station with X and Y coordinates.
 */

public interface Station {

	/**
	 * Returns value of latitude of station.
	 * 
	 * @return latitude
	 */
	Double getLatitude();
	
	/**
	 * Returns value of longitude of station.
	 * 
	 * @return longitude
	 */

	Double getLongitude();
	
}
