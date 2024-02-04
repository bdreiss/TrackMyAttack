package com.bdreiss.trackmyattack.datamanipulation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.bdreiss.dataAPI.core.AbstractData;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.dataAPI.util.IteratorWithIntensity;
import com.bdreiss.trackmyattack.CurrentLocation;
import com.bdreiss.trackmyattack.sync.Synchronizer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;


/*
 * Adds a Datum to the DataModel for the key given.
 */

public class AddDatumListener implements View.OnClickListener {

    private final Context context;
    private String key;
    AbstractData data;

    public AddDatumListener(Context context, AbstractData data) {
        this.context = context;
        this.data = data;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //Creates and shows a dialog that let's the user choose an Intensity
    public static void chooseIntensity(Context context, DialogInterface.OnClickListener listener) {

        String[] intensities = new String[Intensity.values().length - 1];

        //get all intensities without NO_INTENSITY
        for (int i = 1; i < Intensity.values().length; i++)
            intensities[i - 1] = Intensity.values()[i].toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Intensity");
        builder.setItems(intensities, listener);
        builder.setNegativeButton("Cancel", null);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(View view) {

        CurrentLocation.getCurrentLocation(context, location -> {

            Coordinate coordinate = new Coordinate(location.getLongitude(), location.getLatitude());
            try {
                //check whether key has Intensity and show Intensity dialog if so, add Datum without Intensity otherwise
                if (data.getData(key) instanceof IteratorWithIntensity){
                    chooseIntensity(context, (dialogInterface, i) -> {
                        try {
                            data.addData(key, Intensity.values()[i+1], coordinate);

                        } catch (TypeMismatchException e) {
                            e.printStackTrace();
                        }

                    });
                }
                else{
                    data.addData(key, coordinate);
                }

                Synchronizer.autoSynchronize(context, data.getData());

            } catch (EntryNotFoundException | TypeMismatchException e) {
                e.printStackTrace();
            }
        });


    }
}
