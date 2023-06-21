package com.bdreiss.trackmyattack;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;


import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Stack;

import main.java.com.bdreiss.dataAPI.AbstractDataModel;
import main.java.com.bdreiss.dataAPI.enums.Category;
import main.java.com.bdreiss.dataAPI.enums.Intensity;
import main.java.com.bdreiss.dataAPI.exceptions.EntryNotFoundException;
import main.java.com.bdreiss.dataAPI.exceptions.TypeMismatchException;
import main.java.com.bdreiss.dataAPI.util.Datum;
import main.java.com.bdreiss.dataAPI.util.DatumWithIntensity;
import main.java.com.bdreiss.dataAPI.util.IteratorWithIntensity;


/*
 * Creates a Dialog that shows all entries for a key with date, time and intensity (if key has intensity).
 * All the fields can be edited by long pressing on them. There is also a button for deleting entries.
 */
public class EditItemDialog extends DialogFragment {

    private static final int LABEL_TEXT_SIZE = 20;
    private static final int LABEL_PADDING = 40;
    private static final int DELETE_BUTTON_HEIGHT = 10;
    private static final int DELETE_BUTTON_WIDTH = 20;
    private static final int LINEAR_LAYOUT_PADDING = 5;



    //key for which dialog is shown
    private final String key;
    //interface including:
    //Iterator for entries
    //method for removing items
    //methods for editing date and intensity
    //this is necessary, so that the dialog can support different types of data (Causes, Symptoms and Remedies)
    private final AbstractDataModel dataModel;

    //contains method to update original layout in case key is removed from data
    private final AddKeyDialogListener addKeyDialogListener;

    EditItemDialog(String key, AbstractDataModel dataModel, AddKeyDialogListener addKeyDialogListener) {
        this.key = key;
        this.dataModel = dataModel;
        this.addKeyDialogListener = addKeyDialogListener;
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
        itemLabel.setGravity(Gravity.CENTER);
        itemLabel.setTextSize(LABEL_TEXT_SIZE);
        itemLabel.setPadding(LABEL_PADDING,LABEL_PADDING,LABEL_PADDING,LABEL_PADDING);

        ImageView deleteButton = requireView().findViewById(R.id.edit_item_dialog_delete_image_view);

        deleteButton.setPadding(LABEL_PADDING/2,LABEL_PADDING/2,LABEL_PADDING/2,LABEL_PADDING/2);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Erase all data for '" + key + "'?");
        builder.setTitle("Confirm deletion");
        builder.setPositiveButton("YES", (dialog, which) -> {
            dataModel.removeKey(key);addKeyDialogListener.updateOriginalLayout();dismiss();
        });

        builder.setNegativeButton("NO",(dialog, which) -> {});

        AlertDialog deleteKeyConfirmationDialog = builder.create();

        deleteButton.setOnClickListener(v->deleteKeyConfirmationDialog.show());

        if (dataModel.getCategory() == Category.AILMENT){
            deleteButton.setOnClickListener(v->{});
            deleteButton.setVisibility(View.GONE);
        }

        //get linear layout for adding items
        LinearLayout linearLayout = requireView().findViewById(R.id.edit_item_linear_layout);

        linearLayout.setGravity(Gravity.CENTER);

        //if there are any items in the linearLayout (because an item has been updated and called setup) remove them
        linearLayout.removeAllViews();

        assert(entries != null);

        //in order to show the newest entries on top of the list push all entries on top of a Stack
        // so that they are in reverse order and then pop them one by one
        Stack<Datum> entriesStack = new Stack<>();

        while (entries.hasNext())
            entriesStack.push(entries.next());

        //loop through entries and add them to linearLayout
        while (!entriesStack.empty()) {

            Datum entry = entriesStack.pop();

            //add separation line
            linearLayout.addView(EditItemView.getSeparationLine(getContext()));

            LocalDateTime date = entry.getDate();

            //LinearLayout for TextViews showing date, time and intensity and the remove Button
            LinearLayout linearLayoutForEntry = new LinearLayout(getContext());
            linearLayoutForEntry.setGravity(Gravity.CENTER);

            linearLayoutForEntry.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutForEntry.setPadding(0,LINEAR_LAYOUT_PADDING,0,LINEAR_LAYOUT_PADDING);

            //set up TextView for date
            View textViewDate = new EditItemView(getContext(),formatDate(date));


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
            View textViewTime = new EditItemView(getContext(), formatTime(date));

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
                EditItemView textViewIntensity = new EditItemView(getContext(),((DatumWithIntensity) entry).getIntensity().toString());

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
                        textViewIntensity.resetText(newIntensity.toString());

                    });

                    return true;
                });
                linearLayoutForEntry.addView(textViewIntensity);
            }
            else{
                TextView intensityView = new TextView(getContext());
                intensityView.setWidth(EditItemView.INTENSITY_WIDTH);
                linearLayoutForEntry.addView(intensityView);
            }

            //Button to delete entry
            Button deleteItemButton = new Button(getContext());
            //set Drawable
            deleteItemButton.setCompoundDrawablesWithIntrinsicBounds(null, AppCompatResources.getDrawable(requireContext(),R.drawable.ic_action_delete), null,null);

            //add Button to linearLayout
            linearLayoutForEntry.addView(deleteItemButton);

            //set width and height
            deleteItemButton.setWidth(DELETE_BUTTON_WIDTH);
            deleteItemButton.setHeight(DELETE_BUTTON_HEIGHT);

            //delete item on click and remove it from linearLayout
            deleteItemButton.setOnClickListener(v -> {
                dataModel.removeItem(key, entry.getDate());
                //remove separation line
                linearLayout.removeViewAt(linearLayout.indexOfChild(linearLayoutForEntry)+1);
                linearLayout.removeView(linearLayoutForEntry);
            });

            //add linearLayoutForEntry with TextViews and Button to linearLayout
            linearLayout.addView(linearLayoutForEntry);



        }
        //add separation line
        linearLayout.addView(EditItemView.getSeparationLine(getContext()));

        //Button to return to LayoutListener
        Button backButton = requireView().findViewById(R.id.button_edit_back);
        backButton.setOnClickListener(v -> dismiss());

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
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}