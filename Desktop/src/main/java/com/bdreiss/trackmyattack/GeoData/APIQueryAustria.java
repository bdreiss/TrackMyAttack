package main.java.com.bdreiss.trackmyattack.GeoData;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.enums.Category;

public class APIQueryAustria implements APIQuery {

	private static final String PREFIX_AUSTRIA = "https://dataset.api.hub.geosphere.at/v1/station/historical/klima-v1-1d?parameters=t,tmax,tmin,druckmit,rel,dampfmit&start=";
	private static final String INFIX1_AUSTRIA = "T00:00&end=";
	private static final String INFIX2_AUSTRIA = "T00:01&station_ids=";

	public static void main(String args[]) {
		APIQueryAustria apiQuery = new APIQueryAustria();
		
		apiQuery.getNearestStationAustria(new Point2D.Double(48.15980285249154, 16.34069433192093));
	}

	//extract data from the JSONObject retrieved from https://dataset.api.hub.zamg.ac.at 
	public ArrayList<Float> extractData(JSONObject jso) {
		ArrayList<Float> data = new ArrayList<Float>();

		String dataString = jso.get("data").toString();

		dataString = dataString.substring(1, dataString.length() - 1);

		for (String s : dataString.split(","))
			if (s.equals("null"))
				data.add(null);
			else			
				data.add(Float.parseFloat(s));
			
		return data;
	}

	public void parseJSON(String jsonString, DataModel data, Category category) {
		JSONObject jso = new JSONObject(jsonString);

		ArrayList<LocalDate> timestamps = new ArrayList<LocalDate>();

		String timestampsString = jso.get("timestamps").toString();
		timestampsString = timestampsString.substring(1, timestampsString.length() - 1);

		for (String s : timestampsString.split(",")) {

			String dateString = s.substring(1, s.length() - 1).split("T")[0];

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

		for (int i = 0; i < timestamps.size(); i++) {

			LocalDate date = timestamps.get(i);
			Float temperatureMedian = tempData.get(i);
			Float temperatureMax = tempMaxData.get(i);
			Float temperatureMin = tempMinData.get(i);
			Float druck = druckData.get(i);
			Float dampf = dampfData.get(i);
			Float rel = relData.get(i);

			GeoDataType temperatureMedianE = GeoDataType.TEMPERATURE_MEDIAN;
			GeoDataType temperatureMinE = GeoDataType.TEMPERATURE_MIN;
			GeoDataType temperatureMaxE = GeoDataType.TEMPERATURE_MAX;
			GeoDataType pressureE = GeoDataType.PRESSURE;
			GeoDataType vaporE = GeoDataType.VAPOR;
			GeoDataType humidityE = GeoDataType.HUMIDITY;

			if (temperatureMedian != null)
				data.addDatumDirectly(category, temperatureMedianE.toString(),
						new GeoDatum(date.atStartOfDay(), temperatureMedian, temperatureMedianE.lowerBound, temperatureMedianE.upperBound));
			if (temperatureMin != null)
				data.addDatumDirectly(category, temperatureMinE.toString(), 
					new GeoDatum(date.atStartOfDay(), temperatureMin, temperatureMinE.lowerBound, temperatureMinE.upperBound));
			if (temperatureMax != null)
				data.addDatumDirectly(category, temperatureMaxE.toString(), 
						new GeoDatum(date.atStartOfDay(), temperatureMax, temperatureMaxE.lowerBound, temperatureMaxE.upperBound));
			if (druck != null)
				data.addDatumDirectly(category, pressureE.toString(),
					new GeoDatum(date.atStartOfDay(), druck, pressureE.lowerBound, pressureE.upperBound));
			if (dampf != null)
				data.addDatumDirectly(category, vaporE.toString(),
					new GeoDatum(date.atStartOfDay(), dampf, vaporE.lowerBound, vaporE.upperBound));
			if (rel != null)
				data.addDatumDirectly(category, humidityE.toString(),
					new GeoDatum(date.atStartOfDay(), rel, humidityE.lowerBound, humidityE.upperBound));
		}

	}

	public String JSONQuery(LocalDate startDate, LocalDate endDate, Point2D.Double coordinates) {

		String jsonString = null;
		
		try {
			
			jsonString = JSONQuery(PREFIX_AUSTRIA + startDate + INFIX1_AUSTRIA + endDate + INFIX2_AUSTRIA + getNearestStationAustria(coordinates));
			return jsonString;
		} finally {
			if (jsonString != null)
				System.out.println("Got data for " + startDate + " to " + endDate);

		}
	}
	
	public String JSONQuery(String query) {

		String jsonString = null;

		System.out.println("Fetching data...");

		try {
			URL url = new URL(query);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			StringBuilder sb = new StringBuilder();

			String inputLine;
			while ((inputLine = br.readLine()) != null)
				sb.append(inputLine);

			jsonString = sb.toString();

			if (jsonString.isEmpty()) {
				System.out.println("Got no data");
				System.exit(0);
			}
			br.close();

		} catch (MalformedURLException e) {
			System.out.println("Something wrong with the URL.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Something wrong with the connection.");
			e.printStackTrace();

		}

		return jsonString;
	}

	private String getNearestStationAustria(Point2D.Double coordinates) {
		
		if (coordinates == null)
			return null;
		
		Double offset = Double.POSITIVE_INFINITY;
		
		String jsonString = JSONQuery("https://dataset.api.hub.geosphere.at/v1/station/historical/klima-v1-1d/metadata");
		JSONObject jsonObject = new  JSONObject(jsonString);
		
		
		JSONArray stations = jsonObject.getJSONArray("stations");
		
		String nearestStation = null;
		
		for (Object jo : stations) {
			
			jsonObject = (JSONObject) jo;
			
			Double lat = Double.parseDouble(jsonObject.get("lat").toString());
			Double lon = Double.parseDouble(jsonObject.get("lon").toString());
			Double newOffset = Math.abs((coordinates.x - lat)) + Math.abs((coordinates.y - lon));
			
			if (newOffset < offset) {
				nearestStation = jsonObject.get("id").toString();
				offset = newOffset;
			}
		}
			
		return nearestStation;
	}

}
