package main.java.com.bdreiss.trackmyattack;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;

public class LabelFrame extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public LabelFrame(DataModel data) {

		class FillPanel extends JPanel{
			private static final long serialVersionUID = 1L;

			public FillPanel(double heightModifier) {
				Dimension fillPanelDimension = new Dimension(Dimensions.LABEL_WIDTH.value(), (int) (Dimensions.HEIGHT.value()*heightModifier));				
				setPreferredSize(fillPanelDimension);
			
			}
		}
		
		double[] modifiers = {2,0.5,0.5,1};
		
		setLayout(new GridBagLayout());
		

		GridBagSettings c = new GridBagSettings();
		
		c.gridx = 0;
		c.gridy = 0;

//		add(new FillPanel(modifiers[0]),c);	
//		c.gridy++;

		LabelPanel causeLabels = new LabelPanel(new CauseDataModel(data));

		add(causeLabels, c);
		
		c.gridy++;
		add(new FillPanel(modifiers[1]),c);
		
		c.gridy++;

		LabelPanel symptomLabels = new LabelPanel(new SymptomDataModel(data));
		
		add(symptomLabels, c);
		
		c.gridy++;
		add(new FillPanel(modifiers[2]),c);
		
		c.gridy++;

		LabelPanel remedyLabels = new LabelPanel(new RemedyDataModel(data));
		
		add(remedyLabels, c);

		c.gridy++;
		add(new FillPanel(modifiers[3]),c);
	
	}

}
