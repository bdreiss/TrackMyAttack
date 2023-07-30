package com.bdreiss.trackmyattack.sync;

/*
 * Class that provides methods for (auto)synchronizing data
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.trackmyattack.R;
import com.bdreiss.trackmyattack.Settings;

public class Synchronizer {

    //method that synchronizes data
    public static void synchronize(Context context, DataModel data, Button syncButton) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Settings settings = new Settings(context);

        //check whether device is online, if it is not, set synced in settings to false
        // and mark the sync button, synchronize otherwise
        if (netInfo == null || !netInfo.isConnected()) {

            settings.setSynced(false);
            setSyncButton(context, syncButton, true);

        } else {
            Sync sync = null;

            if (settings.getSyncMethod() == SyncMethod.DROPBOX) {
                sync = (new DropboxSync(context, data, () -> {
                    settings.setSynced(true);
                    setSyncButton(context, syncButton, false);
                }));
            }

            //sync should actually never be null
            if (sync != null)
                sync.start();
            else
                System.exit(0);


        }
    }


    //method that checks whether auto sync is on in settings and synchs data if so
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


    // method that sets syncButton to not synced color if data was not successfully synced
    // and to standard color otherwise, given the button is not null
    private static void setSyncButton(Context context, Button syncButton, boolean active) {
        if (syncButton != null) {
            if (!active)
                syncButton.setBackgroundColor(ContextCompat.getColor(context, R.color.primary));
            else
                syncButton.setBackgroundColor(ContextCompat.getColor(context, R.color.button_not_synced));

        }
    }
}
