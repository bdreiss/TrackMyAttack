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
 * Class representing the category SYMPTOM.
 */

public class SymptomDataModel extends AbstractDataModel{

    public SymptomDataModel(DataModel data){
        this.data = data;
        category = Category.SYMPTOM;
        layoutID = R.layout.symptoms;
        linearLayoutID = R.id.linear_layout_symptoms;
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
        throw new TypeMismatchException();
        //TODO: throw new TypeMismatchException(); with message!
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
    public void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException {
        data.editSymptomEntry(key, dateOriginal, dateNew);
    }

    @Override
    public void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException {
        data.editSymptomEntry(key, date, intensity);
    }
}
