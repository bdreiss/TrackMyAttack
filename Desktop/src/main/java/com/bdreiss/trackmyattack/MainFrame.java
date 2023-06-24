package main.java.com.bdreiss.trackmyattack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import main.java.com.bdreiss.dataAPI.CauseDataModel;
import main.java.com.bdreiss.dataAPI.DataModel;

class MainFrame extends JFrame{

	private DataPanel dataPanel;
	
	public MainFrame(DataModel data) throws MalformedURLException{
		super("TrackMyAttack");
		
		
		setLayout(new BorderLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(screenSize);
						
		dataPanel = new DataPanel(new GeoDataModel(new GeoData(LocalDate.now().minusDays(100), null)), (int)screenSize.getWidth(), (int) (screenSize.getHeight()*0.8));

		
		JScrollPane scrollPane = new JScrollPane(dataPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		add(scrollPane, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter(){
		
			@Override
			public void windowClosing(WindowEvent e){
			
				System.exit(0);
			
			}
		
		});	

	}
	
	public void setData(DataModel data) {

		
		//TODO set data in dataPanel
	}
}
