package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DataModel implements Serializable {

	/**
	 * Class that represents a data model for keeping track of ailments.
	 * 
	 * <p>
	 * It contains methods to store and retrieve causes, symptoms and remedies
	 * relating to ailments.
	 * </p>
	 * 
	 * @author Bernd Reiß
	 */


	/*
	 * The following Maps contain all the relevant data. They each contain the title
	 * of the cause/symptom/remedy as a key and an ArrayList containing all relevant
	 * information as single pieces of data (see Datum).
	 */

	private static final long serialVersionUID = 1L;

	private File saveFile;
	private String saveFileName = "DataModel";

	// ArrayList containing ailments
	private Map<String, ArrayList<Datum>> ailments = new HashMap<>();
	// ArrayList containing causes that might be related to ailment
	private Map<String, ArrayList<Datum>> causes = new HashMap<>();
	// ArrayList containing symptoms that might be related to ailment
	private Map<String, ArrayList<Datum>> symptoms = new HashMap<>();
	// ArrayList containing potential remedies tried when symptoms showed up
	private Map<String, ArrayList<Datum>> remedies = new HashMap<>();

	// ArrayList containing all ailments
	private ArrayList<String> ailmentsList = new ArrayList<>();
	// ArrayList containing all causes
	private ArrayList<String> causesList = new ArrayList<>();
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
		if (!causesList.contains(cause)) {
			causesList.add(cause);
			Collections.sort(causesList);
		}
		LocalDateTime date = LocalDateTime.now();
		addEntry(causes, cause, Intensity.noIntensity, date);
		return date;
	}

	/**
	 * Adds a cause with custom time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 * @param date  LocalDateTime when the attack occurred
	 */
	public void addCause(String cause, LocalDateTime date) {
		if (!causesList.contains(cause)) {
			causesList.add(cause);
			Collections.sort(causesList);
		}

		addEntry(causes, cause, Intensity.noIntensity, date);
	}

	/**
	 * Adds a cause with current time stamp and intensity to the data model.
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @return LocalDateTime when cause was added
	 */
	public LocalDateTime addCause(String cause, Intensity intensity) {
		if (!causesList.contains(cause)) {
			causesList.add(cause);
			Collections.sort(causesList);
		}

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
		if (!causesList.contains(cause)) {
			causesList.add(cause);
			Collections.sort(causesList);
		}

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
		if (!symptomsList.contains(symptom)) {
			symptomsList.add(symptom);
			Collections.sort(symptomsList);
		}
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
		if (!symptomsList.contains(symptom)) {
			symptomsList.add(symptom);
			Collections.sort(symptomsList);
		}
		addEntry(symptoms, symptom, intensity, date);
	}

	/**
	 * Adds remedy with current time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @return LocalDateTime when remedy was added
	 */

	public LocalDateTime addRemedy(String remedy) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, Intensity.noIntensity, date);
		return date;
	}


	/**
	 * Adds remedy with custom time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param date   LocalDateTime when remedy was applied
	 */
	public void addRemedy(String remedy, LocalDateTime date) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}

		addEntry(remedies, remedy, Intensity.noIntensity, date);
	}

	/**
	 * Adds remedy with current time stamp and intensity to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @return LocalDateTime when remedy was added
	 */

	public LocalDateTime addRemedy(String remedy, Intensity intensity) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, intensity, date);
		return date;
	}

	/**
	 * Adds remedy with custom time stamp and intensity to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @param date LocalDateTime when remedy was applied
	 */

	public void addRemedy(String remedy, Intensity intensity, LocalDateTime date) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}
		addEntry(remedies, remedy, intensity, date);
	}
	
	// abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, ArrayList<Datum>> map, String key, Intensity intensity, LocalDateTime date) {

		// create entry with key if it doesn't exist
		if (!map.containsKey(key))
			map.put(key, new ArrayList<>());

		// add new piece of data to ArrayList in according cause
		map.get(key).add(new Datum(date, intensity));
	}

	/**
	 * Returns ailments as list iterator.
	 * 
	 * @return Iterator<String> containing ailments
	 */
	public Iterator<String> getAilmentsList() {
		return ailmentsList.iterator();
	}

	/**
	 * Returns all causes as list iterator.
	 *
	 * @return Iterator<String> containing causes
	 */
	public Iterator<String> getCausesList() {
		return causesList.iterator();
	}

	/**
	 * Returns all symptoms as list iterator.
	 *
	 * @return Iterator<String> containing symptoms
	 */
	public Iterator<String> getSymptomsList() {
		return symptomsList.iterator();
	}

	/**
	 * Returns all remedies as list iterator.
	 *
	 * @return Iterator<String> containing remedies
	 */
	public Iterator<String> getRemediesList() {
		return remediesList.iterator();
	}

	/**
	 * Returns the size of list of ailments.
	 * 
	 * @return size of list of ailments
	 */
	public int getAilmentsListSize() {
		return ailments.size();
	}

	/**
	 * Returns the size of list of causes.
	 * 
	 * @return size of list of causes
	 */
	public int getCausesListSize() {
		return causesList.size();
	}

	/**
	 * Returns the size of list of symptoms.
	 * 
	 * @return size of list of symptoms
	 */
	public int getSymptomsListSize() {
		return symptomsList.size();
	}

	/**
	 * Returns the size of list of remedies.
	 * 
	 * @return size of list of remedies
	 */
	public int getRemediesListSize() {
		return remediesList.size();
	}
/**
 *  Returns all data when ailment occurred (including LocalDateTime and Intensity) as Iterator<Datum>.
 * 
 * @param ailment String describing ailment
 * @return Iterator<Datum> containing data when and how intense ailment occurred
 */
	public Iterator<Datum> getAilmentData(String ailment) {
		return ailments.get(ailment).iterator();
	}
/**
 * Returns all datas when cause occurred (including LocalDateTime and Intensity) as Iterator<Datum>.
 * 
 * @param cause String describing cause
 * @return Iterator<Datum> containing data when and how intense cause occurred.
 */
	public Iterator<Datum> getCauseData(String cause) {
		return causes.get(cause).iterator();
	}
/**
 * Returns all data when symptom occurred (including LocalDateTime and Intensity) as Iterator<Datum>.
 * 
 * @param symptom String describing symptom
 * @return Iterator<Datum> containing data when and how intense symptom occurred
 */
	public Iterator<Datum> getSymptomData(String symptom) {
		return symptoms.get(symptom).iterator();
	}
	/**
	 * Returns all data when remedy was used (including LocalDateTime and Intensity) as Iterator<Datum>.
	 * 
	 * @param remedy String describing remedy.
	 * @return Iterator<Datum> containing data when and how intense remedy was used
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
		this.causesList = data.causesList;
		this.ailments = data.ailments;
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
		for (String s : ailments.keySet()) {
			sb.append(s);
			sb.append("\n");
			System.out.println(s);
			for (Datum d : Objects.requireNonNull(ailments.get(s))) {
				sb.append(d.getDate() + ", " + d.getIntensity());
				sb.append("\n");
			}

		}
		sb.append("\n");
		sb.append("Causes");
		sb.append("\n");
		for (String s : causes.keySet()) {
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(causes.get(s))) {
				sb.append(d.getDate() + ", " + d.getIntensity());
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
				sb.append(d.getDate() + ", " + d.getIntensity());
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
				sb.append(d.getDate() + ", " + d.getIntensity());
				sb.append("\n");
			}

		}
		sb.append("\n");
		System.out.println(sb.toString());
		return sb.toString();

	}

}
