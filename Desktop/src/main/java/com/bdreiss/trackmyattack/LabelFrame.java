package main.java.com.bdreiss.trackmyattack;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;

public class LabelFrame extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public LabelFrame(DataModel data) {
		
		setLayout(new GridBagLayout());
		
		GridBagSettings c = new GridBagSettings();
		
		c.gridx = 0;
		c.gridy = 0;
		
		LabelPanel causeLabels = new LabelPanel(new CauseDataModel(data));

		add(causeLabels, c);
		
		c.gridy++;
		
		LabelPanel symptomLabels = new LabelPanel(new SymptomDataModel(data));
		
		add(symptomLabels, c);
		
		c.gridy++;
		
		LabelPanel remedyLabels = new LabelPanel(new RemedyDataModel(data));
		
		add(remedyLabels, c);
	}

}
