package com.bdreiss.trackmyattack.listeners

import android.content.Context
import android.view.View
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity

//Listener for adding data to causes
class CausesOnClickListener(context: Context, data: DataModel) : CustomListener(context, data) {

    override fun onClick(v: View?) {

        if (data.getCauseData(text) is IteratorWithIntensity)
            chooseIntensity(context){
                    chosenEnumValue ->
                data.addCause(text, chosenEnumValue)
                data.save()
            }
        else
            data.addCause(text)
        data.save()

    }

    override fun clone():CustomListener{
        return CausesOnClickListener(context, data)
    }

}