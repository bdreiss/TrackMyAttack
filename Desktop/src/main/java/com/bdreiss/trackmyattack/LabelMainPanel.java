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
 * Panel that holds all labels, consisting of CAUSES, SYMPTOMS, REMEDIES and GEO_DATA 
 */

public class LabelMainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param data
	 * @param geoData
	 */
	public LabelMainPanel(DataModel data, GeoData geoData) {

		// filler Panel for spacing groups
		class FillPanel extends JPanel {
			private static final long serialVersionUID = 1L;

			public FillPanel(double heightModifier) {
				Dimension fillPanelDimension = new Dimension(Units.LABEL_WIDTH.value(),
						(int) (Units.DATA_ROW_BOX_HEIGHT.value() * heightModifier));
				setMinimumSize(fillPanelDimension);
				setPreferredSize(fillPanelDimension);
				setMaximumSize(fillPanelDimension);
			}
		}

		// height modifiers for filler Panels
		double[] modifiers = new double[5];
		modifiers[0] = geoData==null?0:0.25;
		modifiers[1] = 1;
		modifiers[2] = 1;
		modifiers[3] = geoData==null?0.8:1;
		modifiers[4] = 1;

		setLayout(new GridBagLayout());

		GridBagSettings c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;

		// add filler and CAUSES
		add(new FillPanel(modifiers[0]), c);
		c.gridy++;
		LabelPanel causeLabels = new LabelPanel(new CauseData(data));
		add(causeLabels, c);
		c.gridy++;

		// add filler and SYMPTOMS
		add(new FillPanel(modifiers[1]), c);
		c.gridy++;
		LabelPanel symptomLabels = new LabelPanel(new SymptomData(data));
		add(symptomLabels, c);
		c.gridy++;

		// add filler and REMEDIES
		add(new FillPanel(modifiers[2]), c);
		c.gridy++;
		LabelPanel remedyLabels = new LabelPanel(new RemedyData(data));
		add(remedyLabels, c);
		// add filler
		c.gridy++;
		add(new FillPanel(modifiers[3]), c);
					
		//add GEO_DATA and final filler
		if (geoData != null) {
			c.gridy++;
			LabelPanel geoLabels = new LabelPanel(geoData);
			add(geoLabels, c);
			c.gridy++;
			add(new FillPanel(modifiers[4]), c);
		}
	}

}
