package main.java.com.bdreiss.trackmyattack;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;

public class DataPanelFrame extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public DataPanelFrame(DataModel data) {
		
		setLayout(new GridBagLayout());
		
		setBorder(new EmptyBorder(10,10,10,10));
		GridBagSettings c = new GridBagSettings();
		
		c.gridx = 0;
		c.gridy = 0;
		
		DataPanel causeData = new DataPanel(new CauseDataModel(data)); 
		add(causeData, c);
		
		c.gridy++;
		
		DataPanel symptomData = new DataPanel(new SymptomDataModel(data));
		add(symptomData,c);
		
		c.gridy++;
			
		DataPanel remedyData = new DataPanel(new RemedyDataModel(data)); 
		add(remedyData,c);
	}

	
}
