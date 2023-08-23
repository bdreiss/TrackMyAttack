package main.java.com.bdreiss.trackmyattack;

public enum Dimensions {
	DATA_ROW_HEIGHT(20), DATA_ROW_WIDTH(20), LABEL_WIDTH(150), SPACE(0), RATIO(50);

	private int value;

	Dimensions(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
