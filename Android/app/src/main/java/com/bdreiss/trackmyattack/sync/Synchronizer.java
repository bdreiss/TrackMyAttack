package com.bdreiss.trackmyattack.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.trackmyattack.R;
import com.bdreiss.trackmyattack.Settings;

public class Synchronizer {

        public static void synchronize(Context context, DataModel data) {
            synchronize(context,data, null);
        }

        public static void autoSynchronize(Context context, DataModel data){
            Settings settings = new Settings(context);
            if (settings.getAutomaticSync())
                synchronize(context, data);
            else
                settings.setSynced(false);

        }

        public static void synchronize(Context context, DataModel data, Button syncButton) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Settings settings = new Settings(context);

        if (netInfo == null || !netInfo.isConnected()) {

            settings.setSynced(false);
        } else {
            Thread t = (new DropboxSync(context, data, () -> {
                settings.setSynced(true);
                if (syncButton != null){
                    syncButton.setBackgroundColor(ContextCompat.getColor(context, R.color.primary));
                }

            }));

            t.start();



        }
    }
}
