package com.bdreiss.trackmyattack;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings implements Serializable {
    private String SETTINGS_FILE_NAME = "settings";
    private File SAVE_FILE;
    private boolean synched = true;

    public Settings(Context context){
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void transferSettings(Settings settings){
        this.synched = settings.synched;
    }

    private void save(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);

            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSynched(boolean synched){
        this.synched = synched;
        save();

    }

    public boolean getSynched(){
        load();
        return synched;
    }

}
