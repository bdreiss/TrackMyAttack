package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.com.bdreiss.dataAPI.Category;
import main.java.com.bdreiss.dataAPI.DataModel;

import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.ZoneId;

public class DataPanel extends JPanel{

	ArrayList<Color> testColors = new ArrayList<>();
	
	DataModel data;
	
	public DataPanel(DataModel data, int width, int height) {
		super(new GridBagLayout());
		setSize(new Dimension(width, height));
		
		this.data = data;
		
		testColors.add(Color.BLUE);
		testColors.add(Color.GREEN);
		testColors.add(Color.YELLOW);
		setData();

	
	}
	
	public void setData() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.ipadx = super.getWidth();
		
		c.insets.left = 0;
		c.insets.right = 0;
		c.insets.top = 0;
		c.insets.bottom = 0;

		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;

		final int SIZE = 100;
		
		add(new DataRow(SIZE),c);
		
		Iterator<String> it = data.getCauses();

		while (it.hasNext()) {
			
			add(new DataRow(it.next(),data, SIZE, testColors.get(c.gridy%(testColors.size()))),c);
			c.gridy++;
		}


		c.gridy=data.getCausesSize()+1;
		c.weighty = super.getHeight()-(data.getCausesSize()+1)*10;
		add(new JPanel(),c);
		repaint();

	}

}
