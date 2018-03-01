package com.jpdev.o2runninglog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class ViewControllerSettings extends AppCompatActivity {
    private ToggleButton mToggleButtonDistanceUnits;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller_settings);
        mSharedPreferences = this.getSharedPreferences("com.jpdev.o2runninglog", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        ImageView imageViewBackButton = findViewById(R.id.imageView);
        ImageView calendarButton = findViewById(R.id.calendar_icon_home);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbars));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        mToggleButtonDistanceUnits = findViewById(R.id.toggle_distance_units);
        boolean toggleState = mSharedPreferences.getBoolean("com.jpdev.o2runninglog.distance_units", false);
        mToggleButtonDistanceUnits.setChecked(toggleState);
        ControllerNavigation controllerNavigation = new ControllerNavigation(this, calendarButton, imageViewBackButton, "Settings");
        mToggleButtonDistanceUnits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEditor = mSharedPreferences.edit();
                    mEditor.putBoolean("com.jpdev.o2runninglog.distance_units", true);
                    mEditor.apply();
                } else {
                    mEditor = mSharedPreferences.edit();
                    mEditor.putBoolean("com.jpdev.o2runninglog.distance_units", false);
                    mEditor.apply();
                }
            }
        });
    }
}
