package com.bdreiss.dataAPI.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.bdreiss.dataAPI.enums.Intensity;

/**
 * Extending the basic data type Datum containing the date plus intensity of
 * the cause/symptom/remedy.
 */

public class DatumWithIntensity extends Datum implements Serializable{

	private Intensity intensity;
	
	public DatumWithIntensity(LocalDateTime date, Point2D.Double coordinates, Intensity intensity) {
		super(date, coordinates);
		this.intensity = intensity;
	}

	private static final long serialVersionUID = 1L;

	public Intensity getIntensity() {
		return intensity;
	}

	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

}
