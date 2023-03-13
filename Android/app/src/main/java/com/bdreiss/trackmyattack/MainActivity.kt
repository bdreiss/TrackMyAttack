package com.bdreiss.trackmyattack

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.Intensity

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //DataModel.deleteSaveFile(this)

        val data = DataModel(filesDir.absolutePath)

        Log.d("XXX",filesDir.absolutePath)

        data.load()

        data.print()

        val habitsEditTextText = findViewById<EditText>(R.id.edit_text_habit_text)
        val habitsEditTextIntensity = findViewById<EditText>(R.id.edit_text_habit_intensity)
        val habitsButton = findViewById<Button>(R.id.button_habit)

        habitsButton.setOnClickListener {
            val text = habitsEditTextText.text.toString()
            val intensityText = habitsEditTextIntensity.text.toString()

            var intensity = Intensity.noIntensity
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.low
                    1 -> intensity = Intensity.medium
                    2 -> intensity = Intensity.high
                }
            }

            data.addHabit(text, intensity)
            data.save()

            habitsEditTextText.setText("")
            habitsEditTextIntensity.setText("")
        }

        val symptomsEditTextText = findViewById<EditText>(R.id.edit_text_symptom_text)
        val symptomsEditTextIntensity = findViewById<EditText>(R.id.edit_text_symptom_intensity)
        val symptomsButton = findViewById<Button>(R.id.button_symptom)

        symptomsButton.setOnClickListener {
            val text = symptomsEditTextText.text.toString()
            val intensityText = symptomsEditTextIntensity.text.toString()

            var intensity = Intensity.noIntensity
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.low
                    1 -> intensity = Intensity.medium
                    2 -> intensity = Intensity.high
                }
            }

            data.addSymptom(text, intensity)
            data.save()

            symptomsEditTextText.setText("")
            symptomsEditTextIntensity.setText("")
        }

        val migraineEditTextText = findViewById<EditText>(R.id.edit_text_migraine_text)
        val migraineEditTextIntensity = findViewById<EditText>(R.id.edit_text_migraine_intensity)
        val migraineButton = findViewById<Button>(R.id.button_migraine)

        migraineButton.setOnClickListener {
            val intensityText = migraineEditTextIntensity.text.toString()

            var intensity = Intensity.low
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.low
                    1 -> intensity = Intensity.medium
                    2 -> intensity = Intensity.high
                }
            }

            data.addMigraine("Migraine", intensity)
            data.save()

            migraineEditTextText.setText("")
            migraineEditTextIntensity.setText("")
        }
        val remedyEditTextText = findViewById<EditText>(R.id.edit_text_remedy_text)
        val remedyEditTextIntensity = findViewById<EditText>(R.id.edit_text_remedy_intensity)
        val remedyButton = findViewById<Button>(R.id.button_remedy)

        remedyButton.setOnClickListener {
            val text = remedyEditTextText.text.toString()
            val intensityText = remedyEditTextIntensity.text.toString()

            var intensity = Intensity.low
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.low
                    1 -> intensity = Intensity.medium
                    2 -> intensity = Intensity.high
                }
            }

            data.addRemedy(text, intensity)
            data.save()

            remedyEditTextText.setText("")
            remedyEditTextIntensity.setText("")
        }

    }

}