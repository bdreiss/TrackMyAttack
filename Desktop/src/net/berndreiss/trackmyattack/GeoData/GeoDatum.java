package net.berndreiss.trackmyattack.GeoData;

import java.time.LocalDateTime;

import net.berndreiss.trackmyattack.data.util.Datum;

/**
 * Sub class of Datum representing meteorological data (i.e. temperature) for
 * one date.
 */

public class GeoDatum extends Datum {

	private static final long serialVersionUID = 1L;

	private Float value;// value for date

	private GeoDataType dataType;// type of data

	/**
	 * Instantiates a new instance of GeoDatum.
	 * 
	 * @param date     the date corresponding to the datum
	 * @param value    the value of the data
	 * @param dataType the type of data
	 */
	public GeoDatum(LocalDateTime date, Float value, GeoDataType dataType) {
		super(date);
		this.value = value;
		this.dataType = dataType;
	}

	/**
	 * Returns the value for the meteorological data represented by this class.
	 * 
	 * @return value for meteorological data for date
	 */
	public float getValue() {
		return value;
	}
	
	

}
