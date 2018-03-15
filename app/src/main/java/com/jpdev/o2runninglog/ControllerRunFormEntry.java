package com.jpdev.o2runninglog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import net.danlew.android.joda.JodaTimeAndroid;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jonathan on 2/16/2018.
 */

public class ControllerRunFormEntry {
    private DatabaseHelperO2RunningLog dbHelper;
    private String[] databaseColumns;
    private ArrayList<ModelRun> arrayListRuns;
    private ViewControllerRunForm mViewControllerRunFormEntry;
    private ViewControllerCalendar mViewControllerCalendar;

    public ControllerRunFormEntry(Context context, ViewControllerRunForm inst){
        dbHelper = new DatabaseHelperO2RunningLog(context);
        mViewControllerRunFormEntry = inst;
        databaseColumns = new String[] {O2RunningLogContract.RunsEntry._ID,
                O2RunningLogContract.RunsEntry.RUN_NAME,
                O2RunningLogContract.RunsEntry.DISTANCE,
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS, O2RunningLogContract.RunsEntry.DATE,
                O2RunningLogContract.RunsEntry.TIME, O2RunningLogContract.RunsEntry.RATING,
                O2RunningLogContract.RunsEntry.NOTES};
        JodaTimeAndroid.init(context);
    }
    public ControllerRunFormEntry(Context context, ViewControllerCalendar inst){
        dbHelper = new DatabaseHelperO2RunningLog(context);
        mViewControllerCalendar = inst;
        databaseColumns = new String[] {O2RunningLogContract.RunsEntry._ID,
                O2RunningLogContract.RunsEntry.RUN_NAME,
                O2RunningLogContract.RunsEntry.DISTANCE,
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS, O2RunningLogContract.RunsEntry.DATE,
                O2RunningLogContract.RunsEntry.TIME, O2RunningLogContract.RunsEntry.RATING,
                O2RunningLogContract.RunsEntry.NOTES};
        JodaTimeAndroid.init(context);
    }

    public ArrayList<ModelRun> getArrayListRuns(){
        return arrayListRuns;
    }

    public void createRun(String runName, double runDistance, String runDistanceUnits,
                          long runTime, long date, int rating, String notes){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(O2RunningLogContract.RunsEntry.RUN_NAME, runName);
        if(runDistance != 0) {
            values.put(O2RunningLogContract.RunsEntry.DISTANCE, runDistance);
        }
        values.put(O2RunningLogContract.RunsEntry.DISTANCE_UNITS, runDistanceUnits);
        if(runTime != 0) {
            values.put(O2RunningLogContract.RunsEntry.TIME, runTime);
        }
        values.put(O2RunningLogContract.RunsEntry.DISTANCE_UNITS, runDistanceUnits);
        values.put(O2RunningLogContract.RunsEntry.DATE, date);
        values.put(O2RunningLogContract.RunsEntry.RATING, rating);
        values.put(O2RunningLogContract.RunsEntry.NOTES, notes);
        long newRowId = db.insert(O2RunningLogContract.RunsEntry.TABLE_NAME, null, values);
    }

    public ArrayList<ModelRun> getRuns(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        arrayListRuns = new ArrayList<>();
        String sortOrder = O2RunningLogContract.RunsEntry.DATE + " ASC";
        Cursor cursor = db.query(O2RunningLogContract.RunsEntry.TABLE_NAME, databaseColumns, null,
                null, null, null, sortOrder);
        while(cursor.moveToNext()){
            addRunToArrayList(cursor);
        }
        cursor.close();
        return getArrayListRuns();
    }

    public ModelRun checkForRun(){
        ModelRun run = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        arrayListRuns = new ArrayList<>();
        long currentTime = java.util.Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTime);
        org.joda.time.DateTime yodadate = new org.joda.time.DateTime(date).withTimeAtStartOfDay();
        org.joda.time.DateTime tomorrow = new org.joda.time.DateTime(date).plusDays(1).withTimeAtStartOfDay();
        long todayTime = yodadate.getMillis();
        long tomorrowTime = tomorrow.getMillis();
        String[] filterArgs = {Long.toString(todayTime), Long.toString(tomorrowTime)};
        String filter = O2RunningLogContract.RunsEntry.DATE +
                " >= ? AND "+O2RunningLogContract.RunsEntry.DATE+" < ?" ;
        Cursor cursor = db.query(O2RunningLogContract.RunsEntry.TABLE_NAME, databaseColumns, filter, filterArgs, null, null, null);
        if(cursor.getCount() == 0){
        }
        else{
            if(run == null) {
                while (cursor.moveToNext()) {
                    int _id = cursor.getInt(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.RUN_NAME));
                    String notes = cursor.getString(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.NOTES));
                    int rating = cursor.getInt(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.RATING));
                    int distance = cursor.getInt(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.DISTANCE));
                    String distanceUnits = cursor.getString(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.DISTANCE_UNITS));
                    long time = cursor.getLong(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.TIME));
                    long dateFrom = cursor.getLong(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.DATE));
                    run = new ModelRun(name, distance, distanceUnits, time, rating, dateFrom, notes, _id);
                }
                cursor.close();
            }
            else{
            }
        }
        return run;
    }

    public void updateRun(String runName, double runDistance, String runDistanceUnits,
                           long runTime, int rating, String notes, ModelRun id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(O2RunningLogContract.RunsEntry.RUN_NAME, runName);
        if(runDistance != 0) {
            values.put(O2RunningLogContract.RunsEntry.DISTANCE, runDistance);
        }
        values.put(O2RunningLogContract.RunsEntry.DISTANCE_UNITS, runDistanceUnits);
        if(runTime != 0) {
            values.put(O2RunningLogContract.RunsEntry.TIME, runTime);
        }
        values.put(O2RunningLogContract.RunsEntry.DATE, id.getDate());
        values.put(O2RunningLogContract.RunsEntry.RATING, rating);
        values.put(O2RunningLogContract.RunsEntry.NOTES, notes);
        db.update(O2RunningLogContract.RunsEntry.TABLE_NAME, values, "_id = ?",
                new String[]{Integer.toString(id.getId())});

    }

    private void addRunToArrayList(Cursor cursor){
        String runName = cursor.getString(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.RUN_NAME));
        int runId = cursor.getInt(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry._ID));
        long runDistance = cursor.getLong(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.DISTANCE));
        String runDistanceUnits = cursor.getString(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.DISTANCE_UNITS));
        long runTime = cursor.getLong(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.TIME));
        int runRating = cursor.getInt(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.RATING));
        String runNotes = cursor.getString(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.NOTES));
        long runDate = cursor.getLong(cursor.getColumnIndexOrThrow(O2RunningLogContract.RunsEntry.DATE));
        arrayListRuns.add(new ModelRun(runName, runDistance, runDistanceUnits, runTime,runRating,
                runDate, runNotes, runId ));
    }

    public DatabaseHelperO2RunningLog getDbHelper(){
        return dbHelper;
    }

}
