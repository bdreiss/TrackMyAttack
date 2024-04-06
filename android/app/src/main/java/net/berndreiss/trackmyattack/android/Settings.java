package net.berndreiss.trackmyattack.android;

import android.content.Context;

import net.berndreiss.trackmyattack.android.sync.SyncMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 *  Class representing settings for app
 */

public class Settings implements Serializable {

    private final File SAVE_FILE;
    private boolean synced = true;//denotes whether changes have been synced or not
    private boolean automaticSync = false;//denotes whether automatic synchronization is turned on
    private SyncMethod syncMethod;//denotes the sync Method, that has been selected (DROPBOX, GOOGLE etc)

    private boolean deniedLocationAccess = false;

    public Settings(Context context){
        String SETTINGS_FILE_NAME = "settings";
        this.SAVE_FILE = new File(context.getFilesDir() + "/" + SETTINGS_FILE_NAME);
        load();
    }

    //dynamically load settings
    private void load(){
        if (SAVE_FILE.exists()){
            try {
                FileInputStream fis = new FileInputStream(SAVE_FILE);
                ObjectInputStream ois = new ObjectInputStream(fis);

                Settings settings = (Settings) ois.readObject();

                //transfer all attributes of the settings to this instance
                transferSettings(settings);

                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    //transfer all attributes of settings from load file passed as variable to this instance
    private void transferSettings(Settings settings){
        this.synced = settings.synced;
        this.automaticSync = settings.automaticSync;
        this.syncMethod = settings.syncMethod;
        this.deniedLocationAccess = settings.deniedLocationAccess;
    }

    //save changes to settings file
    private void save(){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //change sync method and save
    public void setSyncMethod(SyncMethod syncMethod){
        this.syncMethod = syncMethod;
        save();

    }

    //load and get sync method
    public SyncMethod getSyncMethod(){
        load();
        return syncMethod;
    }


    //set synced and save
    public void setSynced(boolean synced){
        this.synced = synced;
        save();
    }

    //load and get synced
    public boolean getSynced(){
        load();
        return synced;
    }

    //set auto sync and save
    public void setAutomaticSync(boolean autoSync){
        this.automaticSync = autoSync;
        save();
    }

    //load and get auto sync
    public boolean getAutomaticSync(){
        load();
        return this.automaticSync;
    }

    public boolean getDeniedLocationAccess(){
        load();
        return this.deniedLocationAccess;
    }

    public void setDeniedLocationAccess(boolean use){
        this.deniedLocationAccess = use;
        save();
    }

}
