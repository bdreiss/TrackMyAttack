package main.java.com.bdreiss.trackmyattack.GeoSphereAustria;

import java.awt.geom.Point2D;

import main.java.com.bdreiss.trackmyattack.GeoData.Station;

/**
 * 
 * Class representing a GeoSphere Austria metereological station. At the moment
 * only id and coordinates are recorded.
 * 
 * @author bernd
 *
 */

public class GeoSphereAustriaStation implements Station, Comparable<GeoSphereAustriaStation> {

	private int id;
	private Point2D.Double coordinates;

	/**
	 * Creates new instance of GeoSphere Austria station.
	 * 
	 * @param id The id representing the station
	 * @param coordinates Point2D.Double representing longtitude and latitude.
	 */
	public GeoSphereAustriaStation(int id, Point2D.Double coordinates) {
		this.id = id;
		this.coordinates = coordinates;
	}

	/**
	 * Returns id of station.
	 * 
	 * @return id of station
	 */
	public int getId() {
		return id;
	}

	@Override
	public Double getLatitude() {
		return coordinates.x;
	}

	@Override
	public Double getLongitude() {
		return coordinates.y;
	}


	@Override
	public int compareTo(GeoSphereAustriaStation o) {
		if (this.id == o.id)
			return 0;
		
		return this.id-o.id;
	}

	
	/**
	 * Prints station as "int: Point2D.Double" representing "id: coordinates".
	 */
	public void print(){
		System.out.println(id + ": " + coordinates);
	}
}
