package main.java.com.bdreiss.trackmyattack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import javax.swing.JFrame;

import main.java.com.bdreiss.dataAPI.Category;
import main.java.com.bdreiss.dataAPI.DataModel;

class MainFrame extends JFrame{

	private DataPanel dataPanel;
	
	public MainFrame(DataModel data){
		super("TrackMyAttack");
		
		
		setLayout(new BorderLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(screenSize);
						
		dataPanel = new DataPanel(data, (int)screenSize.getWidth(), (int) (screenSize.getHeight()*0.8));
		
		add(dataPanel, BorderLayout.CENTER);

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
