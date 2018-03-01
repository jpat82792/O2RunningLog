package com.jpdev.o2runninglog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Jonathan on 2/23/2018.
 */

public class QuoteWidgetController {
    private String[] quotes;
    private View mTargetView;
    public QuoteWidgetController(Context context, View targetView){
        quotes = context.getResources().getStringArray(R.array.motivational_quotes);
        mTargetView = targetView;
        setTargetView();
    }

    private String getRandomQuote(){
        Random rand = new Random();
        int index = rand.nextInt(quotes.length-1);
        return quotes[index];
    }

    public void setTargetView(){
        TextView textView = (TextView)mTargetView;
        textView.setText(getRandomQuote());
    }
}
