package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;

/**
 * Defines the colors for empty and non-empty cells in the grid.
 */

public enum Colors {
	/**
	 * 
	 */
	EMPTY_COLOR(Color.WHITE), 
	/**
	 * 
	 */
	GRID_COLOR(new Color(240, 240, 240));

	private Color color;

	Colors(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * @return
	 */
	public Color value() {
		return color;
	}
}
