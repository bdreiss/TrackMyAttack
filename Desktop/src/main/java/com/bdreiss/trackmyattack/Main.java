package main.java.com.bdreiss.trackmyattack;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.NetworkException;
import com.bdreiss.dataAPI.network.Dropbox;


import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main{

	public static void main(String[] args) throws MalformedURLException, InterruptedException{

		DataModel data = new DataModel(System.getProperty("user.home") + "/Apps/TrackMyAttack");

		try {
			System.out.println(data.mediumCause("rauchen"));
		} catch (EntryNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		System.exit(0);
		
		
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
//			System.out.println(e.toString());
		}

//		System.out.println(data.print());
		
		MainFrame frame = new MainFrame(data);	

		frame.setVisible(true);
	
//	
//		data.deleteSaveFile();
//		System.out.println(data.print());

//		int days = 4;
//		
//		GeoData gd = new GeoData(LocalDate.now().minusDays(days),null);
//		
//		gd.print();
//		
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
	

