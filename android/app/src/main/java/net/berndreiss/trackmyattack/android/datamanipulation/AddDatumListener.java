package net.berndreiss.trackmyattack.android.datamanipulation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;

import net.berndreiss.trackmyattack.data.core.AbstractData;
import net.berndreiss.trackmyattack.data.enums.Intensity;
import net.berndreiss.trackmyattack.data.exceptions.EntryNotFoundException;
import net.berndreiss.trackmyattack.data.exceptions.TypeMismatchException;
import net.berndreiss.trackmyattack.data.util.Coordinate;
import net.berndreiss.trackmyattack.data.util.IteratorWithIntensity;
import net.berndreiss.trackmyattack.android.CurrentLocation;
import net.berndreiss.trackmyattack.android.sync.Synchronizer;


/*
 * Adds a Datum to the DataModel for the key given.
 */

public class AddDatumListener implements View.OnClickListener {

    private final Context context;
    private String key;
    AbstractData data;

    final private ActivityResultLauncher<Intent> locationSettingsResultLauncher;

    public AddDatumListener(Context context, AbstractData data,  ActivityResultLauncher<Intent> locationSettingsResultLauncher) {
        this.context = context;
        this.data = data;
        this.locationSettingsResultLauncher = locationSettingsResultLauncher;
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

        try {
            data.getData(key);
        } catch (EntryNotFoundException e) {
            throw new RuntimeException("Entry could not be found!");
        }

        CurrentLocation.callback = location -> {

            Coordinate coordinate = new Coordinate(location == null ? null : location.getLongitude(), location == null ? null : location.getLatitude());

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

        };

        CurrentLocation.getLocation(context, locationSettingsResultLauncher);

    }
}
