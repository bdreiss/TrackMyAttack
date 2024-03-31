package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.time.LocalDate;

import com.bdreiss.data.enums.Category;

import main.java.com.bdreiss.trackmyattack.GeoSphereAustria.APIQueryGeoSphereAustria;

/**
 * Class mediating between coordinates and country APIs.
 */
public class DataByCountry {

	private static APIQuery apiQuery;

	/**
	 * Takes an instance of DataModel being represented as GeoData and adds data to
	 * it.
	 * 
	 * @param startDate first date for which data is added
	 * @param endDate last date for which data is added
	 * @param geoData AbstractDataModel in which data should be added
	 * @param category Category being used to store data
	 */
	public static void getData(LocalDate startDate, LocalDate endDate, GeoData geoData, Category category) {
		// TODO: make queries for data that belongs together by country
		apiQuery = getAPI(null);
		apiQuery.query(startDate, endDate, geoData, category);
	}

	// TODO return API according to coordinates
	private static APIQuery getAPI(Point2D.Double coordinates) {
		return new APIQueryGeoSphereAustria();
	}
}
