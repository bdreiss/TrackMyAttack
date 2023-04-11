package com.bdreiss.trackmyattack

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.DatumWithIntensity
import main.java.com.bdreiss.dataAPI.Intensity
import java.io.File

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMain()
    }

    private fun activityMain(){
        setContentView(R.layout.activity_main)
        //DataModel.deleteSaveFile(this)

        val data = DataModel(filesDir.absolutePath)

        data.load()

        File(filesDir.absolutePath + "/Text.txt").printWriter().use{out ->
            out.println(data.print())
        }

        val textView = findViewById<TextView>(R.id.textView)

        textView.text = data.print()

        val habitsEditTextText = findViewById<EditText>(R.id.edit_text_habit_text)
        val habitsEditTextIntensity = findViewById<EditText>(R.id.edit_text_habit_intensity)
        val habitsButton = findViewById<Button>(R.id.button_habit)

        habitsButton.setOnClickListener {
            val text = habitsEditTextText.text.toString()
            val intensityText = habitsEditTextIntensity.text.toString()

            var intensity = Intensity.NO_INTENSITY
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.LOW
                    1 -> intensity = Intensity.MEDIUM
                    2 -> intensity = Intensity.HIGH
                }
                data.addCause(text.trim(), intensity)

            } else{
                data.addCause(text)
            }


            data.save()

            textView.text = data.print()
            habitsEditTextText.setText("")
            habitsEditTextIntensity.setText("")
        }

        val symptomsEditTextText = findViewById<EditText>(R.id.edit_text_symptom_text)
        val symptomsEditTextIntensity = findViewById<EditText>(R.id.edit_text_symptom_intensity)
        val symptomsButton = findViewById<Button>(R.id.button_symptom)

        symptomsButton.setOnClickListener {
            val text = symptomsEditTextText.text.toString()
            val intensityText = symptomsEditTextIntensity.text.toString()

            var intensity = Intensity.NO_INTENSITY
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.LOW
                    1 -> intensity = Intensity.MEDIUM
                    2 -> intensity = Intensity.HIGH
                }

            }

            data.addSymptom(text.trim(), intensity)

            data.save()

            textView.text = data.print()
            symptomsEditTextText.setText("")
            symptomsEditTextIntensity.setText("")
        }

        val migraineEditTextText = findViewById<EditText>(R.id.edit_text_migraine_text)
        val migraineEditTextIntensity = findViewById<EditText>(R.id.edit_text_migraine_intensity)
        val migraineButton = findViewById<Button>(R.id.button_migraine)

        migraineButton.setOnClickListener {
            val intensityText = migraineEditTextIntensity.text.toString()

            var intensity = Intensity.NO_INTENSITY
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.LOW
                    1 -> intensity = Intensity.MEDIUM
                    2 -> intensity = Intensity.HIGH
                }
            }

            data.addAilment("Migraine", intensity)
            data.save()

            textView.text = data.print()
            migraineEditTextText.setText("")
            migraineEditTextIntensity.setText("")
        }
        val remedyEditTextText = findViewById<EditText>(R.id.edit_text_remedy_text)
        val remedyEditTextIntensity = findViewById<EditText>(R.id.edit_text_remedy_intensity)
        val remedyButton = findViewById<Button>(R.id.button_remedy)

        remedyButton.setOnClickListener {
            val text = remedyEditTextText.text.toString()
            val intensityText = remedyEditTextIntensity.text.toString()

            var intensity = Intensity.LOW
            if (intensityText.isNotEmpty()) {
                when(intensityText.toInt()){
                    0 -> intensity = Intensity.LOW
                    1 -> intensity = Intensity.MEDIUM
                    2 -> intensity = Intensity.HIGH
                }
            }

            data.addRemedy(text.trim(), intensity)
            data.save()

            textView.text = data.print()
            remedyEditTextText.setText("")
            remedyEditTextIntensity.setText("")
        }

        findViewById<Button>(R.id.button_causes_view).setOnClickListener {
            causesLayout(data)
        }

    }

    private fun causesLayout(data:DataModel){
        setContentView(R.layout.causes)
        val causesLinearLayout = findViewById<LinearLayout>(R.id.linear_layout_causes)
        val iterator = data.causes

        while (iterator.hasNext()){
            val cause = iterator.next()

            val causeButton = Button(this)

            causeButton.text = cause
            causeButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


            causesLinearLayout.addView(causeButton)

            causeButton.setOnClickListener{
                if (data.getCauseData(cause).next() is DatumWithIntensity)
                    chooseIntensity(this){
                            chosenEnumValue ->
                        data.addCause(cause, chosenEnumValue)
                        data.save()
                    }
                else
                    data.addCause(cause)
                data.save()
            }

        }
        val button = Button(this)

        button.text = "+"
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        causesLinearLayout.addView(button)

        val buttonBack = Button(this)

        buttonBack.text = "BACK"
        buttonBack.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        causesLinearLayout.addView(buttonBack)

        buttonBack.setOnClickListener{
            activityMain()
        }
    }
    private fun chooseIntensity(context: Context, onEnumSelected: (Intensity) -> Unit) {
        val customEnumValues = Intensity.values().copyOfRange(1,Intensity.values().size)
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