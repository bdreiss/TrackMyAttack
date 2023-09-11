package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.DatumWithIntensity;

/*
 * AbstractDataModel getting data about the weather (currently only supported for Austria).
 */

public class GeoData extends AbstractDataModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private static String SAVEPATH;

	private GeoDataType[] TYPES_TO_USE = { GeoDataType.TEMPERATURE_MAX, GeoDataType.TEMPERATURE_MEDIAN,
			GeoDataType.TEMPERATURE_MIN, GeoDataType.VAPOR };

	private DataModel originalData;

	private final APIQuery API_QUERY;

	public GeoData(DataModel originalData, Point2D.Double coordinates) throws MalformedURLException {

		data = new DataModel();

		this.originalData = originalData;

		SAVEPATH = originalData.getSaveFile().getAbsolutePath() + "Geo";

		category = Category.CAUSE;

		File saveFile = new File(SAVEPATH);

		API_QUERY = new APIQueryAustria();

		if (saveFile.exists())
			load(saveFile);

		update();

	}

	public void print() {
		data.print();
	}

	private void load(File saveFile) {

		try {
			FileInputStream fis = new FileInputStream(saveFile);

			ObjectInputStream ois = new ObjectInputStream(fis);

			data = (DataModel) ois.readObject();

			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		data.removeAilmentKey("Migraine");
		// add Migraine data to the loading data model -> the original data for
		// migraines has to be transferred
		// since otherwise migraines can not be shown in the GeoDataPanel
		data.addAilmentKey("Migraine");

		try {
			Iterator<Datum> it = originalData.getAilmentData("Migraine");

			while (it.hasNext()) {
				DatumWithIntensity migraine = (DatumWithIntensity) it.next();
				data.addAilment("Migraine", migraine.getIntensity(), migraine.getDate(), null);
			}

		} catch (EntryNotFoundException e) {
			e.printStackTrace();
		} catch (TypeMismatchException e) {
			e.printStackTrace();
		}
	}

	private void update() {

		// if there is no firstDate in the original data, there are no entries and the
		// method returns
		if (originalData.firstDate == null)
			return;

		LocalDate startDate = originalData.firstDate;

		// if there are no entries for GeoData, start with the first date in the
		// original data
		if (data.firstDate == null) {
			updateRange(startDate, LocalDate.now());
		} else {

			// if the first date in GeoData is bigger than the start date in the original
			// data, get GeoData for this span
			if (data.firstDate.compareTo(startDate) > 0) {
				updateRange(startDate, data.firstDate.minusDays(1));
				startDate = data.firstDate;
			}

			Iterator<Datum> it;

			try {
				it = data.getCauseData(GeoDataType.HUMIDITY.toString());
				while (it.hasNext())
					startDate = it.next().getDate().toLocalDate();
				
				updateRange(startDate, LocalDate.now());
				
			} catch (EntryNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(SAVEPATH);

			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(data);

			oos.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void updateRange(LocalDate startDate, LocalDate finalDate) {
		LocalDate endDate = startDate;

		Point2D.Double currentCoordinates = data.getCoordinatesMean(startDate);
		
		while (startDate.compareTo(finalDate) <= 0) {
			if (!currentCoordinates.equals(data.getCoordinatesMean(endDate)) && !(currentCoordinates == null)) {
				GetDataByCountry.getData(startDate, endDate, currentCoordinates, originalData,
						category);
				startDate = endDate;
			}

			currentCoordinates = data.getCoordinatesMean(endDate);
			endDate = endDate.plusDays(1);
		}

	}

	@Override
	public void addKey(String key, boolean intensity) {
	}

	@Override
	public Iterator<Datum> getData(String key) throws EntryNotFoundException {
		return data.getCauseData(key);
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getCauseData(key, date);
	}

	@Override
	public void addData(String key, Point2D.Double coordinates) throws TypeMismatchException {
	}

	@Override
	public void addData(String key, Intensity intensity, Point2D.Double coordinates) throws TypeMismatchException {
	}

	@Override
	public void removeItem(String key, LocalDateTime date) {
	}

	@Override
	public void removeKey(String key) {
	}

	@Override
	public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
	}

	@Override
	public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
	}

	@Override
	public int getSize() {
		return TYPES_TO_USE.length;
	}

	@Override
	public float count(String key, LocalDate date) throws EntryNotFoundException {
		Iterator<Datum> it = data.getCauseData(key, date);

		return it.hasNext() ? ((GeoDatum) it.next()).getValue() : 0;
	}

	@Override
	public float getMedium(String key) throws EntryNotFoundException {
		int days = 0;

		float total = 0;
		Iterator<Datum> it = data.getCauseData(key);

		while (it.hasNext()) {
			days++;
			total += ((GeoDatum) it.next()).getValue();
		}

		return days == 0 ? 0 : total / days;

	}

	@Override
	public Iterator<String> getKeys() {
		ArrayList<String> keys = new ArrayList<>();
		for (GeoDataType g : TYPES_TO_USE)
			keys.add(g.toString());

		return keys.iterator();
	}

}
