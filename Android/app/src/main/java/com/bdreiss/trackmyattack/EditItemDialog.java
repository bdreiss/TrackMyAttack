package com.bdreiss.trackmyattack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.DatumWithIntensity;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;

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

        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.edit_item_linear_layout);

        while (entries.hasNext()) {
            Datum entry = entries.next();
            LocalDateTime date = entry.getDate();

            LinearLayout linearLayoutForEntry = new LinearLayout(getContext());

            linearLayoutForEntry.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView = new TextView(getContext());
            textView.setText(date.toString());
            linearLayoutForEntry.addView(textView);
            if (entries instanceof IteratorWithIntensity) {
                TextView textViewIntensity = new TextView(getContext());
                textViewIntensity.setText(((DatumWithIntensity) entry).getIntensity().toString());
                linearLayoutForEntry.addView(textViewIntensity);
            }
            linearLayout.addView(linearLayoutForEntry);
        }

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