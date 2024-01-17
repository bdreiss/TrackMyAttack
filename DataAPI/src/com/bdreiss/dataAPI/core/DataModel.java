package com.bdreiss.dataAPI.core;

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
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.DatumWithIntensity;
import com.bdreiss.dataAPI.util.IteratorWithIntensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;

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

	/**
	 * First date for which data exists.
	 */
	public LocalDate firstDate = null;

	// Offset for new day to start (if value is 4, new day starts at 4:00).
	private final int DAY_START_OFFSET = 4;

	/*
	 * The following Maps contain all the relevant data.
	 * 
	 * The Map contains the description of ailments, causes, symptoms or remedies as
	 * keys. -> The value is a List containing dates and intensity as Datum. The
	 * List is guaranteed to be ordered by date, since the dates are inserted using
	 * insertion sort.
	 *
	 */
	private Map<String, List<Datum>> ailments = new TreeMap<>(TREE_COMPARATOR);
	private Map<String, List<Datum>> causes = new TreeMap<>(TREE_COMPARATOR);
	private Map<String, List<Datum>> symptoms = new TreeMap<>(TREE_COMPARATOR);
	private Map<String, List<Datum>> remedies = new TreeMap<>(TREE_COMPARATOR);
	private Map<LocalDate, List<Coordinate>> coordinateTree = new TreeMap<>();

	/**
	 * Gets the save file.
	 * 
	 * @return the save file
	 */
	public File getSaveFile() {
		return saveFile;
	}

	/**
	 * Sets the save file.
	 * 
	 * @param saveFile file to be saved to.
	 */
	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
		save();
	}

	/**
	 * Gets the name of the save file.
	 * 
	 * @return the save file name
	 */
	public String getSaveFileName() {
		return saveFileName;
	}

	/**
	 * Sets the save file name.
	 * 
	 * @param saveFileName the new name for the save file
	 */
	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
		save();
	}

	/**
	 * Creates an instance of DataModel
	 * 
	 * @param savePath absolute path where the DataModel will be stored, if the
	 *                 value is null there will be no safe file and changes are not
	 *                 made persistent
	 */
	public DataModel(String savePath) {

		// initialize and create save file if savePath is given, go into non persistent
		// mode otherwise
		if (savePath != null) {
			saveFile = new File(savePath + "/" + saveFileName);
			load();
			save();// creates file in case it doesn't exist
		} else {
			savePath = null;
		}
	}

	/**
	 * Instantiates a new instance of DataModel without a save file.
	 */
	public DataModel() {
		this(null);
	}

	/**
	 * Adds new ailment key without data if it does not exist.
	 * 
	 * @param ailment ailment to be added as key
	 */
	public void addAilmentKey(String ailment) {
		addKey(ailments, ailment, true);
	}

	/**
	 * Adds new cause key without data if it does not exist.
	 * 
	 * @param cause     cause to be added as key
	 * @param intensity if true, key has intensity as an attribute
	 */
	public void addCauseKey(String cause, boolean intensity) {
		addKey(causes, cause, intensity);
	}

	/**
	 * Adds new symptom key without data if it does not exist.
	 * 
	 * @param symptom symptom to be added as key
	 */
	public void addSymptomKey(String symptom) {
		addKey(symptoms, symptom, true);
	}

	/**
	 * Adds new remedy without data if it does not exist.
	 * 
	 * @param remedy remedy to be added as key
	 * @param intensity if true, key has intensity as an attribute
	 */
	public void addRemedyKey(String remedy, boolean intensity) {
		addKey(remedies, remedy, intensity);
	}

	// abstracts the task of adding keys to maps
	private void addKey(Map<String, List<Datum>> map, String key, boolean intensity) {

		// create entry with key if it doesn't exist
		if (!map.containsKey(key)) {
			if (intensity)
				map.put(key, new ListWithIntensity());
			else
				map.put(key, new ArrayList<Datum>());

		}
		save();
	}

	/**
	 * Adds an ailment with current time stamp and intensity to the data model.
	 * 
	 * @param ailment   ailment to be added
	 * @param intensity intensity of ailment
	 * @param coordinates coordinates where ailment occurred
	 * @return LocalDateTime time when ailment was added
	 * @throws TypeMismatchException
	 */
	public LocalDateTime addAilment(String ailment, Intensity intensity, Coordinate coordinates)
			throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(ailments, ailment, intensity, date, coordinates);
		return date;
	}

	/**
	 * Adds an ailment with custom time stamp to the data model.
	 * 
	 * @param ailment     ailment to be added
	 * @param intensity   Intensity of ailment
	 * @param date        time when ailment occurred
	 * @param coordinates coordinates where ailment occurred
	 * @throws TypeMismatchException
	 */
	public void addAilment(String ailment, Intensity intensity, LocalDateTime date, Coordinate coordinates)
			throws TypeMismatchException {
		addEntry(ailments, ailment, intensity, date, coordinates);
	}

	/**
	 * Adds a cause with current time stamp to the data model.
	 * 
	 * @param cause       String description of the cause
	 * @param coordinates coordinates where cause occurred
	 * @return LocalDateTime when cause was added
	 * @throws TypeMismatchException
	 */
	public LocalDateTime addCause(String cause, Coordinate coordinates) throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(causes, cause, null, date, coordinates);
		return date;
	}

	/**
	 * Adds a cause with custom time stamp to the data model.
	 * 
	 * @param cause       String description of the cause
	 * @param date        LocalDateTime when the attack occurred
	 * @param coordinates coordinates where cause occurred
	 * @throws TypeMismatchException
	 */
	public void addCause(String cause, LocalDateTime date, Coordinate coordinates) throws TypeMismatchException {
		addEntry(causes, cause, null, date, coordinates);
	}

	/**
	 * Adds a cause with current time stamp and intensity to the data model.
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @return LocalDateTime when cause was added
	 * @throws TypeMismatchException
	 */
	public LocalDateTime addCause(String cause, Intensity intensity, Coordinate coordinates)
			throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(causes, cause, intensity, date, coordinates);
		return date;
	}

	/**
	 * Adds a cause with custom timestamp and intensity to the data model
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @param date      LocalDateTime when the cause occurred
	 * @throws TypeMismatchException
	 */
	public void addCause(String cause, Intensity intensity, LocalDateTime date, Coordinate coordinates)
			throws TypeMismatchException {
		addEntry(causes, cause, intensity, date, coordinates);
	}

	/**
	 * Adds a sympton with current time stamp to the data model.
	 * 
	 * @param symptom   String description of the symptom
	 * @param intensity Intensity of the symptom
	 * @return LocalDateTime when symptom was added
	 * @throws TypeMismatchException
	 */
	public LocalDateTime addSymptom(String symptom, Intensity intensity, Coordinate coordinates)
			throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(symptoms, symptom, intensity, date, coordinates);
		return date;
	}

	/**
	 * Adds a sympton with custom time stamp to the data model.
	 * 
	 * @param symptom   String description of the symptom
	 * @param intensity Intensity of the symptom
	 * @param date      LocalDateTime when the symptom occurred
	 * @throws TypeMismatchException
	 */
	public void addSymptom(String symptom, Intensity intensity, LocalDateTime date, Coordinate coordinates)
			throws TypeMismatchException {
		addEntry(symptoms, symptom, intensity, date, coordinates);
	}

	/**
	 * Adds remedy with current time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @return LocalDateTime when remedy was added
	 * @throws TypeMismatchException
	 */

	public LocalDateTime addRemedy(String remedy, Coordinate coordinates) throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, null, date, coordinates);
		return date;
	}

	/**
	 * Adds remedy with custom time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param date   LocalDateTime when remedy was applied
	 * @throws TypeMismatchException
	 */
	public void addRemedy(String remedy, LocalDateTime date, Coordinate coordinates) throws TypeMismatchException {
		addEntry(remedies, remedy, null, date, coordinates);
	}

	/**
	 * Adds remedy with current time stamp and intensity to the data model.
	 * 
	 * @param remedy    String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @return LocalDateTime when remedy was added
	 * @throws TypeMismatchException
	 */

	public LocalDateTime addRemedy(String remedy, Intensity intensity, Coordinate coordinates)
			throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, intensity, date, coordinates);
		return date;
	}

	/**
	 * Adds remedy with custom time stamp and intensity to the data model.
	 * 
	 * @param remedy    String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @param date      LocalDateTime when remedy was applied
	 * @throws TypeMismatchException
	 */

	public void addRemedy(String remedy, Intensity intensity, LocalDateTime date, Coordinate coordinates)
			throws TypeMismatchException {
		addEntry(remedies, remedy, intensity, date, coordinates);
	}

	// abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, List<Datum>> map, String key, Intensity intensity, LocalDateTime date,
			Coordinate coordinates) throws TypeMismatchException {

		// if coordinates have been passed and these coordinates are not already in the
		// tree for the specified date add them
		if (coordinates != null) {
			Iterator<Coordinate> it = getCoordinates(date.toLocalDate());

			if (it == null) {
				coordinateTree.put(date.toLocalDate(), new ArrayList<Coordinate>());
				it = coordinateTree.get(date.toLocalDate()).iterator();
			}
			boolean add = true;
			while (it.hasNext()) {
				if (it.next().equals(coordinates)) {
					add = false;
					break;
				}
			}

			if (add) {
				if (coordinateTree.get(date.toLocalDate()) == null)
					coordinateTree.put(date.toLocalDate(), new ArrayList<Coordinate>());
				coordinateTree.get(date.toLocalDate()).add(coordinates);
			}
		}

		if (firstDate == null || date.toLocalDate().compareTo(firstDate) < 0)
			firstDate = date.toLocalDate();

		// create entry with key if it doesn't exist
		if (!map.containsKey(key))
			addKey(map, key, intensity != null);

		List<Datum> list = map.get(key);

		if (intensity != null && !(map.get(key) instanceof ListWithIntensity))
			throw new TypeMismatchException();

		if (intensity == null && (map.get(key) instanceof ListWithIntensity))
			throw new TypeMismatchException();

		// add new Datum to end of list
		if (intensity != null)
			list.add(new DatumWithIntensity(date, intensity));
		else
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
		save();
	}

	/**
	 * Removes a key in ailments.
	 * 
	 * @param key Ailment which is to be removed.
	 */
	public void removeAilmentKey(String key) {
		removeKey(ailments, key);
	}

	/**
	 * Removes a key in causes.
	 * 
	 * @param key Cause which is to be removed.
	 */
	public void removeCauseKey(String key) {
		removeKey(causes, key);
	}

	/**
	 * Removes a key in symptoms.
	 * 
	 * @param key Symptom which is to be removed.
	 */
	public void removeSymptomKey(String key) {
		removeKey(symptoms, key);
	}

	/**
	 * Removes a key in remedies.
	 * 
	 * @param key Remedy which is to be removed.
	 */
	public void removeRemedyKey(String key) {
		removeKey(remedies, key);
	}

	// abstracts the task of removing entries from the different ArrayLists
	private void removeKey(Map<String, List<Datum>> map, String key) {

		// return if key doesn't exist
		if (!map.containsKey(key))
			return;

		map.remove(key);

		save();
	}

	/**
	 * Removes an entry in ailments.
	 * 
	 * @param ailment Ailment for which entry is to be removed.
	 * @param date    Date of entry which is to be removed.
	 */
	public void removeAilment(String ailment, LocalDateTime date) {
		removeEntry(ailments, ailment, date);
	}

	/**
	 * Removes an entry in causes.
	 * 
	 * @param cause Cause for which entry is to be removed.
	 * @param date  Date of entry which is to be removed.
	 */
	public void removeCause(String cause, LocalDateTime date) {
		removeEntry(causes, cause, date);
	}

	/**
	 * Removes an entry in symptoms.
	 * 
	 * @param symptom Symptom for which entry is to be removed.
	 * @param date    Date of entry which is to be removed.
	 */
	public void removeSymptom(String symptom, LocalDateTime date) {
		removeEntry(symptoms, symptom, date);
	}

	/**
	 * Removes an entry in remedies.
	 * 
	 * @param remedy Remedy for which entry is to be removed.
	 * @param date   Date of entry which is to be removed.
	 */
	public void removeRemedy(String remedy, LocalDateTime date) {
		removeEntry(remedies, remedy, date);
	}

	// abstracts the task of removing entries from the different ArrayLists
	private void removeEntry(Map<String, List<Datum>> map, String key, LocalDateTime date) {

		// return if key doesn't exist
		if (!map.containsKey(key))
			return;

		List<Datum> list = map.get(key);

		// iterate over list and remove entry if it matches date
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDate().equals(date)) {
				list.remove(i);
				break;
			}
		}

		boolean dateHasData = false;
		try {
			dateHasData = dateHasData(getAilments(), date);

			dateHasData = dateHasData(getCauses(), date);

			dateHasData = dateHasData(getSymptoms(), date);

			dateHasData = dateHasData(getRemedies(), date);
		} catch (EntryNotFoundException e) {
			e.printStackTrace();

		}
		if (!dateHasData)
			coordinateTree.remove(date.toLocalDate());

		save();

	}

	private boolean dateHasData(Iterator<String> it, LocalDateTime date) throws EntryNotFoundException {

		boolean dateHasData = false;

		while (it.hasNext() && dateHasData == false) {
			if (getAilmentData(it.next(), date.toLocalDate()).hasNext()) {
				dateHasData = true;
				break;
			}
		}
		return dateHasData;
	}

	/**
	 * Edit date of an ailment entry;
	 * 
	 * @param ailment Ailment String for which entry shall be changed
	 * @param date    LocalDateTime of entry
	 * @param newDate New LocalDateTime entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editAilmentEntry(String ailment, LocalDateTime date, LocalDateTime newDate)
			throws TypeMismatchException {
		editEntry(ailments, ailment, date, null, newDate);
	}

	/**
	 * Edit intensity of an ailment entry
	 * 
	 * @param ailment   Ailment String for which entry shall be changed
	 * @param date      LocalDateTime of entry
	 * @param intensity New Intensity entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editAilmentEntry(String ailment, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
		editEntry(ailments, ailment, date, intensity, null);

	}

	/**
	 * Edit date of a cause entry;
	 * 
	 * @param cause   Cause String for which entry shall be changed
	 * @param date    LocalDateTime of entry
	 * @param newDate New LocalDateTime entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editCauseEntry(String cause, LocalDateTime date, LocalDateTime newDate) throws TypeMismatchException {
		editEntry(causes, cause, date, null, newDate);
	}

	/**
	 * Edit intensity of a cause entry
	 * 
	 * @param cause     Cause String for which entry shall be changed
	 * @param date      LocalDateTime of entry
	 * @param intensity New Intensity entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editCauseEntry(String cause, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
		editEntry(causes, cause, date, intensity, null);
	}

	/**
	 * Edit date of a symptom entry;
	 * 
	 * @param symptom Symptom String for which entry shall be changed
	 * @param date    LocalDateTime of entry
	 * @param newDate New LocalDateTime entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editSymptomEntry(String symptom, LocalDateTime date, LocalDateTime newDate)
			throws TypeMismatchException {
		editEntry(symptoms, symptom, date, null, newDate);
	}

	/**
	 * Edit intensity of a symptom entry
	 * 
	 * @param symptom   Symptom String for which entry shall be changed
	 * @param date      LocalDateTime of entry
	 * @param intensity New Intensity entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editSymptomEntry(String symptom, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
		editEntry(symptoms, symptom, date, intensity, null);
	}

	/**
	 * Edit date of a remedy entry;
	 * 
	 * @param remedy  Remedy String for which entry shall be changed
	 * @param date    LocalDateTime of entry
	 * @param newDate New LocalDateTime entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editRemedyEntry(String remedy, LocalDateTime date, LocalDateTime newDate) throws TypeMismatchException {
		editEntry(remedies, remedy, date, null, newDate);
	}

	/**
	 * Edit intensity of a remedy entry
	 * 
	 * @param remedy    Remedy String for which entry shall be changed
	 * @param date      LocalDateTime of entry
	 * @param intensity New Intensity entry should be changed to
	 * @throws TypeMismatchException
	 */
	public void editRemedyEntry(String remedy, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
		editEntry(remedies, remedy, date, intensity, null);
	}

	private void editEntry(Map<String, List<Datum>> map, String key, LocalDateTime date, Intensity intensity,
			LocalDateTime newDate) throws TypeMismatchException {

		if (date == null)
			return;

		// nothing to do
		if (newDate == null && intensity == null)
			return;
		
		for (Datum d : map.get(key)) {
			if (d.getDate().equals(date) && d.getDate().getNano() == date.getNano()) {
				if (intensity != null) {
					if (!(d instanceof DatumWithIntensity))
						throw new TypeMismatchException();

					((DatumWithIntensity) d).setIntensity(intensity);

				} else {
					if (d instanceof DatumWithIntensity)
						intensity = ((DatumWithIntensity) d).getIntensity();
				}

				// if new date is assigned remove entry and insert new one so list stays sorted
				// via insertion sort
				if (newDate != null) {
					map.get(key).remove(d);
					addEntry(map, key, intensity, newDate, null);
				}
				break;
			}

		}
		save();

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
	 * 
	 * @return
	 */
	public int getSize() {
		return getCausesSize() + getSymptomsSize() + getRemediesSize();
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
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getAilmentData(String ailment, LocalDate date) throws EntryNotFoundException {

		return getIterator(ailments.get(ailment), date);
	}

	/**
	 * Returns data when ailment occurred (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param ailment String describing ailment
	 * @return Iterator containing data when and how intense ailment occurred as
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getAilmentData(String ailment) throws EntryNotFoundException {

		return getIterator(ailments.get(ailment), null);

	}

	/**
	 * Returns all data when cause occurred (including LocalDateTime and Intensity)
	 * as Iterator for date.
	 * 
	 * @param cause String describing cause
	 * @param date  Day for which data should be retrieved
	 * @return Iterator containing data when and how intense cause occurred as
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getCauseData(String cause, LocalDate date) throws EntryNotFoundException {
		return getIterator(causes.get(cause), date);
	}

	/**
	 * Returns data when cause occurred (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param cause String describing cause
	 * @return Iterator containing data when and how intense cause occurred as
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getCauseData(String cause) throws EntryNotFoundException {
		return getIterator(causes.get(cause), null);
	}

	/**
	 * Returns all data when symptom occurred (including LocalDateTime and
	 * Intensity) as Iterator for date.
	 * 
	 * @param symptom String describing symptom
	 * @param date    Day for which data should be retrieved
	 * @return Iterator containing data when and how intense symptom occurred as
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getSymptomData(String symptom, LocalDate date) throws EntryNotFoundException {
		return getIterator(symptoms.get(symptom), date);
	}

	/**
	 * Returns data when symptom occurred (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param symptom String describing symptom
	 * @return Iterator containing data when and how intense symptom occurred as
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getSymptomData(String symptom) throws EntryNotFoundException {
		return getIterator(symptoms.get(symptom), null);
	}

	/**
	 * Returns all data when remedy was used (including LocalDateTime and Intensity)
	 * as Iterator for date.
	 * 
	 * @param remedy String describing remedy.
	 * @param date   Day for which data should be retrieved
	 * @return Iterator containing data when and how intense remedy was used as
	 *         Datum. If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getRemedyData(String remedy, LocalDate date) throws EntryNotFoundException {

		return getIterator(remedies.get(remedy), date);
	}

	/**
	 * Returns data when remedy was used (including LocalDateTime and Intensity) as
	 * Iterator for all dates.
	 * 
	 * @param remedy String describing remedy.
	 * @return Iterator containing data when and how intense remedy was used as
	 *         Datum- If data contains intensity returns an IteratorWithIntensity
	 * @throws EntryNotFoundException
	 */
	public Iterator<Datum> getRemedyData(String remedy) throws EntryNotFoundException {
		return getIterator(remedies.get(remedy), null);
	}

	/**
	 * Count entries for a specific ailment at given date.
	 * 
	 * @param ailment
	 * @param date
	 * @return count
	 * @throws EntryNotFoundException
	 */
	public int countAilment(String ailment, LocalDate date) throws EntryNotFoundException {
		return iteratorToInt(getIterator(ailments.get(ailment), date));
	};

	/**
	 * Count entries for a specific symptom at given date.
	 * 
	 * @param symptom
	 * @param date
	 * @return count
	 * @throws EntryNotFoundException
	 */
	public int countSymptom(String symptom, LocalDate date) throws EntryNotFoundException {
		return iteratorToInt(getIterator(symptoms.get(symptom), date));
	};

	/**
	 * Count entries for a specific cause at given date.
	 * 
	 * @param cause
	 * @param date
	 * @return count
	 * @throws EntryNotFoundException
	 */
	public int countCause(String cause, LocalDate date) throws EntryNotFoundException {
		return iteratorToInt(getIterator(causes.get(cause), date));
	};

	/**
	 * Count entries for a specific remedy at given date.
	 * 
	 * @param remedy
	 * @param date
	 * @return count
	 * @throws EntryNotFoundException
	 */
	public int countRemedy(String remedy, LocalDate date) throws EntryNotFoundException {
		return iteratorToInt(getIterator(remedies.get(remedy), date));
	};

	/**
	 * 
	 * @param datum
	 * @return
	 */
	public Coordinate getCoordinate(Datum datum) {
		// TODO implement
		return null;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public Coordinate getCoordinatesMean(LocalDate date) {
		Set<Coordinate> coordinateSet = new TreeSet<Coordinate>();

		Iterator<Coordinate> it = getCoordinates(date);

		if (it == null)
			return null;
		// return null if there are no coordinates for date
		if (!it.hasNext())
			return null;

		while (it.hasNext()) {
			coordinateSet.add(it.next());
		}

		Double xSum = 0.0;

		Double ySum = 0.0;

		for (Coordinate p : coordinateSet) {
			xSum += p.getLatitude();
			ySum += p.getLongitude();
		}

		return new Coordinate(xSum / coordinateSet.size(), ySum / coordinateSet.size());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public Iterator<Coordinate> getCoordinates(LocalDate date) {

		return coordinateTree.get(date) == null ? null : coordinateTree.get(date).iterator();
	}

	/**
	 * Returns medium occurrence of cause per day (not including days without data)
	 * 
	 * @param cause
	 * @return
	 * @throws EntryNotFoundException
	 */

	public float mediumCause(String cause) throws EntryNotFoundException {

		// if there is no data, return 0
		if (firstDate == null || getCausesSize() == 0)
			return 0;

		return getMedium(getCauseData(cause));

	}

	/**
	 * Returns medium use of remedy per day (not including days without data)
	 * 
	 * @param remedy
	 * @return
	 * @throws EntryNotFoundException
	 */
	public float mediumRemedy(String remedy) throws EntryNotFoundException {

		// if there is no data, return 0
		if (firstDate == null || getRemediesSize() == 0)
			return 0;

		return getMedium(getRemedyData(remedy));
	}

	private float getMedium(Iterator<Datum> it) {

		float daysCount = 0;

		float sum = 0;

		// initialize date at day before first recorded date
		LocalDate date = firstDate.minusDays(1);

		while (it.hasNext()) {
			Datum datum = it.next();
			if (date.compareTo(datum.getDate().minusHours(DAY_START_OFFSET).toLocalDate()) != 0) {
				daysCount++;
				date = datum.getDate().minusHours(DAY_START_OFFSET).toLocalDate();
			}
			sum++;
		}

		return sum / daysCount;
	}

	// method that abstracts the task of returning an Iterator of the right kind
	private Iterator<Datum> getIterator(List<Datum> list, LocalDate date) throws EntryNotFoundException {

		// if item does not exist throw exception
		if (list == null)
			throw new EntryNotFoundException();

		// if date is null return IteratorWithIntensity or Iterator<Datum> for all data
		if (date == null) {
			if (list instanceof ListWithIntensity)
				return new IteratorWithIntensity(list.iterator());
			else
				return list.iterator();
		}

		// return data for specific date as IteratorWithIntensity or Iterator<Datum>
		if (list instanceof ListWithIntensity)
			return new IteratorWithIntensity(new DayIterator(list, date));
		else
			return new DayIterator(list, date);

	}

	// takes Iterator and returns count of items in it.
	private int iteratorToInt(Iterator<?> it) {
		int count = 0;
		while (it.hasNext()) {
			count++;
			it.next();
		}
		return count;
	}

	/**
	 * Saves data in save path provided in constructor. Does not save data when save
	 * file is not specified (==null).
	 */
	private void save() {

		if (saveFile == null)
			return;

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
		if (saveFile == null || !saveFile.exists())
			return;

		try {
			FileInputStream fis = new FileInputStream(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);

			data = (DataModel) ois.readObject();
			this.causes = data.causes;
			this.ailments = data.ailments;
			this.remedies = data.remedies;
			this.symptoms = data.symptoms;
			this.firstDate = data.firstDate;

			ois.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Deletes the file in save path provided in constructor.
	 */
	public void deleteSaveFile() {
		saveFile.delete();
	}

	/**
	 * Adds datum directly to key.
	 * 
	 * @param category Category for which datum should be added.
	 * @param key      Key for which Datum should be added.
	 * @param datum    Datum that ought to be added.
	 */
	public void addDatumDirectly(Category category, String key, Datum datum) {

		if (firstDate == null)
			firstDate = datum.getDate().toLocalDate();

		Map<String, List<Datum>> map = null;
		switch (category) {
		case AILMENT:
			map = ailments;
			break;
		case CAUSE:
			map = causes;
			break;
		case SYMPTOM:
			map = symptoms;
			break;
		case REMEDY:
			map = remedies;
		}

		if (map == null) {
			System.out.println("Category not found.");
			System.exit(0);
		}

		if (!map.containsKey(key)) {
			if (datum instanceof DatumWithIntensity)
				map.put(key, new ListWithIntensity());
			else
				map.put(key, new ArrayList<Datum>());
		}

		List<Datum> list = map.get(key);

		list.add(datum);
		save();

	}

	// Iterator that iterates over all data in a List<Datum> for one date
	private class DayIterator implements Iterator<Datum> {

		private int index;
		private List<Datum> list;
		private LocalDate date;
		final private int OFFSET = DAY_START_OFFSET;

		public DayIterator(List<Datum> list, LocalDate date) {
			this.list = list;
			this.date = date;
			index = getStartingIndex(list, 0, list.size() - 1, this.date);
		}

		// returns the starting index by finding the lowest entry for the given date
		// using binary search
		private int getStartingIndex(List<Datum> list, int b, int e, LocalDate date) {
			if (b > e)
				return list.size();
			int mid = (b + e) / 2;

			int minIndex = list.size();

			if (list.get(mid).getDate().minusHours(OFFSET).toLocalDate().compareTo(date) == 0)
				minIndex = mid;

			if (list.get(mid).getDate().minusHours(OFFSET).toLocalDate().compareTo(date) >= 0)
				return Math.min(minIndex, getStartingIndex(list, b, mid - 1, date));
			else
				return getStartingIndex(list, mid + 1, e, date);
		}

		// if did not reach end of list and date is still in range return true
		@Override
		public boolean hasNext() {
			if (index == list.size())
				return false;
			if (list.get(index).getDate().minusHours(OFFSET).toLocalDate().compareTo(date) > 0)
				return false;
			return true;
		}

		@Override
		public Datum next() {
			return list.get(index++);
		}

	}

	// class that represents a List with items being of the kind DatumWithIntensity
	private class ListWithIntensity extends ArrayList<Datum> {

		private static final long serialVersionUID = 1L;

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
		// TODO: also printCoordinates
		for (LocalDate d : coordinateTree.keySet())
			System.out.println(d + ": " + Coordinate.printArray(getCoordinates(d)));

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
