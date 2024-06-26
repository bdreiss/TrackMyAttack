package net.berndreiss.trackmyattack.data.core;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import net.berndreiss.trackmyattack.data.enums.Category;
import net.berndreiss.trackmyattack.data.enums.Intensity;
import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Coordinate;
import net.berndreiss.trackmyattack.data.util.Datum;

/**
 * Class representing the category SYMPTOM.
 */

public class SymptomData extends AbstractData{

	/**
	 * Instantiates a new instance of SymptomData.
	 * @param data original data model
	 */
    public SymptomData(DataModel data){
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
    public void addData(String key, Coordinate coordinates) throws TypeMismatchException {
        throw new TypeMismatchException("This exception has been thrown because there was an attempt to add a symptom without Intensity.");
    }

    @Override
    public void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException {
        data.addSymptom(key, intensity, coordinates);
    }

    @Override
    public void removeEntry(String key, LocalDateTime date) {
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

	@Override
	public float count(String key, LocalDate date) throws EntryNotFoundException {
		return data.countSymptom(key, date);
	}

	@Override
	public float getMedium(String key) throws EntryNotFoundException {
		return 0;
	}

}
