package com.bdreiss.dataAPI.core;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.dataAPI.util.Datum;
import com.bdreiss.dataAPI.util.IteratorWithIntensity;


/**
 * Class representing the category AILMENT.
 */

public class AilmentData extends AbstractData{

	/**
	 * Instantiates a new instance of AilmentData.
	 * @param data original data model
	 */
    public AilmentData(DataModel data){
        this.data = data;
        category = Category.AILMENT;
    }

    @Override
    public Iterator<String> getKeys() {
        return data.getAilments();
    }

    @Override
    public void addKey(String key, boolean intensity) {
        data.addAilmentKey(key);
    }

    @Override
    public Iterator<Datum> getData(String key) throws EntryNotFoundException {
        return data.getAilmentData(key);
    }

    @Override
    public void addData(String key, Coordinate coordinates) throws TypeMismatchException {
        throw new TypeMismatchException("This exception has been thrown because there was an attempt to add an ailment without Intensity.");
    }

    @Override
    public void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException {
        data.addAilment(key, intensity, coordinates);
    }

    @Override
    public void removeEntry(String key, LocalDateTime date) {
        data.removeAilment(key, date);
    }

    @Override
    public void removeKey(String key) {
        data.removeAilmentKey(key);
    }

    @Override
    public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
        data.editAilmentEntry(key, dateOriginal, dateNew);
    }

    @Override
    public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
        data.editAilmentEntry(key, date, intensity);
    }

	@Override
	public int getSize() {
		return data.getAilmentsSize();
	}
	
	/**
	 * Get data for entry for specific date as iterator.
	 * @param ld the date to get data for
	 * @return the iterator with data for the date
	 * @throws EntryNotFoundException
	 */
	//TODO substitute "Migraine"
	public IteratorWithIntensity getEntry(LocalDate ld) throws EntryNotFoundException {
		return (IteratorWithIntensity) data.getAilmentData("Migraine", ld);
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getAilmentData(key, date);
	}

	@Override
	public float count(String key, LocalDate date) throws EntryNotFoundException {
		return data.countAilment(key, date);
	}

	@Override
	public float getMedium(String key) {
		return 0;
	}


}
