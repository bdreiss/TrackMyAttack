package com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;

/**
 * Interface to pass methods to test functions for entries with Intensity
 */

public interface AddWithIntensity {

	/**
	 * 
	 * @param s
	 * @param i
	 * @return
	 * @throws TypeMismatchException
	 */
	LocalDateTime add(String s, Intensity i) throws TypeMismatchException;

	/**
	 * 
	 * @param s
	 * @param b
	 */
	void addKey(String s, boolean b);

	/**
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * 
	 * @param s
	 * @param d
	 * @return
	 * @throws EntryNotFoundException
	 */
	Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException;

	/**
	 * 
	 * @param s
	 * @return
	 * @throws EntryNotFoundException
	 */
	Iterator<Datum> getAllData(String s) throws EntryNotFoundException;
}
