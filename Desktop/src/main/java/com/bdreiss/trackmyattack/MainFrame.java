package main.java.com.bdreiss.trackmyattack;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.bdreiss.dataAPI.DataModel;

import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;

class MainFrame extends JFrame{

	
	private static final long serialVersionUID = 1L;

	private int DATA_PANELS_OFFSET = 8;
	private double SCREENSIZE_MODIFIER = 0.8;
	
	/*
	 * Main Frame consisting of a Panel with all labels and a ScrollPane with all the data.
	 */
	
	public MainFrame(DataModel data, GeoData geoData) throws MalformedURLException{
		super("TrackMyAttack");
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//dimension of ScrollPanel that holds the data
		//width is defined by screenSize * SCREENSIZE_MODIFIER
		//height is defined by (items in (data + geoData + DATA_PANELS_OFFSET) * (height of data row)
		Dimension scrollSize = new Dimension(
				(int) (screenSize.width * SCREENSIZE_MODIFIER),(data.getSize()+geoData.getSize()+DATA_PANELS_OFFSET)*Dimensions.DATA_ROW_HEIGHT.value());
			
		setSize(screenSize);

		setLayout(new FlowLayout());
		
		//Panel that holds all labels -> it is in a separate frame from data as not to move with scroll bar
		LabelMainPanel labelMainPanel = new LabelMainPanel(data, geoData);
		labelMainPanel.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(), scrollSize.height));
		
		add(labelMainPanel);

		DataMainPanel dataMainPanel = new DataMainPanel(data, geoData);
				
		JScrollPane scrollPane = new JScrollPane(dataMainPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		scrollPane.setPreferredSize(scrollSize);
		add(scrollPane);

		//close program when x is pressed
		addWindowListener(new WindowAdapter(){
		
			@Override
			public void windowClosing(WindowEvent e){
			
				System.exit(0);
			
			}
		
		});	

	}
	
}
