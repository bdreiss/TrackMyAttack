package net.berndreiss.trackmyattack.desktop;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.berndreiss.trackmyattack.data.core.CauseData;
import net.berndreiss.trackmyattack.data.core.DataModel;
import net.berndreiss.trackmyattack.data.core.RemedyData;
import net.berndreiss.trackmyattack.data.core.SymptomData;

/**
 * JPanel representing frame containing data panels.
 *
 */

public class DataMainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new instance of DataMainPanel.
	 * @param dataWrapper wrapper including the data model and geo data
	 */
	public DataMainPanel(DataWrapper dataWrapper) {

		DataModel data = dataWrapper.getData();
		
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

		if (dataWrapper != null) {
			c.gridy++;

			DataPanel geoDataPanel = new DataPanel(dataWrapper);
			add(geoDataPanel, c);
		}
	}

}
