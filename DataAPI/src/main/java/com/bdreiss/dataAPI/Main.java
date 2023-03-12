package main.java.com.bdreiss.dataAPI;

public class Main {

	public static void main(String[] args) {
		
		DataModel data = new DataModel(".");
		

		data.addHabit("null", Intensity.high);
		
		data.addHabit("troll", Intensity.high);
		
		data.save();
		
		System.exit(0);
		
		for (int i = 0; i < 10000000;i++) {
			
			for(int j=0; j < 10; j++) {
				data.addHabit(String.valueOf(i), Intensity.high);
			}
			
			if (i%100000==0) {
				System.out.println("CHECK");
				data.save();

			}
		}		
		System.out.println("DONE");
		
	}
}
