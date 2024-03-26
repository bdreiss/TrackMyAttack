package main.java.com.bdreiss.trackmyattack;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.bdreiss.dataAPI.core.CauseData;
import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.core.RemedyData;
import com.bdreiss.dataAPI.core.SymptomData;

import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;

/**
 * Panel that holds all labels, consisting of CAUSES, SYMPTOMS, REMEDIES and
 * GEO_DATA
 */

public class LabelMainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new instance of LabelMainPanel.
	 * 
	 * @param data    the data model holding relevant data
	 * @param geoData the geo data model holding data for the weather
	 */
	public LabelMainPanel(DataModel data, GeoData geoData) {

		setLayout(new GridBagLayout());

		GridBagSettings c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;

		// add filler and CAUSES
		c.gridy++;
		LabelPanel causeLabels = new LabelPanel(new CauseData(data));
		add(causeLabels, c);
		c.gridy++;

		// add filler and SYMPTOMS
		c.gridy++;
		LabelPanel symptomLabels = new LabelPanel(new SymptomData(data));
		add(symptomLabels, c);
		c.gridy++;

		// add filler and REMEDIES
		c.gridy++;
		LabelPanel remedyLabels = new LabelPanel(new RemedyData(data));
		add(remedyLabels, c);
		// add filler
		c.gridy++;

		// add GEO_DATA and final filler
		if (geoData != null) {
			c.gridy++;
			LabelPanel geoLabels = new LabelPanel(geoData);
			add(geoLabels, c);
			c.gridy++;
		}
	}

}
