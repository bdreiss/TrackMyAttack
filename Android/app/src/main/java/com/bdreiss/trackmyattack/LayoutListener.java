package com.bdreiss.trackmyattack;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

import com.bdreiss.trackmyattack.DataClasses.AbstractDataModel;

import java.util.Iterator;

/*
 * Class that represents a listener that on click initializes a layout (causes, remedies or symptoms)
 */

public class LayoutListener implements View.OnClickListener {

    Context context;//context of main activity
    AbstractDataModel dataModel;
    FragmentManager fragmentManager;
    LayoutListenerInterface layoutListenerInterface;//includes functions for getting data and returning to main activity

    public LayoutListener(Context context, AbstractDataModel dataModel, FragmentManager fragmentManager, LayoutListenerInterface layoutListenerInterface){
        this.context = context;
        this.dataModel = dataModel;
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
        ((Activity) context).setContentView(dataModel.getLayoutID());

        //iterator for keys in category
        Iterator<String> iterator = dataModel.getKeys();

        LinearLayout linearLayout = ((Activity) context).findViewById(dataModel.getLinearLayoutID());

        //iterate through keys, add them to linear layout as buttons and set button listeners
        while (iterator.hasNext()){

            //get next item
            String item = iterator.next();

            //create new button for item
            Button itemButton = new Button(context);
            itemButton.setText(item);
            itemButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //copy the listener for adding entries with current time stamp TODO WHY?
            AddDatumListener listener = new AddDatumListener(context, dataModel);

            //add the button to the linear layout
            linearLayout.addView(itemButton);

            listener.setKey(item);
            itemButton.setOnClickListener(listener);

            itemButton.setOnLongClickListener(v -> {
                EditItemDialog editItemDialog = new EditItemDialog(item, dataModel);
                editItemDialog.show(fragmentManager,"Edit Item Dialog");
                return true;
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
            AddKeyDialog addItemDialog = new AddKeyDialog(dataModel, new AddKeyDialogListener() {
                @Override
                public void addKey(String key, Boolean intensity) {
                    if (dataModel.getCategory() == Category.AILMENT || dataModel.getCategory() == Category.SYMPTOM)
                        dataModel.addKey(key, false);
                    else
                        dataModel.addKey(key, intensity);
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

        //add button to linear layout
        linearLayout.addView(backButton);

        //set listener for button
        backButton.setOnClickListener(view -> layoutListenerInterface.returnToActivity());

    }
}
