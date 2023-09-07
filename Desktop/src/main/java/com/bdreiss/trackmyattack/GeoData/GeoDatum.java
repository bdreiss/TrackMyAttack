package main.java.com.bdreiss.trackmyattack.GeoData;

import java.time.LocalDateTime;

import com.bdreiss.dataAPI.util.Datum;

public class GeoDatum extends Datum {

	private static final long serialVersionUID = 1L;

	Float value;
	float lowerBound;
	float upperBound;

	public GeoDatum(LocalDateTime date, Float value, float lowerBound, float upperBound) {
		super(date, null);
		this.value = value;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

//	@Override
//	public Intensity getIntensity() {
//		
//		if (value < lowerBound)
//			return Intensity.LOW;
//		if (value > upperBound)
//			return Intensity.HIGH;
//		return Intensity.MEDIUM;
//		
//	}

	public float getValue() {
		return value;
	}

}
