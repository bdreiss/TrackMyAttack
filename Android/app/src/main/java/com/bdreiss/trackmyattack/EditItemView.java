package com.bdreiss.trackmyattack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

@SuppressLint("ViewConstructor")
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
        setGravity(Gravity.CENTER_VERTICAL);

    }

    public void resetText(String text){
        setText(text);
    }

    public static TextView getSeparationLine(Context context){
        TextView separationLineView = new TextView(context);
        separationLineView.setGravity(Gravity.CENTER);
        separationLineView.setHeight(70);
        StringBuilder separationLine = new StringBuilder();

        for (int i=0;i<100;i++)
            separationLine.append("-");

        separationLineView.setText(separationLine.toString());

        return separationLineView;
    }
}
