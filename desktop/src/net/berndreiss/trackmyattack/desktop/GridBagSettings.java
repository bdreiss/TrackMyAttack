package net.berndreiss.trackmyattack.desktop;

import java.awt.GridBagConstraints;

/**
 * Standardized GridBagSettings, so that layouts look the same.
 */

public class GridBagSettings extends GridBagConstraints {

	private static final long serialVersionUID = 1L;

	/**
	 * Uniform grid bag settings for DataMainPanel and LabelMainPanel
	 */
	public GridBagSettings() {
		anchor = GridBagConstraints.WEST;

		insets.left = 0;
		insets.right = 0;
		insets.top = 0;
		insets.bottom = 0;

		weightx = 1.0; 
		weighty = 1.0; 
	}

	/**
	 * Sets horizontal padding so that all data is shown.
	 * @param daysSinceStartDate
	 */
	public void setIPadX(int daysSinceStartDate) {
		ipadx = (int) ((Units.DATA_ROW_BOX_WIDTH.value() + Units.SPACE.value()) * (daysSinceStartDate) + 140);

	}

}
