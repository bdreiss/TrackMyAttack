package com.bdreiss.trackmyattack;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;

public interface EditItemDialogInterface {
    Iterator<Datum> getEntries(String key) throws EntryNotFoundException;
}
