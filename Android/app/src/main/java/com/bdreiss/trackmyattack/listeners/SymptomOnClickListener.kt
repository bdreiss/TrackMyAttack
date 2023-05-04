package com.bdreiss.trackmyattack.listeners

import android.content.Context
import android.view.View
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity

//Listener for adding data to symptoms
class SymptomOnClickListener(context: Context, data: DataModel) : CustomListener(context, data) {

    override fun onClick(v: View?) {

        if (data.getSymptomData(text) is IteratorWithIntensity)
            chooseIntensity(context){
                    chosenEnumValue ->
                data.addSymptom(text, chosenEnumValue)
                data.save()
            }
        else
            throw Exception("Somehow a Symptom without Intensity slipped in. You are in SymptomClickListener.")
        data.save()

    }

    override fun clone(): CustomListener {
        return SymptomOnClickListener(context, data)
    }

}
