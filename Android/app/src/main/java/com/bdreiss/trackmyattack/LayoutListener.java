package com.bdreiss.trackmyattack;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

import com.bdreiss.trackmyattack.listeners.CustomListener;

import java.util.Iterator;

import main.java.com.bdreiss.dataAPI.Datum;
import main.java.com.bdreiss.dataAPI.EntryNotFoundException;

/*
 * Class that represents a listener that on click initializes a layout (causes, remedies or symptoms)
 */

public class LayoutListener implements View.OnClickListener {

    Context context;//context of main activity
    Category category;//CAUSE, SYMPTOM or REMEDY
    int layoutID;//id of the layout
    int linearLayoutID;//id of the linearLayout containing the keys of given category
    CustomListener listener;//custom listener for adding entries
    FragmentManager fragmentManager;
    LayoutListenerInterface layoutListenerInterface;//includes functions for getting data and returning to main activity

    public LayoutListener(Context context, Category category, int layoutID, int linearLayoutID, CustomListener listener, FragmentManager fragmentManager, LayoutListenerInterface layoutListenerInterface){
        this.context = context;
        this.category = category;
        this.layoutID = layoutID;
        this.linearLayoutID = linearLayoutID;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.layoutListenerInterface = layoutListenerInterface;

    }

    @Override
    public void onClick(View v) {
        setLayout();
    }

    //initialize and show the layout
    public void setLayout(){

        //show layout
        ((Activity) context).setContentView(layoutID);

        //iterator for keys in category
        Iterator<String> iterator = layoutListenerInterface.getData();
        LinearLayout linearLayout = ((Activity) context).findViewById(linearLayoutID);

        //iterate through keys, add them to linear layout as buttons and set button listeners
        while (iterator.hasNext()){

            //get next item
            String item = iterator.next();

            //create new button for item
            Button itemButton = new Button(context);
            itemButton.setText(item);
            itemButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //copy the listener for adding entries with current time stamp TODO WHY?
            CustomListener listenerCopy = listener.copy();

            //add the button to the linear layout
            linearLayout.addView(itemButton);

            listenerCopy.setTextValue(item);
            itemButton.setOnClickListener(listenerCopy);

            itemButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    EditItemDialog editItemDialog = new EditItemDialog(item, new EditItemDialogInterface() {
                        @Override
                        public Iterator<Datum> getEntries(String key) throws EntryNotFoundException {
                            switch (category){
                                case AILMENT:
                                    return listener.getData().getAilmentData(key);
                                case CAUSE:
                                    return listener.getData().getCauseData(key);
                                case SYMPTOM:
                                    return listener.getData().getSymptomData(key);
                                case REMEDY:
                                    return listener.getData().getRemedyData(key);
                            }
                            return null;
                        }
                    });
                    editItemDialog.show(fragmentManager,"Edit Item Dialog");
                    return true;
                }
            });

        }

        //create button to add new keys
        Button addButton = new Button(context);
        addButton.setText("+");
        addButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //set tag so button can be accessed later on, since it has no id (this is important for testing purposes for example)
        addButton.setTag("add_key_button");

        //add add button to linear layout
        linearLayout.addView(addButton);

        //set listener for addButton that adds new key to given category (see AddItemDialog.java)
        addButton.setOnClickListener(view -> {
            AddItemDialog addItemDialog = new AddItemDialog(listener.getData(), category, this::setLayout);
            addItemDialog.setAddItemDialogListener((data, category1, item, intensity) -> {
                switch(category1){
                    case AILMENT:
                        data.addAilmentKey(item);
                        break;
                    case CAUSE:
                        data.addCauseKey(item, intensity);
                        break;
                    case SYMPTOM:
                        data.addSymptomKey(item);
                        break;
                    case REMEDY:
                        data.addRemedyKey(item, intensity);
                }
            });
            addItemDialog.show(fragmentManager, "AddItemDialog");
        });

        //button for returning back to main activity
        Button backButton = new Button(context);
        backButton.setText(context.getResources().getString(R.string.BACK_BUTTON));
        backButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //add button to linear layout
        linearLayout.addView(backButton);

        //set listener for button
        backButton.setOnClickListener(view -> layoutListenerInterface.returnToActivity());

    }
}
