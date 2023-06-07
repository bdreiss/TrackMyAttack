package com.bdreiss.trackmyattack;

import android.content.Context;
import android.widget.TextView;

import main.java.com.bdreiss.dataAPI.DatumWithIntensity;

public class EditItemView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int TEXT_VIEW_PADDING = 10;
    public static final int INTENSITY_WIDTH = 380;

    public EditItemView(Context context, String text) {
        super(context);
        if (text.length() > 10)
           setWidth(INTENSITY_WIDTH);
        setText(text);
        setClickable(true);
        setPadding(TEXT_VIEW_PADDING,0,TEXT_VIEW_PADDING,0);

    }

    public void resetText(String text){
        setText(text);
    }
}
