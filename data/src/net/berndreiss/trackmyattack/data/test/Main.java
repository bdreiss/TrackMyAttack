package net.berndreiss.trackmyattack.data.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

import net.berndreiss.trackmyattack.data.core.DataModel;
import net.berndreiss.trackmyattack.data.enums.Intensity;
import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Datum;
import net.berndreiss.trackmyattack.data.util.DatumWithIntensity;

/**
 * 
 */
public class Main {

	final static String PATH = System.getProperty("user.home") + "/Apps/TrackMyAttack";

	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 * @throws EntryNotFoundException
	 * @throws TypeMismatchException
	 */
	public static void main(String[] args) throws FileNotFoundException, InterruptedException, EntryNotFoundException, TypeMismatchException {
		
		DataModel data = new DataModel(PATH);
				
		if (data.getSaveFile() != null)
			data.getSaveFile().delete();
		
		data = new DataModel(PATH);
		
		processTextFile(data, PATH + "/Text.txt");
				
		data.print();
	}

	private static void processTextFile(DataModel data, String file) throws FileNotFoundException, TypeMismatchException {
		Scanner scanner = new Scanner(new File(file));

		String lastLine = "";
		boolean check = true;
		while (scanner.hasNextLine() && check) {
			String line = scanner.nextLine();

			while (lastLine.equals(line) && scanner.hasNextLine())
				line = scanner.nextLine();

			lastLine = line;

			if (line.equals("Causes"))
				check = false;
			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addAilment("Migraine", ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum.getDate().toLocalDate()));
				else data.addAilment("Migraine", Intensity.NO_INTENSITY,datum.getDate(),data.getCoordinate(datum.getDate().toLocalDate()));
			}
		}
		check = true;
		String cause = "";

		while (scanner.hasNextLine() && check) {

			String line = scanner.nextLine();

			while (lastLine.equals(line) && scanner.hasNextLine())
				line = scanner.nextLine();

			lastLine = line;

			if (line.equals("Symptoms"))
				check = false;

			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addCause(cause, ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum.getDate().toLocalDate()));
				else
					data.addCause(cause, datum.getDate(), data.getCoordinate(datum.getDate().toLocalDate()));
			} else
				cause = line.trim();
		}

		check = true;
		String symptom = "";

		while (scanner.hasNextLine() && check) {

			String line = scanner.nextLine();
			
			while (lastLine.equals(line) && scanner.hasNextLine())
				line = scanner.nextLine();

			lastLine = line;

			if (line.equals("Remedies"))
				check = false;

			
			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addSymptom(symptom, ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum.getDate().toLocalDate()));
			} else
				symptom = line.trim();
		}

		String remedy = "";

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();

			while (lastLine.equals(line) && scanner.hasNextLine())
				line = scanner.nextLine();

			lastLine = line;

			if (line.contains(",")) {
				Datum datum = getDatum(line);
				if (datum instanceof DatumWithIntensity)
					data.addRemedy(remedy, ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum.getDate().toLocalDate()));
				else
					data.addRemedy(remedy, datum.getDate(), data.getCoordinate(datum.getDate().toLocalDate()));
			} else
				remedy = line.trim();
		}

		scanner.close();
	}

	static Datum getDatum(String line) {
		String[] splitLine = line.split(",");

		LocalDateTime date = LocalDateTime.parse(splitLine[0]);
		
		if (splitLine.length > 2) {
			Intensity intensity = null;

			switch (splitLine[2].trim()) {

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
