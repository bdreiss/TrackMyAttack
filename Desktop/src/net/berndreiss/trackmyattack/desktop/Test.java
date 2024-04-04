package net.berndreiss.trackmyattack.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import net.berndreiss.trackmyattack.data.core.AbstractData;
import net.berndreiss.trackmyattack.data.core.DataModel;
import net.berndreiss.trackmyattack.data.core.RemedyData;
import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;

/**
 * 
 */
public class Test {
	
	/**
	 * 
	 * @param args
	 * @throws EntryNotFoundException 
	 */
	public static void main(String[] args) throws EntryNotFoundException {

		ArrayList<Color[]> colorSets = new ArrayList<>();

		
		Color[] blues = { new Color(65, 105, 225), Color.BLUE, Color.BLUE.darker() };
		Color[] greens = { new Color(93, 255, 54), new Color(45, 221, 12), Color.GREEN.darker() };
		Color[] yellows = { new Color(255, 255, 159), Color.YELLOW, Color.YELLOW.darker() };

		colorSets.add(blues);
		colorSets.add(greens);
		colorSets.add(yellows);
		
		
		DataModel data = new DataModel(System.getProperty("user.home") + "/Apps/TrackMyAttack");
		JFrame frame = new JFrame();
		AbstractData absData = new RemedyData(data);
		
		DataPanel panel = new DataPanel(absData);
				
		frame.add(panel);
		frame.setSize(new Dimension(300,300));
		frame.setVisible(true);
	}

}
