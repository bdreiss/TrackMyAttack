package com.bdreiss.trackmyattack;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DataModel implements Serializable {

	private static final String SAVE_FILE_NAME = "DataModel";

	/*  
	 * Representation of a single piece of data containing the date and intensity of the habit/symptom.
	*/
	
	private class Datum implements Serializable{
		public Date date;
		public Intensity intensity;

		public Datum (Date date, Intensity intensity){
			this.date = date;
			this.intensity = intensity;
		}
		
	}
	
	/*
	 *	The following Maps contain all the relevant data. They each contain the title of the
	 *	habit/symptom as a key and an ArrayList containing all relevant information
	 * 	as single pieces of data (see Datum).
	 */
	
	//ArrayList containing all instances of a migraine
	private Map<String,ArrayList<Datum>> migraines = new HashMap<>();
	//ArrayList containing habits that might be related to migraine attacks
	private Map<String,ArrayList<Datum>> habits = new HashMap<>();
	//ArrayList containing symptoms that might be related to migraine attacks
	private Map<String,ArrayList<Datum>> symptoms = new HashMap<>();

	private ArrayList<String> habitsList = new ArrayList<>();
	private ArrayList<String> symptomsList = new ArrayList<>();

	public void addMigraine(Intensity intensity){
		addEntry(migraines, "Migraine", new Date(),intensity);
	}

	public void addHabit(String habit, Intensity intensity){
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, new Date(), intensity);
	}

	public void addSymptom(String symptom, Intensity intensity){
		if (!symptomsList.contains(symptom))
			symptomsList.add(symptom);
		addEntry(symptoms, symptom, new Date(),intensity);
	}

	//abstracts the task of adding entries to the different ArrayLists
	private void addEntry(Map<String, ArrayList<Datum>> map, String key, Date date, Intensity intensity){

		//create entry with key if it doesn't exist
		if (!map.containsKey(key))
			map.put(key,new ArrayList<>());

		//add new piece of data to ArrayList in according habit/
		map.get(key).add(new Datum(date, intensity));
	}

	public ArrayList<String> returnMigraines(){
		ArrayList<String> al = new ArrayList<>();
		al.add("Migraine");
		return al;
	}

	public ArrayList<String> returnHabits(){
		return habitsList;
	}

	public ArrayList<String> returnSymptoms(){
		return symptomsList;
	}

	public void save(Context context){
		File saveFile = new File(context.getFilesDir() + SAVE_FILE_NAME);

		if (!saveFile.exists()) {
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(this);

			oos.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//load contents of save file
	public static DataModel load(Context context){

		DataModel data = null;

		//map to be returned
		Map<String,ArrayList<Datum>> map = null;

		//get save file
		File saveFile = new File(context.getFilesDir() + SAVE_FILE_NAME);

		//abort if it does not exist
		if (!saveFile.exists())
			return null;

		try {
			FileInputStream fis = new FileInputStream(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);

			data = (DataModel) ois.readObject();

		} catch (IOException | ClassNotFoundException e) {
			Log.d("LOGXXX", e.toString());
		}

		return data;
	}

	public static void deleteSaveFile(Context context){
		new File(context.getFilesDir() + SAVE_FILE_NAME).delete();
	}

	//printing for testing
	public void print(){
		System.out.println();
		for (String s : migraines.keySet()){
			System.out.println(s);
			Log.d("LOGXXX", s);
			for (Datum d: Objects.requireNonNull(migraines.get(s))) {
				Log.d("LOGXXX", d.date + ", " + d.intensity);
				System.out.print(d.date + ", " + d.intensity);
			}

		}
		System.out.println();
		System.out.println("Habits");
		Log.d("LOGXXX", "Habits");

		for (String s : habits.keySet()) {
			System.out.println(s);
			Log.d("LOGXXX", s);
			for (Datum d : Objects.requireNonNull(habits.get(s))){
				Log.d("LOGXXX", d.date + ", " + d.intensity);
				System.out.print(d.date + ", " + d.intensity);
		}
		}
		System.out.println();
		System.out.println("Symptoms");
		Log.d("LOGXXX", "Symptoms");

		for (String s : symptoms.keySet()){
			System.out.println(s);
			Log.d("LOGXXX", s);
			for (Datum d: Objects.requireNonNull(symptoms.get(s))){
				Log.d("LOGXXX", d.date + ", " + d.intensity);
				System.out.print(d.date + ", " + d.intensity);
			}

		}
		System.out.println();


	}

}
