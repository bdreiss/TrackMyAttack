package com.bdreiss.trackmyattack.sync;

/*
 *  Abstract class representing a sync method.
 */

import android.content.Context;

import com.bdreiss.dataAPI.DataModel;

public abstract class Sync extends Thread{

    protected final Context context;
    protected final DataModel data;
    protected final SyncCompleted syncCompleted;//method to be executed when sync is performed

    public Sync(Context context, DataModel data, SyncCompleted syncCompleted){
        this.context = context;
        this.data = data;
        this.syncCompleted = syncCompleted;


    }
}
