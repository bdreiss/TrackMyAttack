package com.bdreiss.dataAPI.util;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representation of a single piece of data containing the date of the
 * cause/symptom/remedy.
 */
public class Datum implements Serializable {

	private static final long serialVersionUID = 1L;
	private LocalDateTime date;

	/**
	 * Instantiates a new instance of datum.
	 * @param date the date of the datum
	 */
	public Datum(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * Gets the date.
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date the new date
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
