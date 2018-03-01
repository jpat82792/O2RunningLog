package com.jpdev.o2runninglog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jonathan on 2/16/2018.
 */

public class DatabaseHelperO2RunningLog extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 13;
    public static final String DATABASE_NAME = "O2RunningLog";
    private static final String SQL_CREATE_RUNS = "CREATE TABLE "+ O2RunningLogContract.RunsEntry.TABLE_NAME +
            " ("+ O2RunningLogContract.RunsEntry._ID+" INTEGER PRIMARY KEY,"+
            O2RunningLogContract.RunsEntry.RUN_NAME+" TEXT DEFAULT NULL,"+
            O2RunningLogContract.RunsEntry.DISTANCE+" INTEGER NOT NULL,"+
            O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" TEXT,"+
            O2RunningLogContract.RunsEntry.TIME+" LONG DEFAULT NULL,"+
            O2RunningLogContract.RunsEntry.RATING+" INTEGER DEFAULT NULL,"+
            O2RunningLogContract.RunsEntry.DATE+" LONG,"+
            O2RunningLogContract.RunsEntry.NOTES+" TEXT DEFAULT NULL"+" )";
    private static final String SQL_DELETE_RUNS = "DROP TABLE IF EXISTS "+O2RunningLogContract.RunsEntry.TABLE_NAME;
    public DatabaseHelperO2RunningLog(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_RUNS);
    }

    public void  onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_RUNS);
        onCreate(db);
    }
}
