package com.jpdev.o2runninglog;

import android.database.Cursor;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jonathan on 2/22/2018.
 */
@RunWith(RobolectricTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DatabaseTests {
    private DatabaseHelperO2RunningLog database;
    private ControllerRunFormEntry controllerRunFormEntry;
    @Before
    public void setup() throws Exception{
        ViewControllerRunForm viewControllerRunForm = Robolectric.setupActivity(ViewControllerRunForm.class);
        controllerRunFormEntry = new ControllerRunFormEntry(viewControllerRunForm.getBaseContext(), viewControllerRunForm);
        ViewControllerHome viewControllerHome = Robolectric.setupActivity(ViewControllerHome.class);
        database = new DatabaseHelperO2RunningLog(viewControllerHome);
    }

    private void setupDatabase(){
        DateTime firstRun = new DateTime(2018, 2, 25, 1,1);
        DateTime secondRun = firstRun.plusDays(1);
        DateTime thirdRun = firstRun.plusDays(2);
        DateTime fourthRun = firstRun.plusDays(3);
        DateTime fifthRun = firstRun.plusDays(5);
        controllerRunFormEntry.createRun("firstRun",11,"mi",10, firstRun.getMillis(), 5, "First run notes");
        controllerRunFormEntry.createRun("secondRun", 22, "mi",20, secondRun.getMillis(), 6,"second run notes");
        controllerRunFormEntry.createRun("thirdRun", 2,"mi", 30, thirdRun.getMillis(), 10, "Third run notes");
        controllerRunFormEntry.createRun("fourthRun", 1,"km", 50, fourthRun.getMillis(), 10, "Fourth run notes");
        controllerRunFormEntry.createRun("fifthRun", 1,"km", 60, fifthRun.getMillis(), 10, "Fifth run notes");
    }


    @Test
    public void testALoadTestData() throws Exception{
        setupDatabase();
        Cursor cursor = database.getReadableDatabase().query(O2RunningLogContract.RunsEntry.TABLE_NAME, null, null, null, null, null, null);
        assertThat(cursor.getCount(), is(5));
    }

    @Test
    public void testBGetRuns() throws Exception{
        setupDatabase();
        ArrayList<ModelRun> runs = controllerRunFormEntry.getRuns();
        assertThat(runs.size(), is(5));
        assertThat(runs.get(0).getName(), is("firstRun"));
        assertThat(runs.get(1).getName(), is("secondRun"));
        assertThat(runs.get(2).getName(), is("thirdRun"));
    }
}
