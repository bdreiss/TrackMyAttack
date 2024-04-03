package net.berndreiss.trackmyattack.data.util;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Class representing coordinates.
 */
public class Coordinate implements Comparable<Coordinate>, Serializable {

	private static final long serialVersionUID = 1L;
	private Double latitude;
	private Double longitude;

	/**
	 * Instantiates a new instance of coordinate.
	 * 
	 * @param latitude  latitude value
	 * @param longitude longitude value
	 * @throws Exception thrown if latitude or longitue are null
	 */
	public Coordinate(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		
		String latitudeString = latitude == null? "null": String.valueOf(latitude);
		String longitudeString = longitude == null? "null": String.valueOf(longitude);
		return latitudeString + ", " + longitudeString;
	}

	@Override
	public int compareTo(Coordinate o) {

		if (o==null)
			return 1;
		
		if (o.latitude.equals(latitude) && o.longitude.equals(longitude))
			return 0;

		return 1;
	}

	/**
	 * Print the iterator passed to the function to the command line as an array.
	 * 
	 * @param it the iterator to be printed as array
	 * @return the string being printed
	 */
	public static String printArray(Iterator<Coordinate> it) {

		StringBuilder sb = new StringBuilder();

		while (it.hasNext()) {
			sb.append("[" + it.next() + "],");
		}

		return "[" + sb.toString() + "]";
	}

}
