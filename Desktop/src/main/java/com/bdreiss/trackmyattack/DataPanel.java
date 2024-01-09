package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import com.bdreiss.dataAPI.AbstractCategoryDataModel;
import com.bdreiss.dataAPI.AilmentDataModel;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;

import java.awt.GridBagLayout;
import java.time.Duration;
import java.time.LocalDate;

/*
 * DataPanel representing data for one specific category.
 */

public class DataPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	//set of alternating colors that make single rows more readable
	//Color[3] consists of a light color, standard color and dark color e.g. light blue, blue, dark blue
	private ArrayList<Color[]> colorSets = new ArrayList<>();

	//AbstractDataModel containing methods for category
	private AbstractCategoryDataModel data;

	public DataPanel(AbstractCategoryDataModel data) {
		super(new GridBagLayout());

		this.data = data;

		Color[] blues = { new Color(65, 105, 225), Color.BLUE, Color.BLUE.darker() };
		Color[] greens = { new Color(93, 255, 54), new Color(45, 221, 12), Color.GREEN.darker() };
		Color[] yellows = { new Color(255, 255, 159), Color.YELLOW, Color.YELLOW.darker() };

		colorSets.add(blues);
		colorSets.add(greens);
		colorSets.add(yellows);

		setData();

	}

	public void setData() {
		int daysSinceStartDate = (int) Duration
				.between(data.getFirstDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

		GridBagSettings c = new GridBagSettings();

		//TODO: why do I need this?
		c.setIPadX(daysSinceStartDate);
		c.gridx = 0;
		c.gridy = 0;

		//keys from category
		Iterator<String> it = data.getKeys();

		//add row with dates
		add(new DataRow(data.getFirstDate()), c);

		c.gridy++;

		//for every key in category add a data row
		while (it.hasNext()) {
			String key = it.next();
			try {
				Color[] colorSet = colorSets.get(c.gridy % (colorSets.size()));
				add(new DataRow(key, data, new AilmentDataModel(data.getData()), colorSet), c);
			} catch (EntryNotFoundException e) {
				e.printStackTrace();
			}
			c.gridy++;
		}

		c.gridy = data.getSize() + 1;
		add(new JPanel(), c);

	}

}