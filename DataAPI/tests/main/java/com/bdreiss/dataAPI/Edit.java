package main.java.com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.Datum;

public interface Edit {

	void add(String s, Intensity intensity, LocalDateTime ldt) throws TypeMismatchException;

	void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) throws TypeMismatchException;

	void editEntry(String s, LocalDateTime ldt, Intensity i) throws TypeMismatchException;

	Iterator<Datum> getData(String s);

}
