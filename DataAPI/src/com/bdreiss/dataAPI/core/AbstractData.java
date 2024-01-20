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
 * Abstract class serving as an interface for methods implemented in DataModel while at the same time
 * housing information about the given category being used in DataModel (Cause, Symptom, Remedy).
 * Each type has their own sub-type.
 */
 public abstract class AbstractData {

    protected DataModel data;
    protected Category category; //CAUSE, SYMPTOM, REMEDY

    /**
     * Gets the original {@link DataModel}.
     * @return
     */
    public DataModel getData(){
        return data;
    }

    /**
     * Gets the {@link Category}.
     * @return
     */
    public Category getCategory(){
        return category;
    }

    /**
     * Gets keys the data model holds.
     * @return the keys
     */
    public abstract Iterator<String> getKeys();
    
    /**
     * Adds a key to the data model.
     * @param key
     * @param intensity
     */
    public abstract void addKey(String key, boolean intensity);
    /**
     * 
     * @param key Key to be added
     * @return the data for the key.
     * @throws EntryNotFoundException
     */
    public abstract Iterator<Datum> getData(String key) throws EntryNotFoundException;
    /**
     * 
     * @param key
     * @param date
     * @return
     * @throws EntryNotFoundException
     */
    public abstract Iterator<Datum> getData(String key, LocalDate date) throws EntryNotFoundException;
    /**
     * 
     * @param key
     * @param coordinates
     * @throws TypeMismatchException
     */
    public abstract void addData(String key, Coordinate coordinates) throws TypeMismatchException;
    /**
     * 
     * @param key
     * @param intensity
     * @param coordinates
     * @throws TypeMismatchException
     */
    public abstract void addData(String key, Intensity intensity, Coordinate coordinates) throws TypeMismatchException;
    /**
     * 
     * @param key
     * @param date
     */
    public abstract void removeItem(String key, LocalDateTime date);
    /**
     * 
     * @param key
     */
    public abstract void removeKey(String key);
    /**
     * 
     * @param key
     * @param dateOriginal
     * @param dateNew
     * @throws TypeMismatchException
     */
    public abstract void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException;
    /**
     * 
     * @param key
     * @param date
     * @param intensity
     * @throws TypeMismatchException
     */
    public abstract void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException;
    /**
     * 
     * @return
     */
    public abstract int getSize();
    /**
     * 
     * @param key
     * @param date
     * @return
     * @throws EntryNotFoundException
     */
    public abstract float count(String key, LocalDate date) throws EntryNotFoundException;
    /**
     * 
     * @param key
     * @return
     * @throws EntryNotFoundException
     */
    public abstract float getMedium(String key) throws EntryNotFoundException;
    /**
     * 
     * @return
     */
    public LocalDate getFirstDate() {
    	return data.firstDate;
    };
    /**
     * 
     * @param category
     * @param string
     * @param datum
     */
	public void addDatumDirectly(Category category, String string, Datum datum) {
		data.addDatumDirectly(category, string, datum);
	}
}
