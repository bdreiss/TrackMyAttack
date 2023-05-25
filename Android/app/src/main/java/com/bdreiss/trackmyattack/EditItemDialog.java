package com.bdreiss.trackmyattack;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.bdreiss.trackmyattack.listeners.CustomListener;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.DatumWithIntensity;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

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
            textView.setText(formatDate(date));
            textView.setClickable(true);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    LocalDateTime newDate = LocalDateTime.now();
                    try {
                        editItemDialogInterface.editDate(key, date, newDate);
                    } catch (TypeMismatchException e) {
                        e.printStackTrace();
                    }
                    textView.setText(formatDate(newDate));
                    return true;
                }
            });
            linearLayoutForEntry.addView(textView);
            if (entries instanceof IteratorWithIntensity) {
                TextView textViewIntensity = new TextView(getContext());
                textViewIntensity.setText(((DatumWithIntensity) entry).getIntensity().toString());
                textViewIntensity.setClickable(true);
                textViewIntensity.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        CustomListener.chooseIntensity(getContext(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //which + 1 because NO_INTENSITY is no option in the dialog
                                Intensity newIntensity = Intensity.values()[which+1];

                                try {
                                    editItemDialogInterface.editIntensity(key,date, newIntensity);
                                } catch (TypeMismatchException e) {
                                    e.printStackTrace();
                                }

                                textViewIntensity.setText(newIntensity.toString());

                            }
                        });



                        return true;
                    }
                });
                linearLayoutForEntry.addView(textViewIntensity);
            }

            Button deleteButton = new Button(getContext());
            deleteButton.setCompoundDrawablesWithIntrinsicBounds(null, getContext().getDrawable(R.drawable.ic_action_delete), null,null);

            linearLayoutForEntry.addView(deleteButton);
            deleteButton.setWidth(20);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItemDialogInterface.removeItem(key, entry.getDate());
                    linearLayout.removeView(linearLayoutForEntry);
                }
            });
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

    private String formatDate(LocalDateTime date){
        return date.toString();
    }
}