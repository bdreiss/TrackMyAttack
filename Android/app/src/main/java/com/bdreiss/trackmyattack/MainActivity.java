package com.bdreiss.trackmyattack;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.bdreiss.dataAPI.core.DataModel;
import com.bdreiss.dataAPI.core.AilmentData;
import com.bdreiss.dataAPI.core.CauseData;
import com.bdreiss.dataAPI.core.RemedyData;
import com.bdreiss.dataAPI.core.SymptomData;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.util.Coordinate;
import com.bdreiss.trackmyattack.datamanipulation.AddKeyDialogListener;
import com.bdreiss.trackmyattack.datamanipulation.EditItemDialog;
import com.bdreiss.trackmyattack.sync.SyncMethod;
import com.bdreiss.trackmyattack.sync.Synchronizer;

public class MainActivity extends AppCompatActivity {

        public static DataModel data;
        public Settings settings;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            activityMain();
            }

        private void activityMain(){
        setContentView(R.layout.activity_main);

        data = new DataModel(getFilesDir().getAbsolutePath());

        //temporary settings until settings user interface has been implemented
                //TODO implement user interface for settings
        settings = new Settings(this);
        settings.setAutomaticSync(true);
        settings.setSyncMethod(SyncMethod.DROPBOX);

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

        if (!settings.getSynced())
                syncButton.setBackgroundColor(Color.RED);

        //sync data on click
        syncButton.setOnClickListener(v -> Synchronizer.synchronize(this, data, syncButton));

        //Button for adding migraines and managing already existent data
        Button migraineButton = findViewById(R.id.button_migraine);

        //sub type of an abstract data model containing methods only pertaining to ailments (in our case migraines)
        AilmentData ailmentData = new AilmentData(data);

        //listener for adding new Datum initiating dialog where user can add migraine with Intensity
        migraineButton.setOnClickListener(v ->{
                String[] intensities = new String[Intensity.values().length];

                //get all intensities
                for (int i=0;i <Intensity.values().length;i++)
                        intensities[i] = Intensity.values()[i].toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Intensity");

                CurrentLocation.getCurrentLocation(this, location ->{

                        //set items and implement adding data on choosing
                        builder.setItems(intensities, (dialog, which) -> {
                                try {
                                        ailmentData.addData("Migraine", Intensity.values()[which], location == null ? null: new Coordinate(location.getLongitude(), location.getLatitude()));//TODO add coordinates
                                        Synchronizer.autoSynchronize(this,data, syncButton);
                                        Log.d("XXX", data.print());
                                        Log.d("XXX", String.valueOf(location == null));
                                        Log.d("XXX",String.valueOf(data.getCoordinate(LocalDate.now())));
                                } catch (TypeMismatchException e) {
                                        e.printStackTrace();
                                }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.create();
                        builder.show();
                });


        });

        //on long click, show existing data via EditItemDialog but leave out the add key elements (because we don't need to add ailment keys)
        migraineButton.setOnLongClickListener(v -> {
                EditItemDialog editItemDialog = new EditItemDialog(this,"Migraine", ailmentData, new AddKeyDialogListener() {
                        @Override
                        public void addKey(String key, Boolean intensity) {}

                        @Override
                        public void updateOriginalLayout() {}
                }, syncButton);
                editItemDialog.show(getSupportFragmentManager(),"Edit Item Dialog");
                return true;
        });

        /*
                implement button for managing data for causes, symptoms and remedies,
                the LayoutListener is a View showing all keys in the category it is representing
                where each Button for each key is equivalent to the migraine button listeners but includes a
                button and methods for adding new keys
         */
        LayoutListener causeLayoutListener = new LayoutListener(this, new CauseData(data), getSupportFragmentManager(), this::activityMain);
        Button causesViewButton = findViewById(R.id.button_causes_view);
        causesViewButton.setOnClickListener(causeLayoutListener);

        LayoutListener symptomLayoutListener = new LayoutListener(this, new SymptomData(data), getSupportFragmentManager(), this::activityMain);
        Button symptomsViewButton = findViewById(R.id.button_symptoms_view);
        symptomsViewButton.setOnClickListener(symptomLayoutListener);

        LayoutListener remedyLayoutListener = new LayoutListener(this, new RemedyData(data), getSupportFragmentManager(), this::activityMain);
        Button remedyViewButton = findViewById(R.id.button_remedies_view);
        remedyViewButton.setOnClickListener(remedyLayoutListener);
        }

}
