package com.bdreiss.dataAPI;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.dataAPI.util.Datum;

/*
 * Class representing the category REMEDY.
 */

public class RemedyDataModel extends AbstractDataModel{

    public RemedyDataModel(DataModel data){
        this.data = data;
        category = Category.REMEDY;
    }

    @Override
    public Iterator<String> getKeys() {
        return data.getRemedies();
    }

    @Override
    public void addKey(String key, boolean intensity) {
        data.addRemedyKey(key, intensity);
    }

    @Override
    public Iterator<Datum> getData(String key) throws EntryNotFoundException {
        return data.getRemedyData(key);
    }

    @Override
    public void addData(String key, Coordinate coordinates) throws TypeMismatchException {
        data.addRemedy(key, coordinates);
    }

    @Override
    public void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException {
        data.addRemedy(key, intensity, coordinates);
    }

    @Override
    public void removeItem(String key, LocalDateTime date) {
        data.removeRemedy(key, date);
    }

    @Override
    public void removeKey(String key) {
        data.removeRemedyKey(key);
    }

    @Override
    public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
        data.editRemedyEntry(key, dateOriginal, dateNew);
    }

    @Override
    public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
        data.editRemedyEntry(key, date, intensity);
    }

	@Override
	public int getSize() {
		return data.getRemediesSize();
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getRemedyData(key, date);
	}

	@Override
	public float count(String key, LocalDate date) throws EntryNotFoundException {
		return data.countRemedy(key, date);
	}

	@Override
	public float getMedium(String key) throws EntryNotFoundException {
		return data.mediumRemedy(key);
	}

}
