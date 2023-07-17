package main.java.com.bdreiss.trackmyattack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

		setLayout(new GridBagLayout());
		GridBagSettings c = new GridBagSettings();
		
		c.gridx=0;
		c.gridy=0;
		
		Dimension scrollSize = new Dimension((int) (screenSize.width*0.8),(data.getSize()+6)*Dimensions.HEIGHT.value());
		
		LabelFrame labelFrame = new LabelFrame(data);
		
		labelFrame.setSize(new Dimension(Dimensions.LABEL_WIDTH.value(), scrollSize.height));
		
		add(labelFrame,c);

		

		DataPanelFrame dataFrame = new DataPanelFrame(data);
				
		JScrollPane scrollPane = new JScrollPane(dataFrame,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


//		scrollSize = new Dimension(1500,1000);
		scrollPane.setPreferredSize(scrollSize);
		c.gridx=1;

		System.out.println(scrollSize);
		add(scrollPane,c);

		addWindowListener(new WindowAdapter(){
		
			@Override
			public void windowClosing(WindowEvent e){
			
				System.exit(0);
			
			}
		
		});	

	}
	
}
