package net.berndreiss.trackmyattack.desktop;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
	 * @param dataWrapper wrapper including the data model and geo data
	 */
	public LabelMainPanel(DataWrapper dataWrapper) {

		DataModel data = dataWrapper.getData();
		setLayout(new GridBagLayout());

		setBorder(new EmptyBorder(16, 10, 24, 10));
		
		GridBagSettings c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;
		
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
		LabelPanel geoLabels = new LabelPanel(new DataWrapper(data));
		add(geoLabels, c);
		c.gridy++;
		
	}

}
