package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataModel implements Serializable {

	/**
	 * Class that represents the data model.
	 * 
	 * @author Bernd Reiß
	 */

	/*
	 * Representation of a single piece of data containing the date and intensity of
	 * the habit/symptom.
	 */

	private class Datum implements Serializable {
		private static final long serialVersionUID = 1L;
		public Date date;
		public Intensity intensity;

		public Datum(Date date, Intensity intensity) {
			this.date = date;
			this.intensity = intensity;
		}

	}

	/*
	 * The following Maps contain all the relevant data. They each contain the title
	 * of the habit/symptom as a key and an ArrayList containing all relevant
	 * information as single pieces of data (see Datum).
	 */

	private static final long serialVersionUID = 1L;

	private File saveFile;
	private String saveFileName = "DataModel";

	// ArrayList containing all instances of a migraine
	private Map<String, ArrayList<Datum>> migraines = new HashMap<>();
	// ArrayList containing habits that might be related to migraine attacks
	private Map<String, ArrayList<Datum>> habits = new HashMap<>();
	// ArrayList containing symptoms that might be related to migraine attacks
	private Map<String, ArrayList<Datum>> symptoms = new HashMap<>();
	// ArrayList containing potential remedies tried when symptoms showed up
	private Map<String, ArrayList<Datum>> remedies = new HashMap<>();

	// ArrayList containing all habits
	private ArrayList<String> habitsList = new ArrayList<>();
	// ArrayList containing all symptoms
	private ArrayList<String> symptomsList = new ArrayList<>();
	// ArrayList containing all remedies
	private ArrayList<String> remediesList = new ArrayList<>();

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
	 * @param savePath an absolute path where the DataModel will be stored
	 */
	public DataModel(String savePath) {
		saveFile = new File(savePath + "/" + saveFileName);
	}

	/**
	 * Adds a migraine with current time stamp to the data model.
	 * 
	 * @param intensity intensity of the attack
	 */
	public void addMigraine(Intensity intensity) {
		addEntry(migraines, "Migraine", new Date(), intensity);
	}

	/**
	 * Adds a migraine with custom time stamp to the data model.
	 * 
	 * @param intensity intensity of the attack
	 * @param date      Date when the attack occurred
	 */
	public void addMigraine(Intensity intensity, Date date) {
		addEntry(migraines, "Migraine", date, intensity);
	}

	/**
	 * Adds a habit with current time stamp to the data model.
	 * 
	 * @param habit description of the habit
	 */
	public void addHabit(String habit) {
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, new Date(), Intensity.noIntensity);
	}

	/**
	 * Adds a habit with custom time stamp to the data model.
	 * 
	 * @param habit description of the habit
	 * @param date  Date when the attack occurred
	 */
	public void addHabit(String habit, Date date) {
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, date, Intensity.noIntensity);
	}

	/**
	 * Adds a habit with current time stamp and intensity to the data model.
	 * 
	 * @param habit     description of the habit
	 * @param intensity intensity of the habit
	 */
	public void addHabit(String habit, Intensity intensity) {
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, new Date(), intensity);
	}

	/**
	 * Adds a habit with custom timestamp and intensity to the data model
	 * 
	 * @param habit     description of the habit
	 * @param intensity intensity of the habit
	 * @param date      Date when the habit occurred
	 */
	public void addHabit(String habit, Intensity intensity, Date date) {
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, date, intensity);
	}

	/**
	 * Adds a sympton with current time stamp to the data model.
	 * 
	 * @param symptom   description of the symptom
	 * @param intensity intensity of the symptom
	 */
	public void addSymptom(String symptom, Intensity intensity) {
		if (!symptomsList.contains(symptom))
			symptomsList.add(symptom);
		addEntry(symptoms, symptom, new Date(), intensity);
	}

	/**
	 * Adds a sympton with custom time stamp to the data model.
	 * 
	 * @param symptom   description of the symptom
	 * @param intensity intensity of the symptom
	 * @param date      Date when the symptom occurred
	 */
	public void addSymptom(String symptom, Intensity intensity, Date date) {
		if (!symptomsList.contains(symptom))
			symptomsList.add(symptom);
		addEntry(symptoms, symptom, date, intensity);
	}

	/**
	 * Adds remedy with current time stamp to the data model.
	 * 
	 * @param remedy description of the remedy
	 */

	public void addRemedy(String remedy) {
		if (!remediesList.contains(remedy))
			remediesList.add(remedy);
		addEntry(remedies, remedy, new Date(), Intensity.noIntensity);
	}

	/**
	 * Adds remedy with custom time stamp to the data model.
	 * 
	 * @param remedy description of the remedy
	 * @param date   Date when remedy was applied
	 */
	public void addRemedy(String remedy, Date date) {
		if (!remediesList.contains(remedy))
			remediesList.add(remedy);
		addEntry(remedies, remedy, date, Intensity.noIntensity);
	}

	// abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, ArrayList<Datum>> map, String key, Date date, Intensity intensity) {

		// create entry with key if it doesn't exist
		if (!map.containsKey(key))
			map.put(key, new ArrayList<>());

		// add new piece of data to ArrayList in according habit/
		map.get(key).add(new Datum(date, intensity));
	}

	/**
	 * Returns migraines as list iterator.
	 * 
	 * @return Iterator<String> of size 1 containing "Migraine"
	 */
	public Iterator<String> returnMigraines() {
		ArrayList<String> al = new ArrayList<>();
		al.add("Migraine");
		return al.iterator();
	}

	/**
	 * Returns all habits as list iterator.
	 *
	 * @return Iterator<String> containing habits
	 */
	public Iterator<String> returnHabits() {
		return habitsList.iterator();
	}

	/**
	 * Returns all symptoms as list iterator.
	 *
	 * @return Iterator<String> containing symptoms
	 */
	public Iterator<String> returnSymptoms() {
		return symptomsList.iterator();
	}

	/**
	 * Returns all remedies as list iterator.
	 *
	 * @return Iterator<String> containing remedies
	 */
	public List<String> returnRemedies() {
		return remediesList;
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

		this.habits = data.habits;
		this.habitsList = data.habitsList;
		this.migraines = data.migraines;
		this.remedies = data.remedies;
		this.remediesList = data.remediesList;
		this.symptoms = data.symptoms;
		this.symptomsList = data.symptomsList;
	}

	/**
	 * Deletes the file in save path provided in constructor.
	 */
	public void deleteSaveFile() {
		saveFile.delete();
	}

	/**
	 * Prints all data in the terminal.
	 * 
	 * @return String containing the contents printed to the terminal
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (String s : migraines.keySet()) {
			sb.append(s);
			sb.append("\n");
			System.out.println(s);
			for (Datum d : Objects.requireNonNull(migraines.get(s))) {
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}

		}
		sb.append("\n");
		sb.append("Habits");
		sb.append("\n");
		for (String s : habits.keySet()) {
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(habits.get(s))) {
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}
		}

		sb.append("\n");
		sb.append("Symptoms");
		sb.append("\n");

		for (String s : symptoms.keySet()) {
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(symptoms.get(s))) {
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}

		}

		sb.append("\n");
		sb.append("Remedies");
		sb.append("\n");

		for (String s : remedies.keySet()) {
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(remedies.get(s))) {
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}

		}
		sb.append("\n");
		System.out.println(sb.toString());
		return sb.toString();

	}

}
