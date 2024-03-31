package com.bdreiss.data.util;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.bdreiss.data.enums.Intensity;

/**
 * Extending the basic data type Datum containing the date plus intensity of
 * the cause/symptom/remedy.
 */

public class DatumWithIntensity extends Datum implements Serializable{

	private Intensity intensity;
	
	/**
	 * Instantiates new instance of datum with intensity.
	 * @param date the date of the datum
	 * @param intensity the intensity of the datum
	 */
	public DatumWithIntensity(LocalDateTime date, Intensity intensity) {
		super(date);
		this.intensity = intensity;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Gets the intensity.
	 * @return the intensity
	 */
	public Intensity getIntensity() {
		return intensity;
	}

	/**
	 * Sets the intensity.
	 * @param intensity the new intensity
	 */
	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

}
