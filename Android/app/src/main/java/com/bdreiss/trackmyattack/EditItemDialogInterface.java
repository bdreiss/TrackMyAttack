package com.bdreiss.trackmyattack;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;

public interface EditItemDialogInterface {
    Iterator<Datum> getEntries(String key) throws EntryNotFoundException;
    void removeItem(String key, LocalDateTime date);
}
