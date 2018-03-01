package com.jpdev.o2runninglog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidGridAdapter;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Jonathan on 2/18/2018.
 */

public class CalendarAdapter extends CaldroidGridAdapter {

    final private Context mContext;

    public CalendarAdapter(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData){
        super(context, month, year, caldroidData, extraData);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
        if(convertView == null){
            cellView = inflater.inflate(R.layout.calendar_cell, null);
        }
        ArrayList<ModelRun> runs =(ArrayList<ModelRun>) extraData.get("MONTH_EVENTS");
        TextView textViewDay = cellView.findViewById(R.id.day_label);
        DateTime dateTime = this.datetimeList.get(position);
        textViewDay.setText(Integer.toString(dateTime.getDay()));
        setRunEvent(dateTime, runs, cellView);
        return cellView;
    }

    private void setRunEvent(DateTime calendarDay, ArrayList<ModelRun> runs, View view){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        long date = calendarDay.getMilliseconds(tz);
        String dateMonth =(String)  DateFormat.format("MMM", date);
        String dateDay = (String) DateFormat.format("dd", date);
        for(int i = 0; i < runs.size(); i++){
            ModelRun run = runs.get(i);
            long runDate = run.getDate();
            String runMonth = (String) DateFormat.format("MMM", runDate);
            String runDay = (String) DateFormat.format("dd", runDate);
            setCalendarDay(runMonth,runDay, dateMonth, dateDay, view, run);
        }
    }

    private void setCalendarDay(String runMonth, String runDay, String dateMonth, String dateDay,
                                View cellView, ModelRun run){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat monthFormat = new java.text.SimpleDateFormat("MMM");
        SimpleDateFormat dayFormat = new java.text.SimpleDateFormat("dd");
        String currentMonth, currentDay;
        currentMonth = monthFormat.format(currentTime);
        currentDay = dayFormat.format(currentTime);
        ImageView imageView;
        imageView = cellView.findViewById(R.id.calendar_cell_background);
        if(!(currentDay.equals(dateDay) && currentMonth.equals(dateMonth))) {
            if (runMonth.equals(dateMonth) && runDay.equals(dateDay)) {
                imageView.setImageResource(R.drawable.run_event_background);
                imageView.setScaleX((float)0.7);
                imageView.setScaleY((float)0.7);
                imageView.setVisibility(View.VISIBLE);
                final long currentClock = System.currentTimeMillis();
                setOpenRunForm(run, cellView, currentClock);
            }
        }
        else{
            if(runMonth.equals(dateMonth) && runDay.equals(dateDay)) {
                imageView.setImageResource(R.drawable.active_circle);
                imageView.setScaleX((float) 0.7);
                imageView.setScaleY((float) 0.7);
                imageView.setVisibility(View.VISIBLE);
                ((TextView) cellView.findViewById(R.id.day_label)).setTextColor(Color.parseColor("#FFFFFF"));
                final long currentClock = System.currentTimeMillis();
                setOpenRunForm(run, cellView, currentClock);
            }
            else {
                imageView.setImageResource(R.drawable.active_circle);
                imageView.setScaleX((float) 0.7);
                imageView.setScaleY((float) 0.7);
                imageView.setVisibility(View.VISIBLE);
                ((TextView) cellView.findViewById(R.id.day_label)).setTextColor(Color.parseColor("#FFFFFF"));
                final long currentClock = System.currentTimeMillis();
            }
        }
    }
    private void setOpenRunForm(ModelRun run, View cellView,final long currentClock){
        final ModelRun finalRun = run;
        cellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runFormIntent = new Intent(mContext, ViewControllerRunForm.class);
                runFormIntent.putExtra("run", new Gson().toJson(finalRun));
                runFormIntent.setAction(new Gson().toJson(finalRun));
                mContext.startActivity(runFormIntent);
            }
        });
    }
}
