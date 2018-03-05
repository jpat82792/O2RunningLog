package com.jpdev.o2runninglog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationUtil;

//TODO: add way to add run from previous date

public class ViewControllerRunForm extends AppCompatActivity {
    private Button buttonSubmit;
    private ControllerRunFormEntry mControllerRunFormEntry;
    private ArrayList<EditText> editTexts;
    private EditText editTextName, editTextDistance,  editTextRating, editTextNotes;
    private ModelRun mModelRun;
    private TimeDurationPicker timePickerTime;
    private boolean notCalendar, showPicker, pastNoRecord;
    private int mId;
    private ControllerRatingWidget mControllerRatingWidget;
    private Spinner unitSpinner;
    private Button buttonShowPicker;
    private String stringDay, stringMonth, stringYear;
    private boolean existing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller_run_form);
        mModelRun = null;
        pastNoRecord = false;
        View calendarButton = findViewById(R.id.calendar_icon_home);
        View goBackButton = findViewById(R.id.imageView);
        LinearLayout starContainer = findViewById(R.id.rating_widget_container);
        EditText starWidgetValue = findViewById(R.id.rating_widget_value);
        mControllerRatingWidget = new ControllerRatingWidget(starContainer, this, starWidgetValue);
        ControllerNavigation controllerNavigation = new ControllerNavigation(this,
                calendarButton,goBackButton, ControllerNavigation.NAV_RUN_FORM);
        notCalendar = true;
        Bundle extras = getIntent().getExtras();
        buttonSubmit = (Button) findViewById(R.id.run_form_submit);
        editTexts = new ArrayList<EditText>();
        initializeEditTexts(extras);
        mControllerRunFormEntry = new ControllerRunFormEntry(this, this);
        setSupportActionBar((android.support.v7.widget.Toolbar)findViewById(R.id.my_toolbars));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        existing = false;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        if(mModelRun == null) {
            //TODO:checkForRun needs to check bundle date if one exists
            if(extras != null) {
                if (extras.get("month") != null) {
                    existing = true;
                } else {
                    mModelRun = mControllerRunFormEntry.checkForRun();
                }
            }
            else{
                mModelRun = mControllerRunFormEntry.checkForRun();
            }
        }
        if(mModelRun != null){
            setEditTextContent(editTextName, editTextDistance, unitSpinner, timePickerTime, editTextRating, editTextNotes, mModelRun);
            existing = true;
        }
        setSpinner();
        buttonSubmit.setOnClickListener(setButtonSubmitListener(existing, pastNoRecord));

        showPicker = true;
        buttonShowPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showPicker) {
                    timePickerTime = findViewById(R.id.run_time_edittext);
                    timePickerTime.setVisibility(View.VISIBLE);
                    timePickerTime.setFocusable(true);
                    timePickerTime.setFocusableInTouchMode(true);
                    timePickerTime.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    showPicker = false;
                    buttonShowPicker.setText("Close");
                }
                else{
                    timePickerTime = findViewById(R.id.run_time_edittext);
                    timePickerTime.setVisibility(View.GONE);
                    timePickerTime.setFocusable(false);
                    showPicker = true;
                    buttonShowPicker.setText(TimeDurationUtil.formatHoursMinutesSeconds(timePickerTime.getDuration()));
                }
            }
        });
    }

    private void setSpinner(){
        unitSpinner = findViewById(R.id.distance_unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distance_options, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
    }

    public void setModelRun(ModelRun run){
        mModelRun = run;
    }

    public ModelRun getModelRun(){
        return mModelRun;
    }

    public int getId(){
        return mId;
    }
    //To add runs from past.
    private void setPreviousRun(){
        int year = Integer.parseInt(stringYear);
        int month = Integer.parseInt(stringMonth)-1;
        int day = Integer.parseInt(stringDay);
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(year, month, day, 0,0);
        long dateRun = calendarDate.getTimeInMillis();
        if(!editTextDistance.getText().toString().equals("")) {
            mControllerRunFormEntry.createRun(editTextName.getText().toString(),
                    Integer.parseInt(editTextDistance.getText().toString()),
                    "mi",
                    (long) timePickerTime.getDuration(), dateRun,
                    Integer.parseInt(editTextRating.getText().toString()),
                    editTextNotes.getText().toString());
            Toast.makeText(ViewControllerRunForm.this, "Run saved no prev", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ViewControllerRunForm.this, "Please add a distance no prev", Toast.LENGTH_SHORT).show();
        }
    }
    private void updatePreviousRun(){
        if (!editTextDistance.getText().toString().equals("")) {
            mControllerRunFormEntry.updateRun(editTextName.getText().toString(),
                    Integer.parseInt(editTextDistance.getText().toString()), unitSpinner.getSelectedItem().toString(),
                    (long) timePickerTime.getDuration(), Integer.parseInt(editTextRating.getText().toString()),
                    editTextNotes.getText().toString(), mModelRun);
            Toast.makeText(ViewControllerRunForm.this, "Run updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ViewControllerRunForm.this, "Please add a distance", Toast.LENGTH_SHORT).show();
        }
    }

    private void setNewRun(){
        Date today = new Date();
        long currentTime;
        currentTime = today.getTime();
        if(!editTextDistance.getText().toString().equals("")) {
            mControllerRunFormEntry.createRun(editTextName.getText().toString(),
                    Integer.parseInt(editTextDistance.getText().toString()),
                    "mi",
                    (long) timePickerTime.getDuration(), currentTime,
                    Integer.parseInt(editTextRating.getText().toString()),
                    editTextNotes.getText().toString());
            Toast.makeText(ViewControllerRunForm.this, "Run saved", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ViewControllerRunForm.this, "Please add a distance", Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener setButtonSubmitListener(boolean exists, boolean pastRecord){
        if(exists){
            if(pastRecord){
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setPreviousRun();
                    }
                };
            }
            else {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatePreviousRun();
                    }
                };
            }
        }
        else{
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNewRun();
                }
            };
        }
    }

    public void setId(int id){
        mId = id;
    }

    private void setTimeStrings(String day, String month, String year){
        stringDay = day;
        stringMonth = month;
        stringYear = year;
    }

    private void initializeEditTexts(Bundle bundle){
        editTextName = (EditText) findViewById(R.id.run_name_edittext);
        editTextDistance = (EditText) findViewById(R.id.run_distance_edittext);
        timePickerTime = (TimeDurationPicker) findViewById(R.id.run_time_edittext);
        timePickerTime.setDuration(0);
        editTextRating = (EditText) findViewById(R.id.rating_widget_value);
        editTextRating.setText("0");
        editTextNotes = (EditText) findViewById(R.id.run_notes_edittext);
        setSpinner();
        buttonShowPicker = findViewById(R.id.show_time_picker);
        if(bundle != null){
            if(bundle.get("month") == null) {
                String runJson;
                runJson = bundle.getString("run");
                ModelRun run = new Gson().fromJson(runJson, ModelRun.class);
                setEditTextContent(editTextName, editTextDistance, unitSpinner, timePickerTime, editTextRating,
                        editTextNotes, run);
                setId(run.getId());
                setModelRun(run);
            }
            else{
                setTimeStrings(bundle.get("day").toString(), bundle.get("month").toString(),bundle.get("year").toString());
                pastNoRecord = true;
            }
            notCalendar = false;
        }
    }
    private void setEditTextContent(EditText editTextName,EditText editTextDistance,Spinner spinnerDistanceUnit,
                                    TimeDurationPicker editTextTime,EditText editTextRating,
                                    EditText editTextNotes, ModelRun run){
        editTextName.setText(run.getName());
        editTextDistance.setText(Long.toString(run.getDistance()));
        editTextTime.setDuration(run.getTime());
        editTextRating.setText(Integer.toString(run.getRating()));
        editTextNotes.setText(run.getNotes());
        Adapter adapter = spinnerDistanceUnit.getAdapter();
        for (int i = 0; i < spinnerDistanceUnit.getAdapter().getCount(); i++){
            if(adapter.getItem(i).equals(spinnerDistanceUnit)){
                spinnerDistanceUnit.setSelection(i);
            }
        }
        buttonShowPicker.setText(TimeDurationUtil.formatHoursMinutesSeconds(editTextTime.getDuration()));
        mControllerRatingWidget.setStarRatingValue(run.getRating());
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

    public void onDestroy(){
        mControllerRunFormEntry.getDbHelper().close();
        super.onDestroy();
    }
}
