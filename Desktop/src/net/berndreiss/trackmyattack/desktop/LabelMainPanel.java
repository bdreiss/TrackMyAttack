package net.berndreiss.trackmyattack.desktop;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.berndreiss.trackmyattack.GeoData.GeoData;
import net.berndreiss.trackmyattack.data.core.CauseData;
import net.berndreiss.trackmyattack.data.core.DataModel;
import net.berndreiss.trackmyattack.data.core.RemedyData;
import net.berndreiss.trackmyattack.data.core.SymptomData;

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

		setBorder(new EmptyBorder(16, 10, 24, 10));
		
		GridBagSettings c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;
		
//		c.fill = GridBagConstraints.VERTICAL;

		// add CAUSES
		LabelPanel causeLabels = new LabelPanel(new CauseData(data));
		add(causeLabels, c);
		c.gridy++;

		// add SYMPTOMS
		LabelPanel symptomLabels = new LabelPanel(new SymptomData(data));
		add(symptomLabels, c);
		c.gridy++;

		//add REMEDIES
		LabelPanel remedyLabels = new LabelPanel(new RemedyData(data));
		add(remedyLabels, c);
		c.gridy++;

		//add GEODATA
		LabelPanel geoLabels = new LabelPanel(new GeoData(data));
		add(geoLabels, c);
		c.gridy++;
		
	}

}
