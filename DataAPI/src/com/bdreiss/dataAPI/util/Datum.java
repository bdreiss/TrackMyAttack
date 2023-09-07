package com.bdreiss.dataAPI.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representation of a single piece of data containing the date of the
 * cause/symptom/remedy.
 */
public class Datum implements Serializable {

	private static final long serialVersionUID = 1L;
	private LocalDateTime date;
	private Point2D.Double coordinates;

	public Datum(LocalDateTime date, Point2D.Double coordinates) {
		this.date = date;
		this.coordinates = coordinates;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Point2D.Double getCoordinates() {
		return coordinates;
	}

}
