package com.bdreiss.trackmyattack;

import main.java.com.bdreiss.dataAPI.DataModel;

public interface AddItemDialogListener {
    void onDialogPositiveClick(DataModel data, Category category, String item, Boolean intensity);
}
