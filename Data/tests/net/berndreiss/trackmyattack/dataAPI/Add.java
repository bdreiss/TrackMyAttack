package net.berndreiss.trackmyattack.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Datum;


/**
 * Interface to pass methods to test functions.
 */
public interface Add {
	
	/**
	 * 
	 * @param s
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
