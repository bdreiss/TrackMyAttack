package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;


//interface to pass methods to test functions with custom dates
public interface AddWithCustomDate {
	
	void add(String s, LocalDateTime d);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d);
	Iterator<Datum> getAllData(String s);
}
