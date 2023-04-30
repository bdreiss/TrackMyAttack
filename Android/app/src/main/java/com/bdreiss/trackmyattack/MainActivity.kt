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
import main.java.com.bdreiss.dataAPI.Intensity
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity
import java.io.File

class MainActivity : AppCompatActivity(), AddItemDialog.AddItemDialogListener {
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
            val text = habitsEditTextText.text.toString().trim()
            val intensityText = habitsEditTextIntensity.text.toString()

            if (text.isNotEmpty()) {

                var intensity = Intensity.NO_INTENSITY
                if (intensityText.isNotEmpty()) {
                    when (intensityText.toInt()) {
                        0 -> intensity = Intensity.LOW
                        1 -> intensity = Intensity.MEDIUM
                        2 -> intensity = Intensity.HIGH
                    }
                    data.addCause(text, intensity)

                } else {
                    data.addCause(text)
                }


                data.save()

                textView.text = data.print()
                habitsEditTextText.setText("")
                habitsEditTextIntensity.setText("")
            }
        }
        val symptomsEditTextText = findViewById<EditText>(R.id.edit_text_symptom_text)
        val symptomsEditTextIntensity = findViewById<EditText>(R.id.edit_text_symptom_intensity)
        val symptomsButton = findViewById<Button>(R.id.button_symptom)

        symptomsButton.setOnClickListener {
            val text = symptomsEditTextText.text.toString().trim()
            val intensityText = symptomsEditTextIntensity.text.toString()

            if (text.isNotEmpty()) {

                var intensity = Intensity.NO_INTENSITY
                if (intensityText.isNotEmpty()) {
                    when (intensityText.toInt()) {
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
            val text = remedyEditTextText.text.toString().trim()
            val intensityText = remedyEditTextIntensity.text.toString()
            if (text.isNotEmpty()) {


                var intensity = Intensity.LOW
                if (intensityText.isNotEmpty()) {
                    when (intensityText.toInt()) {
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
        }
        findViewById<Button>(R.id.button_causes_view).setOnClickListener {
            setLayout(Category.CAUSE, R.layout.causes,R.id.linear_layout_causes, data.causes, CausesOnClickListener(this,data))
        }

        findViewById<Button>(R.id.button_symptoms_view).setOnClickListener{
            setLayout(Category.SYMPTOM, R.layout.symptoms,R.id.linear_layout_symptoms,data.symptoms,SymptomOnClickListener(this,data))
        }

        findViewById<Button>(R.id.button_remedies_view).setOnClickListener{
            setLayout(Category.REMEDY, R.layout.remedies,R.id.linear_layout_remedies, data.remedies, RemedyOnClickListener(this,data))
        }


    }

    private fun setLayout(category: Category, idLayout : Int, idLinearLayout: Int, iterator : Iterator<String>, listener : CustomListener){
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
        val buttonAdd = Button(this)

        buttonAdd.text = "+"
        buttonAdd.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.addView(buttonAdd)

        buttonAdd.setOnClickListener{

            val addItemDialog = AddItemDialog(listener.data, category)
            addItemDialog.setAddItemDialogListener(this)
            addItemDialog.show(supportFragmentManager, "AddItemDialog")


        }



        val buttonBack = Button(this)

        buttonBack.text = getString(R.string.BACK_BUTTON)
        buttonBack.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.addView(buttonBack)

        buttonBack.setOnClickListener{
            activityMain()
        }
    }

    //abstract class for listeners being passed to layouts showing causes, symptoms and remedies
    abstract class CustomListener(val context: Context, val data: DataModel) : View.OnClickListener{
        lateinit var text : String

        fun setTextValue(text : String){
            this.text = text
        }

        override fun onClick(v: View?) {
        }
        abstract fun clone():CustomListener

        //dialog that lets users choose intensity of item
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

        override fun clone():CustomListener{
            return SymptomOnClickListener(context, data)
        }

    }

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

        override fun clone():CustomListener{
            return RemedyOnClickListener(context, data)
        }

    }

    override fun onDialogPositiveClick(data: DataModel, category: Category, item: String, intensity: Boolean) {
        when(category){
            Category.AILMENT -> {
                data.addAilmentKey(item, intensity)
            }
            Category.CAUSE -> {
                data.addCauseKey(item, intensity)
                setLayout(Category.CAUSE, R.layout.causes,R.id.linear_layout_causes, data.causes, CausesOnClickListener(this,data))

            }
            Category.SYMPTOM -> {
                data.addSymptomKey(item, intensity)
                setLayout(Category.SYMPTOM, R.layout.symptoms,R.id.linear_layout_symptoms, data.symptoms, SymptomOnClickListener(this,data))

            }
            Category.REMEDY -> {
                data.addRemedyKey(item, intensity)
                setLayout(Category.REMEDY, R.layout.remedies,R.id.linear_layout_remedies, data.remedies, RemedyOnClickListener(this,data))
            }
        }

        data.save()

        // Use the data from the EditText and ToggleButton
        // You can store the data, update the UI, etc.
    }



}

enum class Category {
    AILMENT, CAUSE, SYMPTOM, REMEDY
}