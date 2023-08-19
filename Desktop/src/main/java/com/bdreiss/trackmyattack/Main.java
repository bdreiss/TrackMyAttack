package main.java.com.bdreiss.trackmyattack;

import com.bdreiss.dataAPI.AilmentDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.NetworkException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.network.Dropbox;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.DatumWithIntensity;
import com.bdreiss.dataAPI.util.IteratorWithIntensity;

import main.java.com.bdreiss.trackmyattack.GeoData.GeoData;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main{

	public static void main(String[] args) throws MalformedURLException, InterruptedException{

		DataModel data = new DataModel(System.getProperty("user.home") + "/Apps/TrackMyAttack");				
		
		if (!(new File(Dropbox.getDbxFilePath(data)).exists())) {
			try {
				URL url = new URL(Dropbox.getAuthorizationURL(getKey()));
				System.out.println(Dropbox.message);
				System.out.println(url);
				
				InputStreamReader isr = new InputStreamReader(System.in);
				
				BufferedReader br = new BufferedReader(isr);
								
				String authorizationToken = br.readLine();
				
				br.close();
				isr.close();
				
				Dropbox.authorize(getKey(), authorizationToken, data);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NetworkException e) {
				e.printStackTrace();
			}

		}
			

		try {
			Dropbox.download(data);
		} catch (NetworkException e) {
			e.printStackTrace();
		}

		GeoData geoData = new GeoData(data, null);
	
				
		MainFrame frame = new MainFrame(data, geoData);	

		frame.setVisible(true);
	

		
				
	}
	private static String getKey() throws NetworkException{

		String key = "";
		try {
			
			FileReader is = new FileReader(System.getProperty("user.home") + "/Apps/TrackMyAttack/TrackMyAttackKey");
			BufferedReader bis = new BufferedReader(is);
			
			key = bis.readLine();
			
			bis.close();
			is.close();
		} catch (IOException e) {
			throw new NetworkException(e);
		}
		System.out.println(key);
		return key;
	}
	
}
	

