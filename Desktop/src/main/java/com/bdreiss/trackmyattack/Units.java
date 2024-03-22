package main.java.com.bdreiss.trackmyattack;

/**
 * Units defining sizes of GUI elements
 */

public enum Units {
	/**
	 * The height of a data row.
	 */
	DATA_ROW_BOX_HEIGHT(20), 
	/**
	 * The width of a data row.
	 */
	DATA_ROW_BOX_WIDTH(20), 
	/**
	 * The width of a label.
	 */
	LABEL_WIDTH(150), 
	/**
	 * The space between boxes.
	 */
	SPACE(0);

	private int value;

	Units(int value) {
		this.value = value;
	}

	/**
	 * Returns the value of the unit.
	 * 
	 * @return the value of the unit
	 */
	public int value() {
		return value;
	}
}
