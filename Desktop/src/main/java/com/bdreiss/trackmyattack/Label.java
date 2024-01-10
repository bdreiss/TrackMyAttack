package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * Standard container for labels.
 */

public class Label extends JLabel {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param text
	 */
	public Label(String text) {
		super(text);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		setOpaque(true);//so background color is shown
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value()));//labels always have to be the same height as boxes
		
		//draw the grid
		graphics.setColor(Colors.GRID_COLOR.value());
		graphics.drawRect(0, 0, Dimensions.LABEL_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value());

	}

}
