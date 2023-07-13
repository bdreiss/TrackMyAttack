package main.java.com.bdreiss.trackmyattack;

import java.awt.GridBagConstraints;

public class GridBagSettings extends GridBagConstraints {

	public GridBagSettings() {
		anchor = GridBagConstraints.WEST;
		
		insets.left = 0;
		insets.right = 0;
		insets.top = 0;
		insets.bottom = 0;

		
	}
	
	public void setIPadX(int daysSinceStartDate) {
		ipadx = (int) ((Dimensions.WIDTH.value()+Dimensions.SPACE.value())*(daysSinceStartDate)*1.048);

	}
	
	
}

