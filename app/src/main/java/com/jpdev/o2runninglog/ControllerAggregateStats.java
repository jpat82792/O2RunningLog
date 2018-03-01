package com.jpdev.o2runninglog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.TimeZone;

/**
 * Created by Jonathan on 2/23/2018.
 */

public class ControllerAggregateStats {
    private DatabaseHelperO2RunningLog dbHelper;
    public static final String TREND_DIRECTION_UP = "up";
    public static final String TREND_DIRECTION_EVEN = "even";
    public static final String TREND_DIRECTION_DOWN = "down";
    public ControllerAggregateStats(Context context){
        dbHelper = new DatabaseHelperO2RunningLog(context);
    }

     double previousMonthStart(){
        DateTime previousMonth = new DateTime().minusMonths(1).withDayOfMonth(1);
        return previousMonth.getMillis();
    }

     double previousMonthEnd(){
        DateTime previousMonthEnd = new DateTime().minusMonths(1);
        previousMonthEnd = previousMonthEnd.dayOfMonth().withMaximumValue();
        return previousMonthEnd.getMillis();
    }

    private int getDaysOfMonth(DateTime month){
        return month.dayOfMonth().getMaximumValue();
    }

    private double previousWeekStart(){
        DateTime previousWeekStart = new DateTime().minusWeeks(1);
        previousWeekStart = previousWeekStart.dayOfWeek().withMinimumValue();
        return previousWeekStart.getMillis();
    }

    private double previousWeekEnd(){
        DateTime previousWeekEnd = new DateTime().minusWeeks(1);
        previousWeekEnd = previousWeekEnd.dayOfWeek().withMinimumValue();
        return previousWeekEnd.getMillis();
    }

    private double getCurrentMonthStart(){
        DateTime currentMonthStart = new DateTime().withDayOfMonth(1);
        return currentMonthStart.getMillis();
    }

    private double getCurrentMonthEnd(){
        DateTime currentMonthEnd = new DateTime().dayOfMonth().withMaximumValue();
        return currentMonthEnd.getMillis();
    }

