package main.java.com.bdreiss.dataAPI;

import java.time.LocalDateTime;
import java.util.Iterator;

public interface Remove {

	void remove(String s, LocalDateTime d);
	void add(String s, LocalDateTime d);
	Iterator<Datum> getData(String s);
}