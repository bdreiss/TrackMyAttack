package net.berndreiss.trackmyattack.data.enums;

/**
 * Categories being supported by the DataModel.
 */

public enum Category {
	/**
	 * Data representing ailemnts. Always has the attribute intensity (see
	 * {@link Intensity}.
	 */
	AILMENT,
	/**
	 * Data representing causes. Can have the attribute intensity (see
	 * {@link Intensity}.
	 */
	CAUSE,
	/**
	 * Data representing symptoms. Always has the attribute intensity (see
	 * {@link Intensity}.
	 */
	SYMPTOM,
	/**
	 * Data representing remedies. Can have the attribute intensity (see
	 * {@link Intensity}.
	 */
	REMEDY
}
