package com.bdreiss.trackmyattack;

public interface AddKeyDialogListener {
    //takes key and whether it should have Intensity assigned to it, adds new key to DataModel
    //upon positive Click
    void addKey(String key, Boolean intensity);
    //updates the data in the Layout from which AddKeyDialog was initiated (so that key is shown there)
    void updateOriginalLayout();
}
