package main.java.com.bdreiss.dataAPI;

import java.io.Serializable;
import java.util.Date;

public class Datum implements Serializable {
	/**
	 * Representation of a single piece of data containing the date and intensity of
	 * the cause/symptom.
	 */

	private static final long serialVersionUID = 1L;
	private Date date;
	private Intensity intensity;

	public Datum(Date date, Intensity intensity) {
		this.date = date;
		this.intensity = intensity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Intensity getIntensity() {
		return intensity;
	}

	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

}
