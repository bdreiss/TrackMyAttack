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

public class GeoSphereAustriaStation implements Station {

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

	/**
	 * Returns X coordinate of station.
	 * 
	 * @return X coordinate of station
	 */
	@Override
	public Double getX() {
		return coordinates.x;
	}

	/**
	 * Returns Y coordinate of station.
	 * 
	 * @return Y coordinate of station
	 */
	@Override
	public Double getY() {
		return coordinates.y;
	}

}
