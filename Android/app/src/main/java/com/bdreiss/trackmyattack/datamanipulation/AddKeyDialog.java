package com.bdreiss.trackmyattack.datamanipulation;

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

import com.bdreiss.data.core.AbstractData;
import com.bdreiss.data.enums.Category;
import com.bdreiss.trackmyattack.R;
import com.bdreiss.trackmyattack.sync.Synchronizer;

/*
 * Dialog that adds a new key to the DataModel.
 */

public class AddKeyDialog extends DialogFragment {

    private final Context context;
    private final AbstractData data;
    private final AddKeyDialogListener listener;

    public AddKeyDialog(Context context, AbstractData data, AddKeyDialogListener listener){
        this.context = context;
        this.data = data;
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

        if (data.getCategory() == Category.SYMPTOM) {
            chooseIntensityButton.setChecked(true);
            chooseIntensityButton.setEnabled(false);

        }

        //add new key and update Layout from which dialog was called
        addItemButton.setOnClickListener(v -> {
            String item = keyToBeAdded.getText().toString();
            Boolean intensity = chooseIntensityButton.isChecked();

            listener.addKey(item, intensity);
            Synchronizer.autoSynchronize(context, data.getData());
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
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }



}
