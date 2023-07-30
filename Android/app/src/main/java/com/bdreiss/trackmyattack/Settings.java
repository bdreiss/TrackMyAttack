package com.bdreiss.trackmyattack;

import android.content.Context;

import com.bdreiss.trackmyattack.sync.SyncMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings implements Serializable {
    private final File SAVE_FILE;
    private boolean synced = true;
    private boolean automaticSync = false;
    private SyncMethod syncMethod;

    public Settings(Context context){
        String SETTINGS_FILE_NAME = "settings";
        this.SAVE_FILE = new File(context.getFilesDir() + "/" + SETTINGS_FILE_NAME);
        load();
    }

    private void load(){
        if (SAVE_FILE.exists()){
            try {
                FileInputStream fis = new FileInputStream(SAVE_FILE);
                ObjectInputStream ois = new ObjectInputStream(fis);

                Settings settings = (Settings) ois.readObject();

                transferSettings(settings);

                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void transferSettings(Settings settings){
        this.synced = settings.synced;
        this.automaticSync = settings.automaticSync;
    }

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

    public void setSyncMethod(SyncMethod syncMethod){
        this.syncMethod = syncMethod;
    }
    public SyncMethod getSyncMethod(){
        return syncMethod;
    }


    public void setSynced(boolean synced){
        this.synced = synced;
        save();

    }

    public boolean getSynced(){
        load();
        return synced;
    }

    public void setAutomaticSync(boolean autoSync){
        this.automaticSync = autoSync;
        save();
    }

    public boolean getAutomaticSync(){
        load();
        return this.automaticSync;
    }

}
