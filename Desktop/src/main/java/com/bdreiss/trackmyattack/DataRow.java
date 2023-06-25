package main.java.com.bdreiss.trackmyattack;

import javax.swing.JPanel;

import main.java.com.bdreiss.dataAPI.AbstractDataModel;
import main.java.com.bdreiss.dataAPI.AilmentDataModel;
import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.util.Datum;
import main.java.com.bdreiss.dataAPI.util.DatumWithIntensity;
import main.java.com.bdreiss.dataAPI.util.IteratorWithIntensity;

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

	
	public DataRow(String key, AbstractDataModel data, AilmentDataModel ailments, LocalDate startDate, Color[] colorSet) throws EntryNotFoundException{

		
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
						
		
		LocalDate dateCounter = startDate;
		
		
		while (dateCounter.compareTo(LocalDate.now()) <= 0) {

			final LocalDate currentDate = dateCounter;
			
			Color colorToSet = Colors.EMPTY_COLOR.value();
			
			Iterator<Datum> it = data.getData(key, currentDate);
			
			Intensity intensity = Intensity.NO_INTENSITY;
			
			while (it.hasNext()) {
				
				if (it instanceof IteratorWithIntensity) {
					DatumWithIntensity datum = (DatumWithIntensity) it.next();
					if (datum.getIntensity().compareTo(intensity)>0)
						intensity = datum.getIntensity();
				}
				else {
					intensity = Intensity.MEDIUM;
					it.next();
				}
				
			}

			if (intensity == Intensity.LOW)
				colorToSet = colorSet[0];
			else if (intensity == Intensity.MEDIUM)
				colorToSet = colorSet[1];
			else if (intensity == Intensity.HIGH)
				colorToSet = colorSet[2];


			final Color colorToSetFinal = colorToSet;

			JPanel box = new JPanel(){
			
				
				private static final long serialVersionUID = 1L;

				@Override
				public void paint(Graphics graphics){
			
					graphics.setColor(colorToSetFinal);
					
					graphics.fillRect(0,0,Dimensions.WIDTH.value(),Dimensions.HEIGHT.value());
					
					graphics.setColor(new Color(240,240,240));
					
					graphics.drawRect(0, 0, Dimensions.WIDTH.value(), Dimensions.HEIGHT.value()+20);
					
					try {
						if (ailments.getEntry(currentDate).hasNext()) {
							
							IteratorWithIntensity it = ailments.getEntry(currentDate);
							
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

	public DataRow(LocalDate startDate){
		
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
			
		LocalDate dateCounter = startDate;
				
		while (dateCounter.compareTo(LocalDate.now())<=0) {
			
			
			
			int month = dateCounter.getMonthValue();
			int day = dateCounter.getDayOfMonth();
			JLabel dateLabel = new JLabel(month + "-" + day);
			dateLabel.setMinimumSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
			dateLabel.setMaximumSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
			dateLabel.setPreferredSize(new Dimension(Dimensions.WIDTH.value()*7,Dimensions.HEIGHT.value()));
			
			dataPanel.add(dateLabel);
			
			dateCounter = dateCounter.plusWeeks(1);
			
		}
		
		c.weightx = Dimensions.RATIO.value();
		c.gridx = 1;
		c.gridy = 0;
		add(dataPanel,c);
		
	} 
}