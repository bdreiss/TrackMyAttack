package com.bdreiss.trackmyattack;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

import com.bdreiss.trackmyattack.listeners.CauseOnClickListener;
import com.bdreiss.trackmyattack.listeners.CustomListener;
import com.bdreiss.trackmyattack.listeners.RemedyOnClickListener;
import com.bdreiss.trackmyattack.listeners.SymptomOnClickListener;

import java.util.Iterator;

public class LayoutListener implements View.OnClickListener {

    Context context;
    Category category;
    int layoutID;
    int linearLayoutID;
    Iterator<String> iterator;
    CustomListener listener;
    FragmentManager fragmentManager;
    LayoutListenerInterface layoutListenerInterface;

    public LayoutListener(Context context, Category category, int layoutID, int linearLayoutID, CustomListener listener, FragmentManager fragmentManager, LayoutListenerInterface layoutListenerInterface){
        this.context = context;
        this.category = category;
        this.layoutID = layoutID;
        this.linearLayoutID = linearLayoutID;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
        this.layoutListenerInterface = layoutListenerInterface;
        iterator = layoutListenerInterface.getData();

    }

    @Override
    public void onClick(View v) {
        setLayout();
    }

    public void setLayout(){
        ((Activity) context).setContentView(layoutID);

        LinearLayout linearLayout = ((Activity) context).findViewById(linearLayoutID);
        while (iterator.hasNext()){
            String item = iterator.next();

            Button itemButton = new Button(context);

            CustomListener listenerCopy = listener.copy();
            itemButton.setText(item);

            itemButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(itemButton);

            listenerCopy.setTextValue(item);
            itemButton.setOnClickListener(listenerCopy);

        }
        Button addButton = new Button(context);

        addButton.setText("+");
        addButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        addButton.setTag("add_key_button");

        linearLayout.addView(addButton);

        addButton.setOnClickListener(view -> {
            AddItemDialog addItemDialog = new AddItemDialog(listener.getData(), category,()-> {iterator = layoutListenerInterface.getData();setLayout();});
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

        Button backButton = new Button(context);

        backButton.setText(context.getResources().getString(R.string.BACK_BUTTON));
        backButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(backButton);

        backButton.setOnClickListener(view -> layoutListenerInterface.returnToActivity());

    }
}
