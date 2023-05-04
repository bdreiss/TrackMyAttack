package com.bdreiss.trackmyattack

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.bdreiss.trackmyattack.listeners.CausesOnClickListener
import com.bdreiss.trackmyattack.listeners.CustomListener
import com.bdreiss.trackmyattack.listeners.RemedyOnClickListener
import com.bdreiss.trackmyattack.listeners.SymptomOnClickListener
import main.java.com.bdreiss.dataAPI.DataModel
import main.java.com.bdreiss.dataAPI.Intensity
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

        findViewById<Button>(R.id.button_causes_view).setOnClickListener {
            setLayout(Category.CAUSE, R.layout.causes,R.id.linear_layout_causes, data.causes, CausesOnClickListener(this,data))
        }

        findViewById<Button>(R.id.button_symptoms_view).setOnClickListener{
            setLayout(Category.SYMPTOM, R.layout.symptoms,R.id.linear_layout_symptoms,data.symptoms, SymptomOnClickListener(this,data)
            )
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

    }



}

enum class Category {
    AILMENT, CAUSE, SYMPTOM, REMEDY
}