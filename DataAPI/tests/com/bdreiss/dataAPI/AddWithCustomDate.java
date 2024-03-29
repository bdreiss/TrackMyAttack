package com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;


/**
* Interface to pass methods to test functions with custom dates.
*/

public interface AddWithCustomDate {

	/**
	 * 
	 * @param s
	 * @param d
	 * @throws TypeMismatchException
	 */
	void add(String s, LocalDateTime d) throws TypeMismatchException;
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
	 * @throws TypeMismatchException
	 */
	Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException, TypeMismatchException;
	/**
	 * 
	 * @param s
	 * @return
	 * @throws EntryNotFoundException
	 */
	Iterator<Datum> getAllData(String s) throws EntryNotFoundException;
}
