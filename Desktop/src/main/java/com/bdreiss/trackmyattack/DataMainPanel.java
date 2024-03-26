package main.java.com.bdreiss.trackmyattack;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.bdreiss.dataAPI.core.CauseData;
import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.core.RemedyData;
import com.bdreiss.dataAPI.core.SymptomData;

import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;

/**
 * JPanel representing frame containing data panels.
 *
 */

public class DataMainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new instance of DataMainPanel.
	 * 
	 * @param data    the data model containing data to be represented
	 * @param geoData a geo data model containing data representing the weather
	 */
	public DataMainPanel(DataModel data, GeoData geoData) {

		setLayout(new GridBagLayout());

		setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagSettings c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;

		DataPanel causeDataPanel = new DataPanel(new CauseData(data));
		add(causeDataPanel, c);

		c.gridy++;

		DataPanel symptomDataPanel = new DataPanel(new SymptomData(data));
		add(symptomDataPanel, c);

		c.gridy++;

		DataPanel remedyDataPanel = new DataPanel(new RemedyData(data));
		add(remedyDataPanel, c);

		if (geoData != null) {
			c.gridy++;

			DataPanel geoDataPanel = new DataPanel(geoData);
			add(geoDataPanel, c);
		}
	}

}
