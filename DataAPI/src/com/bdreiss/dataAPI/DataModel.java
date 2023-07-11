package com.bdreiss.dataAPI;

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

import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
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

	public LocalDate firstDate = null;
	
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

	public File getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
		save();
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
		save();
	}

	/**
	 * Creates an instance of DataModel
	 * 
	 * @param savePath String absolute path where the DataModel will be stored
	 */
	public DataModel(String savePath) {
		saveFile = new File(savePath + "/" + saveFileName);
		load();
		save();//creates file in case it doesn't exist
	}

	/**
	 * Adds new ailment without data if it does not exist.
	 * 
	 * @param ailment Ailment to be added as key
	 */
	public void addAilmentKey(String ailment) {
		addKey(ailments, ailment, true);
	}

	/**
	 * Adds new cause without data if it does not exist.
	 * 
	 * @param cause Cause to be added as key
	 */
	public void addCauseKey(String cause, boolean intensity) {
		addKey(causes, cause, intensity);
	}

	/**
	 * Adds new symptom without data if it does not exist.
	 * 
	 * @param symptom Symptom to be added as key
	 */
	public void addSymptomKey(String symptom) {
		addKey(symptoms, symptom, true);
	}

	/**
	 * Adds new remedy without data if it does not exist.
	 * 
	 * @param remedy Remedy to be added as key
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
	 * @param ailment   String ailment to be added
	 * @param intensity Intensity of ailment
	 * @return LocalDateTime when ailment was added
	 * @throws TypeMismatchException 
	 */
	public LocalDateTime addAilment(String ailment, Intensity intensity) throws TypeMismatchException {
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
	 * @throws TypeMismatchException 
	 */
	public void addAilment(String ailment, Intensity intensity, LocalDateTime date) throws TypeMismatchException {
		addEntry(ailments, ailment, intensity, date);
	}

	/**
	 * Adds a cause with current time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 * @return LocalDateTime when cause was added
	 * @throws TypeMismatchException 
	 */
	public LocalDateTime addCause(String cause) throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(causes, cause, null, date);
		return date;
	}

	/**
	 * Adds a cause with custom time stamp to the data model.
	 * 
	 * @param cause String description of the cause
	 * @param date  LocalDateTime when the attack occurred
	 * @throws TypeMismatchException 
	 */
	public void addCause(String cause, LocalDateTime date) throws TypeMismatchException {
		addEntry(causes, cause, null, date);
	}

	/**
	 * Adds a cause with current time stamp and intensity to the data model.
	 * 
	 * @param cause     String description of the cause
	 * @param intensity Intensity of the cause
	 * @return LocalDateTime when cause was added
	 * @throws TypeMismatchException 
	 */
	public LocalDateTime addCause(String cause, Intensity intensity) throws TypeMismatchException {
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
	 * @throws TypeMismatchException 
	 */
	public void addCause(String cause, Intensity intensity, LocalDateTime date) throws TypeMismatchException {
		addEntry(causes, cause, intensity, date);
	}

	/**
	 * Adds a sympton with current time stamp to the data model.
	 * 
	 * @param symptom   String description of the symptom
	 * @param intensity Intensity of the symptom
	 * @return LocalDateTime when symptom was added
	 * @throws TypeMismatchException 
	 */
	public LocalDateTime addSymptom(String symptom, Intensity intensity) throws TypeMismatchException {
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
	 * @throws TypeMismatchException 
	 */
	public void addSymptom(String symptom, Intensity intensity, LocalDateTime date) throws TypeMismatchException {
		addEntry(symptoms, symptom, intensity, date);
	}

	/**
	 * Adds remedy with current time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @return LocalDateTime when remedy was added
	 * @throws TypeMismatchException 
	 */

	public LocalDateTime addRemedy(String remedy) throws TypeMismatchException {
		LocalDateTime date = LocalDateTime.now();
		addEntry(remedies, remedy, null, date);
		return date;
	}

	/**
	 * Adds remedy with custom time stamp to the data model.
	 * 
	 * @param remedy String description of the remedy
	 * @param date   LocalDateTime when remedy was applied
	 * @throws TypeMismatchException 
	 */
	public void addRemedy(String remedy, LocalDateTime date) throws TypeMismatchException {
		addEntry(remedies, remedy, null, date);
	}

	/**
	 * Adds remedy with current time stamp and intensity to the data model.
	 * 
	 * @param remedy    String description of the remedy
	 * @param intensity Intensity with which remedy was applied
	 * @return LocalDateTime when remedy was added
	 * @throws TypeMismatchException 
	 */

	public LocalDateTime addRemedy(String remedy, Intensity intensity) throws TypeMismatchException {
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
	 * @throws TypeMismatchException 
	 */

	public void addRemedy(String remedy, Intensity intensity, LocalDateTime date) throws TypeMismatchException {
		addEntry(remedies, remedy, intensity, date);
	}

	// abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, List<Datum>> map, String key, Intensity intensity, LocalDateTime date) throws TypeMismatchException {

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

		// iterate over list and remove enty if it matches date
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getDate().equals(date)) {
				list.remove(i);
				break;
			}
		}
		save();

	}

	/**
	 * Edit date of an ailment entry;
	 * 
	 * @param ailment Ailment String for which entry shall be changed
	 * @param date    LocalDateTime of entry
	 * @param newDate New LocalDateTime entry should be changed to
	 * @throws TypeMismatchException 
	 */
	public void editAilmentEntry(String ailment, LocalDateTime date, LocalDateTime newDate) throws TypeMismatchException {
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
	public void editSymptomEntry(String symptom, LocalDateTime date, LocalDateTime newDate) throws TypeMismatchException {
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

		if (map.get(key) != null && map.get(key).size() > 0)
			if (intensity != null && !(map.get(key).get(0) instanceof DatumWithIntensity))
				return;

		for (Datum d : map.get(key)) {
			if (d.getDate().equals(date) && d.getDate().getNano() == date.getNano()) {
				if (intensity != null) {
					if (!(d instanceof DatumWithIntensity))
						throw new TypeMismatchException();
					
					((DatumWithIntensity) d).setIntensity(intensity);
				
				}else {
					if (d instanceof DatumWithIntensity)
						intensity = ((DatumWithIntensity) d).getIntensity();
				}
				
				// if new date is assigned remove entry and insert new one so list stays sorted
				// via insertion sort
				if (newDate != null) {
					map.get(key).remove(d);
					addEntry(map, key, intensity, newDate);
					d.setDate(newDate);
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

		return getIterator(remedies.get(remedy), null);
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

	/**
	 * Saves data in save path provided in constructor.
	 */
	private void save() {

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

	// Iterator that iterates over all data in a List<Datum> for one date
	private class DayIterator implements Iterator<Datum> {

		private int index;
		private List<Datum> list;
		private LocalDate date;
		final private int OFFSET = 4;

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

			if (list.get(mid).getDate().minusHours(OFFSET).toLocalDate().compareTo(date)==0)
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
