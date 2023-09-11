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

	public Datum(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
