package main.java.com.bdreiss.dataAPI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	final static String PATH = "files/";

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {

		DataModel data = new DataModel(PATH);

//		data.addCause("Test",Intensity.low);
//		data.addCause("Test",Intensity.medium);
//		data.addCause("Test",Intensity.high);
//
//		LocalDateTime today = LocalDateTime.now();
//		data.addCause("Test",Intensity.low, today.minusDays(1));
//		today = LocalDateTime.now();
//		
//		data.addCause("Test",Intensity.medium, today.minusDays(1));
//		today = LocalDateTime.now();
//		data.addCause("Test",Intensity.high,today.minusDays(1));
//
//		data.addCause("Test",Intensity.low, today.minusDays(2));
//		today = LocalDateTime.now();
//		data.addCause("Test",Intensity.medium, today.minusDays(2));
//		today = LocalDateTime.now();
//		data.addCause("Test",Intensity.high,today.minusDays(2));
//
//
//		
//		
//		Iterator<Datum> it = data.getCauseData("Test", today.minusDays(0).toLocalDate());
//		
//		while (it.hasNext()) {
//			Datum datum = it.next();
//			System.out.println(datum.getDate() + " " +  datum.getIntensity());
//		}
//		

//		data.print();
//		
//		data.save();
//
		processTextFile(data, PATH + "Text.txt");
////		
		data.save();
		data.print();
//		LocalDateTime[] dates = {
//				LocalDateTime.now().minusDays(2),
//				LocalDateTime.now().minusDays(2),
//				LocalDateTime.now().minusDays(2),
//				LocalDateTime.now().minusDays(1),
//				LocalDateTime.now().minusDays(1),
//				LocalDateTime.now().minusDays(1),
//				LocalDateTime.now().minusDays(0),
//				LocalDateTime.now().minusDays(0),
//				LocalDateTime.now().minusDays(0)
//								};
//		
//		for (LocalDateTime d : dates)
//			System.out.println(d);
	}

	private static void processTextFile(DataModel data, String file) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(file));

		boolean check = true;
		while (scanner.hasNextLine() && check) {

			String line = scanner.nextLine();
			if (line.equals("Causes"))
				check = false;
			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addAilment("Migraine", ((DatumWithIntensity) datum).getIntensity(), datum.getDate());
			}
		}
		check = true;
		String cause = "";

		while (scanner.hasNextLine() && check) {

			String line = scanner.nextLine();

			if (line.equals("Symptoms"))
				check = false;

			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addCause(cause, ((DatumWithIntensity) datum).getIntensity(), datum.getDate());
				else
					data.addCause(cause, datum.getDate());
			} else
				cause = line;
		}

		check = true;
		String symptom = "";

		while (scanner.hasNextLine() && check) {

			String line = scanner.nextLine();
			if (line.equals("Remedies"))
				check = false;

			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addSymptom(symptom, ((DatumWithIntensity) datum).getIntensity(), datum.getDate());
			} else
				symptom = line;
		}

		String remedy = "";

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();

			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addRemedy(remedy, ((DatumWithIntensity) datum).getIntensity(), datum.getDate());
				else
					data.addRemedy(remedy, datum.getDate());
			} else
				remedy = line;
		}

		scanner.close();
	}

	static Datum getDatum(String line) {
		String[] splitLine = line.split(",");

		String[] splitDate = splitLine[0].split(" ");

//		Month month = Month.MARCH;
//		int year = 2023;
//		int day = Integer.parseInt(splitDate[2]);
//
//		String[] splitTime = splitDate[3].split(":");
//
//		int hour = Integer.parseInt(splitTime[0]);
//		int minute = Integer.parseInt(splitTime[1]);
//		int second = Integer.parseInt(splitTime[2]);
//
//		LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute, second);

		LocalDateTime date = LocalDateTime.parse(splitLine[0]);


		if (splitLine.length > 1) {
			Intensity intensity = null;

			switch (splitLine[1].trim()) {

			case "low":
				intensity = Intensity.low;
				break;
			case "medium":
				intensity = Intensity.medium;
				break;
			case "high":
				intensity = Intensity.high;
				break;

			default:
				intensity = null;
			}

			if (intensity == null)
				return new Datum(date);
						
			return new DatumWithIntensity(date, intensity);
		}
		
		return new Datum(date);

	}

}
