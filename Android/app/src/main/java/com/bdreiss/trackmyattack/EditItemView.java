package com.bdreiss.trackmyattack;

import android.content.Context;
import android.widget.TextView;

import main.java.com.bdreiss.dataAPI.DatumWithIntensity;

public class EditItemView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int TEXT_VIEW_PADDING = 5;

    public EditItemView(Context context, String text) {
        super(context);
        setText(text);
        setClickable(true);
        setPadding(TEXT_VIEW_PADDING,0,TEXT_VIEW_PADDING,0);
    }

    public void resetText(String text){
        setText(text);
    }
}
