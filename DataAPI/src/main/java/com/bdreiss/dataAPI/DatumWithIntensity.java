package main.java.com.bdreiss.dataAPI;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DatumWithIntensity extends Datum implements Serializable{

	private Intensity intensity;
	
	public DatumWithIntensity(LocalDateTime date, Intensity intensity) {
		super(date);
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
