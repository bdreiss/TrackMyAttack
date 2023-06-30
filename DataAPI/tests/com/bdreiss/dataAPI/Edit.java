package com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;

public interface Edit {

	void add(String s, Intensity intensity, LocalDateTime ldt) throws TypeMismatchException;

	void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) throws TypeMismatchException;

	void editEntry(String s, LocalDateTime ldt, Intensity i) throws TypeMismatchException;

	Iterator<Datum> getData(String s);

}
