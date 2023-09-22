package main.java.com.bdreiss.trackmyattack.GeoData;

import java.time.LocalDateTime;

import com.bdreiss.dataAPI.util.Datum;

/**
 * Sub class of Datum representing meteorological data (i.e. temperature) for one date.
 */

public class GeoDatum extends Datum {

	private static final long serialVersionUID = 1L;

	private Float value;//value for date

	public GeoDatum(LocalDateTime date, Float value, float lowerBound, float upperBound) {
		super(date);
		this.value = value;
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
