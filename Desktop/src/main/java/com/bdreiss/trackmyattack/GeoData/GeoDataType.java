package main.java.com.bdreiss.trackmyattack.GeoData;

public enum GeoDataType {
	TEMPERATURE_MEDIAN("temperature median", 0, 100), TEMPERATURE_MAX("temperature max", 0, 100),
	TEMPERATURE_MIN("temperature min", 0, 100), HUMIDITY("humidity", 0, 100), VAPOR("vapor", 0, 100),
	PRESSURE("pressure", 0, 100);

	private String type;
	private float lowerBound;
	private float upperBound;

	GeoDataType(String type, float lowerBound, float upperBound) {
		this.type = type;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public String toString() {
		return type;
	}

	public float lowerBound() {
		return lowerBound;
	}

	public float upperBound() {
		return upperBound;
	}
}
