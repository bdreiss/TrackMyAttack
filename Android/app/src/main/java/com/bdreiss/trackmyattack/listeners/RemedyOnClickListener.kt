package com.bdreiss.trackmyattack.listeners

import android.content.Context
import android.view.View
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity

//Listener for adding data to remedies
class RemedyOnClickListener(context: Context, data: DataModel) : CustomListener(context, data) {

    override fun onClick(v: View?) {

        if (data.getRemedyData(text) is IteratorWithIntensity)
            chooseIntensity(context){
                    chosenEnumValue ->
                data.addRemedy(text, chosenEnumValue)
                data.save()
            }
        else
            data.addRemedy(text)
        data.save()

    }

    override fun clone(): CustomListener {
        return RemedyOnClickListener(context, data)
    }

}
