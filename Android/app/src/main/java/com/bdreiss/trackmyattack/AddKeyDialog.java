package com.bdreiss.trackmyattack;

import android.content.Context;
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

import com.bdreiss.dataAPI.AbstractDataModel;
import com.bdreiss.dataAPI.enums.Category;

/*
 * Dialog that adds a new key to the DataModel.
 */

public class AddKeyDialog extends DialogFragment {

    private Context context;
    private AbstractDataModel dataModel;
    private AddKeyDialogListener listener;

    public AddKeyDialog(Context context, AbstractDataModel dataModel, AddKeyDialogListener listener){
        this.context = context;
        this.dataModel = dataModel;
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

        EditText keyToBeAdded = view.findViewById(R.id.key_to_be_added);
        ToggleButton chooseIntensityButton = view.findViewById(R.id.choose_intensity_button);
        Button addItemButton = view.findViewById(R.id.add_key_button);

        if (dataModel.getCategory() == Category.SYMPTOM) {
            chooseIntensityButton.setChecked(true);
            chooseIntensityButton.setEnabled(false);

        }

        //add new key and update Layout from which dialog was called
        addItemButton.setOnClickListener(v -> {
            String item = keyToBeAdded.getText().toString();
            Boolean intensity = chooseIntensityButton.isChecked();

            listener.addKey(item, intensity);
            Synchronizer.autoSynchronize(context, dataModel.getData());
            listener.updateOriginalLayout();

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
