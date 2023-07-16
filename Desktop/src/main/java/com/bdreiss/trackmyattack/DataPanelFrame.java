package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;

public class DataPanelFrame extends JPanel{
	
	private static final long serialVersionUID = 1L;

	private DataModel data;
	
	public DataPanelFrame(DataModel data) {
		
		this.data = data;
		
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
