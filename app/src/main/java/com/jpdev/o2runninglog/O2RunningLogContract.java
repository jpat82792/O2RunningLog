package com.jpdev.o2runninglog;

import android.provider.BaseColumns;

/**
 * Created by Jonathan on 2/16/2018.
 */

public final class O2RunningLogContract {
    private O2RunningLogContract(){

    }

    public static class RunsEntry implements BaseColumns{
        public static final String TABLE_NAME = "runs";
        public static final String _ID = "_id";
        public static final String RUN_NAME = "run_name";
        public static final String DISTANCE = "run_distance";
        public static final String DISTANCE_UNITS = "run_distance_unit";
        public static final String TIME = "run_time";
        public static final String RATING = "run_rating";
        public static final String NOTES = "run_notes";
        public static final String DATE = "run_date";

    }


}
