package main.java.com.bdreiss.dataAPI;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.enums.Category;
import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.Datum;

/*
 * Class representing the category SYMPTOM.
 */

public class SymptomDataModel extends AbstractDataModel{

    public SymptomDataModel(DataModel data){
        this.data = data;
        category = Category.SYMPTOM;
    }

    @Override
    public Iterator<String> getKeys() {
        return data.getSymptoms();
    }

    @Override
    public void addKey(String key, boolean intensity) {
        data.addSymptomKey(key);
    }

    @Override
    public Iterator<Datum> getData(String key) throws EntryNotFoundException {
        return data.getSymptomData(key);
    }

    @Override
    public void addData(String key) throws TypeMismatchException {
        throw new TypeMismatchException("This exception has been thrown because there was an attempt to add a symptom without Intensity.");
    }

    @Override
    public void addData(String key, Intensity intensity) throws TypeMismatchException {
        data.addSymptom(key, intensity);
    }

    @Override
    public void removeItem(String key, LocalDateTime date) {
        data.removeSymptom(key, date);
    }

    @Override
    public void removeKey(String key) {
        data.removeSymptomKey(key);
    }

    @Override
    public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
        data.editSymptomEntry(key, dateOriginal, dateNew);
    }

    @Override
    public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
        data.editSymptomEntry(key, date, intensity);
    }

	@Override
	public int getSize() {
		return data.getSymptomsSize();
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getSymptomData(key, date);
	}

}
