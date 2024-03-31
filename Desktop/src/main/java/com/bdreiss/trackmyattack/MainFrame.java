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

import com.bdreiss.data.core.DataModel;

import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;

class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int DATA_PANELS_OFFSET = 8;
	private static final double SCREENSIZE_MODIFIER = 0.8;

	/*
	 * Main Frame consisting of a Panel with all labels and a ScrollPane with all
	 * the data.
	 */

	public MainFrame(DataModel data, GeoData geoData) throws MalformedURLException {
		super("TrackMyAttack");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// dimension of ScrollPanel that holds the data
		// width is defined by screenSize * SCREENSIZE_MODIFIER
		// height is defined by (items in (data + geoData + DATA_PANELS_OFFSET) *
		// (height of data row)
		
		int scrollHeight = (data.getSize() + (geoData == null ? 0 : geoData.getSize()) + DATA_PANELS_OFFSET) * Units.DATA_ROW_BOX_HEIGHT.value();
		
		Dimension scrollSize = new Dimension((int) (screenSize.width * SCREENSIZE_MODIFIER),
				scrollHeight);

		setSize(screenSize);

		setLayout(new FlowLayout(FlowLayout.LEFT));

		// Panel that holds all labels -> it is in a separate frame from data as not to
		// move with scroll bar
		LabelMainPanel labelMainPanel = new LabelMainPanel(data, geoData);
		labelMainPanel.setPreferredSize(new Dimension(Units.LABEL_WIDTH.value(), scrollHeight));

		JScrollPane scrollPaneLabel = new JScrollPane(labelMainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		scrollPaneLabel.setPreferredSize(new Dimension(Units.LABEL_WIDTH.value(), scrollHeight));
		add(scrollPaneLabel);
//		add(labelMainPanel);

		DataMainPanel dataMainPanel = new DataMainPanel(data, geoData);

		JScrollPane scrollPane = new JScrollPane(dataMainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		scrollPane.setPreferredSize(scrollSize);
		add(scrollPane);

		// close program when x is pressed
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				System.exit(0);

			}

		});

	}

}
