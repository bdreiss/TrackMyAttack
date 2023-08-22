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

	public MainFrame(DataModel data, GeoData geoData) throws MalformedURLException{
		super("TrackMyAttack");
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scrollSize = new Dimension(
				(int) (screenSize.width * 0.8),(data.getSize()+geoData.getSize()+8)*Dimensions.HEIGHT.value());
			
		setSize(screenSize);

		setLayout(new FlowLayout());
		
		LabelFrame labelFrame = new LabelFrame(data, geoData);
		
		labelFrame.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(), scrollSize.height));
		
		add(labelFrame);

		DataPanelFrame dataFrame = new DataPanelFrame(data, geoData);
				
		JScrollPane scrollPane = new JScrollPane(dataFrame,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


		scrollPane.setPreferredSize(scrollSize);
		add(scrollPane);

		addWindowListener(new WindowAdapter(){
		
			@Override
			public void windowClosing(WindowEvent e){
			
				System.exit(0);
			
			}
		
		});	

	}
	
}
