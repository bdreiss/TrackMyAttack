package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;


//interface to pass methods to test function adding entries with custom date
public interface AddWithIntensityAndCustomDate {
	void add(String s, Intensity i, LocalDateTime d);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d);
}
