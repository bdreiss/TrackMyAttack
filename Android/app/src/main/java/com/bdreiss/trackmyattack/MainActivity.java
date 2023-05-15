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

        Button causesViewButton = findViewById(R.id.button_causes_view);
        causesViewButton.setOnClickListener(view -> setLayout(Category.CAUSE, R.layout.causes,R.id.linear_layout_causes, data.getCauses(), new CauseOnClickListener(view.getContext(),data))); {
        }

        Button symptomsViewButton = findViewById(R.id.button_symptoms_view);
                symptomsViewButton.setOnClickListener(view -> setLayout(Category.SYMPTOM, R.layout.symptoms,R.id.linear_layout_symptoms,data.getSymptoms(), new SymptomOnClickListener(view.getContext(),data)));


        Button remedyViewButton = findViewById(R.id.button_remedies_view);
        remedyViewButton.setOnClickListener(view -> setLayout(Category.REMEDY, R.layout.remedies, R.id.linear_layout_remedies, data.getRemedies(), new RemedyOnClickListener(view.getContext(),data)));

        }

        private void setLayout(Category category, int layoutID, int linearLayoutID, Iterator<String> iterator, CustomListener listener){
                setContentView(layoutID);

                LinearLayout linearLayout = findViewById(linearLayoutID);
                while (iterator.hasNext()){
                        String item = iterator.next();

                        Button itemButton = new Button(this);

                        CustomListener listenerCopy = listener.copy();
                        itemButton.setText(item);

                        itemButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        linearLayout.addView(itemButton);

                        listenerCopy.setTextValue(item);
                         itemButton.setOnClickListener(listenerCopy);

                }
                Button addButton = new Button(this);

                addButton.setText("+");
                addButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                addButton.setTag("add_key_button");

                linearLayout.addView(addButton);

                addButton.setOnClickListener(view -> {
                        AddItemDialog addItemDialog = new AddItemDialog(listener.getData(), category);
                        addItemDialog.setAddItemDialogListener((data, category1, item, intensity) -> {
                                switch(category1){
                                        case AILMENT:
                                                data.addAilmentKey(item);
                                                break;
                                        case CAUSE:
                                                data.addCauseKey(item, intensity);
                                                setLayout(Category.CAUSE, R.layout.causes,R.id.linear_layout_causes, data.getCauses(), new CauseOnClickListener(view.getContext(),data));
                                                break;
                                        case SYMPTOM:
                                                data.addSymptomKey(item);
                                                setLayout(Category.SYMPTOM, R.layout.symptoms,R.id.linear_layout_symptoms, data.getSymptoms(), new SymptomOnClickListener(view.getContext(),data));
                                                break;
                                        case REMEDY:
                                                data.addRemedyKey(item, intensity);
                                                setLayout(Category.REMEDY, R.layout.remedies,R.id.linear_layout_remedies, data.getRemedies(), new RemedyOnClickListener(view.getContext(),data));
                                }
                        });
                        addItemDialog.show(getSupportFragmentManager(), "AddItemDialog");

                });

                Button backButton = new Button(this);

                backButton.setText(getString(R.string.BACK_BUTTON));
                backButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(backButton);

                backButton.setOnClickListener(view -> activityMain());

        }

}

enum Category {
        AILMENT, CAUSE, SYMPTOM, REMEDY
}
