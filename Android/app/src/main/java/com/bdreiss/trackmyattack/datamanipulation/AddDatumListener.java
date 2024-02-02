package com.bdreiss.trackmyattack.datamanipulation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.bdreiss.dataAPI.core.AbstractData;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.IteratorWithIntensity;
import com.bdreiss.trackmyattack.sync.Synchronizer;


/*
 * Adds a Datum to the DataModel for the key given.
 */

public class AddDatumListener implements View.OnClickListener {

    private final Context context;
    private String key;
    AbstractData data;

    public AddDatumListener(Context context, AbstractData data){
        this.context = context;
        this.data = data;
    }

    public void setKey(String key){
        this.key = key;
    }

    //Creates and shows a dialog that let's the user choose an Intensity
    public static void chooseIntensity(Context context, DialogInterface.OnClickListener listener){

        String[] intensities = new String[Intensity.values().length-1];

        //get all intensities without NO_INTENSITY
        for (int i=1;i <Intensity.values().length;i++)
            intensities[i-1] = Intensity.values()[i].toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Intensity");
        builder.setItems(intensities, listener);
        builder.setNegativeButton("Cancel", null);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(View view) {

        try {
            //check whether key has Intensity and show Intensity dialog if so, add Datum without Intensity otherwise
            if (data.getData(key) instanceof IteratorWithIntensity){
                chooseIntensity(context, (dialogInterface, i) -> {
                    try {
                        data.addData(key, Intensity.values()[i+1], null);//TODO implement add coordinates

                    } catch (TypeMismatchException e) {
                        e.printStackTrace();
                    }

                });
            }
            else{
                data.addData(key, null);//TODO implement add coordinates
            }

            Synchronizer.autoSynchronize(context, data.getData());

        } catch (EntryNotFoundException | TypeMismatchException e) {
            e.printStackTrace();
        }

    }
}
