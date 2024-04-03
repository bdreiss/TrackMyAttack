package net.berndreiss.trackmyattack.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Datum;

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
