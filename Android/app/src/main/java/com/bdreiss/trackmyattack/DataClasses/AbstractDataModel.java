package com.bdreiss.trackmyattack.DataClasses;

import com.bdreiss.trackmyattack.Category;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

/*
 * Abstract class serving as an interface for methods implemented in DataModel while at the same time
 * housing information about the given category being used in DataModel (Cause, Symptom, Remedy).
 * Each type has their own sub-type.
 */
public abstract class AbstractDataModel {

    DataModel data;
    Category category; //CAUSE, SYMPTOM, REMEDY
    int layoutID; //Layout representing the category
    int linearLayoutID; //LinearLayout holding entries of the category above

    public DataModel getData(){
        return data;
    }

    public Category getCategory(){
        return category;
    }

    public int getLayoutID(){
        return layoutID;
    }

    public int getLinearLayoutID(){
        return linearLayoutID;
    }

    //for documentation of these methods look into DataAPI
    public abstract Iterator<String> getKeys();
    public abstract void addKey(String key, boolean intensity);
    public abstract Iterator<Datum> getData(String key) throws EntryNotFoundException;
    public abstract void addData(String key) throws TypeMismatchException;
    public abstract void addData(String key, Intensity intensity) throws TypeMismatchException;
    public abstract void removeItem(String key, LocalDateTime date);
    public abstract void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException;
    public abstract void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException;


}
