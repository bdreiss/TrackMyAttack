package net.berndreiss.trackmyattack.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.berndreiss.trackmyattack.data.core.AbstractData;

/**
 * JPanel that holds labels for all keys in Category (Causes, Symptoms, Remedies) represented by AbstractDataModel
 */

public class LabelPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private AbstractData data;//AbstractDataModel for certain Category (see above)
	
	/**
	 * 
	 * @param data
	 */
	public LabelPanel(AbstractData data) {
		this.data = data;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		setLayout(new GridBagLayout());

		//get custom GridBagContstraints
		GridBagConstraints c = new GridBagSettings();

		c.gridx = 0;
		c.gridy = 0;

		//add empty label for date row
		add(new Label(), c);
		
		Iterator<String> it = data.getKeys();
		
		//add all keys as labels
		while (it.hasNext()) {
			c.gridy++;

			JLabel label = new Label(it.next());
			label.setMinimumSize(new Dimension(Units.LABEL_WIDTH.value(), Units.DATA_ROW_BOX_HEIGHT.value()));
			label.setMaximumSize(new Dimension(Units.LABEL_WIDTH.value(), Units.DATA_ROW_BOX_HEIGHT.value()));
			label.setPreferredSize(new Dimension(Units.LABEL_WIDTH.value(), Units.DATA_ROW_BOX_HEIGHT.value()));

			add(label, c);

		}

	}
}
