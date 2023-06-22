package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.Datum;


//interface to pass methods to test function adding entries with custom date
public interface AddWithIntensityAndCustomDate {
	void add(String s, Intensity i, LocalDateTime d) throws TypeMismatchException;
	void addKey(String s, boolean b);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException;
	Iterator<Datum> getAllData(String s) throws EntryNotFoundException;
}
