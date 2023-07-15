package main.java.com.bdreiss.trackmyattack;

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
		
		
		Box box = Box.createVerticalBox();
				
		box.add(new LabelPanel(new CauseDataModel(data)));
		//box.add(new LabelPanel(new SymptomDataModel(data)));
		//box.add(new LabelPanel(new RemedyDataModel(data)));
		add(box);
	}

}
