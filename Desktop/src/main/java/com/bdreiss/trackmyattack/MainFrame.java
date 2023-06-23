package main.java.com.bdreiss.trackmyattack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import main.java.com.bdreiss.dataAPI.CauseDataModel;
import main.java.com.bdreiss.dataAPI.DataModel;

class MainFrame extends JFrame{

	private DataPanel dataPanel;
	
	public MainFrame(DataModel data){
		super("TrackMyAttack");
		
		
		setLayout(new BorderLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(screenSize);
						
		dataPanel = new DataPanel(new CauseDataModel(data), (int)screenSize.getWidth(), (int) (screenSize.getHeight()*0.8));

		
		JScrollPane scrollPane = new JScrollPane(dataPanel);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);;
		
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
