package com.bdreiss.trackmyattack.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.View;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;

public class CauseOnClickListener extends CustomListener {



    public CauseOnClickListener(Context context, DataModel data){
        super(context, data);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);

        try {
            if (data.getCauseData(text) instanceof IteratorWithIntensity){
                chooseIntensity(context, (dialogInterface, i) -> data.addCause(text, Intensity.values()[i]));
            }
            else{
                data.addCause(text);
            }
        } catch (EntryNotFoundException e) {
            e.printStackTrace();
        }

        data.save();

    }

    @Override
    public CustomListener copy() {
        CauseOnClickListener newListener = new CauseOnClickListener(context, data);
        newListener.setTextValue(text);
        return newListener;
    }

}
