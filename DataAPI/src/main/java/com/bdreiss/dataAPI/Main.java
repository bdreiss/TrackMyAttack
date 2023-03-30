package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

	final static String PATH = "files/";

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {

		DataModel data = new DataModel(PATH);


//
//		processTextFile(data, PATH + "Text.txt");
//		data.save();
		data.print();
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
				else data.addAilment("Migraine", Intensity.NO_INTENSITY,datum.getDate());
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
				cause = line.trim();
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
				symptom = line.trim();
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
				remedy = line.trim();
		}

		scanner.close();
	}

	static Datum getDatum(String line) {
		String[] splitLine = line.split(",");

		LocalDateTime date = LocalDateTime.parse(splitLine[0]);


		if (splitLine.length > 1) {
			Intensity intensity = null;

			switch (splitLine[1].trim()) {

			case "low intensity":
				intensity = Intensity.LOW;
				break;
			case "medium intensity":
				intensity = Intensity.MEDIUM;
				break;
			case "high intensity":
				intensity = Intensity.HIGH;
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
