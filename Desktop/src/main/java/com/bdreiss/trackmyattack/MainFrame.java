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

import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;

class MainFrame extends JFrame{

	
	public MainFrame(DataModel data) throws MalformedURLException{
		super("TrackMyAttack");
		
		
		setLayout(new BorderLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(screenSize);
						
		PanelWithLabel panelWithLabel = new PanelWithLabel(new CauseDataModel(data));

		add(panelWithLabel, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter(){
		
			@Override
			public void windowClosing(WindowEvent e){
			
				System.exit(0);
			
			}
		
		});	

	}
	
}
