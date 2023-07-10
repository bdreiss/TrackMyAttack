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

import com.bdreiss.dataAPI.AbstractDataModel;

public class PanelWithLabel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public PanelWithLabel(AbstractDataModel data) {

		setLayout(new GridBagLayout());
		
		setSize(super.getWidth(), Dimensions.HEIGHT.value()*(data.getSize()+1));
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridBagLayout());
	
		
		JPanel emptyLabel = new JPanel();
		

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;

		c.insets.left = 0;
		c.insets.right = 0;
		c.insets.top = 0;
		c.insets.bottom = 0;

		c.gridx = 0;
		c.gridy = 0;
		
		labelPanel.add(emptyLabel,c);
		
		Iterator<String> it = data.getKeys();
		
		while (it.hasNext()) {
			c.gridy++;
			
			JLabel label = new JLabel(it.next());
			label.setBackground(Color.WHITE);
			label.setSize(new Dimension(Dimensions.LABEL_WIDTH.value(), Dimensions.HEIGHT.value()));
			labelPanel.add(label, c);
			
		}

		c.gridy = 0;
		add(labelPanel, c);

		
		c.gridx = 1;

		add(new DataPanel(data), c);
	}

	
}
