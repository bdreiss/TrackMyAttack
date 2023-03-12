package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;

public enum Colors {
	EMPTY_COLOR(Color.WHITE);
	
	private Color color;
	
	Colors(Color color){
		this.color = color;
	}
	
	public Color value() {
		return color;
	}
}
