package main.java.com.bdreiss.trackmyattack.GeoData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import com.bdreiss.dataAPI.core.AbstractData;
import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.DatumWithIntensity;

import main.java.com.bdreiss.trackmyattack.GeoSphereAustria.APIQueryGeoSphereAustria;

/**
 * AbstractCategoryDataModel getting data about the weather (currently only supported for Austria).
 */

public class GeoData extends AbstractData implements Serializable {

	private static final long serialVersionUID = 1L;
	private static String SAVEPATH;

	private GeoDataType[] TYPES_TO_USE = { GeoDataType.TEMPERATURE_MAX, GeoDataType.TEMPERATURE_MEDIAN,
			GeoDataType.TEMPERATURE_MIN, GeoDataType.VAPOR };

	private DataModel originalData;

	/**
	 * 
	 * @param args
	 * @throws TypeMismatchException
	 */
	public static void main(String[] args) throws TypeMismatchException {
		DataModel data = new DataModel();

		class TestCase {

			public LocalDateTime date;
			public Coordinate coords;

			public TestCase(LocalDateTime date, Double x, Double y) {
				this.date = date;
				this.coords = new Coordinate(x, y);
			}
		}

		TestCase[] testCases = { new TestCase(LocalDateTime.now(), 48.187028431591635, 16.313893863502347),//Wien Schönbrunn
				new TestCase(LocalDateTime.now().minusDays(1), 48.187028431591635, 16.313893863502347),//Wien Schönbrunn
				new TestCase(LocalDateTime.now().minusDays(2), 47.07678064842626, 15.42400131089504),//Graz
				new TestCase(LocalDateTime.now().minusDays(3), 47.07678064842626, 15.42400131089504),//Graz
				new TestCase(LocalDateTime.now().minusDays(4), 47.26349950302004, 11.386525823183192),//Innsbruck
				new TestCase(LocalDateTime.now().minusDays(5), 47.26349950302004, 11.386525823183192),//Innsrbruck
				new TestCase(LocalDateTime.now().minusDays(7), 47.26349950302004, 11.386525823183192), };//Innsbruck

		for (int i = 0; i < testCases.length; i++) {
			data.addCause("test", testCases[i].date, testCases[i].coords);
		}

		GeoData geoData = new GeoData(data);
	}

	/**
	 * 
	 * @param originalData
	 */
	public GeoData(DataModel originalData) {

		data = new DataModel();

		this.originalData = originalData;

		if (SAVEPATH != null) {
			SAVEPATH = originalData.getSaveFile().getAbsolutePath() + "Geo";
			File saveFile = new File(SAVEPATH);

			if (saveFile.exists())
				load(saveFile);

		}
		category = Category.CAUSE;

		update();

	}

	/**
	 * 
	 */
	public String print() {
		return data.print();
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

		System.out.println("Test");
		if (SAVEPATH != null)
			save();
	}
	
	private void save() {
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

		GetDataByCountry.getData(startDate, finalDate, this, category);

	}

	// TODO implement add methods so the whole category thing isn't necessary ->
	// also adapt queries to take GeoData instead of GeoDataModel?
	@Override
	public void addKey(String key, boolean intensity) {
	}
	
	@Override
	public DataModel getData() {
		return originalData;
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
	public void addData(String key, Coordinate coordinates) throws TypeMismatchException {
	}

	@Override
	public void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException {
	}

	@Override
	public void removeEntry(String key, LocalDateTime date) {
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
