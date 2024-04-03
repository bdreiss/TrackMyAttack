package net.berndreiss.trackmyattack.data.core;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import net.berndreiss.trackmyattack.data.enums.Category;
import net.berndreiss.trackmyattack.data.enums.Intensity;
import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Coordinate;
import net.berndreiss.trackmyattack.data.util.Datum;


/**
 * Abstract class serving as an interface for methods implemented in DataModel while at the same time
 * housing information about the given category being used in DataModel (Cause, Symptom, Remedy).
 * Each type has their own sub-type.
 */
 public abstract class AbstractData {

    protected DataModel data;
    protected Category category; //CAUSE, SYMPTOM, REMEDY

    /**
     * Gets the original {@link DataModel}.
     * @return the original data model containing data
     */
    public DataModel getData(){
        return data;
    }

    /**
     * Gets the {@link Category}.
     * @return the category
     */
    public Category getCategory(){
        return category;
    }

    /**
     * Gets keys the data model holds.
     * @return the keys
     */
    public abstract Iterator<String> getKeys();
    
    /**
     * Adds a key to the data model.
     * @param key key to add
     * @param intensity true if key keeps track of intensity
     */
    public abstract void addKey(String key, boolean intensity);
    /**
     * Gets the iterator for a given key.
     * @param key key to get data for
     * @return iterator containing data
     * @throws EntryNotFoundException thrown if key cannot be found
     */
    public abstract Iterator<Datum> getData(String key) throws EntryNotFoundException;
    /**
     * Gets the iterator for a given key and date.
     * @param key key to get data for
     * @param date date for which to get data
     * @return iterator containing data
     * @throws EntryNotFoundException thrown if key cannot be found
     */
    public abstract Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException;
    /**
     * Adds an entry for a given key.
     * @param key key for which to add entry
     * @param coordinates coordinates where event happened
     * @throws TypeMismatchException thrown if entry is of wrong type
     */
    public abstract void addData(String key, Coordinate coordinates) throws TypeMismatchException;
    /**
     * Adds an entry for a given key with intensity.
     * @param key key for which to add entry
     * @param intensity intensity of the event
     * @param coordinates coordinates where event happened
     * @throws TypeMismatchException thrown if entry is of wrong type
     */
    public abstract void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException;
    /**
     * Removes an entry for a given key at certain date.
     * @param key key from which to remove from
     * @param date date at which entry is stored
     */
    public abstract void removeEntry(String key, LocalDateTime date);
    /**
     * Removes a key altogether.
     * @param key key to remove
     */
    public abstract void removeKey(String key);
    /**
     * Edits the date of an entry.
     * @param key key for which to edit a date
     * @param dateOriginal the original date
     * @param dateNew the new date
     * @throws TypeMismatchException thrown if entry is of wrong type
     */
    public abstract void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException;
    /**
     * Edits the intensity of an entry.
     * @param key key for which to edit the intensity
     * @param date date of the entry
     * @param intensity the new intensity
     * @throws TypeMismatchException thrown if entry is of wrong type
     */
    public abstract void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException;
    /**
     * Gets the size of the data.
     * @return the size of the data
     */
    public abstract int getSize();
    /**
     * Gets the count of entries for a key at a given date.
     * @param key key for which to get count
     * @param date date for which to get count
     * @return count of entries for a given date
     * @throws EntryNotFoundException thrown if key cannot be found
     */
    public abstract float count(String key, LocalDate date) throws EntryNotFoundException;
    /**
     * Get medium number of entries per day for a given key.
     * @param key key for which to get medium
     * @return the medium number of entries per day
     * @throws EntryNotFoundException thrown if key cannot be found
     */
    public abstract float getMedium(String key) throws EntryNotFoundException;
    /**
     * Gets the first date of any entry for any key.
     * @return the first date overall
     */
    public LocalDate getFirstDate() {
    	return data.firstDate;
    };
    /**
     *
     * @param category
     * @param string
     * @param datum
     */
  //TODO what was that for?
	public void addDatumDirectly(Category category, String string, Datum datum) {
		data.addDatumDirectly(category, string, datum);
	}
	
	/**
	 * Print data for debugging purposes.
	 * @return printable data
	 * 
	 */
	public String print() {
		return data.print();
	}
}
