package com.bdreiss.dataAPI.test;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.DatumWithIntensity;
import com.dropbox.core.oauth.DbxCredential;

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
		String nextLine = "";
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
					data.addAilment("Migraine", ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum));
				else data.addAilment("Migraine", Intensity.NO_INTENSITY,datum.getDate(),data.getCoordinate(datum));
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
					data.addCause(cause, ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum));
				else
					data.addCause(cause, datum.getDate(), data.getCoordinate(datum));
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
					data.addSymptom(symptom, ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum));
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
					data.addRemedy(remedy, ((DatumWithIntensity) datum).getIntensity(), datum.getDate(), data.getCoordinate(datum));
				else
					data.addRemedy(remedy, datum.getDate(), data.getCoordinate(datum));
			} else
				remedy = line.trim();
		}

		scanner.close();
	}

	static Datum getDatum(String line) {
		String[] splitLine = line.split(",");

		LocalDateTime date = LocalDateTime.parse(splitLine[0]);


		Point2D.Double coordinates = splitLine[1].trim().equals("null") ? null : new Point2D.Double(Double.parseDouble(splitLine[1].split(":")[0].trim()),Double.parseDouble(splitLine[1].split(":")[1].trim()));
		
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
