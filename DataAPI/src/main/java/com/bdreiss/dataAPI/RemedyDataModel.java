package main.java.com.bdreiss.dataAPI;


import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.enums.Category;
import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.Datum;

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
    public void addData(String key) throws TypeMismatchException {
        data.addRemedy(key);
    }

    @Override
    public void addData(String key, Intensity intensity) throws TypeMismatchException {
        data.addRemedy(key, intensity);
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

}
