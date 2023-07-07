package main.java.com.bdreiss.trackmyattack;

public enum Dimensions {
	HEIGHT(20), WIDTH (20), LABEL_WIDTH (50), SPACE(0), RATIO (50);
	
	private int value;
	
	Dimensions(int value){
		this.value = value;
	}
	
	public int value() {
		return value;
	}
}
