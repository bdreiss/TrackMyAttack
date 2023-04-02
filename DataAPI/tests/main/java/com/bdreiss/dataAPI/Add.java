package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;


//interface to pass methods to test functions
public interface Add {
	
	LocalDateTime add(String s);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d);
	Iterator<Datum> getAllData(String s);
}
