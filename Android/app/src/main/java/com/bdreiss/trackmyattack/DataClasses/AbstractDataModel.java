package com.bdreiss.trackmyattack.DataClasses;

import android.text.Layout;
import android.widget.LinearLayout;

import com.bdreiss.trackmyattack.Category;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

public abstract class AbstractDataModel {

    DataModel data;
    Category category;
    int layoutID;
    int linearLayoutID;

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

    public abstract Iterator<String> getKeys();
    public abstract void addKey(String key, boolean intensity);
    public abstract Iterator<Datum> getData(String key) throws EntryNotFoundException;
    public abstract void add(String key) throws TypeMismatchException;
    public abstract void add(String key, Intensity intensity) throws TypeMismatchException;
    public abstract void removeItem(String key, LocalDateTime date);
    public abstract void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException;
    public abstract void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException;


}
