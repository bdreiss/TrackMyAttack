package com.bdreiss.trackmyattack.DataClasses;

import com.bdreiss.trackmyattack.Category;
import com.bdreiss.trackmyattack.R;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

/*
 * Class representing the category CAUSE.
 */

public class CauseDataModel extends AbstractDataModel{

    public CauseDataModel(DataModel data){
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
    public void addData(String key) throws TypeMismatchException {
        data.addCause(key);
    }

    @Override
    public void addData(String key, Intensity intensity) throws TypeMismatchException {
        data.addCause(key, intensity);
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
}
