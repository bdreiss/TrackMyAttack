package main.java.com.bdreiss.dataAPI;


import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.enums.Category;
import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.Datum;


/*
 * Class representing the category AILMENT.
 */

public class AilmentDataModel extends AbstractDataModel{

    public AilmentDataModel(DataModel data){
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
    public void addData(String key) throws TypeMismatchException {
        throw new TypeMismatchException("This exception has been thrown because there was an attempt to add an ailment without Intensity.");
    }

    @Override
    public void addData(String key, Intensity intensity) throws TypeMismatchException {
        data.addAilment(key, intensity);
    }

    @Override
    public void removeItem(String key, LocalDateTime date) {
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


}
