package main.java.com.bdreiss.dataAPI;

import java.io.File;
import java.io.FileInputStream;
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

public class DataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	//Name of the save file
	public static final String SAVE_FILE_NAME = "DataModel";

	private static File SAVE_FILE;
	
	/*  
	 * Representation of a single piece of data containing the date and intensity of the habit/symptom.
	*/
	
	private class Datum implements Serializable{
		private static final long serialVersionUID = 1L;
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
	//ArrayList containing potential remedies tried when symptoms showed up
	private Map<String, ArrayList<Datum>> remedies = new HashMap<>();

	//ArrayList containing all habits
	private ArrayList<String> habitsList = new ArrayList<>();
	//ArrayList containing all symptoms
	private ArrayList<String> symptomsList = new ArrayList<>();
	//ArrayList containing all remedies
	private ArrayList<String> remediesList = new ArrayList<>();

	
	public DataModel(String savePath) {
		SAVE_FILE = new File(savePath + "/" + SAVE_FILE_NAME);
	}
	
	public void addMigraine(String migraine, Intensity intensity){
		addEntry(migraines, "Migraine", new Date(),intensity);
	}

	public void addMigraine(String migraine, Intensity intensity, Date date){
		addEntry(migraines, "Migraine", date,intensity);
	}

	public void addHabit(String habit, Intensity intensity){
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, new Date(), intensity);
	}

	public void addHabit(String habit, Intensity intensity, Date date){
		if (!habitsList.contains(habit))
			habitsList.add(habit);

		addEntry(habits, habit, date, intensity);
	}

	public void addSymptom(String symptom, Intensity intensity){
		if (!symptomsList.contains(symptom))
			symptomsList.add(symptom);
		addEntry(symptoms, symptom, new Date(),intensity);
	}

	public void addSymptom(String symptom, Intensity intensity, Date date){
		if (!symptomsList.contains(symptom))
			symptomsList.add(symptom);
		addEntry(symptoms, symptom, date,intensity);
	}

	public void addRemedy(String remedy, Intensity intensity){
		if (!remediesList.contains(remedy))
			remediesList.add(remedy);
		addEntry(remedies, remedy, new Date(),intensity);
	}

	public void addRemedy(String remedy, Intensity intensity, Date date){
		if (!remediesList.contains(remedy))
			remediesList.add(remedy);
		addEntry(remedies, remedy, date,intensity);
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

	public ArrayList<String> returnRemedies(){ return remediesList;}

	public void save(){
		
		if (!SAVE_FILE.exists()) {
			try {
				SAVE_FILE.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(SAVE_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(this);

			oos.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//load contents of save file
	public void load(){

		DataModel data = null;

		//abort if it does not exist
		if (!SAVE_FILE.exists())
			return;

		try {
			FileInputStream fis = new FileInputStream(SAVE_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);

			data = (DataModel) ois.readObject();
			
			ois.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.habits = data.habits;
		this.habitsList = data.habitsList;
		this.migraines = data.migraines;
		this.remedies = data.remedies;
		this.remediesList = data.remediesList;
		this.symptoms = data.symptoms;
		this.symptomsList = data.symptomsList;
	}

	public static void deleteSaveFile(){
		SAVE_FILE.delete();
	}

	//printing for testing
	public String print(){
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (String s : migraines.keySet()){
			sb.append(s);
			sb.append("\n");
			System.out.println(s);
			for (Datum d: Objects.requireNonNull(migraines.get(s))) {
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}

		}
		sb.append("\n");
		sb.append("Habits");
		sb.append("\n");		
		for (String s : habits.keySet()) {
			sb.append(s);
			sb.append("\n");
			for (Datum d : Objects.requireNonNull(habits.get(s))){
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
		}
		}

		sb.append("\n");	
		sb.append("Symptoms");	
		sb.append("\n");	
		
		for (String s : symptoms.keySet()){
			sb.append(s);
			sb.append("\n");	
		for (Datum d: Objects.requireNonNull(symptoms.get(s))){
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}

		}

		sb.append("\n");	
		sb.append("Remedies");	
		sb.append("\n");	
		
		for (String s : remedies.keySet()){
			sb.append(s);
			sb.append("\n");	
			for (Datum d: Objects.requireNonNull(remedies.get(s))){
				sb.append(d.date + ", " + d.intensity);
				sb.append("\n");
			}

		}
		sb.append("\n");
		System.out.println(sb.toString());
		return sb.toString();

	}

}
