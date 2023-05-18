package com.bdreiss.trackmyattack;

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

import java.util.Objects;

import main.java.com.bdreiss.dataAPI.DataModel;

public class AddItemDialog extends DialogFragment {

    private final DataModel data;
    private final Category category;
    DataUpdater dataUpdater;

    public AddItemDialog(DataModel data, Category category,DataUpdater dataUpdater){
        this.data = data;
        this.category = category;
        this.dataUpdater = dataUpdater;
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

        EditText itemToBeAdded = view.findViewById(R.id.key_to_be_added);
        ToggleButton chooseIntensityButton = view.findViewById(R.id.choose_intensity_button);
        Button addItemButton = view.findViewById(R.id.add_key_button);

        if (category == Category.SYMPTOM) {
            chooseIntensityButton.setChecked(true);
            chooseIntensityButton.setEnabled(false);

        }
        addItemButton.setOnClickListener(view1 -> {
            String item = itemToBeAdded.getText().toString();
            Boolean intensity = chooseIntensityButton.isChecked();

            listener.onDialogPositiveClick(data, category, item, intensity);

            dataUpdater.update();

            dismiss();

        });
    {
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        Objects.requireNonNull(getDialog()).getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }



}
