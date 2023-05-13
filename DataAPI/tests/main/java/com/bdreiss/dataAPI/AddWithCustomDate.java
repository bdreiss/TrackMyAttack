package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;


//interface to pass methods to test functions with custom dates
public interface AddWithCustomDate {
	
	void add(String s, LocalDateTime d) throws TypeMismatchException;
	void addKey(String s, boolean b);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException, TypeMismatchException;
	Iterator<Datum> getAllData(String s) throws EntryNotFoundException;
}
