package com.jpdev.o2runninglog.CustomUiElements;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Jonathan on 3/18/2018.
 */

public class O2ButtonBold extends AppCompatButton {
    public O2ButtonBold(Context context) {
        super(context);
        setFont();
    }
    public O2ButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public O2ButtonBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }
    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Oxygen-Bold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
