package main.java.com.bdreiss.trackmyattack.GeoSphereAustria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.util.Coordinate;

import main.java.com.bdreiss.trackmyattack.GeoData.APIQuery;
import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;
import main.java.com.bdreiss.trackmyattack.GeoData.GeoDataType;
import main.java.com.bdreiss.trackmyattack.GeoData.GeoDatum;
import main.java.com.bdreiss.trackmyattack.GeoData.Station;

/**
 * Class implementing the APIQuery interface, containing methods for retrieving
 * meteorological data from Austrian stations via the API of GeoSphere Austria.
 */

public class APIQueryGeoSphereAustria implements APIQuery {

	private static final String PREFIX_AUSTRIA = "https://dataset.api.hub.geosphere.at/v1/station/historical/klima-v1-1d?parameters=t,tmax,tmin,druckmit,rel,dampfmit&start=";
	private static final String INFIX1_AUSTRIA = "T00:00&end=";
	private static final String INFIX2_AUSTRIA = "T00:01&station_ids=";
	private static final String STATION_LIST_QUERY = "https://dataset.api.hub.geosphere.at/v1/station/historical/klima-v1-1d/metadata";

	private static List<Station> stations;

	private GeoData geoData;
	private DataModel originalData;
	
	/**
	 * Creates a new instance and initializes list of GeoSphere Austria weather stations.
	 */
	public APIQueryGeoSphereAustria() {
		initializeStations();
		
	}
	
	@Override
	public void query(LocalDate startDate, LocalDate endDate, GeoData geoData, Category category) {
		
		this.geoData = geoData;
		this.originalData = geoData.getData();
		
		//keep count of date
		LocalDate currentDate = startDate;
		
		//keep track of start date for query
		LocalDate queryStartDate = startDate;
		
		//keep track of current station for date
		GeoSphereAustriaStation currentStation = getNearestStationAustria(originalData.getCoordinate(currentDate));
		
		
		//TODO: get date regardless of whether there is data or not
		//go from startDate to endDate and make a query if nearest station changes
		while (!(currentDate.compareTo(endDate) > 0)) {
			GeoSphereAustriaStation newStation = getNearestStationAustria(originalData.getCoordinate(currentDate));
			
			if (newStation == null)
				newStation = currentStation;
			
			if (newStation.compareTo(currentStation) != 0) {
				String jsonString = JSONQuery(queryStartDate, currentDate.minusDays(1), currentStation);
				
				parseJSON(jsonString, category);

				currentStation = newStation;
				queryStartDate = currentDate;
				
			}

			currentDate = currentDate.plusDays(1);

		}
		
		//make final query
		String jsonString = JSONQuery(queryStartDate, endDate, currentStation);
		parseJSON(jsonString, category);

				
	}

	/**
	 * parse JSON and add meteorological data to DataModel
	 * @param jsonString
	 * @param category
	 */
	public void parseJSON(String jsonString, Category category) {
		
		JSONObject jso = new JSONObject(jsonString);

		ArrayList<LocalDate> timestamps = new ArrayList<LocalDate>();

		//get JSON string for timestamps and remove first and last character '[' and ']'
		String timestampsString = jso.get("timestamps").toString();
		timestampsString = timestampsString.substring(1, timestampsString.length() - 1);

		if (timestampsString.isEmpty())
			return;
		
		//split string, get single dates and add them to timestamps
		for (String s : timestampsString.split(",")) {
			String dateString = s.substring(1, s.length() - 1).split("T")[0];

			int year = Integer.parseInt(dateString.split("-")[0]);
			int month = Integer.parseInt(dateString.split("-")[1]);
			int day = Integer.parseInt(dateString.split("-")[2]);
			LocalDate ld = LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day);
			timestamps.add(ld);
		}

		//get JSON strings for Druck, Temp etc. and extractData resulting in ArrayList<Float>
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

