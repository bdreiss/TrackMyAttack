package com.bdreiss.trackmyattack

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
            setLayout(R.layout.causes,R.id.linear_layout_causes, data.causes, CausesOnClickListener(this,data))
        }

        findViewById<Button>(R.id.button_symptoms_view).setOnClickListener{
            setLayout(R.layout.symptoms,R.id.linear_layout_symptoms,data.symptoms,SymptomOnClickListener(this,data))
        }

        findViewById<Button>(R.id.button_remedies_view).setOnClickListener{
            setLayout(R.layout.remedies,R.id.linear_layout_remedies, data.remedies, RemedyOnClickListener(this,data))
        }


    }

    private fun setLayout(idLayout : Int, idLinearLayout: Int, iterator : Iterator<String>, listener : CustomListener){
        setContentView(idLayout)
        val linearLayout = findViewById<LinearLayout>(idLinearLayout)

        while (iterator.hasNext()){
            val text = iterator.next()

            val button = Button(this)
            val listenerCopy = listener.clone()
            button.text = text
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


            linearLayout.addView(button)

            listenerCopy.setTextValue(text)
            button.setOnClickListener(listenerCopy)

        }
        val button = Button(this)

        button.text = "+"
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.addView(button)

        val buttonBack = Button(this)

        buttonBack.text = "BACK"
        buttonBack.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.addView(buttonBack)

        buttonBack.setOnClickListener{
            activityMain()
        }
    }

    open class CustomListener(val context: Context, val data: DataModel) : View.OnClickListener{
        lateinit var text : String

        fun setTextValue(text : String){
            this.text = text
        }

        override fun onClick(v: View?) {
        }
        open fun clone():CustomListener{
            return CustomListener(context, data)
        }
        fun chooseIntensity(context: Context, onEnumSelected: (Intensity) -> Unit) {
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


    class CausesOnClickListener(context: Context, data: DataModel) : CustomListener(context, data) {

        override fun onClick(v: View?) {

            if (data.getCauseData(text).next() is DatumWithIntensity)
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

    class SymptomOnClickListener(context: Context, data: DataModel) : CustomListener(context, data) {

        override fun onClick(v: View?) {

            if (data.getSymptomData(text).next() is DatumWithIntensity)
                chooseIntensity(context){
                        chosenEnumValue ->
                    data.addSymptom(text, chosenEnumValue)
                    data.save()
                }
            else
                throw Exception("Somehow a Symptom without Intensity slipped in. You are in SymptomClickListener.")
            data.save()

        }

        override fun clone():CustomListener{
            return SymptomOnClickListener(context, data)
        }

    }

    class RemedyOnClickListener(context: Context, data: DataModel) : CustomListener(context, data) {

        override fun onClick(v: View?) {

            if (data.getRemedyData(text).next() is DatumWithIntensity)
                chooseIntensity(context){
                        chosenEnumValue ->
                    data.addRemedy(text, chosenEnumValue)
                    data.save()
                }
            else
                data.addRemedy(text)
            data.save()

        }

        override fun clone():CustomListener{
            return RemedyOnClickListener(context, data)
        }

    }


}