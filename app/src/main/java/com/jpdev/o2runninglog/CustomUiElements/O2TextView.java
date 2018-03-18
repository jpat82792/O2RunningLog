package com.jpdev.o2runninglog.CustomUiElements;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jonathan on 3/18/2018.
 */

public class O2TextView extends AppCompatTextView {
    public O2TextView(Context context) {
        super(context);
        setFont();
    }
    public O2TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public O2TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Oxygen-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
