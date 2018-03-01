package com.jpdev.o2runninglog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jonathan on 2/26/2018.
 */

public class ControllerNavigation {
    public static final String NAV_HOME = "home";
    public static final String NAV_CALENDAR = "cal";
    public static final String NAV_RUN_FORM = "run";
    private Activity parentActivity;

    public ControllerNavigation(Activity activity, View calendarButton, View runFormButton, String currentActivity){
        if(NAV_HOME.equals(currentActivity)) {
            setRunFormHome(activity, runFormButton);
            setCalendarHome(activity, calendarButton);
        }
        else if(NAV_CALENDAR.equals(currentActivity)){
            goBack(activity, calendarButton);
        }
        else if(NAV_RUN_FORM.equals(currentActivity)){
            goBack(activity, runFormButton);
            setCalendarButton(activity, calendarButton);
        }
        else{
            goBack(activity,runFormButton);
            setCalendarButton(activity, calendarButton);
        }
        parentActivity = activity;

    }

    private void setCurrentActivityNavigation(){

    }

    private void setRunFormHome(final Activity activity, View runButton){
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivityForResult(new Intent(activity, ViewControllerRunForm.class),1);
            }
        });
    }

    private void setCalendarHome(final Activity activity, View calendarButton){
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Loading calendar", Toast.LENGTH_SHORT).show();
                activity.startActivityForResult(new Intent(activity, ViewControllerCalendar.class), 2);
            }
        });
    }

    private void setRunFormButton(final Activity activity, View runButton){
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ViewControllerRunForm.class));
            }
        });
    }

    private void setCalendarButton(final Activity activity, View calendarButton){
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Loading calendar", Toast.LENGTH_SHORT).show();
                activity.startActivity(new Intent(activity, ViewControllerCalendar.class));
            }
        });
    }

    private void goBack(final Activity activity, View backButton){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setResult(Activity.RESULT_OK, null);
                activity.finish();
            }
        });
    }
}
