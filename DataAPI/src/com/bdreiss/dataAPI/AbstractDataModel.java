package com.bdreiss.dataAPI;


import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Datum;


/*
 * Abstract class serving as an interface for methods implemented in DataModel while at the same time
 * housing information about the given category being used in DataModel (Cause, Symptom, Remedy).
 * Each type has their own sub-type.
 */
 public abstract class AbstractDataModel {

    protected DataModel data;
    protected Category category; //CAUSE, SYMPTOM, REMEDY

    public DataModel getData(){
        return data;
    }

    public Category getCategory(){
        return category;
    }

    //for documentation of these methods look into DataAPI
    public abstract Iterator<String> getKeys();
    public abstract void addKey(String key, boolean intensity);
    public abstract Iterator<Datum> getData(String key) throws EntryNotFoundException;
    public abstract Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException;
    public abstract void addData(String key, Point2D.Double coordinates) throws TypeMismatchException;
    public abstract void addData(String key, Intensity intensity, Point2D.Double coordinates) throws TypeMismatchException;
    public abstract void removeItem(String key, LocalDateTime date);
    public abstract void removeKey(String key);
    public abstract void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException;
    public abstract void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException;
    public abstract int getSize();
    public abstract float count(String key, LocalDate date) throws EntryNotFoundException;
    public abstract float getMedium(String key) throws EntryNotFoundException;
    public LocalDate getFirstDate() {
    	return data.firstDate;
    };
}
