package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;

public enum Colors {
	EMPTY_COLOR(Color.WHITE), GRID_COLOR(new Color(240,240,240));
	
	private Color color;
	
	Colors(Color color){
		this.color = color;
	}
	
	public Color value() {
		return color;
	}
}
