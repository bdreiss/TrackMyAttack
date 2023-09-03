package main.java.com.bdreiss.trackmyattack.GeoData;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONObject;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.enums.Category;

public interface APIQuery {
	
	ArrayList<Float> extractData(JSONObject jso);
	void parseJSON(String jsonString, DataModel data, Category category);
	String JSONQuery(LocalDate startDate, LocalDate endDate);
}
