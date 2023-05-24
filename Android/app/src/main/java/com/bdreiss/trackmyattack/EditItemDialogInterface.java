package com.bdreiss.trackmyattack;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

public interface EditItemDialogInterface {
    Iterator<Datum> getEntries(String key) throws EntryNotFoundException;
    void removeItem(String key, LocalDateTime date);
    void editDate(String key, LocalDateTime dateOriginal, LocalDateTime dateNew) throws TypeMismatchException;
    void editIntensity(String key, LocalDateTime date, Intensity intensity) throws TypeMismatchException;
}
