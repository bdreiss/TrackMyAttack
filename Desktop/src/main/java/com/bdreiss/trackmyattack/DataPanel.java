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

import main.java.com.bdreiss.dataAPI.AbstractDataModel;
import main.java.com.bdreiss.dataAPI.AilmentDataModel;
import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.util.Datum;

import java.awt.GridBagLayout;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;

public class DataPanel extends JPanel{

	ArrayList<Color> testColors = new ArrayList<>();
	
	AbstractDataModel data;
	
	public DataPanel(AbstractDataModel data, int width, int height) {
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

		Iterator<String> it = data.getKeys();

		int size = 0;
		
		while (it.hasNext()) {
			Iterator<Datum> itForKey = null;
			try {
				itForKey = data.getData(it.next());
			} catch (EntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int entriesSize = 0;
			
			if (itForKey != null && itForKey.hasNext()) {
			
				LocalDate earliestDate = itForKey.next().getDate().toLocalDate();
				entriesSize = (int) Duration.between(earliestDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
				
				System.out.println(earliestDate + ": " + entriesSize);
			
			}
			System.out.println(entriesSize);
			
			if (entriesSize >= 0)
				entriesSize += 1;
			else
				entriesSize = 0;

			System.out.println(entriesSize);

			size = entriesSize > size ? entriesSize : size;
		}
		
		it = data.getKeys();
		
		it = data.getKeys();
		
		add(new DataRow(size),c);
		

		while (it.hasNext()) {
			String key = it.next();
			try {
				add(new DataRow(key, data.getData(key), size, new AilmentDataModel(data.getData()), testColors.get(c.gridy%(testColors.size()))),c);
			} catch (EntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c.gridy++;
		}


		c.gridy=data.getSize()+1;
		c.weighty = super.getHeight()-(data.getSize()+1)*10;
		add(new JPanel(),c);
		repaint();

	}

}
