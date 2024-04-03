package net.berndreiss.trackmyattack.android.datamanipulation;


/**
 *  Interface that includes methods for adding a new key and updating the original layout so that
 *  the new key is shown after being added.
 */

public interface AddKeyDialogListener {
    //takes key and whether it should have Intensity assigned to it, adds new key to DataModel
    //upon positive Click
    void addKey(String key, Boolean intensity);
    //updates the data in the Layout from which AddKeyDialog was initiated (so that key is shown there)
    void updateOriginalLayout();
}
