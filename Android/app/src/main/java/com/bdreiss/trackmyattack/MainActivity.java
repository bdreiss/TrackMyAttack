package com.bdreiss.trackmyattack;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.bdreiss.dataAPI.AilmentDataModel;
import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.trackmyattack.datamanipulation.AddKeyDialogListener;
import com.bdreiss.trackmyattack.datamanipulation.EditItemDialog;
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
        //DataModel.deleteSaveFile(this)

        data = new DataModel(getFilesDir().getAbsolutePath());
        settings = new Settings(this);
        settings.setAutomaticSync(true);

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir().getAbsolutePath() + "/Text.txt"));
                bw.write(data.print());
                bw.close();
        } catch (IOException e) {
                e.printStackTrace();
        }

        Button syncButton = findViewById(R.id.button_sync);

        if (!settings.getSynced())
                syncButton.setBackgroundColor(Color.RED);

        syncButton.setOnClickListener(v -> Synchronizer.synchronize(this, data, syncButton));

        Button migraineButton = findViewById(R.id.button_migraine);

        AilmentDataModel ailmentDataModel = new AilmentDataModel(data);
        //listener for adding new Datum

        migraineButton.setOnClickListener(v ->{
                String[] intensities = new String[Intensity.values().length];

                //get all intensities
                for (int i=0;i <Intensity.values().length;i++)
                        intensities[i] = Intensity.values()[i].toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Intensity");
                builder.setItems(intensities, (dialog, which) -> {
                        try {
                                ailmentDataModel.addData("Migraine", Intensity.values()[which]);
                                Synchronizer.autoSynchronize(this,data, syncButton);
                        } catch (TypeMismatchException e) {
                                e.printStackTrace();
                        }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create();
                builder.show();
        });

        migraineButton.setOnLongClickListener(v -> {
                EditItemDialog editItemDialog = new EditItemDialog(this,"Migraine", ailmentDataModel, new AddKeyDialogListener() {
                        @Override
                        public void addKey(String key, Boolean intensity) {

                        }

                        @Override
                        public void updateOriginalLayout() {

                        }
                }, syncButton);
                editItemDialog.show(getSupportFragmentManager(),"Edit Item Dialog");
                return true;
        });


        LayoutListener causeLayoutListener = new LayoutListener(this, new CauseDataModel(data), getSupportFragmentManager(), this::activityMain);
        Button causesViewButton = findViewById(R.id.button_causes_view);
        causesViewButton.setOnClickListener(causeLayoutListener);

        LayoutListener symptomLayoutListener = new LayoutListener(this, new SymptomDataModel(data), getSupportFragmentManager(), this::activityMain);
        Button symptomsViewButton = findViewById(R.id.button_symptoms_view);
        symptomsViewButton.setOnClickListener(symptomLayoutListener);

        LayoutListener remedyLayoutListener = new LayoutListener(this, new RemedyDataModel(data), getSupportFragmentManager(), this::activityMain);
        Button remedyViewButton = findViewById(R.id.button_remedies_view);
        remedyViewButton.setOnClickListener(remedyLayoutListener);
        }

}
