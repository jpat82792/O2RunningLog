package com.jpdev.o2runninglog;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ViewControllerCalendar extends AppCompatActivity {
    private CaldroidListener calendarListener;
    private ControllerRunFormEntry controllerRunFormEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller_calendar);
        controllerRunFormEntry = new ControllerRunFormEntry(this, this);
        CaldroidFragment caldroidFragment = new CustomCalendar();
        Map<String, Object> extraData = caldroidFragment.getExtraData();
        extraData.put("MONTH_EVENTS", controllerRunFormEntry.getRuns());
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbars));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        ImageView imageViewBackArrow = findViewById(R.id.imageView);

        ControllerNavigation controllerNavigation = new ControllerNavigation(this, imageViewBackArrow, null, ControllerNavigation.NAV_CALENDAR);
        t.commit();
        final java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-mm-dd");
        calendarListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            }
        };
        caldroidFragment.setCaldroidListener(calendarListener);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = super.getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, ViewControllerSettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
