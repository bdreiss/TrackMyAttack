package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	 */
	public void addAilment(String ailment, Intensity intensity) {
		addEntry(ailments, ailment, intensity, new Date());
	}

	/**
	 * Adds an ailment with custom time stamp to the data model.
	 * 
	 * @param ailment   String ailment to be added
	 * @param intensity Intensity of ailment
	 * @param date      Date when ailment occurred
	 */
	public void addAilment(String ailment, Intensity intensity, Date date) {
		addEntry(ailments, ailment, intensity, date);
	}

	/**
	 * Adds a cause with current time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 */
	public void addCause(String cause) {
		if (!causesList.contains(cause)) {
			causesList.add(cause);
			Collections.sort(causesList);
		}
		addEntry(causes, cause, Intensity.noIntensity, new Date());
	}

	/**
	 * Adds a cause with custom time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 * @param date  Date when the attack occurred
	 */
	public void addCause(String cause, Date date) {
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
	 */
	public void addCause(String cause, Intensity intensity) {
		if (!causesList.contains(cause)) {
			causesList.add(cause);
			Collections.sort(causesList);
		}

		addEntry(causes, cause, intensity, new Date());
	}

	/**
	 * Adds a cause with custom timestamp and intensity to the data model
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @param date      Date when the cause occurred
	 */
	public void addCause(String cause, Intensity intensity, Date date) {
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
	 */
	public void addSymptom(String symptom, Intensity intensity) {
		if (!symptomsList.contains(symptom)) {
			symptomsList.add(symptom);
			Collections.sort(symptomsList);
		}
		addEntry(symptoms, symptom, intensity, new Date());
	}

	/**
	 * Adds a sympton with custom time stamp to the data model.
	 * 
	 * @param symptom   String description of the symptom
	 * @param intensity Intensity of the symptom
	 * @param date      Date when the symptom occurred
	 */
	public void addSymptom(String symptom, Intensity intensity, Date date) {
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
	 */

	public void addRemedy(String remedy) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}
		addEntry(remedies, remedy, Intensity.noIntensity, new Date());
	}


	/**
	 * Adds remedy with custom time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param date   Date when remedy was applied
	 */
	public void addRemedy(String remedy, Date date) {
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
	 */

	public void addRemedy(String remedy, Intensity intensity) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}
		addEntry(remedies, remedy, intensity, new Date());
	}

	/**
	 * Adds remedy with custom time stamp and intensity to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @param date Date when remedy was applied
	 */

	public void addRemedy(String remedy, Intensity intensity, Date date) {
		if (!remediesList.contains(remedy)) {
			remediesList.add(remedy);
			Collections.sort(remediesList);
		}
		addEntry(remedies, remedy, intensity, date);
	}
	
	// abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, ArrayList<Datum>> map, String key, Intensity intensity, Date date) {

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
 *  Returns all data when ailment occurred (including Date and Intensity) as Iterator<Datum>.
 * 
 * @param ailment String describing ailment
 * @return Iterator<Datum> containing data when and how intense ailment occurred
 */
	public Iterator<Datum> getAilmentData(String ailment) {
		// TODO implement return AilmentData
		return null;
	}
/**
 * Returns all datas when cause occurred (including Date and Intensity) as Iterator<Datum>.
 * 
 * @param cause String describing cause
 * @return Iterator<Datum> containing data when and how intense cause occurred.
 */
	public Iterator<Datum> getCauseData(String cause) {
		// TODO implement return CauseData
		return null;
	}
/**
 * Returns all data when symptom occurred (including Date and Intensity) as Iterator<Datum>.
 * 
 * @param symptom String describing symptom
 * @return Iterator<Datum> containing data when and how intense symptom occurred
 */
	public Iterator<Datum> getSymptomData(String symptom) {
		// TODO implement return SymptomtData
		return null;
	}
	/**
	 * Returns all data when remedy was used (including Date and Intensity) as Iterator<Datum>.
	 * 
	 * @param remedy String describing remedy.
	 * @return Iterator<Datum> containing data when and how intense remedy was used
	 */
	public Iterator<Datum> getRemedyData(String remedy) {
		// TODO implement return RemedyData
		return null;
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
