package main.java.com.bdreiss.trackmyattack;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;


public class GeoData extends AbstractDataModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String JSONPATH = "data/JSON.txt";
	private static final String SAVEPATH = "data/";
	private String PREFIX = "https://dataset.api.hub.zamg.ac.at/v1/station/historical/klima-v1-1d?parameters=t,tmax,tmin,druckmit,rel,dampfmit&start=";
	private String INFIX1 = "T00:00&end=";
	private String INFIX2 = "T00:01&station_ids=";
	
	private LocalDate startDate;
	
	private ArrayList<GeoDay> data;
	
	private Point2D.Double coordinates;
	
	public GeoData(LocalDate startDate, Point2D.Double coordinates) throws MalformedURLException {
		this.startDate = startDate;
		
		this.coordinates = new Point2D.Double(48.1553234784118, 16.347233789433627);
		
		data = new ArrayList<>();
		
		File saveFile = new File(SAVEPATH + this.coordinates.x + this.coordinates.y);
		
		if (saveFile.exists())
			load(saveFile);
		
		update();
	
	}

	public void print() {
		for (GeoDay gd : data) 
			if (gd.date.compareTo(startDate) >=0)
				System.out.println("\n" + gd.date + ": \nDampf: " + gd.dampf + "\nLuftdruck: " + gd.druck + "\nFeuchtigkeit: " + gd.rel);
	}
	
	private void load(File saveFile) {
		try {
			FileInputStream fis = new FileInputStream(saveFile);
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			data = (ArrayList<GeoDay>) ois.readObject();
			
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Iterator<String> getKeys(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("humidity");
		return list.iterator();
	}
	
	
	public Intensity getHumdity(LocalDate ld) throws EntryNotFoundException {
		if (data.size() == 0)
			return Intensity.NO_INTENSITY;
		GeoDay geoDay = data.get(getIndex(ld,0,data.size()));
		
		Intensity intensity = Intensity.MEDIUM;
				
		if (geoDay.rel < 40)
			intensity = Intensity.LOW;
		if (geoDay.rel > 70)
			intensity = Intensity.HIGH;
		return intensity;
	}
	
	private int getIndex(LocalDate ld, int b, int e) {
					
		int index = -1;
		
		if (b>e)
			return index;
		
		int mid = (b+e)/2;
		
		if (data.get(mid).date.compareTo(ld)==0)
			return mid;
		
		if (data.get(mid).date.compareTo(ld) > 0)
			index = getIndex(ld, mid+1, e);
		else
			index = getIndex(ld, b, mid-1);
		
		return index;
	}
	
	
	private static ArrayList<Float> extractData(JSONObject jso){
		ArrayList<Float> data = new ArrayList<Float>();
		
		String druckDataString = jso.get("data").toString();
		
		druckDataString = druckDataString.substring(1, druckDataString.length()-1);
		
		
		for (String s: druckDataString.split(","))
				data.add(Float.parseFloat(s));

		return data;
	}
	
	private void update() {
			
		if (data.size() == 0)
			parseJSON(JSONQuery(startDate, LocalDate.now().minusDays(1)));
		else { 
			if (data.get(0).date.compareTo(startDate)>0)
				parseJSON(JSONQuery(startDate, data.get(0).date.minusDays(1)));
			
			if (data.get(data.size()-1).date.compareTo(LocalDate.now().minusDays(1)) != 0)
				parseJSON(JSONQuery(data.get(data.size()-1).date.plusDays(1), LocalDate.now().minusDays(1)));
			
			
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(SAVEPATH + coordinates.x + coordinates.y);
			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(data);
			
			oos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	private void parseJSON(String jsonString) {
		JSONObject jso = new JSONObject(jsonString);
		
		ArrayList<LocalDate> timestamps = new ArrayList<LocalDate>();

		String timestampsString = jso.get("timestamps").toString();
		timestampsString = timestampsString.substring(1, timestampsString.length()-1);
		
		for (String s: timestampsString.split(",")) {

			String dateString = s.substring(1,s.length()-1).split("T")[0];
			
			int year = Integer.parseInt(dateString.split("-")[0]); 
			int month = Integer.parseInt(dateString.split("-")[1]); 
			int day = Integer.parseInt(dateString.split("-")[2]); 
			LocalDate ld = LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day);
			timestamps.add(ld);
		}
		JSONArray jsa = (JSONArray) jso.get("features");
		JSONObject jso2 = (JSONObject) jsa.get(0);
		JSONObject jso3 = (JSONObject) jso2.get("properties");
		JSONObject jso4 = (JSONObject) jso3.get("parameters");
		
		JSONObject jsoDruck = (JSONObject) jso4.get("druckmit");
		JSONObject jsoTemp = (JSONObject) jso4.get("t");
		JSONObject jsoTempMax = (JSONObject) jso4.get("tmax");
		JSONObject jsoTempMin = (JSONObject) jso4.get("tmin");
		JSONObject jsoRel = (JSONObject) jso4.get("rel");
		JSONObject jsoDampf = (JSONObject) jso4.get("dampfmit");

		ArrayList<Float> druckData = extractData(jsoDruck);
		ArrayList<Float> tempData = extractData(jsoTemp);
		ArrayList<Float> tempMaxData = extractData(jsoTempMax);
		ArrayList<Float> tempMinData = extractData(jsoTempMin);
		ArrayList<Float> relData = extractData(jsoRel);
		ArrayList<Float> dampfData = extractData(jsoDampf);
		
		for (int i=0;i<timestamps.size();i++) {
			
			LocalDate date = timestamps.get(i);
			float temperatureMedian = tempData.get(i); 
			float temperatureMax = tempMaxData.get(i); 
			float temperatureMin = tempMinData.get(i);
			float druck = druckData.get(i); 
			float dampf = dampfData.get(i); 
			float rel = relData.get(i);
			
			if (data.size() != 0 && date.compareTo(data.get(0).date)<0)
				data.add(i, new GeoDay(date, temperatureMedian, temperatureMax, temperatureMin, druck, dampf, rel));
			else
				data.add(new GeoDay(date, temperatureMedian, temperatureMax, temperatureMin, druck, dampf, rel));
		}
		
		
	}
	
	private String JSONQuery(LocalDate startDate, LocalDate endDate) {

		String query = PREFIX + startDate + INFIX1 + endDate + INFIX2 + getNearestStation();
				
		String jsonString = "";
		
		System.out.println("Fetching data...");
		
		try {
			URL url = new URL(query);
		
		
			
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			
				
			InputStream is = connection.getInputStream();

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			jsonString = br.readLine();

			if (jsonString.isEmpty()) {
				System.out.println("Got no data");
				System.exit(0);
			}
			System.out.println("Got data for " + startDate + " to " + endDate);
			br.close();
			isr.close();
			is.close();
			connection.disconnect();
			
		} catch (MalformedURLException e) {
			System.out.println("Something wrong with the URL.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Something wrong with the connection.");
			e.printStackTrace();
			
		} 	
	
		return jsonString;
	}
	
	private LocalDate getQueryStartDate() {
		return startDate;
	}
	
	private String getNearestStation() {
		return "5802";
	}
	
	private class GeoDay implements Serializable{
		private static final long serialVersionUID = 1L;
		final LocalDate date;
		final float temperatureMedian;
		final float temperatureMax;
		final float temperatureMin;
		final float druck;
		final float dampf;
		final float rel;
	
		
		GeoDay(LocalDate date, float temperatureMedian, float temperatureMax, float temperatureMin, float druck, float dampf, float rel){
			this.date = date;
			this.temperatureMedian = temperatureMedian;
			this.temperatureMax = temperatureMax;
			this.temperatureMin = temperatureMin;
			this.druck = druck;
			this.dampf = dampf;
			this.rel = rel;
		}
		
	}

	@Override
	public void addKey(String key, boolean intensity) {}

	@Override
	public Iterator<Datum> getData(String key) throws EntryNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addData(String key) throws TypeMismatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addData(String key, Intensity intensity) throws TypeMismatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeItem(String key, LocalDateTime date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeKey(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int count(String key, LocalDate date) throws EntryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMedium(String key) throws EntryNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}