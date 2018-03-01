package com.jpdev.o2runninglog;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Jonathan on 2/26/2018.
 */

public class ControllerRatingWidget {
    private Activity mCurrentActivity;
    private EditText editTextRatingValue;
    private LinearLayout mLinearLayoutStarContainer;
    public ControllerRatingWidget(LinearLayout linearLayoutStarContainer, Activity currentActivity, EditText ratingValue){
        mCurrentActivity = currentActivity;
        editTextRatingValue = ratingValue;
        mLinearLayoutStarContainer = linearLayoutStarContainer;
        initRatingWidget(mLinearLayoutStarContainer);
    }

    private void initRatingWidget(final LinearLayout linearLayoutStarContainer){
        int starCount = linearLayoutStarContainer.getChildCount();
        for(int i = 0; i <starCount;i++){
            View star = linearLayoutStarContainer.getChildAt(i);
            final int temp = i;
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectStar(temp, linearLayoutStarContainer);
                }
            });
        }
    }

    public void setStarRatingValue(int rating){
        if(rating > 5){
            rating = 5;
        }
        int sdk = android.os.Build.VERSION.SDK_INT;
        editTextRatingValue.setText(Integer.toString(rating));
        for(int i = 0; i < mLinearLayoutStarContainer.getChildCount(); i++){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mLinearLayoutStarContainer.getChildAt(i).setBackgroundDrawable(mCurrentActivity.getResources().getDrawable(R.drawable.inactive_star));
            } else {
                mLinearLayoutStarContainer.getChildAt(i).setBackground(mCurrentActivity.getResources().getDrawable(R.drawable.inactive_star));
            }
        }
        for(int i=0; i < rating; i++){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mLinearLayoutStarContainer.getChildAt(i).setBackgroundDrawable(mCurrentActivity.getResources().getDrawable(R.drawable.active_star));
            } else {
                mLinearLayoutStarContainer.getChildAt(i).setBackground(mCurrentActivity.getResources().getDrawable(R.drawable.active_star));
            }
        }
    }

    private void selectStar(int index, LinearLayout starContainer){
        setRatingValue(index);
        resetStarColors(index, starContainer);
    }
    private void setRatingValue(int rating){
        editTextRatingValue.setText(Integer.toString(rating+1));
    }

    private void resetStarColors(int index, LinearLayout starContainer){
        int sdk = android.os.Build.VERSION.SDK_INT;

        for(int i = 0; i < starContainer.getChildCount();i++){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                starContainer.getChildAt(i).setBackgroundDrawable(mCurrentActivity.getResources().getDrawable(R.drawable.inactive_star));
            } else {
                starContainer.getChildAt(i).setBackground(mCurrentActivity.getResources().getDrawable(R.drawable.inactive_star));
            }
        }
        for(int i = 0; i <= index; i++){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                starContainer.getChildAt(i).setBackgroundDrawable(mCurrentActivity.getResources().getDrawable(R.drawable.active_star));
            } else {
                starContainer.getChildAt(i).setBackground(mCurrentActivity.getResources().getDrawable(R.drawable.active_star));
            }
        }
    }

}
