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
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	final static String PATH = "files/";

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {

		DataModel data = new DataModel(PATH);

//		data.print();
//		
//		data.save();
//
		processTextFile(data,PATH + "Text.txt");
//		
//		data.save();
		data.print();
//
		data.save();
		
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
				data.addAilment("Migraine", datum.getIntensity(), datum.getDate());
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
				data.addCause(cause, datum.getIntensity(), datum.getDate());
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
				data.addSymptom(symptom, datum.getIntensity(), datum.getDate());
			} else
				symptom = line;
		}

		String remedy = "";

		while (scanner.hasNextLine()) {


			String line = scanner.nextLine();

			if (line.contains(",")) {
				Datum datum = getDatum(line);
				data.addRemedy(remedy, datum.getIntensity(), datum.getDate());
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
		
		Intensity intensity = Intensity.noIntensity;

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
			intensity = Intensity.noIntensity;

		}

		return new Datum(date, intensity);
	}

}
