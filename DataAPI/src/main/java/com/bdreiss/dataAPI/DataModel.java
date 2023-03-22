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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

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

	private static final Comparator<String> TREE_COMPARATOR = String.CASE_INSENSITIVE_ORDER;

	private File saveFile;
	private String saveFileName = "DataModel";

	// ArrayList containing ailments
	private Map<String, Map<LocalDate, ArrayList<Datum>>> ailments = new TreeMap<>(TREE_COMPARATOR);
	// ArrayList containing causes that might be related to ailment
	private Map<String, Map<LocalDate, ArrayList<Datum>>> causes = new TreeMap<>(TREE_COMPARATOR);
	// ArrayList containing symptoms that might be related to ailment
	private Map<String, Map<LocalDate, ArrayList<Datum>>> symptoms = new TreeMap<>(TREE_COMPARATOR);
	// ArrayList containing potential remedies tried when symptoms showed up
	private Map<String, Map<LocalDate, ArrayList<Datum>>> remedies = new TreeMap<>(TREE_COMPARATOR);

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
		addEntry(remedies, remedy, Intensity.noIntensity, date);
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
	private void addEntry(Map<String, Map<LocalDate, ArrayList<Datum>>> map, String key, Intensity intensity,
			LocalDateTime date) {

		// create entry with key if it doesn't exist
		if (!map.containsKey(key))
			map.put(key, new TreeMap<LocalDate, ArrayList<Datum>>());

		//create entry with key for date if it doesn't exist
		if (!map.get(key).containsKey(date.toLocalDate()))
			map.get(key).put(date.toLocalDate(), new ArrayList<Datum>());
		
		// add new piece of data to ArrayList in according cause
		map.get(key).get(date.toLocalDate()).add(new Datum(date, intensity));
	}

	/**
	 * Returns ailments as list iterator.
	 * 
	 * @return Iterator<String> containing ailments
	 */
	public Iterator<String> getAilments() {
		return ailments.keySet().iterator();
	}

	/**
	 * Returns all causes as list iterator.
	 *
	 * @return Iterator<String> containing causes
	 */
	public Iterator<String> getCauses() {
		return causes.keySet().iterator();
	}

	/**
	 * Returns all symptoms as list iterator.
	 *
	 * @return Iterator<String> containing symptoms
	 */
	public Iterator<String> getSymptoms() {
		return symptoms.keySet().iterator();
	}

	/**
	 * Returns all remedies as list iterator.
	 *
	 * @return Iterator<String> containing remedies
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
	 * Intensity) as Iterator<Datum>.
	 * 
	 * @param ailment String describing ailment
	 * @return Iterator<Datum> containing data when and how intense ailment occurred
	 */
	public Iterator<Datum> getAilmentData(String ailment, LocalDate date) {
		return ailments.get(ailment).get(date).iterator();
	}

	/**
	 * Returns all datas when cause occurred (including LocalDateTime and Intensity)
	 * as Iterator<Datum>.
	 * 
	 * @param cause String describing cause
	 * @return Iterator<Datum> containing data when and how intense cause occurred.
	 */
	public Iterator<Datum> getCauseData(String cause, LocalDate date) {
		return causes.get(cause).get(date).iterator();
	}

	/**
	 * Returns all data when symptom occurred (including LocalDateTime and
	 * Intensity) as Iterator<Datum>.
	 * 
	 * @param symptom String describing symptom
	 * @return Iterator<Datum> containing data when and how intense symptom occurred
	 */
	public Iterator<Datum> getSymptomData(String symptom, LocalDate date) {
		return symptoms.get(symptom).get(date).iterator();
	}

	/**
	 * Returns all data when remedy was used (including LocalDateTime and Intensity)
	 * as Iterator<Datum>.
	 * 
	 * @param remedy String describing remedy.
	 * @return Iterator<Datum> containing data when and how intense remedy was used
	 */
	public Iterator<Datum> getRemedyData(String remedy, LocalDate date) {
		return remedies.get(remedy).get(date).iterator();
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
			for (LocalDate date : ailments.get(s).keySet()) {
				for (Datum d : Objects.requireNonNull(ailments.get(s).get(date))) {
					sb.append(d.getDate() + ", " + d.getIntensity());
					sb.append("\n");
				}
			}
		}
		
		sb.append(header("Causes"));

		for (String s : causes.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (LocalDate date : causes.get(s).keySet()) {
				for (Datum d : Objects.requireNonNull(causes.get(s).get(date))) {
					sb.append(d.getDate() + ", " + d.getIntensity());
					sb.append("\n");
				}
			}
		}

		sb.append(header("Symptoms"));

		for (String s : symptoms.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (LocalDate date : symptoms.get(s).keySet()) {
				for (Datum d : Objects.requireNonNull(symptoms.get(s).get(date))) {
					sb.append(d.getDate() + ", " + d.getIntensity());
					sb.append("\n");
				}
			}

		}

		sb.append(header("Remedies"));

		for (String s : remedies.keySet()) {
			sb.append("\n");
			sb.append(s);
			sb.append("\n");
			for (LocalDate date : remedies.get(s).keySet()) {
				for (Datum d : Objects.requireNonNull(remedies.get(s).get(date))) {
					sb.append(d.getDate() + ", " + d.getIntensity());
					sb.append("\n");
				}
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
