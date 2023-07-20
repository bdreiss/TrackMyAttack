package main.java.com.bdreiss.trackmyattack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bdreiss.dataAPI.AbstractDataModel;

public class LabelPanel extends JPanel {
	
	private AbstractDataModel data;
	
	public LabelPanel(AbstractDataModel data) {
		this.data = data;
	}
	
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		setLayout(new GridBagLayout());
	
		Dimension labelDimension = new Dimension(Dimensions.LABEL_WIDTH.value(), Dimensions.HEIGHT.value());

		
		JPanel emptyLabel = new JPanel();
		emptyLabel.setPreferredSize(labelDimension);
	
		GridBagConstraints c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;

		
		add(emptyLabel,c);
		
		Iterator<String> it = data.getKeys();
		
		while (it.hasNext()) {
			c.gridy++;
			
			JLabel label = new Label(it.next());
			add(label, c);

		}

	}
}
