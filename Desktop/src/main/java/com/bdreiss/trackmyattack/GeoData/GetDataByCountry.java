package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.time.LocalDate;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.enums.Category;

import main.java.com.bdreiss.trackmyattack.GeoSphereAustria.APIQueryGeoSphereAustria;

public class GetDataByCountry {
	
	private static APIQuery apiQuery;
	
	public static void getData(LocalDate startDate, LocalDate endDate, Point2D.Double coordinates, DataModel data, Category category) {
		apiQuery = getAPI(coordinates);
		apiQuery.query(startDate, endDate, coordinates, data, category);
		
	}

	private static APIQuery getAPI(Point2D.Double coordinates) {
		return new APIQueryGeoSphereAustria();
	}
}
