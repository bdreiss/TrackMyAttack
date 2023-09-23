package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.time.LocalDate;


import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.enums.Category;

/**
 * This interface represents an APIQuery.
 */

public interface APIQuery {
	
	/**
	 * A query takes an instance of DataModel and adds GeoDates between two dates to it.
	 * 
	 * @param startDate first date in range for which to look up data
	 * @param endDate last date in range for which to look up data
	 * @param coordinates Point2D.Double representing coordinates for which meteorological data should be queried
	 * @param data DataModel in which data should be added 
	 * @param category Category that is used for storing data
	 */
	void query(LocalDate startDate, LocalDate endDate, Point2D.Double coordinates, DataModel data, Category category);

}
