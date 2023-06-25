package com.bdreiss.trackmyattack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import main.java.com.bdreiss.dataAPI.AbstractDataModel;
import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.IteratorWithIntensity;


/*
 * Adds a Datum to the DataModel for the key given.
 */

public class AddDatumListener implements View.OnClickListener {

    private final Context context;
    private String key;
    AbstractDataModel dataModel;

    public AddDatumListener(Context context, AbstractDataModel dataModel){
        this.context = context;
        this.dataModel = dataModel;
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
            if (dataModel.getData(key) instanceof IteratorWithIntensity){
                chooseIntensity(context, (dialogInterface, i) -> {
                    try {
                        dataModel.addData(key, Intensity.values()[i+1]);
                    } catch (TypeMismatchException e) {
                        e.printStackTrace();
                    }

                });
            }
            else{
                dataModel.addData(key);

            }
        } catch (EntryNotFoundException | TypeMismatchException e) {
            e.printStackTrace();
        }

    }
}