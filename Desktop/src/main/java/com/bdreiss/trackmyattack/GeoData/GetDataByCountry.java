package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.time.LocalDate;

import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.enums.Category;

import main.java.com.bdreiss.trackmyattack.GeoSphereAustria.APIQueryGeoSphereAustria;

/**
 * 
 */
public class GetDataByCountry {
	
	private static APIQuery apiQuery;

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param geoData
	 * @param category
	 */
	public static void getData(LocalDate startDate, LocalDate endDate, GeoData geoData, Category category) {
		//TODO: make queries for data that belongs together by country
		apiQuery = getAPI(null);
		apiQuery.query(startDate, endDate, geoData, category);
	}

	private static APIQuery getAPI(Point2D.Double coordinates) {
		return new APIQueryGeoSphereAustria();
	}
}
