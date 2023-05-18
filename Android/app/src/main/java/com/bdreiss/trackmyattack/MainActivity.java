package com.bdreiss.trackmyattack;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bdreiss.trackmyattack.listeners.CauseOnClickListener;
import com.bdreiss.trackmyattack.listeners.CustomListener;
import com.bdreiss.trackmyattack.listeners.RemedyOnClickListener;
import com.bdreiss.trackmyattack.listeners.SymptomOnClickListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

public class MainActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            activityMain();
            }

        private void activityMain(){
        setContentView(R.layout.activity_main);
        //DataModel.deleteSaveFile(this)

        DataModel data = new DataModel(getFilesDir().getAbsolutePath());

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir().getAbsolutePath() + "/Text.txt"));
                bw.write(data.print());
                bw.close();
        } catch (IOException e) {
                e.printStackTrace();
        }

        TextView textView = findViewById(R.id.textView);

        textView.setText(data.print());

        EditText migraineEditTextText = findViewById(R.id.edit_text_migraine_text);
        EditText migraineEditTextIntensity = findViewById(R.id.edit_text_migraine_intensity);
        Button migraineButton = findViewById(R.id.button_migraine);

        migraineButton.setOnClickListener(view -> {

                String intensityText = migraineEditTextIntensity.getText().toString();

                Intensity intensity = Intensity.NO_INTENSITY;
                if (!intensityText.isEmpty()) {
                        switch(Integer.parseInt(intensityText)){
                                case 0: intensity = Intensity.LOW;
                                break;
                                case 1: intensity = Intensity.MEDIUM;
                                break;
                                case 2: intensity = Intensity.HIGH;
                        }
                }

                try {
                        data.addAilment("Migraine", intensity);
                } catch (TypeMismatchException e) {
                        e.printStackTrace();
                }

                textView.setText(data.print());
                migraineEditTextText.setText("");
                migraineEditTextIntensity.setText("");

        });

        LayoutListener causeLayoutListener = new LayoutListener(this, Category.CAUSE, R.layout.causes, R.id.linear_layout_causes, new CauseOnClickListener(this, data), getSupportFragmentManager(), new LayoutListenerInterface() {
                @Override
                public Iterator<String> getData() {
                        return data.getCauses();
                }

                @Override
                public void returnToActivity() {
                        activityMain();
                }
        });

        Button causesViewButton = findViewById(R.id.button_causes_view);
        causesViewButton.setOnClickListener(causeLayoutListener);

        LayoutListener symptomLayoutListener = new LayoutListener(this,Category.SYMPTOM,R.layout.symptoms,R.id.linear_layout_symptoms,new SymptomOnClickListener(this,data),getSupportFragmentManager(),new LayoutListenerInterface(){
                @Override
                public Iterator<String> getData() {
                        return data.getSymptoms();
                }

                @Override
                public void returnToActivity() {
                        activityMain();
                }

        });
        Button symptomsViewButton = findViewById(R.id.button_symptoms_view);
                symptomsViewButton.setOnClickListener(symptomLayoutListener);
        LayoutListener remedyLayoutListener = new LayoutListener(this, Category.REMEDY, R.layout.remedies, R.id.linear_layout_remedies, new RemedyOnClickListener(this, data), getSupportFragmentManager(), new LayoutListenerInterface() {
                @Override
                public Iterator<String> getData() {
                        return data.getRemedies();
                }

                @Override
                public void returnToActivity() {
                        activityMain();
                }
        });

        Button remedyViewButton = findViewById(R.id.button_remedies_view);
        remedyViewButton.setOnClickListener(remedyLayoutListener);
        }

}
