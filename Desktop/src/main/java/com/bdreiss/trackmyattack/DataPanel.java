package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.AilmentDataModel;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.util.Datum;

import java.awt.GridBagLayout;
import java.time.Duration;
import java.time.LocalDate;

public class DataPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Color[]> colorSets = new ArrayList<>();
	
	private AbstractDataModel data;
	
	private LocalDate startDate;
	
	public DataPanel(AbstractDataModel data) {
		super(new GridBagLayout());
		
		this.data = data;
		
		Color[] blues = {new Color(65,105,225), Color.BLUE, Color.BLUE.darker()};
		Color[] greens = {Color.GREEN.brighter(), Color.GREEN,Color.GREEN.darker()};
		Color[] yellows = {Color.YELLOW.brighter(), Color.YELLOW, Color.YELLOW.darker()};
		
		colorSets.add(blues);
		colorSets.add(greens);
		colorSets.add(yellows);

		
		startDate = LocalDate.now();

		Iterator<String> it = data.getKeys();

		while (it.hasNext()) {
			Iterator<Datum> itForKey = null;
			try {
				itForKey = data.getData(it.next());
			} catch (EntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (itForKey != null && itForKey.hasNext()) {
			
				LocalDate earliestDate = itForKey.next().getDate().toLocalDate();
				
				if (earliestDate.compareTo(startDate)< 0)
					startDate = earliestDate;
				
			}
			
		}
		int daysSinceStartDate = (int) Duration.between(startDate.atStartOfDay(),LocalDate.now().atStartOfDay()).toDays();

		setSize(new Dimension(Dimensions.WIDTH.value()*(daysSinceStartDate), Dimensions.HEIGHT.value()*data.getSize()+1));

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

		
		
		add(new DataRow(startDate),c);
		

		while (it.hasNext()) {
			String key = it.next();
			try {
				Color[] colorSet = colorSets.get(c.gridy%(colorSets.size()));
				add(new DataRow(key, data, new AilmentDataModel(data.getData()), startDate, colorSet),c);
			} catch (EntryNotFoundException e) {
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