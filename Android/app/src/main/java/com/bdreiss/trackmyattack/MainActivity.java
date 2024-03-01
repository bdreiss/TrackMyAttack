package com.bdreiss.trackmyattack;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.core.AilmentData;
import com.bdreiss.dataAPI.core.CauseData;
import com.bdreiss.dataAPI.core.RemedyData;
import com.bdreiss.dataAPI.core.SymptomData;
import com.bdreiss.trackmyattack.datamanipulation.AddDatumListener;
import com.bdreiss.trackmyattack.datamanipulation.AddKeyDialogListener;
import com.bdreiss.trackmyattack.datamanipulation.EditItemDialog;
import com.bdreiss.trackmyattack.sync.SyncMethod;
import com.bdreiss.trackmyattack.sync.Synchronizer;

public class MainActivity extends AppCompatActivity {

    //data model containing data (see DataAPI)
    public static DataModel data;

    //settings for synchronization
    public Settings settings;

    //used for returning from settings when asked to turn on GPS when adding data
    private ActivityResultLauncher<Intent> locationSettingsResultLauncher;

    public final static int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // returning from settings
        locationSettingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> CurrentLocation.finishGettingLocation(this));

        super.onCreate(savedInstanceState);

        activityMain();
    }

    private void activityMain() {
        setContentView(R.layout.activity_main);

        data = new DataModel(getFilesDir().getAbsolutePath());

        //add "Migraine" in case it hasn't been added before
        //avoids EntryNotFoundException in AddDatumListener
        data.addAilmentKey("Migraine");

        //temporary settings until settings user interface has been implemented
        //TODO implement user interface for settings
        settings = new Settings(this);
        settings.setAutomaticSync(true);
        settings.setSyncMethod(SyncMethod.DROPBOX);

        // if no permission has been granted pass null to the callback and return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && !settings.getDeniedLocationAccess()) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        //write data to text file, so when DataModel in DataAPI is changed, data can be transferred (temporary measure)
        //TODO implement better method for migrating data
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir().getAbsolutePath() + "/Text.txt"));
            bw.write(data.print());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sync Button that turns a different color, if data has not been synced (i.e. because there was not internet connection)
        Button syncButton = findViewById(R.id.button_sync);

        //if data has not been synced turn sync button red
        if (!settings.getSynced())
            syncButton.setBackgroundColor(Color.RED);

        //sync data on click
        syncButton.setOnClickListener(v -> Synchronizer.synchronize(this, data, syncButton));

        //Button for adding migraines and managing already existent data
        Button migraineButton = findViewById(R.id.button_migraine);

        //sub type of an abstract data model containing methods only pertaining to ailments (in our case migraines)
        AilmentData ailmentData = new AilmentData(data);

        //listener for migraine button
        AddDatumListener addDatumListener = new AddDatumListener(this, ailmentData, locationSettingsResultLauncher);
        addDatumListener.setKey("Migraine");

        //listener for adding new Datum initiating dialog where user can add migraine with Intensity
        migraineButton.setOnClickListener(addDatumListener);

        //on long click, show existing data via EditItemDialog but leave out the add key elements (because we don't need to add ailment keys)
        migraineButton.setOnLongClickListener(v -> {
            EditItemDialog editItemDialog = new EditItemDialog(this, "Migraine", ailmentData, new AddKeyDialogListener() {
                //nothing to do here
                @Override
                public void addKey(String key, Boolean intensity) {
                }

                //nothing to do here
                @Override
                public void updateOriginalLayout() {
                }
            }, syncButton);
            editItemDialog.show(getSupportFragmentManager(), "Edit Item Dialog");
            return true;
        });

        /*
                implement button for managing data for causes, symptoms and remedies,
                the LayoutListener is a View showing all keys in the category it is representing
                where each Button for each key is equivalent to the migraine button listeners but includes a
                button and methods for adding new keys
         */
        LayoutListener causeLayoutListener = new LayoutListener(this, new CauseData(data), getSupportFragmentManager(), this::activityMain, locationSettingsResultLauncher);
        Button causesViewButton = findViewById(R.id.button_causes_view);
        causesViewButton.setOnClickListener(causeLayoutListener);

        LayoutListener symptomLayoutListener = new LayoutListener(this, new SymptomData(data), getSupportFragmentManager(), this::activityMain, locationSettingsResultLauncher);
        Button symptomsViewButton = findViewById(R.id.button_symptoms_view);
        symptomsViewButton.setOnClickListener(symptomLayoutListener);

        LayoutListener remedyLayoutListener = new LayoutListener(this, new RemedyData(data), getSupportFragmentManager(), this::activityMain, locationSettingsResultLauncher);
        Button remedyViewButton = findViewById(R.id.button_remedies_view);
        remedyViewButton.setOnClickListener(remedyLayoutListener);

        TextView textView = findViewById(R.id.text_view);
        textView.setText(String.valueOf(data.getCoordinate(LocalDate.now())));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    new Settings(this).setDeniedLocationAccess(true);
                }
            }
        }
    }
}