		//for every timestamp add data from ArrayLists extracted above
		for (int i = 0; i < timestamps.size(); i++) {

			LocalDate date = timestamps.get(i);
			Float temperatureMedian = tempData.get(i);
			Float temperatureMax = tempMaxData.get(i);
			Float temperatureMin = tempMinData.get(i);
			Float druck = druckData.get(i);
			Float dampf = dampfData.get(i);
			Float rel = relData.get(i);

			System.out.println(date);
			//TODO: handle case of data not being complete: first case all values are null -> remove station and make new query (make function boolean?), make nearestStation take List of Nonos -> make inner class query 
			
			GeoDataType temperatureMedianE = GeoDataType.TEMPERATURE_MEDIAN;
			GeoDataType temperatureMinE = GeoDataType.TEMPERATURE_MIN;
			GeoDataType temperatureMaxE = GeoDataType.TEMPERATURE_MAX;
			GeoDataType pressureE = GeoDataType.PRESSURE;
			GeoDataType vaporE = GeoDataType.VAPOR;
			GeoDataType humidityE = GeoDataType.HUMIDITY;

			if (temperatureMedian != null)
				geoData.addDatumDirectly(category, temperatureMedianE.toString(), new GeoDatum(date.atStartOfDay(),
						temperatureMedian, temperatureMedianE.lowerBound(), temperatureMedianE.upperBound()));
			if (temperatureMin != null)
				geoData.addDatumDirectly(category, temperatureMinE.toString(), new GeoDatum(date.atStartOfDay(),
						temperatureMin, temperatureMinE.lowerBound(), temperatureMinE.upperBound()));
			if (temperatureMax != null)
				geoData.addDatumDirectly(category, temperatureMaxE.toString(), new GeoDatum(date.atStartOfDay(),
						temperatureMax, temperatureMaxE.lowerBound(), temperatureMaxE.upperBound()));
			if (druck != null)
				geoData.addDatumDirectly(category, pressureE.toString(),
						new GeoDatum(date.atStartOfDay(), druck, pressureE.lowerBound(), pressureE.upperBound()));
			if (dampf != null)
				geoData.addDatumDirectly(category, vaporE.toString(),
						new GeoDatum(date.atStartOfDay(), dampf, vaporE.lowerBound(), vaporE.upperBound()));
			if (rel != null)
				geoData.addDatumDirectly(category, humidityE.toString(),
						new GeoDatum(date.atStartOfDay(), rel, humidityE.lowerBound(), humidityE.upperBound()));
		}

	}

	// extract data from the JSONObject retrieved from
	// https://dataset.api.hub.zamg.ac.at
	private static ArrayList<Float> extractData(JSONObject jso) {
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

	// make query to https://dataset.api.hub.geosphere.at for date range with
	// regards to coordinates and return JSON-String
	private static String JSONQuery(LocalDate startDate, LocalDate endDate, GeoSphereAustriaStation station) {

		String jsonString = null;
		System.out.println("Fetching data for " + startDate + " to " + endDate);

		jsonString = JSONQuery(PREFIX_AUSTRIA + startDate + INFIX1_AUSTRIA + endDate + INFIX2_AUSTRIA
				+ station.getId());
		return jsonString;
	}

	// make an arbitrary JSONQuery and return JSON-String
	private static String JSONQuery(String query) {

		String jsonString = null;

		// open connection and build string
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
				// TODO: how to handle not getting data?
			}
			br.close();


		} catch (MalformedURLException e) {
			System.out.println("Something wrong with the URL.");
			e.printStackTrace();
			// TODO: how to handle wrong URL?

		} catch (IOException e) {
			System.out.println("Something wrong with the connection.");
			e.printStackTrace();
			// TODO: how to handle IOException?

		}
		return jsonString;
	}

	//query stations from GeoSphere Austria and add them to the stations array
	private static void initializeStations() {
		String jsonString = JSONQuery(STATION_LIST_QUERY);
		JSONObject jsonObject = new JSONObject(jsonString);

		JSONArray jsonStations = jsonObject.getJSONArray("stations");
		

		List<Station> stationsArrayList = new ArrayList<Station>();
		
		for (Object jo : jsonStations) {

			jsonObject = (JSONObject) jo;
			Double lat = Double.parseDouble(jsonObject.get("lat").toString());
			Double lon = Double.parseDouble(jsonObject.get("lon").toString());
			int id = Integer.parseInt(jsonObject.get("id").toString());
			stationsArrayList.add(new GeoSphereAustriaStation(id, new Coordinate(lat,lon)));
		}
	
		//at the moment sorting is not necessary, since getNearestStationAustria() looks through the whole list
		//stations = CoordinateMergeSort.sort(stationsArrayList);
			
		stations = stationsArrayList;
	}
	
	// returns the GeoSphere Austria station nearest to coordinates
	private static GeoSphereAustriaStation getNearestStationAustria(Coordinate coordinates) {

		if (coordinates == null)
			return null;

		Double distance = Double.POSITIVE_INFINITY;

		GeoSphereAustriaStation nearestStation = null;

		for (Station s: stations) {

			Double lat = s.getLatitude();
			Double lon = s.getLongitude();
			Double newDistance = Math.abs((coordinates.getLatitude() - lat)) + Math.abs((coordinates.getLongitude() - lon));

			if (newDistance < distance) {
				nearestStation = (GeoSphereAustriaStation) s;
				distance = newDistance;
			}
		}

		System.out.println(nearestStation.getId());
		return nearestStation;
	}

}
