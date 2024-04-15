package net.berndreiss.trackmyattack.GeoData;

import java.time.LocalDate;

import net.berndreiss.trackmyattack.data.enums.Category;
import net.berndreiss.trackmyattack.desktop.DataWrapper;

/**
 * This interface represents an APIQuery.
 */

public interface APIQuery {

	/**
	 * A query takes an instance of DataModel being represented as GeoData and adds
	 * GeoDates between two dates to it.
	 * 
	 * @param startDate first date in range for which to look up data
	 * @param endDate   last date in range for which to look up data
	 * @param geoData   AbstractDataModel in which data should be added
	 * @param category  Category that is used for storing data
	 */
	void query(LocalDate startDate, LocalDate endDate, DataWrapper geoData, Category category);

}
