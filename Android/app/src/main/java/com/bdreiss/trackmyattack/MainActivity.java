package com.bdreiss.trackmyattack;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.text.InputType;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import com.bdreiss.dataAPI.AilmentDataModel;
import com.bdreiss.dataAPI.CauseDataModel;
import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.RemedyDataModel;
import com.bdreiss.dataAPI.SymptomDataModel;
import com.bdreiss.dataAPI.enums.Intensity;
import com.bdreiss.dataAPI.exceptions.NetworkException;
import com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import com.bdreiss.dataAPI.network.Dropbox;

public class MainActivity extends AppCompatActivity {

        public static DataModel data;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            activityMain();
            }

        private void activityMain(){
        setContentView(R.layout.activity_main);
        //DataModel.deleteSaveFile(this)

        data = new DataModel(getFilesDir().getAbsolutePath());

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir().getAbsolutePath() + "/Text.txt"));
                bw.write(data.print());
                bw.close();
        } catch (IOException e) {
                e.printStackTrace();
        }

        Button syncButton = findViewById(R.id.button_sync);

        syncButton.setOnClickListener(v -> {

                Thread t = new Thread(() -> {
                        try {
                                Dropbox.upload(data, getKey(), (url, s) -> {
                                        ArrayList<String> inputString = new ArrayList<>();

                                        final boolean[] dismissed = {false};

                                        runOnUiThread(() -> {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                                View alertView = getLayoutInflater().inflate(R.layout.custom_alert, null);
                                                builder.setView(alertView);
                                                builder.create().show();
                                                builder.setTitle("Dropbox Authentication");
                                                EditText input = (EditText) alertView.findViewById(R.id.custom_alert_edit_text);
                                                TextView text = (TextView) alertView.findViewById(R.id.custom_alert_text_view);
                                                text.setMovementMethod(LinkMovementMethod.getInstance());

                                                text.setText(s + "\n\n" + url);
                                                //text.setText(Html.fromHtml(text.getText().toString()));
                                                text.setClickable(true);
                                                text.setTextSize(20);

                                                text.setTextIsSelectable(true);

                                                alertView.findViewById(R.id.custom_alert_button_confirm).setOnClickListener(v1 ->{
                                                        inputString.add(input.getText().toString());
                                                        dismissed[0] = true;});

                                                alertView.findViewById(R.id.custom_alert_button_cancel).setOnClickListener(v2 ->{
                                                        dismissed[0] = true;});

                                                builder.setOnDismissListener(dialog -> {
                                                        dismissed[0] = true;
                                                });
                                        });

                                        while (!dismissed[0]) {

                                                try {
                                                        Log.d("XXX", "XXX");

                                                        Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                        return inputString.size() > 0 ? inputString.get(0) : null;
                                });
                        } catch (NetworkException e) {
                                e.printStackTrace();
                                Log.d("Dropbox", e.toString());
                        }


                });
                t.start();
                try {
                        t.join();
                        Log.d("Dropbox", "Upload complete");
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

        });

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
                        } catch (TypeMismatchException e) {
                                e.printStackTrace();
                        }
                });
                builder.setNegativeButton("Cancel", null);
                builder.create();
                builder.show();
        });

        migraineButton.setOnLongClickListener(v -> {
                EditItemDialog editItemDialog = new EditItemDialog("Migraine", ailmentDataModel, new AddKeyDialogListener() {
                        @Override
                        public void addKey(String key, Boolean intensity) {

                        }

                        @Override
                        public void updateOriginalLayout() {

                        }
                });
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

        private String getKey() {
                String key = "";
                try {

                        FileReader is = new FileReader(getFilesDir() + "/TrackMyAttackKey");
                        BufferedReader bis = new BufferedReader(is);

                        key = bis.readLine();

                        bis.close();
                        is.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return key;
        }
}
