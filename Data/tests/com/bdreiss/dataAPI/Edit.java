package com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.data.enums.Intensity;
import com.bdreiss.data.exceptions.TypeMismatchException;
import com.bdreiss.data.util.Datum;

/**
 * This interface provides methods for editing entries.
 */
public interface Edit {

	/**
	 * Adds an entry with intensity.
	 * @param s string representing the entry 
	 * @param intensity intensity, can also be null //TODO is that so?
	 * @param ldt LocalDateTime time of the occurrence of entry
	 * @throws TypeMismatchException //TODO when does this get thrown?
	 */
	void add(String s, Intensity intensity, LocalDateTime ldt) throws TypeMismatchException;

	/**
	 * Edits the entries time.
	 * @param s the entry to be edited
	 * @param ldt the time of the entry
	 * @param ldtNew the new date
	 * @throws TypeMismatchException //TODO when does this get thrown?
	 */
	void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) throws TypeMismatchException;

	/**
	 * Edits the entries intensity.
	 * @param s the entry to be edited
	 * @param ldt the time of the entry
	 * @param i the new intensity
	 * @throws TypeMismatchException //TODO when does this get thrown?
	 */
	void editEntry(String s, LocalDateTime ldt, Intensity i) throws TypeMismatchException;

	/**
	 * Get the data (time and intensity, if intensity exists).
	 * @param s string representing an entry
	 * @return the data for the entry
	 */
	Iterator<Datum> getData(String s);

}
