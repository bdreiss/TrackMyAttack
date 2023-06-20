package main.java.com.bdreiss.trackmyattack;

import javax.swing.JPanel;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.util.Datum;
import main.java.com.bdreiss.dataAPI.util.DatumWithIntensity;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

class DataRow extends JPanel{
	
	final int CIRCLE_OFFSET_LOW = 8;
	final int CIRCLE_OFFSET_MEDIUM = 6;
	final int CIRCLE_OFFSET_HIGH = 2;

	
	public DataRow(String key, DataModel data, int size, Color color){

		
		setMinimumSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		setMaximumSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		setPreferredSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets.left = Dimensions.SPACE.value();
		c.insets.right = 0;
		c.insets.bottom = 0;
		c.insets.top = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;

		JLabel label = new JLabel(key);

		
		label.setMinimumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setMaximumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));

		add(label,c);
				
		JPanel dataPanel = new JPanel();
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setVgap(0);
		layout.setHgap(0);

		dataPanel.setLayout(layout);
		

		int dataSize = data.getCausesSize();
				
		
		LocalDate dateCounter = LocalDate.now().minusDays(size);
		


		while (dateCounter.compareTo(LocalDate.now()) <= 0) {


			final LocalDate currentDate = dateCounter;
			
			boolean dataExists = false;
			
			
			try {
				dataExists = data.getCauseData(key, dateCounter).hasNext();
			} catch (EntryNotFoundException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

			if (!dataExists)
				System.out.println(key + " has not data for " + dateCounter.toString());
			final Color colorToSet = dataExists ? color : Colors.EMPTY_COLOR.value();

			JPanel box = new JPanel(){
			
				
				private static final long serialVersionUID = 1L;

				@Override
				public void paint(Graphics graphics){
			
					graphics.setColor(colorToSet);
					
					graphics.fillRect(0,0,Dimensions.WIDTH.value(),Dimensions.HEIGHT.value());
					
					graphics.setColor(new Color(240,240,240));
					
					graphics.drawRect(0, 0, Dimensions.WIDTH.value(), Dimensions.HEIGHT.value()+20);
					
					try {
						if (data.getAilmentData("Migraine", currentDate).hasNext()) {
							
							Iterator<Datum> it = data.getAilmentData("Migraine",currentDate);
							
							Intensity intensity = Intensity.NO_INTENSITY;
							
							while (it.hasNext()) {
								DatumWithIntensity datum = (DatumWithIntensity) it.next();
								if (datum.getIntensity().ordinal() > intensity.ordinal())
									intensity = datum.getIntensity();
							}
							graphics.setColor(Color.RED);
							
							int offset;
							
							switch (intensity) {
							case LOW: offset = CIRCLE_OFFSET_LOW;
									   break;
							case MEDIUM: offset = CIRCLE_OFFSET_MEDIUM;
							   break;
							case HIGH: offset = CIRCLE_OFFSET_HIGH;
							   break;
							 default: offset = 0;


							}
							graphics.fillOval(offset,offset,Dimensions.WIDTH.value()-2*offset,Dimensions.WIDTH.value()-2*offset);
						}
					} catch (EntryNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

				
			};

			
			box.setMinimumSize(new Dimension(Dimensions.WIDTH.value(),Dimensions.HEIGHT.value()));
			box.setMaximumSize(new Dimension(Dimensions.WIDTH.value(),Dimensions.HEIGHT.value()));
			box.setPreferredSize(new Dimension(Dimensions.WIDTH.value(),Dimensions.HEIGHT.value()));
			
			dataPanel.add(box);	
			dateCounter = dateCounter.plusDays(1);
		}

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = Dimensions.RATIO.value();
		c.gridx = 1;
		c.gridy = 0;
		add(dataPanel,c);

		
	} 

	public DataRow(int size){
		
		setPreferredSize(new Dimension(super.getWidth(),Dimensions.HEIGHT.value()));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets.left = Dimensions.SPACE.value();
		c.insets.right = 0;
		c.insets.bottom = 0;
		c.insets.top = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		

		

		JLabel label = new JLabel();

		
		label.setMinimumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setMaximumSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));
		label.setPreferredSize(new Dimension(Dimensions.LABEL_WIDTH.value(),Dimensions.HEIGHT.value()));

		add(label,c);
				
		JPanel dataPanel = new JPanel();
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		layout.setVgap(0);
		layout.setHgap(0);

		dataPanel.setLayout(layout);
			
		ArrayList<JLabel> labels = new ArrayList<>();
		
		if ((size>0) && (size>6)){

			Date date = new Date();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			for (int i = size; i > 0; i -= 7) {
				
				localDate = localDate.minusWeeks(1);
				int month = localDate.getMonthValue();
				int day = localDate.getDayOfMonth();
				JLabel dateLabel = new JLabel(month + "-" + day);
				dateLabel.setMinimumSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
				dateLabel.setMaximumSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
				dateLabel.setPreferredSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
				
				labels.add(dateLabel);
			}
		}
		
		for (int i = labels.size(); i > 0; i--)
			dataPanel.add(labels.get(i-1));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = Dimensions.RATIO.value();
		c.gridx = 1;
		c.gridy = 0;
		add(dataPanel,c);
		
	} 
}
