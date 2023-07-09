package main.java.com.bdreiss.trackmyattack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;

class MainFrame extends JFrame{

	
	private static final long serialVersionUID = 1L;

	public MainFrame(DataModel data) throws MalformedURLException{
		super("TrackMyAttack");
		
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(screenSize);
		
		setLayout(new BorderLayout());
				
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		
		JScrollPane scrollPane = new JScrollPane(centerPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(screenSize);

		
		
		
		PanelWithLabel causeDataPanel = new PanelWithLabel(new CauseDataModel(data));
		

		c.gridx = 0;
		c.gridy = 0;

		
		
		centerPanel.add(causeDataPanel,c);

		c.gridy = 1;
		
		centerPanel.add(new PanelWithLabel(new SymptomDataModel(data)),c);
		
		c.gridy = 2;
		
		centerPanel.add(new PanelWithLabel(new RemedyDataModel(data)),c);

		add(scrollPane, BorderLayout.CENTER);

		
		addWindowListener(new WindowAdapter(){
		
			@Override
			public void windowClosing(WindowEvent e){
			
				System.exit(0);
			
			}
		
		});	

	}
	
}
