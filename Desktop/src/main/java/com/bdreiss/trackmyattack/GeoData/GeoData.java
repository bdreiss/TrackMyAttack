package main.java.com.bdreiss.trackmyattack.GeoData;

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
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;


public class GeoData extends AbstractDataModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String SAVEPATH = "data/";
	private String PREFIX = "https://dataset.api.hub.zamg.ac.at/v1/station/historical/klima-v1-1d?parameters=t,tmax,tmin,druckmit,rel,dampfmit&start=";
	private String INFIX1 = "T00:00&end=";
	private String INFIX2 = "T00:01&station_ids=";
	
	private LocalDate startDate;
	
	private DataModel data;
	private Category CATEGORY = Category.CAUSE;
	
	private Point2D.Double coordinates;
	
	
	public GeoData(LocalDate startDate, Point2D.Double coordinates) throws MalformedURLException {
		this.startDate = startDate;
		System.out.println(JSONQuery(LocalDate.now().minusDays(4),LocalDate.now().minusDays(1)));	
		data = new DataModel();
		
		this.coordinates = new Point2D.Double(48.1553234784118, 16.347233789433627);
				
		File saveFile = new File(SAVEPATH + this.coordinates.x + this.coordinates.y);
		
		if (saveFile.exists())
			load(saveFile);
		
		update();
	
	}

	public void print() {
		data.print();
	}
	
	private void load(File saveFile) {
		try {
			FileInputStream fis = new FileInputStream(saveFile);
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			data = (DataModel) ois.readObject();
			
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
	
	
				
//		if (geoDay.rel < 40)
//			intensity = Intensity.LOW;
//		if (geoDay.rel > 70)
//			intensity = Intensity.HIGH;
	
	
	
	private static ArrayList<Float> extractData(JSONObject jso){
		ArrayList<Float> data = new ArrayList<Float>();
		
		String druckDataString = jso.get("data").toString();
		
		druckDataString = druckDataString.substring(1, druckDataString.length()-1);
		
		
		for (String s: druckDataString.split(","))
				data.add(Float.parseFloat(s));

		return data;
	}
	
	private void update() {
			
		if (data.getCausesSize() == 0)
			parseJSON(JSONQuery(startDate, LocalDate.now().minusDays(1)));
		else { 
			
			if (data.firstDate.compareTo(startDate)>0)
				parseJSON(JSONQuery(startDate, data.firstDate.minusDays(1)));
			
			LocalDate lastDate = null;
			
			Iterator<Datum> it;
			try {
				it = data.getCauseData(GeoDataType.HUMIDITY.toString());
				while (it.hasNext())
					lastDate = it.next().getDate().toLocalDate();
				if (lastDate.compareTo(LocalDate.now().minusDays(1)) != 0)
					parseJSON(JSONQuery(lastDate.plusDays(1), LocalDate.now().minusDays(1)));

			} catch (EntryNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
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

			System.out.println(timestamps.get(i));
			
			GeoDataType temperatureMedianE = GeoDataType.TEMPERATURE_MEDIAN;
			GeoDataType temperatureMinE = GeoDataType.TEMPERATURE_MIN;
			GeoDataType temperatureMaxE = GeoDataType.TEMPERATURE_MAX;
			GeoDataType pressureE = GeoDataType.PRESSURE;
			GeoDataType vaporE = GeoDataType.VAPOR;
			GeoDataType humidityE = GeoDataType.HUMIDITY;

			data.addDatumDirectly(CATEGORY, temperatureMedianE.toString(), new GeoDatum(date.atStartOfDay(), temperatureMedian, temperatureMedianE.lowerBound, temperatureMedianE.upperBound));
			data.addDatumDirectly(CATEGORY, temperatureMinE.toString(), new GeoDatum(date.atStartOfDay(), temperatureMin, temperatureMinE.lowerBound, temperatureMinE.upperBound));
			data.addDatumDirectly(CATEGORY, temperatureMaxE.toString(), new GeoDatum(date.atStartOfDay(), temperatureMax, temperatureMaxE.lowerBound, temperatureMaxE.upperBound));
			data.addDatumDirectly(CATEGORY, pressureE.toString(), new GeoDatum(date.atStartOfDay(), druck, pressureE.lowerBound, pressureE.upperBound));
			data.addDatumDirectly(CATEGORY, vaporE.toString(), new GeoDatum(date.atStartOfDay(), dampf, vaporE.lowerBound, vaporE.upperBound));
			data.addDatumDirectly(CATEGORY, humidityE.toString(), new GeoDatum(date.atStartOfDay(), rel, humidityE.lowerBound, humidityE.upperBound));
		}
		
		
	}
	
	private String JSONQuery(LocalDate startDate, LocalDate endDate) {

		String query = PREFIX + startDate + INFIX1 + endDate + INFIX2 + getNearestStation();
				
		String jsonString = "";
		
		System.out.println("Fetching data...");
		
		try {
//			query = "https://dataset.api.hub.zamg.ac.at";
			URL url = new URL(query);
			URLConnection connection = url.openConnection();
			System.out.println("HERE");

			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			
			System.out.println("HERE");
			
			BufferedReader br = new BufferedReader(isr);
			
			System.out.println("HERE");
			

			StringBuilder sb = new StringBuilder();
			
			String inputLine;
			while ((inputLine = br.readLine())!=null)
				sb.append(inputLine);

				
			jsonString = sb.toString();
			
			if (jsonString.isEmpty()) {
				System.out.println("Got no data");
				System.exit(0);
			}
			System.out.println("Got data for " + startDate + " to " + endDate);
			br.close();
			
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
	
	
	private String getNearestStation() {
		return "5802";
	}
	
	@Override
	public void addKey(String key, boolean intensity) {}

	@Override
	public Iterator<Datum> getData(String key) throws EntryNotFoundException {
		return data.getCauseData(key);
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getCauseData(key, date);
	}

	@Override
	public void addData(String key) throws TypeMismatchException {}

	@Override
	public void addData(String key, Intensity intensity) throws TypeMismatchException {}

	@Override
	public void removeItem(String key, LocalDateTime date) {}

	@Override
	public void removeKey(String key) {}

	@Override
	public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {}

	@Override
	public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {}

	@Override
	public int getSize() {
		return data.getCausesSize();
	}

	@Override
	public int count(String key, LocalDate date) throws EntryNotFoundException {
		return data.countCause(key, date);
	}

	@Override
	public float getMedium(String key) throws EntryNotFoundException {
		return data.mediumCause(key);
	}

	@Override
	public Iterator<String> getKeys() {
		return data.getCauses();
	}
	
}
