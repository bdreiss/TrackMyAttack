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
	
		labelPanel.setSize(new Dimension(Dimensions.LABEL_WIDTH.value(), Dimensions.HEIGHT.value()*(data.getSize()+1)));
		
		JPanel emptyLabel = new JPanel();
		
		emptyLabel.setSize(new Dimension(Dimensions.WIDTH.value(), Dimensions.HEIGHT.value()));
		

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.ipadx = Dimensions.LABEL_WIDTH.value();
		
		c.insets.left = 0;
		c.insets.right = 0;
		c.insets.top = 0;
		c.insets.bottom = 0;

		c.gridx = 0;
		c.gridy = 0;

		add(labelPanel, c);
		
		labelPanel.add(emptyLabel,c);
		
		Iterator<String> it = data.getKeys();
		
		while (it.hasNext()) {
			c.gridy++;
			
			JLabel label = new JLabel(it.next());
			label.setBackground(Color.WHITE);
			label.setSize(new Dimension(Dimensions.LABEL_WIDTH.value(), Dimensions.HEIGHT.value()));
			labelPanel.add(label, c);
			
		}

		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 1000;
		c.ipady = labelPanel.getHeight();

		JScrollPane scrollPane = new JScrollPane(new DataPanel(data), ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(new Dimension(100,100));
		add(scrollPane, c);
	}

	
}
