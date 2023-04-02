package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;


//interface to pass methods to test functions for entries with Intensity
public interface AddWithIntensity {
	
	LocalDateTime add(String s, Intensity i);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d);
	Iterator<Datum> getAllData(String s);
}
