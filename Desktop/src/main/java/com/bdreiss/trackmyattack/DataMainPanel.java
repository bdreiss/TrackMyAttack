package main.java.com.bdreiss.trackmyattack;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;

import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;

/**
 * 	JPanel representing frame containing data panels.  
 *
 */

public class DataMainPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param data
	 * @param geoData
	 */
	public DataMainPanel(DataModel data, GeoData geoData) {
		
		setLayout(new GridBagLayout());
		
		setBorder(new EmptyBorder(10,10,10,10));
		GridBagSettings c = new GridBagSettings();
		
		c.gridx = 0;
		c.gridy = 0;
		
		DataPanel causeDataPanel = new DataPanel(new CauseDataModel(data)); 
		add(causeDataPanel, c);
		
		c.gridy++;
		
		DataPanel symptomDataPanel = new DataPanel(new SymptomDataModel(data));
		add(symptomDataPanel,c);
		
		c.gridy++;
			
		DataPanel remedyDataPanel = new DataPanel(new RemedyDataModel(data)); 
		add(remedyDataPanel,c);


		if (geoData != null) {
			c.gridy++;
		
			DataPanel geoDataPanel = new DataPanel(geoData);
			add(geoDataPanel,c);
		}
	}

	
}
