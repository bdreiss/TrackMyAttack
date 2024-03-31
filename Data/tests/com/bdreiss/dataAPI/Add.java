package com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.data.exceptions.EntryNotFoundException;
import com.bdreiss.data.exceptions.TypeMismatchException;
import com.bdreiss.data.util.Datum;


/**
 * Interface to pass methods to test functions.

 * @param s
 * @return
 * @throws EntryNotFoundException
 */
public interface Add {
	
	/**
	 * 
	 * @param s
	 * @return
	 * @throws TypeMismatchException
	 */
	LocalDateTime add(String s) throws TypeMismatchException;
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
