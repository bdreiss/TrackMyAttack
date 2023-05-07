package com.bdreiss.trackmyattack.listeners;

import android.content.Context;
import android.view.View;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;

public class SymptomOnClickListener extends CustomListener{

    public SymptomOnClickListener(Context context, DataModel data){
        super(context, data);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);

        try {
            if (data.getSymptomData(text) instanceof IteratorWithIntensity){
                chooseIntensity(context, (dialogInterface, i) ->{

                 data.addSymptom(text, Intensity.values()[i+1]);
                 data.save();

                });
            }

        } catch (EntryNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public CustomListener copy() {
        SymptomOnClickListener newListener = new SymptomOnClickListener(context, data);
        newListener.setTextValue(text);
        return newListener;
    }
}
