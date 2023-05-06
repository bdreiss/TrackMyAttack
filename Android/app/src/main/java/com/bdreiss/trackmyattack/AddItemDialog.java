package com.bdreiss.trackmyattack;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import main.java.com.bdreiss.dataAPI.DataModel;

public class AddItemDialog extends DialogFragment {

    private DataModel data;
    private Category category;

    public AddItemDialog(DataModel data, Category category){
        this.data = data;
        this.category = category;
    }

    private AddItemDialogListener listener;

    public void setAddItemDialogListener(AddItemDialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_item_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText itemToBeAdded = view.findViewById(R.id.item_to_be_added);
        ToggleButton chooseIntensityButton = view.findViewById(R.id.choose_intensity_button);
        Button addItemButton = view.findViewById(R.id.add_item_button);

        if (category == Category.SYMPTOM) {
            chooseIntensityButton.setChecked(true);
            chooseIntensityButton.setEnabled(false);

        }
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = itemToBeAdded.getText().toString();
                Boolean intensity = chooseIntensityButton.isChecked();

                listener.onDialogPositiveClick(data, category, item, intensity);

                dismiss();

            }
        });
    {
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void onDialogPositiveClick(DataModel data, Category category, String item, Boolean intensity) {
    }



}
