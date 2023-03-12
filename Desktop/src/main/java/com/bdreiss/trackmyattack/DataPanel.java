package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.ZoneId;

public class DataPanel extends JPanel{

	ArrayList<Color> testColors = new ArrayList<>();
	
	public DataPanel(int width, int height) {
		super(new GridBagLayout());
		setSize(new Dimension(width, height));
		
		testColors.add(Color.BLUE);
		testColors.add(Color.GREEN);
		testColors.add(Color.YELLOW);

	
	}
	
	public void setData(ArrayList<ArrayList<Boolean>> lists) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.ipadx = super.getWidth();
		
		c.insets.left = 0;
		c.insets.right = 0;
		c.insets.top = 0;
		c.insets.bottom = 0;

		c.gridx = 0;
		c.weighty = 1;

		
		
		
		c.gridy = 0;
		add(new DataRow(lists),c);
		
		for (int i = 0; i < lists.size(); i++) {
			c.gridy = i+1;
			add(new DataRow("Test " + i, lists.get(i),testColors.get(i%(testColors.size()))),c);
			
		}

		c.gridy=lists.size()+1;
		c.weighty = super.getHeight()-(lists.size()+1)*10;
		add(new JPanel(),c);
		repaint();

	}

}
