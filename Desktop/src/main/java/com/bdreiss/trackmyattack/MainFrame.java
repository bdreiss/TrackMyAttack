package main.java.com.bdreiss.trackmyattack;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.bdreiss.dataAPI.DataModel;

class MainFrame extends JFrame{

	
	private static final long serialVersionUID = 1L;

	public MainFrame(DataModel data) throws MalformedURLException{
		super("TrackMyAttack");
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension scrollSize = new Dimension((int) (screenSize.width*0.8),(data.getSize()+6)*Dimensions.HEIGHT.value());
			
		setSize(screenSize);

		setLayout(new GridBagLayout());
		GridBagSettings c = new GridBagSettings();
		
		c.gridx=0;
		c.gridy=0;
		
	
		c.ipady = scrollSize.height;		
		c.ipadx = Dimensions.LABEL_WIDTH.value();
		
		LabelFrame labelFrame = new LabelFrame(data);
		
		labelFrame.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(), scrollSize.height));
		
		add(labelFrame,c);


		c.ipadx = scrollSize.width+20;

		DataPanelFrame dataFrame = new DataPanelFrame(data);
				
		JScrollPane scrollPane = new JScrollPane(dataFrame,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


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
