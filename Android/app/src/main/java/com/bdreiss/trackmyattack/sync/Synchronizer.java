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

    public static void autoSynchronize(Context context, DataModel data, Button syncButton){
        Settings settings = new Settings(context);
        if (settings.getAutomaticSync()) {
            synchronize(context, data, syncButton);
        }
        else {
            settings.setSynced(false);
            setSyncButton(context, syncButton, true);
        }

    }

        public static void autoSynchronize(Context context, DataModel data){
            autoSynchronize(context, data, null);
        }

        public static void synchronize(Context context, DataModel data, Button syncButton) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Settings settings = new Settings(context);

        if (netInfo == null || !netInfo.isConnected()) {

            settings.setSynced(false);
            setSyncButton(context, syncButton, true);

        } else {
            Thread t = (new DropboxSync(context, data, () -> {
                settings.setSynced(true);
                setSyncButton(context, syncButton, false);
            }));

            t.start();



        }
    }

    private static void setSyncButton(Context context, Button syncButton, boolean active){
            if (syncButton != null)
                if (!active)
                    syncButton.setBackgroundColor(ContextCompat.getColor(context, R.color.primary));
                else
                    syncButton.setBackgroundColor(ContextCompat.getColor(context, R.color.button_not_synced));
    }
}
