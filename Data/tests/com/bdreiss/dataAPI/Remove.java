package com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.data.exceptions.EntryNotFoundException;
import com.bdreiss.data.exceptions.TypeMismatchException;
import com.bdreiss.data.util.Datum;

/**
 * 
 */
public interface Remove {

	/**
	 * 
	 * @param s
	 * @param d
	 */
	void remove(String s, LocalDateTime d);
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
	 * @return
	 * @throws EntryNotFoundException
	 */
	Iterator<Datum> getData(String s) throws EntryNotFoundException;
}
