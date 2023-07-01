package com.bdreiss.trackmyattack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.dataAPI.exceptions.NetworkException;
import com.bdreiss.dataAPI.network.Dropbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DropboxSync extends Thread{

    private Context context;
    private DataModel data;

    public DropboxSync(Context context, DataModel data){
        this.context = context;
        this.data = data;
    }


    @Override
    public void run() {

        @SuppressLint("SetTextI18n") Thread authorizationThread = new Thread(() -> {
            try {
                if (!(new File(Dropbox.getDbxFilePath(data)).exists())) {
                    URL url = new URL(Dropbox.getAuthorizationURL(getKey()));
                    ((Activity) context).runOnUiThread(() -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        View alertView = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_alert, null);
                        alertDialog.setView(alertView);
                        alertDialog.show();

                        alertDialog.setTitle("Dropbox Authentication");
                        EditText input = alertView.findViewById(R.id.custom_alert_edit_text);

                        TextView text = alertView.findViewById(R.id.custom_alert_text_view);
                        text.setMovementMethod(LinkMovementMethod.getInstance());
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        });
        authorizationThread.start();
        try {
            authorizationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread uploadThread = new Thread(() -> {

            if ((new File(Dropbox.getDbxFilePath(data)).exists())) {
                try {

                    Dropbox.upload(data);
                    ((Activity) context).runOnUiThread(()-> Toast.makeText(context,"Dropbox synchronized",Toast.LENGTH_LONG).show());

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

    }
    private String getKey() {
        String key = "";
        try {

            FileReader is = new FileReader(((Activity) context).getFilesDir() + "/TrackMyAttackKey");
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
