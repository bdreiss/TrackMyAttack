package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Label extends JLabel {

	private static final long serialVersionUID = 1L;

	public Label(String text) {
		super(text);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		setOpaque(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(), Dimensions.DATA_ROW_HEIGHT.value()));
		graphics.setColor(Colors.GRID_COLOR.value());
		graphics.drawRect(0, 0, Dimensions.LABEL_WIDTH.value(), Dimensions.DATA_ROW_HEIGHT.value());

	}

}
