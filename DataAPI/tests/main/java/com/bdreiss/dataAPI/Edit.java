package main.java.com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

public interface Edit {

	void add(String s, Intensity intensity, LocalDateTime ldt) throws TypeMismatchException;

	void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew) throws TypeMismatchException;

	void editEntry(String s, LocalDateTime ldt, Intensity i) throws TypeMismatchException;

	Iterator<Datum> getData(String s);

}
