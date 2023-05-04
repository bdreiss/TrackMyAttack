package com.bdreiss.trackmyattack.listeners

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.Intensity

//abstract class for listeners being passed to layouts showing causes, symptoms and remedies
abstract class CustomListener(val context: Context, val data: DataModel) : View.OnClickListener{
    lateinit var text : String

    fun setTextValue(text : String){
        this.text = text
    }

    override fun onClick(v: View?) {
    }
    abstract fun clone(): CustomListener

    //dialog that lets users choose intensity of item
    fun chooseIntensity(context: Context, onEnumSelected: (Intensity) -> Unit) {
        val customEnumValues = Intensity.values().copyOfRange(1, Intensity.values().size)
        val customEnumNames = customEnumValues.map { it.name }.toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("Choose Intensity")
            .setItems(customEnumNames) { _, which ->
                onEnumSelected(customEnumValues[which])
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

}
