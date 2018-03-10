package com.jpdev.o2runninglog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewControllerOnboard extends AppCompatActivity {
    private Button nextButton, skipButton;
    private TextView textViewTitle, textViewDescription;
    private String[] stepTextContent;
    private int stepIdentifier;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller_onboard);
        mSharedPreferences = this.getSharedPreferences("com.jpdev.o2runninglog", Context.MODE_PRIVATE);
        boolean tutorialComplete = mSharedPreferences.getBoolean("com.jpdev.o2runninglog.tutorial", false);

        if(tutorialComplete){
            Log.d("Tutorial", "yep, got here");
            startActivity(new Intent(this, ViewControllerHome.class));
            this.finish();
        }
        else{
            Log.d("Tutorial", "Ugh really?");
        }

        setUIElements();
        setElementsValues();
        setButtons();

    }
    private void setUIElements(){
        nextButton = findViewById(R.id.onboard_button_next);
        skipButton = findViewById(R.id.onboard_button_skip_onboard);
        textViewTitle = findViewById(R.id.onboard_title_text);
        textViewDescription = findViewById(R.id.onboard_description_text);
    }
    private void setStepTextContent(){
        String currentStep = getIntent().getStringExtra("step");
        if(currentStep != null) {
            switch (currentStep) {
                case "1":
                    stepTextContent = getResources().getStringArray(R.array.onboard_home);
                    stepIdentifier = 2;
                    break;
                case "2":
                    stepTextContent = getResources().getStringArray(R.array.onboard_run_form);
                    stepIdentifier = 3;
                    break;
                case "3":
                    stepTextContent = getResources().getStringArray(R.array.onboard_calendar);
                    stepIdentifier = 0;
                    break;
                default:
                    stepTextContent = getResources().getStringArray(R.array.onboard_welcome);
                    stepIdentifier = 1;
            }
        }
        else{
            stepTextContent = getResources().getStringArray(R.array.onboard_welcome);
            stepIdentifier = 1;
        }

    }
    private void setElementsValues(){
        setStepTextContent();
        textViewTitle.setText(stepTextContent[0]);
        textViewDescription.setText(stepTextContent[1]);
    }
    private void setButtons(){
        final ViewControllerOnboard currentActivity = this;
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(currentActivity, ViewControllerHome.class));
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepIdentifier != 0){
                    Intent currentIntent = new Intent(currentActivity, ViewControllerOnboard.class);
                    currentIntent.putExtra("step", Integer.toString(stepIdentifier));
                    startActivity(currentIntent);
                    finish();

                }
                else{
                    setOnboardCompletionStatus();
                    startActivity(new Intent(currentActivity, ViewControllerHome.class));
                    finish();
                }
            }
        });
    }
    private void setOnboardCompletionStatus(){

        editor = mSharedPreferences.edit();
        boolean booleanDistanceUnit = mSharedPreferences.getBoolean("com.jpdev.o2runninglog.tutorial", false);
        editor.putBoolean("com.jpdev.o2runninglog.tutorial", true);
        editor.commit();
    }

}
