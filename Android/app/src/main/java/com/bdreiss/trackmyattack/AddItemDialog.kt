package com.bdreiss.trackmyattack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import androidx.fragment.app.DialogFragment
import main.java.com.bdreiss.dataAPI.DataModel

class AddItemDialog(private val data: DataModel, private val category: Category) : DialogFragment() {

    interface AddItemDialogListener {
        fun onDialogPositiveClick(data: DataModel, category: Category, item: String, intensity: Boolean)
    }


    private lateinit var listener: AddItemDialogListener

    fun setAddItemDialogListener(listener: AddItemDialogListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_item_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemToBeAdded = view.findViewById<EditText>(R.id.item_to_be_added)
        val chooseIntensityButton = view.findViewById<ToggleButton>(R.id.choose_intensity_button)
        val addItemButton = view.findViewById<Button>(R.id.add_item_button)

        if (category == Category.SYMPTOM) {
            chooseIntensityButton.isChecked = true
            chooseIntensityButton.isEnabled = false

        }
        addItemButton.setOnClickListener {
            val item = itemToBeAdded.text.toString()
            val intensity = chooseIntensityButton.isChecked

            listener.onDialogPositiveClick(data, category, item, intensity)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
