package com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;


//interface to pass methods to test functions
public interface Add {
	
	LocalDateTime add(String s) throws TypeMismatchException;
	void addKey(String s, boolean b);
	int getSize();
	Iterator<Datum> getData(String s, LocalDate d) throws EntryNotFoundException;
	Iterator<Datum> getAllData(String s) throws EntryNotFoundException;
}