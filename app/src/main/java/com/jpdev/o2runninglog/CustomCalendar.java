package com.jpdev.o2runninglog;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by Jonathan on 2/18/2018.
 */

public class CustomCalendar extends CaldroidFragment {
    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CalendarAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }
    @Override
    protected int getGridViewRes(){
        return R.layout.custom_grid_fragment;
    }

    public CustomCalendar(){
        super();
    }
}
