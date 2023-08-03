package com.bdreiss.trackmyattack.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.bdreiss.dataAPI.DataModel;
import com.bdreiss.trackmyattack.R;
import com.bdreiss.trackmyattack.Settings;

/*
 *  Abstract class representing methods to synchronize. The actual sync methods (Dropbox, Google etc.)
 *  are represented by subclasses.
 */

public abstract class Synchronizer extends Thread{

    protected final Context context;
    protected final DataModel data;
    protected final SyncCompleted syncCompleted;//method to be executed when sync is performed

    public Synchronizer(Context context, DataModel data, SyncCompleted syncCompleted){
        this.context = context;
        this.data = data;
        this.syncCompleted = syncCompleted;


    }

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
            Synchronizer sync = null;

            if (settings.getSyncMethod() == SyncMethod.DROPBOX) {
                sync = new DropboxSynchronizer(context, data, () -> {
                    settings.setSynced(true);
                    setSyncButton(context, syncButton, false);
                });
            }

            //sync should actually never be null
            if (sync != null)
                sync.start();
            else
                Log.e("SyncError", "No sync method has been identified");

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
