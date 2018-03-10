package com.jpdev.o2runninglog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ViewControllerHome extends AppCompatActivity {
    private QuoteWidgetController mQuoteWidgetController;
    private ControllerAggregateStats mControllerAggregateStats;
    private ImageView imageViewTrendArrow;
    private TextView textViewWeekMonthAmountLabel, textViewAllTimeLabel, textViewWeekMonthLabel;
    private Button buttonEnterRun, buttonWeekTotal, buttonMonthTotal;
    private Context mContext;
    private String distanceUnit;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller_home);
        TextView textViewQuote = findViewById(R.id.textView7);
        buttonWeekTotal = findViewById(R.id.week_total_button);
        buttonMonthTotal = findViewById(R.id.month_total_button);
        textViewWeekMonthLabel = findViewById(R.id.textView2);
        mSharedPreferences = this.getSharedPreferences("com.jpdev.o2runninglog", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        boolean booleanDistanceUnit = mSharedPreferences.getBoolean("com.jpdev.o2runninglog.distance_units", false);
        setDistanceUnit(booleanDistanceUnit);
        TextView textViewTotalMileage = findViewById(R.id.total_mileage);
        textViewWeekMonthAmountLabel = findViewById(R.id.month_week_mileage);
        textViewAllTimeLabel = findViewById(R.id.textView8);
        mQuoteWidgetController = new QuoteWidgetController(this, textViewQuote);
        mControllerAggregateStats = new ControllerAggregateStats(this);
        imageViewTrendArrow = findViewById(R.id.imageViewTrendArrow);
        ImageView imageViewCalendar = findViewById(R.id.calendar_icon_home);
        String currentPeriodSelector = mSharedPreferences.getString("com.jpdev.o2runninglog.period_selector", "week");
        if(currentPeriodSelector.equals("week")){
            setMileageTextView(mControllerAggregateStats.getWeeklyMileage(distanceUnit),textViewWeekMonthAmountLabel);
            buttonWeekTotal.setTypeface(null, Typeface.BOLD);
        }
        else{
            buttonMonthTotal.setTypeface(null, Typeface.BOLD);
            setMileageTextView(mControllerAggregateStats.getMonthlyMileage(distanceUnit),textViewWeekMonthAmountLabel);
        }

        setMileageTextView(mControllerAggregateStats.getTotalMileage(distanceUnit), textViewTotalMileage);
        buttonEnterRun = findViewById(R.id.enter_run_button);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbars));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        mContext = this;
        setTrendArrowRotation();
        setMileageMeasurementLabels(distanceUnit);
        ControllerNavigation controllerNavigation = new ControllerNavigation(this, imageViewCalendar, buttonEnterRun, ControllerNavigation.NAV_HOME);

        if(mSharedPreferences.getString("com.jpdev.o2runninglog.time_period", "month").equals("month")){
            getMonthTotal();
        }
        else{
            getWeekTotal();
        }

        buttonWeekTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonWeekTotal.setTypeface(null, Typeface.BOLD);
                buttonMonthTotal.setTypeface(null, Typeface.NORMAL);
                getWeekTotal();
                setTimePeriod("week");
                editor.putString("com.jpdev.o2runninglog.period_selector", "week");
                editor.apply();
            }
        });
        buttonMonthTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonMonthTotal.setTypeface(null, Typeface.BOLD);
                buttonWeekTotal.setTypeface(null, Typeface.NORMAL);
                getMonthTotal();
                setTimePeriod("month");
                editor.putString("com.jpdev.o2runninglog.period_selector", "month");
                editor.apply();
            }
        });
    }

    private void setDistanceUnit(boolean unit){
        if(unit){
            distanceUnit = "km";
        }
        else{
            distanceUnit="mi";
        }
    }

    private void setTimePeriod(String arg){
        if(arg.equals("month")){
            editor.putString("com.jpdev.o2runninglog.time_period","month");
            editor.apply();
        }
        else{
            editor.putString("com.jpdev.o2runninglog.time_period","week");
            editor.apply();
        }
    }

    private void getWeekTotal(){
        setMileageTextView(mControllerAggregateStats.getWeeklyMileage(distanceUnit),textViewWeekMonthAmountLabel);
    }

    private void getMonthTotal(){
        setMileageTextView(mControllerAggregateStats.getMonthlyMileage(distanceUnit),textViewWeekMonthAmountLabel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            Intent refresh = new Intent(this, ViewControllerHome.class);
            startActivity(refresh);
            this.finish();
        }

    }

    private void setMileageMeasurementLabels(String distanceUnit){
        textViewAllTimeLabel.setText(distanceUnit);
        textViewWeekMonthLabel.setText(distanceUnit);
    }

    private void setTrendArrowRotation(){
        String rate = mControllerAggregateStats.getMonthTrendRate(distanceUnit);
        switch(rate){
            case "up":
                rotateTrendArrow(-81);
                break;
            case "down":
                rotateTrendArrow(0);
                break;
            default:
                rotateTrendArrow(-41);
        }
    }

    private void rotateTrendArrow(int degrees){
        imageViewTrendArrow.setRotation(degrees);
    }

    private void setMileageTextView(double value, TextView textView){
        BigDecimal temp = new BigDecimal(value);
        BigDecimal temp1 = temp.setScale(2, RoundingMode.HALF_UP);
        textView.setText(temp1.toString());
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = super.getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.settings:
                startActivityForResult(new Intent(this, ViewControllerSettings.class), 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
