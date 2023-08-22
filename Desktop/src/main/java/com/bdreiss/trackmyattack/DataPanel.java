package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.AilmentDataModel;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;

import java.awt.GridBagLayout;
import java.time.Duration;
import java.time.LocalDate;

public class DataPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayList<Color[]> colorSets = new ArrayList<>();

	private AbstractDataModel data;

	public DataPanel(AbstractDataModel data) {
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

		c.setIPadX(daysSinceStartDate);
		c.gridx = 0;
		c.gridy = 0;

		Iterator<String> it = data.getKeys();

		add(new DataRow(data.getFirstDate()), c);

		c.gridy++;

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