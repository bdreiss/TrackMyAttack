package main.java.com.bdreiss.trackmyattack.GeoData;

/**
 * Enum representing meteorological data, including median, max and min temperature, humidity, vapor and pressure
 */
public enum GeoDataType {
	//TODO: multilingual support?
	TEMPERATURE_MEDIAN("temperature median", 0, 100), TEMPERATURE_MAX("temperature max", 0, 100),
	TEMPERATURE_MIN("temperature min", 0, 100), HUMIDITY("humidity", 0, 100), VAPOR("vapor", 0, 100),
	PRESSURE("pressure", 0, 100);

	private String label;
	private float lowerBound;
	private float upperBound;

	/**
	 * Creates an instance of GeoDataType.
	 * @param label String that should be returned for toString()
	 * @param lowerBound min value of type of data
	 * @param upperBound max value of type of data
	 */
	GeoDataType(String label, float lowerBound, float upperBound) {
		this.label = label;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public String toString() {
		return label;
	}

	/**
	 * Returns min value for the type of data.
	 * @return lower bound
	 */
	public float lowerBound() {
		return lowerBound;
	}

	/**
	 * Returns max value for the type of data.
	 * @return upper bound
	 */
	public float upperBound() {
		return upperBound;
	}
}
