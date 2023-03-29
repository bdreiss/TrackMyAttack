package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Class that represents a data model for keeping track of ailments.
 * 
 * <p>
 * It contains methods to store and retrieve data for causes, symptoms and
 * remedies relating to ailments. The data model represents dates and intensity
 * (see {@link Intensity}) of these fields in a class named {@link Datum}.
 * </p>
 * 
 * @author Bernd Reiß bd_reiss@gmx.at
 */

public class DataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Comparator<String> TREE_COMPARATOR = String.CASE_INSENSITIVE_ORDER;

	private File saveFile;
	private String saveFileName = "DataModel";

	/*
	 * The following Maps contain all the relevant data.
	 * 
	 * The Map contains the description of ailments, causes, symptoms or remedies as
	 * keys. -> The value is a List containing dates and intensity as Datum. The
	 * List is guaranteed to be ordered, since the dates are inserted accordingly.
	 *
	 */
	private Map<String, List<Datum>> ailments = new TreeMap<>(TREE_COMPARATOR);
	private Map<String, List<Datum>> causes = new TreeMap<>(TREE_COMPARATOR);
	private Map<String, List<Datum>> symptoms = new TreeMap<>(TREE_COMPARATOR);
	private Map<String, List<Datum>> remedies = new TreeMap<>(TREE_COMPARATOR);

	public File getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	/**
	 * Creates an instance of DataModel
	 * 
	 * @param savePath String absolute path where the DataModel will be stored
	 */
	public DataModel(String savePath) {
		saveFile = new File(savePath + "/" + saveFileName);
	}

	/**
	 * Adds an ailment with current time stamp and intensity to the data model.
	 * 
	 * @param ailment   String ailment to be added
	 * @param intensity Intensity of ailment
	 * @return LocalDateTime when ailment was added
	 */
	public LocalDateTime addAilment(String ailment, Intensity intensity) {
		LocalDateTime date = LocalDateTime.now();
		addEntry(ailments, ailment, intensity, date);
		return date;
	}

	/**
	 * Adds an ailment with custom time stamp to the data model.
	 * 
	 * @param ailment   String ailment to be added
	 * @param intensity Intensity of ailment
	 * @param date      LocalDateTime when ailment occurred
	 */
	public void addAilment(String ailment, Intensity intensity, LocalDateTime date) {
		addEntry(ailments, ailment, intensity, date);
	}

	/**
	 * Adds a cause with current time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 * @return LocalDateTime when cause was added
	 */
	public LocalDateTime addCause(String cause) {
		LocalDateTime date = LocalDateTime.now();
		addEntry(causes, cause, date);
		return date;
	}

	/**
	 * Adds a cause with custom time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 * @param date  LocalDateTime when the attack occurred
	 */
	public void addCause(String cause, LocalDateTime date) {
		addEntry(causes, cause, date);
	}

	/**
	 * Adds a cause with current time stamp and intensity to the data model.
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @return LocalDateTime when cause was added
	 */
	public LocalDateTime addCause(String cause, Intensity intensity) {
		LocalDateTime date = LocalDateTime.now();
		addEntry(causes, cause, intensity, date);
		return date;
	}

	/**
	 * Adds a cause with custom timestamp and intensity to the data model
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @param date      LocalDateTime when the cause occurred
	 */
	public void addCause(String cause, Intensity intensity, LocalDateTime date) {
		addEntry(causes, cause, intensity, date);
	}

	/**
	 * Adds a sympton with current time stamp to the data model.
	 * 
	 * @param symptom   String description of the symptom
	 * @param intensity Intensity of the symptom
	 * @return LocalDateTime when symptom was added
	 */
	public LocalDateTime addSymptom(String symptom, Intensity intensity) {
		LocalDateTime date = LocalDateTime.now();
		addEntry(symptoms, symptom, intensity, date);
		return date;
	}

	/**
	 * Adds a sympton with custom time stamp to the data model.
	 * 
	 * @param symptom   String description of the symptom
	 * @param intensity Intensity of the symptom
	 * @param date      LocalDateTime when the symptom occurred
	 */
	public void addSymptom(String symptom, Intensity intensity, LocalDateTime date) {
		addEntry(symptoms, symptom, intensity, date);
	}

	/**
	 * Adds remedy with current time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @return LocalDateTime when remedy was added
	 */

	public LocalDateTime addRemedy(String remedy) {
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, date);
		return date;
	}

	/**
	 * Adds remedy with custom time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param date   LocalDateTime when remedy was applied
	 */
	public void addRemedy(String remedy, LocalDateTime date) {
		addEntry(remedies, remedy, date);
	}

	/**
	 * Adds remedy with current time stamp and intensity to the data model.
	 * 
	 * @param remedy    String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @return LocalDateTime when remedy was added
	 */

	public LocalDateTime addRemedy(String remedy, Intensity intensity) {
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, intensity, date);
		return date;
	}

	/**
	 * Adds remedy with custom time stamp and intensity to the data model.
	 * 
	 * @param remedy    String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @param date      LocalDateTime when remedy was applied
	 */

	public void addRemedy(String remedy, Intensity intensity, LocalDateTime date) {
		addEntry(remedies, remedy, intensity, date);
	}

	// abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, List<Datum>> map, String key, LocalDateTime date) {

		// create entry with key if it doesn't exist
		if (!map.containsKey(key))
			map.put(key, new ArrayList<Datum>());

		List<Datum> list = map.get(key);

		// add new Datum to end of list
		list.add(new Datum(date));

		// as long as the date is smaller than the date before, swap it with the
		// previous item
		// stop when dates are smaller than date to be added or when beginning of list
		// is reached
		// this means that the lists are always ordered
		int i = list.size() - 1;
		while (i > 0 && list.get(i).getDate().compareTo(list.get(i - 1).getDate()) < 0) {
			Datum temp = list.get(i);
			list.set(i, list.get(i - 1));
			list.set(i - 1, temp);
			i--;
		}
	}

	// abstracts the task of adding entries with Intensity to the different
	// ArrayLists
	private void addEntry(Map<String, List<Datum>> map, String key, Intensity intensity, LocalDateTime date) {

		// create entry with key if it doesn't exist
		if (!map.containsKey(key))
			map.put(key, new ArrayList<Datum>());

		List<Datum> list = map.get(key);

		// add new Datum to end of list
		list.add(new DatumWithIntensity(date, intensity));

		// as long as the date is smaller than the date before, swap it with the
		// previous item
		// stop when dates are smaller than date to be added or when beginning of list
		// is reached
		// this means that the lists are always ordered
		int i = list.size() - 1;
		while (i > 0 && list.get(i).getDate().compareTo(list.get(i - 1).getDate()) < 0) {
			Datum temp = list.get(i);
			list.set(i, list.get(i - 1));
			list.set(i - 1, temp);
			i--;
		}
	}

	/**
	 * Returns ailments as list iterator.
	 * 
	 * @return Iterator containing ailments as String
	 */
	public Iterator<String> getAilments() {
		return ailments.keySet().iterator();
	}

	/**
	 * Returns all causes as list iterator.
	 *
	 * @return Iterator containing causes as String
	 */
	public Iterator<String> getCauses() {
		return causes.keySet().iterator();
	}

	/**
	 * Returns all symptoms as list iterator.
	 *
	 * @return Iterator containing symptoms as String
	 */
	public Iterator<String> getSymptoms() {
		return symptoms.keySet().iterator();
	}

	/**
	 * Returns all remedies as list iterator.
	 *
	 * @return Iterator containing remedies as String
	 */
	public Iterator<String> getRemedies() {
		return remedies.keySet().iterator();
	}

	/**
	 * Returns the size of list of ailments.
	 * 
	 * @return size of list of ailments
	 */
	public int getAilmentsSize() {
		return ailments.size();
	}

	/**
	 * Returns the size of list of causes.
	 * 
	 * @return size of list of causes
	 */
	public int getCausesSize() {
		return causes.size();
	}

	/**
	 * Returns the size of list of symptoms.
	 * 
	 * @return size of list of symptoms
	 */
	public int getSymptomsSize() {
		return symptoms.keySet().size();
	}

	/**
	 * Returns the size of list of remedies.
	 * 
	 * @return size of list of remedies
	 */
	public int getRemediesSize() {
		return remedies.keySet().size();
	}

	/**
	 * Returns all data when ailment occurred (including LocalDateTime and
	 * Intensity) as Iterator for date.
	 * 
	 * @param ailment String describing ailment
	 * @param date    Day for which data should be retrieved
	 * @return Iterator containing data when and how intense ailment occurred as
	 *         Datum
	 */
	public Iterator<Datum> getAilmentData(String ailment, LocalDate date) {
		return new DayIterator(ailments.get(ailment), date);
	}

	/**
	 * Returns data when ailment occurred (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param ailment String describing ailment
	 * @return Iterator containing data when and how intense ailment occurred as
	 *         Datum
	 */
	public Iterator<Datum> getAilmentData(String ailment) {
		return ailments.get(ailment).iterator();
	}

	/**
	 * Returns all data when cause occurred (including LocalDateTime and Intensity)
	 * as Iterator for date.
	 * 
	 * @param cause String describing cause
	 * @param date  Day for which data should be retrieved
	 * @return Iterator containing data when and how intense cause occurred as Datum
	 */
	public Iterator<Datum> getCauseData(String cause, LocalDate date) {
		return new DayIterator(causes.get(cause), date);
	}

	/**
	 * Returns data when cause occurred (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param cause String describing cause
	 * @return Iterator containing data when and how intense cause occurred as Datum
	 */
	public Iterator<Datum> getCauseData(String cause) {
		return causes.get(cause).iterator();
	}

	/**
	 * Returns all data when symptom occurred (including LocalDateTime and
	 * Intensity) as Iterator for date.
	 * 
	 * @param symptom String describing symptom
	 * @param date    Day for which data should be retrieved
	 * @return Iterator containing data when and how intense symptom occurred as
	 *         Datum
	 */
	public Iterator<Datum> getSymptomData(String symptom, LocalDate date) {
		return new DayIterator(symptoms.get(symptom), date);
	}

	/**
	 * Returns data when symptom occurred (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param symptom String describing symptom
	 * @return Iterator containing data when and how intense symptom occurred as
	 *         Datum
	 */
	public Iterator<Datum> getSymptomData(String symptom) {
		return symptoms.get(symptom).iterator();
	}

	/**
	 * Returns all data when remedy was used (including LocalDateTime and Intensity)
	 * as Iterator for date.
	 * 
	 * @param remedy String describing remedy.
	 * @param date   Day for which data should be retrieved
	 * @return Iterator containing data when and how intense remedy was used as
	 *         Datum
	 */
	public Iterator<Datum> getRemedyData(String remedy, LocalDate date) {
		return new DayIterator(remedies.get(remedy), date);
	}

	/**
	 * Returns data when remedy was used (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param remedy String describing remedy.
	 * @return Iterator containing data when and how intense remedy was used as
	 *         Datum
	 */
	public Iterator<Datum> getRemedyData(String remedy) {
		return remedies.get(remedy).iterator();
	}

	/**
	 * Saves data in save path provided in constructor.
	 */
	public void save() {

		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(this);

			oos.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads data if file exists in save path provided in constructor.
	 */
	public void load() {

		DataModel data = null;

		// abort if it does not exist
		if (!saveFile.exists())
			return;

		try {
			FileInputStream fis = new FileInputStream(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);

			data = (DataModel) ois.readObject();

			ois.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.causes = data.causes;
		this.ailments = data.ailments;
		this.remedies = data.remedies;
		this.symptoms = data.symptoms;
	}

	/**
	 * Deletes the file in save path provided in constructor.
	 */
	public void deleteSaveFile() {
		saveFile.delete();
	}

	// Iterator that iterates over all data in a List<Datum> for one date
	private class DayIterator implements Iterator<Datum> {

		private int index;
		private List<Datum> list;
		private LocalDate date;

		public DayIterator(List<Datum> list, LocalDate date) {
			this.list = list;
			this.date = date;
			index = getStartingIndex(list, 0, list.size() - 1, date);
		}

		// returns the starting index by finding the lowest entry for the given date
		// using binary search
		private int getStartingIndex(List<Datum> list, int b, int e, LocalDate date) {
			if (b > e)
				return list.size() - 1;
			int mid = (b + e) / 2;

			int minIndex = list.size() - 1;

			if (list.get(mid).getDate().toLocalDate().equals(date))
				minIndex = mid;

			if (list.get(mid).getDate().toLocalDate().compareTo(date) >= 0)
				return Math.min(minIndex, getStartingIndex(list, b, mid - 1, date));
			else
				return getStartingIndex(list, mid + 1, e, date);
		}

		// if did not reach end of list and date is still in range return true
		@Override
		public boolean hasNext() {
			if (index == list.size())
				return false;
			if (list.get(index).getDate().toLocalDate().compareTo(date) > 0)
				return false;
			return true;
		}

		@Override
		public Datum next() {
			return list.get(index++);
		}

	}

	/**
	 * Prints all data in the terminal.
	 * 
	 * @return String containing the contents printed to the terminal
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		sb.append(header("Ailments"));
		for (String s : ailments.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(ailments.get(s))) {
				sb.append(d.getDate());
				if (d instanceof DatumWithIntensity)
					sb.append(", " + ((DatumWithIntensity) d).getIntensity());
				else
					sb.append(",");
				sb.append("\n");
			}
		}

		sb.append(header("Causes"));

		for (String s : causes.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(causes.get(s))) {
				sb.append(d.getDate());
				if (d instanceof DatumWithIntensity)
					sb.append(", " + ((DatumWithIntensity) d).getIntensity());
				else
					sb.append(",");
				sb.append("\n");
			}
		}

		sb.append(header("Symptoms"));

		for (String s : symptoms.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(symptoms.get(s))) {
				sb.append(d.getDate());
				if (d instanceof DatumWithIntensity)
					sb.append(", " + ((DatumWithIntensity) d).getIntensity());
				else
					sb.append(",");
				sb.append("\n");
			}

		}

		sb.append(header("Remedies"));

		for (String s : remedies.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(remedies.get(s))) {
				sb.append(d.getDate());
				if (d instanceof DatumWithIntensity)
					sb.append(", " + ((DatumWithIntensity) d).getIntensity());
				else
					sb.append(",");
				sb.append("\n");
			}

		}
		sb.append("\n");
		System.out.println(sb.toString());
		return sb.toString();

	}

	private String header(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n----------------------------------------------------\n");
		sb.append(title);
		sb.append("\n");
		sb.append("----------------------------------------------------\n");
		return sb.toString();

	}

}
