package main.java.com.bdreiss.trackmyattack;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;


public class GeoData implements Serializable{
	
	private String PREFIX = "https://dataset.api.hub.zamg.ac.at/v1/station/historical/klima-v1-1d?parameters=t,tmax,tmin,druckmit,rel,dampfmit&start=";
	private String INFIX1 = "T00:00&end=";
	private String INFIX2 = "T00:01&station_ids=";
	
	private LocalDate startDate;
	
	private ArrayList<GeoDay> data;
	
	private Point2D.Float coordinates;
	
	public GeoData(LocalDate startDate, Point2D.Float coordinates) {
		this.startDate = startDate;
		this.coordinates = coordinates;
	}
	
	public static void main(String args[]) throws MalformedURLException {
		
		GeoData gd = new GeoData(LocalDate.now().minusDays(7), new Point2D.Float());
		
		gd.update();
		
		
	}
	
	private void update() {
		
		String query = PREFIX + getQueryStartDate() + INFIX1 + LocalDate.now() + INFIX2 + getNearestStation();
		
		System.out.println(query);
		
		String jsonString = "";
		
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
			System.out.println("Got it!");
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
		
		JSONObject jso = new JSONObject(jsonString);
		
		System.out.println(jso.get("timestamps"));
	}
	
	private LocalDate getQueryStartDate() {
		return startDate;
	}
	
	private String getNearestStation() {
		return "5802";
	}
	
	private class GeoDay{
		LocalDate date;
		float temperatureMedian;
		float temperatureMax;
		float temperatureMin;
		
	}
	
}
