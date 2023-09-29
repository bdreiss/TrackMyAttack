package com.bdreiss.dataAPI.util;

import java.util.Iterator;

public class Coordinate implements Comparable<Coordinate>{
	
	private Double latitude;
	private Double longitude;
	
	public Coordinate(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}
	
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
	
	public static String printArray(Iterator<Coordinate> it) {
		
		StringBuilder sb = new StringBuilder();

		while(it.hasNext()) {
			sb.append("[" + it.next() + "],");
		}
		
		return "[" +  sb.toString() + "]";
	}
	
}

	