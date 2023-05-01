package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;


//interface to pass methods to test functions for entries with Intensity
public interface AddWithIntensity {
	
	LocalDateTime add(String s, Intensity i);
	void addKey(String s, boolean b);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException;
	Iterator<Datum> getAllData(String s) throws EntryNotFoundException;
}