    private double getMonthTotalMileage(double firstDay, double lastDay, String distanceUnit){
        double miles, kilometers, total;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        total = 0;
        miles=0;
        kilometers = 0;
        Cursor cursorMiles = db.rawQuery("SELECT TOTAL("+
                O2RunningLogContract.RunsEntry.DISTANCE+") AS total FROM "+
                O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'mi' AND "+
                O2RunningLogContract.RunsEntry.DATE + ">= "+Double.toString(firstDay)+" AND "+
                O2RunningLogContract.RunsEntry.DATE+" < "+Double.toString(lastDay)+"",
                null);

        Cursor cursorKilo = db.rawQuery("SELECT TOTAL("+ O2RunningLogContract.RunsEntry.DISTANCE
                +") AS total FROM "+ O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'km'", null);

        while(cursorMiles.moveToNext()){
            miles = cursorMiles.getDouble(cursorMiles.getColumnIndexOrThrow("total"));
        }
        while(cursorKilo.moveToNext()){
            kilometers = cursorKilo.getDouble(cursorKilo.getColumnIndexOrThrow("total"));
        }
        if(distanceUnit.equals("mi")) {
            kilometers = convertKmToMi(kilometers);
        }
        else{
            miles = convertMiToKm(miles);
        }
        total = miles + kilometers;
        return total;
    }
    private int dayDifferenceMonths(){
        DateTime previousMonth = new DateTime().minusMonths(1);
        DateTime currentMonth = new DateTime();
        int daysInCurrentMonth = currentMonth.getDayOfMonth();
        int daysInPreviousMonth = getDaysOfMonth(previousMonth);
        int dayDifference = daysInPreviousMonth - daysInCurrentMonth;
        return dayDifference;
    }

    public String getMonthTrendRate(String distanceUnit){
        double previousMonthTotal, currentMonthTotal;
        DateTime previousMonth = new DateTime().minusMonths(1);
        DateTime currentMonth = new DateTime();
        previousMonthTotal = getMonthTotalMileage(previousMonthStart(), previousMonthEnd(), distanceUnit);
        currentMonthTotal = getMonthTotalMileage(getCurrentMonthStart(), getCurrentMonthEnd(), distanceUnit);
        double dailyRate = currentMonthTotal/ currentMonth.getDayOfMonth();
        int dayDifference = dayDifferenceMonths();
        double previousMonthDailyRate = previousMonthTotal/ getDaysOfMonth(previousMonth);
        double weightedTotal = previousMonthTotal + (dayDifference * previousMonthDailyRate);
        weightedTotal = setDayLimitedWeightedTotal(currentMonth, weightedTotal,
                previousMonthDailyRate);
        double weightedRate = weightedTotal / getDaysOfMonth(currentMonth);
        String rate = compareRates(weightedRate, dailyRate);
        return rate;
    }

    public String compareRates(double previousRate, double weightedRate){
        double rateDifference = weightedRate - previousRate;
        String rate;
        if(rateDifference > 1){
            rate = TREND_DIRECTION_UP;
        }
        else if(rateDifference <= 1 && rateDifference >= -1){
            rate = TREND_DIRECTION_EVEN;
        }
        else{
            rate = TREND_DIRECTION_DOWN;
        }
        return rate;
    }

    public double setDayLimitedWeightedTotal(DateTime currentMonth, double weightedTotal, double previousDaily){
        int daysOfMonth = getDaysOfMonth(currentMonth);
        int currentDay = currentMonth.getDayOfMonth();
        return (weightedTotal - ((daysOfMonth - currentDay)*previousDaily));
    }



    public double getTotalMileage(String distanceUnit){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double miles, kilometers, total;
        total = 0;
        miles=0;
        kilometers = 0;
        Cursor cursorMiles = db.rawQuery("SELECT TOTAL("+
                O2RunningLogContract.RunsEntry.DISTANCE+") AS total FROM "+
                O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'mi'", null);

        Cursor cursorKilo = db.rawQuery("SELECT TOTAL("+ O2RunningLogContract.RunsEntry.DISTANCE
                +") AS total FROM "+ O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'km'", null);

        while(cursorMiles.moveToNext()){
            miles = cursorMiles.getDouble(cursorMiles.getColumnIndexOrThrow("total"));
        }
        while(cursorKilo.moveToNext()){
            kilometers = cursorKilo.getDouble(cursorKilo.getColumnIndexOrThrow("total"));
        }

        if(distanceUnit.equals("mi")) {
            //TODO: Add function to convert kilometers to miles
            kilometers = convertKmToMi(kilometers);
        }
        else{
            //TODO: Add function to convert miles to kilometers
            //parseDistances(cursorMiles, cursorKilo, miles, kilometers);
            miles = convertMiToKm(miles);
        }
        total = miles + kilometers;
        return total;
    }
    public double convertKmToMi(double km){
        double miles = km/1.609344;
        return miles;
    }
    public double convertMiToKm(double mi){
        double kilometers = mi*1.609344;
        return kilometers;
    }
    public void parseDistances(Cursor cursorMiles, Cursor cursorKilo, double miles,
                               double kilometers){
        while(cursorMiles.moveToNext()){
            miles = cursorMiles.getDouble(cursorMiles.getColumnIndexOrThrow("total"));
        }
        while(cursorKilo.moveToNext()){
            kilometers = cursorKilo.getDouble(cursorKilo.getColumnIndexOrThrow("total"));
        }
    }

    public double getMonthlyMileage(String distanceUnit){
        DateTime startMonth = new DateTime();
        long startDay = startMonth.monthOfYear().withMinimumValue().getMillis();
        DateTime endMonth = new DateTime();
        long endDay = startMonth.dayOfMonth().withMaximumValue().getMillis();
        double weekMileTotal, weekKilometerTotal;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double weekTotal = 0;
        weekMileTotal = 0;
        weekKilometerTotal = 0;
        Cursor cursorWeekMileTotal = db.rawQuery("SELECT TOTAL("+
                        O2RunningLogContract.RunsEntry.DISTANCE+") AS total, "+
                        O2RunningLogContract.RunsEntry.DATE+","+ O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" FROM "+
                        O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                        O2RunningLogContract.RunsEntry.DATE +" >= "+Long.toString(startDay)+" AND "+
                        O2RunningLogContract.RunsEntry.DATE+" < "+Long.toString(endDay)+" AND "+
                        O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'mi'",
                null);
        Cursor cursorWeekKilometerTotal = db.rawQuery("SELECT TOTAL("+
                O2RunningLogContract.RunsEntry.DISTANCE+") AS total, "+
                O2RunningLogContract.RunsEntry.DATE+","+ O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" FROM "+
                O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                O2RunningLogContract.RunsEntry.DATE +" >= "+Long.toString(startDay)+" AND "+
                O2RunningLogContract.RunsEntry.DATE+" < "+Long.toString(endDay)+" AND "+
                O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'km'",null);
        while(cursorWeekMileTotal.moveToNext()){
            weekMileTotal = cursorWeekMileTotal.getDouble(cursorWeekMileTotal.getColumnIndexOrThrow("total"));
        }
        while(cursorWeekKilometerTotal.moveToNext()){
            weekKilometerTotal = cursorWeekKilometerTotal.getDouble(cursorWeekKilometerTotal.getColumnIndexOrThrow("total"));
        }
        if(distanceUnit.equals("mi")) {
            weekKilometerTotal = convertKmToMi(weekKilometerTotal);
        }
        else{
            weekMileTotal = convertMiToKm(weekMileTotal);
        }
        weekTotal = (weekKilometerTotal + weekMileTotal);
        return weekTotal;

    }

    public double getWeeklyMileage(String distanceUnit){
        LocalDate today = new LocalDate();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        LocalDate weekStart =today.dayOfWeek().withMinimumValue();
        LocalDate weekEnd = today.dayOfWeek().withMaximumValue();
        TimeZone tz = TimeZone.getDefault();
        //DateTimeZone datetz = DateTimeZone.forID(tz.toString());
        LocalTime localTime = new LocalTime();
        double weekMileTotal = 0;
        double weekKilometerTotal = 0;
        double weekTotal = 0;
        long weekEndSeconds = weekEnd.toDateTime(localTime).getMillis();
        long weekStartSeconds = weekStart.toDateTime(localTime).getMillis();
        Cursor cursorWeekMile = db.rawQuery("SELECT TOTAL("+
                        O2RunningLogContract.RunsEntry.DISTANCE+") AS total, "+
                        O2RunningLogContract.RunsEntry.DATE+","+ O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" FROM "+
                        O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                        O2RunningLogContract.RunsEntry.DATE +" >= "+Long.toString(weekStartSeconds)+" AND "+
                        O2RunningLogContract.RunsEntry.DATE+" < "+Long.toString(weekEndSeconds)+" AND "+
                        O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'mi'",
                null);
        Cursor cursorWeekKilometer = db.rawQuery("SELECT TOTAL("+
                        O2RunningLogContract.RunsEntry.DISTANCE+") AS total, "+
                        O2RunningLogContract.RunsEntry.DATE+","+ O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" FROM "+
                        O2RunningLogContract.RunsEntry.TABLE_NAME+" WHERE "+
                        O2RunningLogContract.RunsEntry.DATE +" >= "+Long.toString(weekStartSeconds)+" AND "+
                        O2RunningLogContract.RunsEntry.DATE+" < "+Long.toString(weekEndSeconds)+" AND "+
                        O2RunningLogContract.RunsEntry.DISTANCE_UNITS+" = 'km'",
                null);
        while(cursorWeekMile.moveToNext()){
            weekMileTotal = cursorWeekMile.getDouble(cursorWeekMile.getColumnIndexOrThrow("total"));
        }
        while(cursorWeekKilometer.moveToNext()){
            weekKilometerTotal = cursorWeekKilometer.getDouble(cursorWeekKilometer.getColumnIndexOrThrow("total"));
        }
        if(distanceUnit.equals("mi")) {
            weekKilometerTotal = convertKmToMi(weekKilometerTotal);
        }
        else{
            weekMileTotal = convertMiToKm(weekMileTotal);
        }
        weekTotal = (weekMileTotal + weekKilometerTotal);
        return weekTotal;
    }
}
