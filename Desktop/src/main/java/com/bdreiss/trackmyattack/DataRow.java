package main.java.com.bdreiss.trackmyattack;

import javax.swing.JPanel;

import com.bdreiss.dataAPI.core.AbstractData;
import com.bdreiss.dataAPI.core.AilmentData;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.DatumWithIntensity;
import com.bdreiss.dataAPI.util.IteratorWithIntensity;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;

/*
 * JPanel that represents a data row if data is passed to the constructor.
 * In case there is no data passed to the constructor it represents a date row (having dates for every 7 days).
 */
class DataRow extends JPanel {

	private static final long serialVersionUID = 1L;

	// offsets for intensity of attacks
	final int CIRCLE_OFFSET_LOW = 8;
	final int CIRCLE_OFFSET_MEDIUM = 6;
	final int CIRCLE_OFFSET_HIGH = 2;
	
	//thresholds for intensity based on occurrences on a given date
	//if its > x*THRESHOLD_HIGH -> intensity = high
	//if its < x*THRESHOLD_LOW -> intensity = low
	//intensity = medium otherwise
	final double THRESHOLD_HIGH = 2;
	final double THRESHOLD_LOW = 0.5;
	
	

	public DataRow(String key, AbstractData data, AilmentData ailments, Color[] colorSet)
			throws EntryNotFoundException {

		setPreferredSize(new Dimension(super.getWidth(), Dimensions.DATA_ROW_BOX_HEIGHT.value()));

		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);

		// remove gaps between boxes
		layout.setVgap(0);
		layout.setHgap(0);

		setLayout(layout);

		// date to start from
		LocalDate dateCounter = data.getFirstDate();

		// mean occurrences (needed for defining intensity if data set is not of type
		// DatumWithIntensity)
		float mean = 0;
		if (!(data.getData(key) instanceof IteratorWithIntensity))
			mean = data.getMedium(key);

		// add data until today is reached
		while (dateCounter.compareTo(LocalDate.now()) <= 0) {

			final LocalDate currentDate = dateCounter;

			Color colorToSet = Colors.EMPTY_COLOR.value();

			Iterator<Datum> it = data.getData(key, currentDate);

			Intensity intensity = Intensity.NO_INTENSITY;

			// if data is of type DatumWithIntensity get maximum intensity for date
			if (it instanceof IteratorWithIntensity) {

				while (it.hasNext()) {
					DatumWithIntensity datum = (DatumWithIntensity) it.next();
					if (datum.getIntensity().compareTo(intensity) > 0)
						intensity = datum.getIntensity();
				}
			} 
			
			//otherwise 
			else {
				float countToday = data.count(key, currentDate);

				float intensityCount = countToday / mean;

				if (countToday > 0) {
					if (intensityCount > THRESHOLD_HIGH)
						intensity = Intensity.HIGH;
					else if (intensityCount < THRESHOLD_LOW)
						intensity = Intensity.LOW;
					else
						intensity = Intensity.MEDIUM;
				}

			}

			//set color according to intensity
			if (intensity == Intensity.LOW)
				colorToSet = colorSet[0];
			else if (intensity == Intensity.MEDIUM)
				colorToSet = colorSet[1];
			else if (intensity == Intensity.HIGH)
				colorToSet = colorSet[2];

			//because we need to create another JPanel for the box representing the Datum for today
			//we create a final variable to pass it to the box
			final Color colorToSetFinal = colorToSet;

			JPanel box = new JPanel() {

				private static final long serialVersionUID = 1L;

				@Override
				public void paint(Graphics graphics) {

					//set box to designated color
					graphics.setColor(colorToSetFinal);
					graphics.fillRect(0, 0, Dimensions.DATA_ROW_BOX_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value());

					//add grid border
					graphics.setColor(Colors.GRID_COLOR.value());
					graphics.drawRect(0, 0, Dimensions.DATA_ROW_BOX_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value() + 20);

					//check if attack occurred and paint circle according to intensity if so
					try {
						
						//if there is a data set for date, add intensity
						if (ailments.getEntry(currentDate).hasNext()) {

							IteratorWithIntensity it = ailments.getEntry(currentDate);
							Intensity intensity = Intensity.NO_INTENSITY;

							//get maximum intensity
							while (it.hasNext()) {
								DatumWithIntensity datum = (DatumWithIntensity) it.next();
								if (datum.getIntensity().ordinal() > intensity.ordinal())
									intensity = datum.getIntensity();
							}
							
							graphics.setColor(Color.RED);

							//set offset
							int offset;
							switch (intensity) {
							case LOW:
								offset = CIRCLE_OFFSET_LOW;
								break;
							case MEDIUM:
								offset = CIRCLE_OFFSET_MEDIUM;
								break;
							case HIGH:
								offset = CIRCLE_OFFSET_HIGH;
								break;
							default:
								offset = 0;

							}
							
							//paint circle
							graphics.fillOval(offset, offset, Dimensions.DATA_ROW_BOX_WIDTH.value() - 2 * offset,
									Dimensions.DATA_ROW_BOX_WIDTH.value() - 2 * offset);
						}
					} catch (EntryNotFoundException e) {
						e.printStackTrace();
					}

				}

			};

			//set box to fixed size and add it
			box.setMinimumSize(new Dimension(Dimensions.DATA_ROW_BOX_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value()));
			box.setMaximumSize(new Dimension(Dimensions.DATA_ROW_BOX_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value()));
			box.setPreferredSize(new Dimension(Dimensions.DATA_ROW_BOX_WIDTH.value(), Dimensions.DATA_ROW_BOX_HEIGHT.value()));
			add(box);
			
			//date++
			dateCounter = dateCounter.plusDays(1);

		}

	}

	//if only date is passed to the constructor create a date row
	public DataRow(LocalDate startDate) {

		setPreferredSize(new Dimension(super.getWidth(), Dimensions.DATA_ROW_BOX_HEIGHT.value()));

		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		
		//remove gaps
		layout.setVgap(0);
		layout.setHgap(0);

		setLayout(layout);

		LocalDate dateCounter = startDate;

		//from start date to today add date every 7 days
		while (dateCounter.compareTo(LocalDate.now()) <= 0) {

			int month = dateCounter.getMonthValue();
			int day = dateCounter.getDayOfMonth();
			
			JLabel dateLabel = new JLabel(month + "-" + day);

			//set label to fixed size
			dateLabel.setMinimumSize(
					new Dimension(Dimensions.DATA_ROW_BOX_WIDTH.value() * 7, Dimensions.DATA_ROW_BOX_HEIGHT.value()));
			dateLabel.setMaximumSize(
					new Dimension(Dimensions.DATA_ROW_BOX_WIDTH.value() * 7, Dimensions.DATA_ROW_BOX_HEIGHT.value()));
			dateLabel.setPreferredSize(
					new Dimension(Dimensions.DATA_ROW_BOX_WIDTH.value() * 7, Dimensions.DATA_ROW_BOX_HEIGHT.value()));

			add(dateLabel);

			//date++
			dateCounter = dateCounter.plusWeeks(1);

		}

	}
}