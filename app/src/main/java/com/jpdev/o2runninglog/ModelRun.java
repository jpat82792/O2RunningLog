package com.jpdev.o2runninglog;

/**
 * Created by Jonathan on 2/20/2018.
 */

public class ModelRun {
    private String mName, mNotes, mDistanceUnits;
    private long mDate, mTime;
    private float mDistance;
    private int mRating;
    private int mId;
    public ModelRun(String name, float distance, String distanceUnits, long time, int rating,
                    long date, String notes){
        setUniversalProperties(name, distance, distanceUnits, time, rating, date, notes);
    }
    public ModelRun(String name, float distance, String distanceUnits, long time, int rating,
                    long date, String notes, int id){
        setUniversalProperties(name, distance, distanceUnits, time, rating, date, notes);
        setId(id);
    }
    private void setUniversalProperties(String name, float distance, String distanceUnits, long time, int rating,
                                        long date, String notes){
        setName(name);
        setTime(time);
        setNotes(notes);
        setDate(date);
        setRating(rating);
        setDistance(distance);
        setDistanceUnits(distanceUnits);
    }
    private void setId(int id){
        mId = id;
    }
    private void setRating(int rating){
        mRating = rating;
    }
    private void setDate(long date){
        mDate = date;
    }
    private void setName(String name){
        mName = name;
    }
    private void setTime(long time){
        mTime = time;
    }
    private void setNotes(String notes){
        mNotes = notes;
    }
    private void setDistance(float distance){
        mDistance = distance;
    }
    private void setDistanceUnits(String distanceUnits){
        mDistanceUnits = distanceUnits;
    }
    public int getId(){return mId;}
    public int getRating(){
        return mRating;
    }
    public String getName(){
        return mName;
    }
    public long getTime(){
        return mTime;
    }
    public String getNotes(){
        return mNotes;
    }
    public float getDistance(){
        return mDistance;
    }
    public long getDate(){
        return mDate;
    }
    public String getDistanceUnits(){
        return mDistanceUnits;
    }
}
