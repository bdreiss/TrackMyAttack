package com.bdreiss.trackmyattack;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;

import com.bdreiss.trackmyattack.DataClasses.AbstractDataModel;

import java.time.LocalDateTime;
import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.DatumWithIntensity;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.Intensity;
import main.java.com.bdreiss.dataAPI.IteratorWithIntensity;
import main.java.com.bdreiss.dataAPI.TypeMismatchException;

/*
 * Creates a Dialog that shows all entries for a key with date, time and intensity (if key has intensity).
 * All the fields can be edited by long pressing on them. There is also a button for deleting entries.
 */
public class EditItemDialog extends DialogFragment {

    private static final int DELETE_BUTTON_WIDTH = 20;
    //key for which dialog is shown
    private final String key;
    //interface including:
    //Iterator for entries
    //method for removing items
    //methods for editing date and intensity
    //this is necessary, so that the dialog can support different types of data (Causes, Symptoms and Remedies)
    private final AbstractDataModel dataModel;

    EditItemDialog(String key, AbstractDataModel dataModel) {
        this.key = key;
        this.dataModel = dataModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_item_dialog, container, false);
    }

    //method that sets the dialog up
    private void setup() {

        //get Iterator for data and handle entry not found
        Iterator<Datum> entries;
        try {
            entries = dataModel.getData(key);
        } catch (EntryNotFoundException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"Key not found", Toast.LENGTH_LONG).show();
            return;
        }

        //set label text for dialog
        TextView itemLabel = requireView().findViewById(R.id.edit_item_dialog_label);
        itemLabel.setText(key);

        //get linear layout for adding items
        LinearLayout linearLayout = requireView().findViewById(R.id.edit_item_linear_layout);
        //if there are any items in the linearLayout (because an item has been updated and called setup) remove them
        linearLayout.removeAllViews();

        assert(entries != null);

        //loop through entries and add them to linearLayout
        while (entries.hasNext()) {

            Datum entry = entries.next();
            LocalDateTime date = entry.getDate();

            //LinearLayout for TextViews showing date, time and intensity and the remove Button
            LinearLayout linearLayoutForEntry = new LinearLayout(getContext());
            linearLayoutForEntry.setOrientation(LinearLayout.HORIZONTAL);

            //set up TextView for date
            TextView textViewDate = new TextView(getContext());
            textViewDate.setText(formatDate(date));
            textViewDate.setClickable(true);

            //edit on long click
            textViewDate.setOnLongClickListener(v -> {

                //open DatePickerDialog, write back data and call setup again because order of items might have changed due to new date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());

                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {

                    //for some reason month is always given -1
                    LocalDateTime newDate = date.withYear(year).withMonth(month+1).withDayOfMonth(dayOfMonth);

                    try {
                        dataModel.editDate(key, date, newDate);
                    } catch (TypeMismatchException e) {
                        e.printStackTrace();
                    }

                    //reload entries so they are shown in right order after changes
                    setup();
                });

                datePickerDialog.show();
                return true;
            });

            linearLayoutForEntry.addView(textViewDate);

            //set up TextView for time
            TextView textViewTime = new TextView(getContext());
            textViewTime.setText(formatTime(date));
            textViewTime.setClickable(true);

            //edit on long click
            textViewTime.setOnLongClickListener(v -> {

                //open TimePickerDialog, write back data and call setup again because order of entries might have changed due to new time
                TimePickerDialog.OnTimeSetListener timeListener = (view, hourOfDay, minute) -> {
                    LocalDateTime newDate = date.withHour(hourOfDay).withMinute(minute);
                    try {
                        dataModel.editDate(key, date, newDate);
                    } catch (TypeMismatchException e) {
                        e.printStackTrace();
                    }

                    //reload entries so they are shown in right order after changes
                    setup();
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),timeListener,LocalDateTime.now().getHour(),LocalDateTime.now().getMinute(),true);

                timePickerDialog.show();

                return true;

            });

            linearLayoutForEntry.addView(textViewTime);

            //if entry has intensity add TextView for it
            if (entries instanceof IteratorWithIntensity) {

                //set up TextView and add to linearLayout
                TextView textViewIntensity = new TextView(getContext());
                textViewIntensity.setText(((DatumWithIntensity) entry).getIntensity().toString());
                textViewIntensity.setClickable(true);

                //edit on long click
                textViewIntensity.setOnLongClickListener(v -> {

                    //open choose Intensity dialog and write back data
                    AddDatumListener.chooseIntensity(getContext(), (dialog, which) -> {

                        //which + 1 because NO_INTENSITY is no option in the dialog
                        Intensity newIntensity = Intensity.values()[which+1];

                        try {
                            dataModel.editIntensity(key,date, newIntensity);
                        } catch (TypeMismatchException e) {
                            e.printStackTrace();
                        }

                        //update TextView
                        textViewIntensity.setText(newIntensity.toString());

                    });

                    return true;
                });
                linearLayoutForEntry.addView(textViewIntensity);
            }

            //Button to delete entry
            Button deleteButton = new Button(getContext());
            //set Drawable
            deleteButton.setCompoundDrawablesWithIntrinsicBounds(null, AppCompatResources.getDrawable(requireContext(),R.drawable.ic_action_delete), null,null);

            //add Button to linearLayout
            linearLayoutForEntry.addView(deleteButton);

            //set width
            deleteButton.setWidth(DELETE_BUTTON_WIDTH);

            //delete item on click and remove it from linearLayout
            deleteButton.setOnClickListener(v -> {
                dataModel.removeItem(key, entry.getDate());
                linearLayout.removeView(linearLayoutForEntry);
            });

            //add linearLayoutForEntry with TextViews and Button to linearLayout
            linearLayout.addView(linearLayoutForEntry);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    //takes LocalDateTime as parameter and returns it as yyyy-mm-dd
    private String formatDate(LocalDateTime date){

        if (date == null)
            return "";

        String year = String.valueOf(date.getYear());
        String month = addZeroToNumber(date.getMonthValue());
        String day = addZeroToNumber(date.getDayOfMonth());

        return year + "-" + month + "-" + day;
    }
    //takes LocalDateTime as parameter and returns its as hh:mm:ss
    private String formatTime(LocalDateTime date){

        if (date == null)
            return "";

        String hour = addZeroToNumber(date.getHour());
        String minute = addZeroToNumber(date.getMinute());
        String second = addZeroToNumber(date.getSecond());

        return hour + ":" + minute + ":" + second;
    }

    //returns number < 10 with additional 0 in the beginning, i.e. 2 -> 02
    private String addZeroToNumber(int number){
        if (number < 10)
            return "0" + number;

        return String.valueOf(number);
    }
}