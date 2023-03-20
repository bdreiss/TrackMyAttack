package main.java.com.bdreiss.dataAPI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Main {

	public static void main(String[] args) {
		
		DataModel data = new DataModel("files/");
		data.load();

		//create instance of Date for yesterday
		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		ldt = ldt.minusDays(1);
		Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		

		data.print();
		
		data.save();
		
		System.exit(0);
		
		for (int i = 0; i < 10000000;i++) {
			
			for(int j=0; j < 10; j++) {
				data.addCause(String.valueOf(i), Intensity.high);
			}
			
			if (i%100000==0) {
				System.out.println("CHECK");
				data.save();

			}
		}		
		System.out.println("DONE");
		
	}
}
