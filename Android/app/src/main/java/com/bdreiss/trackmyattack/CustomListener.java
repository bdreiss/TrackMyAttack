package com.bdreiss.trackmyattack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;


public class CustomListener implements View.OnClickListener {

    private  Context context;
    private String key;
    CustomListenerInterface customListenerInterface;

    public CustomListener(Context context, CustomListenerInterface customListenerInterface){
        this.context = context;
        this.customListenerInterface = customListenerInterface;
    }



    public void setTextValue(String text){
        this.key = text;
    }

    public CustomListener copy(){
        CustomListener newListener = new CustomListener(context, customListenerInterface);
        newListener.setTextValue(key);
        return newListener;
    }

    public static void chooseIntensity(Context context, DialogInterface.OnClickListener listener){
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

        try {
            if (customListenerInterface.getData(key) instanceof IteratorWithIntensity){
                chooseIntensity(context, (dialogInterface, i) -> {
                    try {
                        customListenerInterface.add(key, Intensity.values()[i+1]);
                    } catch (TypeMismatchException e) {
                        e.printStackTrace();
                    }

                });
            }
            else{
                customListenerInterface.add(key);

            }
        } catch (EntryNotFoundException | TypeMismatchException e) {
            e.printStackTrace();
        }

    }
}
