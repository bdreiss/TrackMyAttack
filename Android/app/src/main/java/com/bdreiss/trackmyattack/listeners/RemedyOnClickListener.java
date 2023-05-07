package com.bdreiss.trackmyattack.listeners;

import android.content.Context;
import android.util.Log;
import android.view.View;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;

public class RemedyOnClickListener extends CustomListener{

    public RemedyOnClickListener(Context context, DataModel data){
        super(context, data);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);

        try {
            if (data.getRemedyData(text) instanceof IteratorWithIntensity){
                chooseIntensity(context, (dialogInterface, i) -> {
                    data.addRemedy(text, Intensity.values()[i+1]);
                    data.save();
                });
            }
            else{
                data.addRemedy(text);
                data.save();

            }
        } catch (EntryNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public CustomListener copy() {
        RemedyOnClickListener newListener = new RemedyOnClickListener(context, data);
        newListener.setTextValue(text);
        return newListener;
    }
}
