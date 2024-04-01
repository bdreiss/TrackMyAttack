package com.bdreiss.trackmyattack.sync;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bdreiss.data.core.DataModel;
import com.bdreiss.data.exceptions.NetworkException;
import com.bdreiss.data.network.Dropbox;
import com.bdreiss.trackmyattack.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/*
 *  Class that represents synchronization via Dropbox
 *
 * //TODO handle API-key properly
 */

public class DropboxSynchronizer extends Synchronizer {


    public DropboxSynchronizer(Context context, DataModel data, SyncCompleted syncCompleted){
        super(context, data, syncCompleted);
    }


    @Override
    public void run() {

        //thread to check for and preform authorization
        @SuppressLint("SetTextI18n") Thread authorizationThread = new Thread(() -> {
            try {

                if (getKey() == null || getKey().isEmpty())
                    retrieveKey();

                //if there are no Dropbox credentials, initialize authentication process
                if (!(new File(Dropbox.getDbxFilePath(data)).exists())) {
                    URL url = new URL(Dropbox.getAuthorizationURL(getKey()));
                    ((Activity) context).runOnUiThread(() -> {

                        //Initialize and show authentication dialog
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        View alertView = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_alert, null);
                        alertDialog.setView(alertView);
                        alertDialog.show();

                        alertDialog.setTitle("Dropbox Authentication");
                        EditText input = alertView.findViewById(R.id.custom_alert_edit_text);

                        TextView text = alertView.findViewById(R.id.custom_alert_text_view);
                        text.setMovementMethod(LinkMovementMethod.getInstance());

                        //Get message from DataAPI and display
                        text.setText(Dropbox.message + "\n\n" + url);
                        text.setClickable(true);
                        text.setTextSize(20);
                        text.setTextIsSelectable(true);

                        alertView.findViewById(R.id.custom_alert_button_confirm).setOnClickListener(v1 -> {
                            String inputString = input.getText().toString();
                            alertDialog.dismiss();

                            Thread finalizeThread = new Thread(()->{
                                try {
                                    Dropbox.authorize(getKey(), inputString, data);
                                } catch (NetworkException e) {
                                    e.printStackTrace();
                                }

                            });
                            finalizeThread.start();

                            try {
                                finalizeThread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        });

                        alertView.findViewById(R.id.custom_alert_button_cancel).setOnClickListener(v2 -> alertDialog.dismiss());

                    });
                }

            } catch (MalformedURLException | NetworkException e) {
                e.printStackTrace();
            }


        });

        //start thread to check for and preform authorization and wait for it to finish
        authorizationThread.start();
        try {
            authorizationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //create and start upload thread and wait for it to finish
        Thread uploadThread = new Thread(() -> {

            if ((new File(Dropbox.getDbxFilePath(data)).exists())) {
                try {
                    Dropbox.upload(data);
                } catch (NetworkException e) {
                    e.printStackTrace();
                }
            }
        });

        uploadThread.start();
        try {
            uploadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //perform on sync complete action
        syncCompleted.onComplete();
    }


    private String getKey() {
        String key = "";
        try {

            File keyFile = new File(context.getFilesDir() + "/TrackMyAttackKey");
            FileReader is = new FileReader(keyFile);
            BufferedReader bis = new BufferedReader(is);

            key = bis.readLine();

            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return key;
    }

    private void retrieveKey(){

    }

}
