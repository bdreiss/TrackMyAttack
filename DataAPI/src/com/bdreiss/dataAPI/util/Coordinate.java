package com.bdreiss.dataAPI.util;

import java.util.Iterator;

/**
 * 
 */
public class Coordinate implements Comparable<Coordinate>{
	
	private Double latitude;
	private Double longitude;
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public Coordinate(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * 
	 * @return
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return String.valueOf(latitude) + ", " + longitude;
	}
	
	@Override
	public int compareTo(Coordinate o) {

		if (o.latitude == latitude && o.longitude == longitude)	
			return 0;
		
		return 1;
	}
	
	/**
	 * 
	 * @param it
	 * @return
	 */
	public static String printArray(Iterator<Coordinate> it) {
		
		StringBuilder sb = new StringBuilder();

		while(it.hasNext()) {
			sb.append("[" + it.next() + "],");
		}
		
		return "[" +  sb.toString() + "]";
	}
	
}

	