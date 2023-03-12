package main.java.com.bdreiss.trackmyattack;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

class MainFrame extends JFrame{

	private DataPanel dataPanel;
	
	public MainFrame(){
		super("TrackMyAttack");
		
		setLayout(new BorderLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(screenSize);
				
		dataPanel = new DataPanel((int)screenSize.getWidth(), (int) (screenSize.getHeight()*0.8));
		
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
