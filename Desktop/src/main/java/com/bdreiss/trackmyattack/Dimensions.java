package main.java.com.bdreiss.trackmyattack;

/**
 * Dimensions defining sizes of GUI elements
 */

public enum Dimensions {
	/**
	 * 
	 */
	DATA_ROW_BOX_HEIGHT(20), 
	/**
	 * 
	 */
	DATA_ROW_BOX_WIDTH(20), 
	/**
	 * 
	 */
	LABEL_WIDTH(150), 
	/**
	 * 
	 */
	SPACE(0);

	private int value;

	Dimensions(int value) {
		this.value = value;
	}

	/**
	 * 
	 * @return
	 */
	public int value() {
		return value;
	}
}
