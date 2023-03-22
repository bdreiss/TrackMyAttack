package main.java.com.bdreiss.dataAPI;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Representation of a single piece of data containing the date and intensity of
 * the cause/symptom/remedy.
 */
public class Datum implements Serializable {

	private static final long serialVersionUID = 1L;
	private LocalDateTime date;
	private Intensity intensity;

	public Datum(LocalDateTime date, Intensity intensity) {
		this.date = date;
		this.intensity = intensity;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Intensity getIntensity() {
		return intensity;
	}

	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

}
