package com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;

public interface Remove {

	void remove(String s, LocalDateTime d);
	void add(String s, LocalDateTime d) throws TypeMismatchException;
	Iterator<Datum> getData(String s) throws EntryNotFoundException;
}
