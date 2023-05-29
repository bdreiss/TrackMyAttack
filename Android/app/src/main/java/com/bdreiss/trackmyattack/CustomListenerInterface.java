package com.bdreiss.trackmyattack;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

public interface CustomListenerInterface {
    Iterator<Datum> getData(String key) throws EntryNotFoundException;
    void add(String key) throws TypeMismatchException;
    void add(String key, Intensity intensity) throws TypeMismatchException;
}
