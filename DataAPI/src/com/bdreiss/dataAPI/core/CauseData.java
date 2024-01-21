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

/**
 * Class representing the category CAUSE.
 */

public class CauseData extends AbstractData{

	/**
	 * Instantiates a new instance of CauseData.
	 * @param data original data model
	 */
    public CauseData(DataModel data){
        this.data = data;
        category = Category.CAUSE;
    }

    @Override
    public Iterator<String> getKeys() {
        return data.getCauses();
    }

    @Override
    public void addKey(String key, boolean intensity) {
        data.addCauseKey(key, intensity);
    }

    @Override
    public Iterator<Datum> getData(String key) throws EntryNotFoundException {
        return data.getCauseData(key);
    }

    @Override
    public void addData(String key, Coordinate coordinates) throws TypeMismatchException {
        data.addCause(key, coordinates);
    }

    @Override
    public void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException {
        data.addCause(key, intensity, coordinates);
    }

    @Override
    public void removeItem(String key, LocalDateTime date) {
        data.removeCause(key, date);
    }

    @Override
    public void removeKey(String key) {
        data.removeCauseKey(key);
    }

    @Override
    public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
        data.editCauseEntry(key, dateOriginal, dateNew);
    }

    @Override
    public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
        data.editCauseEntry(key, date, intensity);
    }

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return data.getCausesSize();
	}

	@Override
	public Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException {
		return data.getCauseData(key, date);
	}

	@Override
	public float count(String key, LocalDate date) throws EntryNotFoundException {
		return data.countCause(key, date);
	}

	@Override
	public float getMedium(String key) throws EntryNotFoundException {
		return data.mediumCause(key);
	}

}
