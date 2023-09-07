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
	
	private LocalDate startDate;

	private DataModel originalData;

	private Point2D.Double coordinates;

	private final APIQuery API_QUERY;
	
	public GeoData(DataModel originalData, Point2D.Double coordinates) throws MalformedURLException {
		startDate = originalData.firstDate;

		data = new DataModel();
		
		this.originalData = originalData;

		SAVEPATH = originalData.getSaveFile().getAbsolutePath() + "Geo";

		category = Category.CAUSE;
		this.coordinates = new Point2D.Double(48.1553234784118, 16.347233789433627);

		File saveFile = new File(SAVEPATH + this.coordinates.x + this.coordinates.y);

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
		//add Migraine data to the loading data model -> the original data for migraines has to be transferred
		//since otherwise migraines can not be shown in the GeoDataPanel
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

		if (data.firstDate == null)
			API_QUERY.parseJSON(API_QUERY.JSONQuery(startDate, LocalDate.now().minusDays(1)), data, category);
		else {

			if (data.firstDate.compareTo(startDate) > 0)
				API_QUERY.parseJSON(API_QUERY.JSONQuery(startDate, LocalDate.now().minusDays(1)), data, category);
			
			LocalDate lastDate = null;

			Iterator<Datum> it;
			try {
				it = data.getCauseData(GeoDataType.HUMIDITY.toString());
				while (it.hasNext())
					lastDate = it.next().getDate().toLocalDate();
				if (lastDate.compareTo(LocalDate.now().minusDays(1)) != 0)
					API_QUERY.parseJSON(API_QUERY.JSONQuery(lastDate, LocalDate.now().minusDays(1)), data, category);

			} catch (EntryNotFoundException e) {
				e.printStackTrace();
			}

		}

		try {
			FileOutputStream fos = new FileOutputStream(SAVEPATH + coordinates.x + coordinates.y);

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



	@Override
	public void addKey(String key, boolean intensity) {}

	@Override
	public Iterator<Datum> getData(String key) throws EntryNotFoundException {
		return data.getCauseData(key);
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getCauseData(key, date);
	}

	@Override
	public void addData(String key, Point2D.Double coordinates) throws TypeMismatchException {}

	@Override
	public void addData(String key, Intensity intensity, Point2D.Double coordinates) throws TypeMismatchException {}

	@Override
	public void removeItem(String key, LocalDateTime date) {}

	@Override
	public void removeKey(String key) {}

	@Override
	public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {}

	@Override
	public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {}

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
