package com.bdreiss.trackmyattack;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;

public interface AddKey {
    int getViewId();
    int getScrollViewId();
    Iterator<String> getData(DataModel data);
    void removeKey(DataModel data, String key);

}
