package com.bdreiss.trackmyattack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.util.Iterator;

import com.bdreiss.dataAPI.core.AbstractData;
import com.bdreiss.dataAPI.enums.Category;
import com.bdreiss.trackmyattack.datamanipulation.AddDatumListener;
import com.bdreiss.trackmyattack.datamanipulation.AddKeyDialog;
import com.bdreiss.trackmyattack.datamanipulation.AddKeyDialogListener;
import com.bdreiss.trackmyattack.datamanipulation.EditItemDialog;

/*
 * Class that represents a listener that on click initializes a layout (causes, remedies or symptoms)
 */

public class LayoutListener implements View.OnClickListener {

    Context context;//context of main activity
    AbstractData data;
    FragmentManager fragmentManager;
    LayoutListenerInterface layoutListenerInterface;//includes functions for getting data and returning to main activity


    private ActivityResultLauncher<Intent> locationSettingsResultLauncher;
    public LayoutListener(Context context, AbstractData data, FragmentManager fragmentManager, LayoutListenerInterface layoutListenerInterface, ActivityResultLauncher<Intent> locationSettingsResultLauncher){
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
        this.layoutListenerInterface = layoutListenerInterface;
        this.locationSettingsResultLauncher = locationSettingsResultLauncher;
    }

    @Override
    public void onClick(View v) {
        setLayout();
    }

    //initialize and show the layout
    public void setLayout(){

        //show layout
        ((Activity) context).setContentView(R.layout.data_interface);

        //iterator for keys in category
        Iterator<String> iterator = data.getKeys();

        LinearLayout linearLayout = ((Activity) context).findViewById(R.id.linear_layout_data_interface);

        //iterate through keys, add them to linear layout as buttons and set button listeners
        while (iterator.hasNext()){

            //get next item
            String item = iterator.next();

            //create new button for item
            Button itemButton = new Button(context);
            itemButton.setText(item);
            itemButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //listener for adding new Datum
            AddDatumListener listener = new AddDatumListener(context, data, locationSettingsResultLauncher);

            //add the button to the linear layout
            linearLayout.addView(itemButton);

            listener.setKey(item);
            itemButton.setOnClickListener(listener);

            itemButton.setOnLongClickListener(v -> {
                EditItemDialog editItemDialog = new EditItemDialog(context, item, data, new AddKeyDialogListener() {
                    @Override
                    public void addKey(String key, Boolean intensity) {}

                    @Override
                    public void updateOriginalLayout() {
                        setLayout();
                    }
                });
                editItemDialog.show(fragmentManager,"Edit Item Dialog");
                return true;
            });
        }

        //create button to add new keys
        Button addButton = new Button(context);
        addButton.setText("+");
        addButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        addButton.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_variant));
        addButton.setTextColor(Color.WHITE);

        //set tag so button can be accessed later on, since it has no id (this is important for testing purposes for example)
        addButton.setTag("add_key_button");

        //add add button to linear layout
        linearLayout.addView(addButton);

        //set listener for addButton that adds new key to given category (see AddItemDialog.java)
        addButton.setOnClickListener(view -> {
            AddKeyDialog addItemDialog = new AddKeyDialog(context, data, new AddKeyDialogListener() {
                @Override
                public void addKey(String key, Boolean intensity) {
                    if (data.getCategory() == Category.AILMENT || data.getCategory() == Category.SYMPTOM)
                        data.addKey(key, false);
                    else
                        data.addKey(key, intensity);
                }

                @Override
                public void updateOriginalLayout() {
                    setLayout();
                }
            });

            addItemDialog.show(fragmentManager, "AddItemDialog");
        });

        //button for returning back to main activity
        Button backButton = new Button(context);
        backButton.setText(context.getResources().getString(R.string.BACK_BUTTON));
        backButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        backButton.setBackgroundColor(ContextCompat.getColor(context, R.color.primary));
        backButton.setTextColor(Color.WHITE);

        //add button to linear layout
        linearLayout.addView(backButton);

        //set listener for button
        backButton.setOnClickListener(view -> layoutListenerInterface.returnToActivity());

    }
}
