package com.bdreiss.trackmyattack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;

public class EditItemDialog extends DialogFragment {

    private String key;
    private EditItemDialogInterface editItemDialogInterface;

    EditItemDialog(String key, EditItemDialogInterface editItemDialogInterface) {
        this.key = key;
        this.editItemDialogInterface = editItemDialogInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_item_dialog, container, false);
    }

    private void setup() throws EntryNotFoundException {
        Iterator<Datum> entries = editItemDialogInterface.getEntries(key);
        TextView itemLabel = (TextView) getView().findViewById(R.id.edit_item_dialog_label);
        itemLabel.setText(key);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setup();
        } catch (EntryNotFoundException e) {
            e.printStackTrace();
        }
    }
}