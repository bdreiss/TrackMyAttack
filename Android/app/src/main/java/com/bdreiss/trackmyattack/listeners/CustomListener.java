package com.bdreiss.trackmyattack.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import main.java.com.bdreiss.dataAPI.DataModel;
import main.java.com.bdreiss.dataAPI.Intensity;

public class CustomListener implements View.OnClickListener {

    protected  Context context;
    protected DataModel data;
    protected String text;

    CustomListener(Context context, DataModel data){
        this.context = context;
        this.data = data;
    }



    public void setTextValue(String text){
        this.text = text;
    }

    public CustomListener copy(){
        CustomListener newListener = new CustomListener(context, data);
        newListener.setTextValue(text);
        return newListener;
    }

    public DataModel getData(){
        return data;
    }

    public void chooseIntensity(Context context, DialogInterface.OnClickListener listener){
        String[] intensities = new String[Intensity.values().length-1];

        for (int i=1;i <Intensity.values().length;i++)
            intensities[i-1] = Intensity.values()[i].toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Intensity");
        builder.setItems(intensities, listener);
        builder.setNegativeButton("Cancel", null);
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(View view) {

    }
}
