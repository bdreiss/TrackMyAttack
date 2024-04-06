package net.berndreiss.trackmyattack.desktop;

import java.awt.Color;

/**
 * Defines the colors for empty and non-empty cells in the grid.
 */

public enum Colors {
	/**
	 * Color used for empty cells.
	 */
	EMPTY_COLOR(Color.WHITE), 
	/**
	 * Color used for the grid.
	 */
	GRID_COLOR(new Color(240, 240, 240));

	private Color color;

	Colors(Color color) {
		this.color = color;
	}

	/**
	 * Returns the color.
	 * 
	 * @return the color
	 */
	public Color value() {
		return color;
	}
}
