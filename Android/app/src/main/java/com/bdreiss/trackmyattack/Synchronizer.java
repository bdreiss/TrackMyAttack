package com.bdreiss.trackmyattack;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;
import android.widget.Toast;

import com.bdreiss.dataAPI.DataModel;

public class Synchronizer {

        public static void synchronize(Context context, DataModel data) {
            synchronize(context,data, null);
        }

        public static void synchronize(Context context, DataModel data, Button syncButton) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Settings settings = new Settings(context);

        if (netInfo == null || !netInfo.isConnected()) {

            settings.setSynched(false);
        } else {
            Thread t = (new DropboxSync(context, data, () -> {
                settings.setSynched(true);
                ((Activity) context).runOnUiThread(()-> Toast.makeText(context,"Dropbox synchronized",Toast.LENGTH_LONG).show());
                if (syncButton != null){
                    syncButton.setBackgroundColor(context.getResources().getColor(R.color.primary));
                }

            }));

            t.start();



        }
    }
}
