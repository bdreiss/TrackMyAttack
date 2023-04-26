package main.java.com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

public interface Edit {

	void addKey(String key);

	void add(String s, Intensity intensity, LocalDateTime ldt);

	void editEntry(String s, LocalDateTime ldt, LocalDateTime ldtNew);

	void editEntry(String s, LocalDateTime ldt, Intensity i);

	Iterator<Datum> getData(String s);

}
