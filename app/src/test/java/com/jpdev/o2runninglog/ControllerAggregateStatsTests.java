package com.jpdev.o2runninglog;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Jonathan on 2/23/2018.
 */
@RunWith(RobolectricTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerAggregateStatsTests {
    private DatabaseHelperO2RunningLog database;
    private ControllerRunFormEntry controllerRunFormEntry;
    private ControllerAggregateStats controllerAggregateStats;
    @Before
    public void setup() throws Exception{
        ViewControllerRunForm viewControllerRunForm = Robolectric.setupActivity(ViewControllerRunForm.class);
        controllerRunFormEntry = new ControllerRunFormEntry(viewControllerRunForm.getBaseContext(), viewControllerRunForm);
        ViewControllerHome viewControllerHome = Robolectric.setupActivity(ViewControllerHome.class);
        database = new DatabaseHelperO2RunningLog(viewControllerHome);
        controllerAggregateStats = new ControllerAggregateStats(viewControllerHome.getBaseContext());

    }

    private void setupDatabase(){
        DateTime firstRun = new DateTime(2018, 2, 25, 1,1);
        DateTime secondRun = firstRun.plusDays(1);
        DateTime thirdRun = firstRun.plusDays(2);
        DateTime fourthRun = firstRun.plusDays(3);
        DateTime fifthRun = firstRun.plusDays(5);
        controllerRunFormEntry.createRun("firstRun",11,"mi",20, firstRun.getMillis(), 5, "First run notes");
        controllerRunFormEntry.createRun("secondRun", 22, "mi",30, secondRun.getMillis(), 6,"second run notes");
        controllerRunFormEntry.createRun("thirdRun", 2,"mi", 40, thirdRun.getMillis(), 10, "Third run notes");
        controllerRunFormEntry.createRun("fourthRun", 1,"km", 40, fourthRun.getMillis(), 10, "Fourth run notes");
        controllerRunFormEntry.createRun("fifthRun", 1,"km", 40, fifthRun.getMillis(), 10, "Fifth run notes");
    }

    private void setupTrendDatabaseUp(){
        DateTime previousMonth = new DateTime().minusMonths(1).withDayOfMonth(1);
        DateTime firstRun = previousMonth.plusDays(1);
        DateTime secondRun = previousMonth.plusDays(2);
        DateTime thirdRun = previousMonth.plusDays(3);
        DateTime fourthRun = previousMonth.plusDays(10);
        DateTime fifthRun = previousMonth.plusDays(11);
        DateTime sixthRun = previousMonth.plusDays(12);
        DateTime seventhRun = previousMonth.plusDays(17);
        DateTime eighthRun = previousMonth.plusDays(18);
        DateTime ninethRun = previousMonth.plusDays(19);
        DateTime tenthRun = previousMonth.plusDays(27);
        controllerRunFormEntry.createRun("prevFirst",3, "mi",30, firstRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSecond",3, "mi",30, secondRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevThird",3, "mi",30, thirdRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFourth",3, "mi",30, fourthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFifth",3, "mi",30, fifthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSixth",3, "mi",30, sixthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSeventh",3, "mi",30, seventhRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevEighth",3, "mi",30, eighthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevNineth",3, "mi",30, ninethRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevTenth",3, "mi",30, tenthRun.getMillis(), 5, "notes");
    }
    private void setupTrendDatabaseUpCurrent(){
        DateTime currentMonth = new DateTime(2018, 2, 1, 1,1);
        DateTime firstRun = currentMonth.plusDays(1);
        DateTime secondRun = currentMonth.plusDays(2);
        DateTime thirdRun = currentMonth.plusDays(3);
        DateTime fourthRun = currentMonth.plusDays(10);
        DateTime fifthRun = currentMonth.plusDays(11);
        DateTime sixthRun = currentMonth.plusDays(12);
        DateTime seventhRun = currentMonth.plusDays(17);
        DateTime eighthRun = currentMonth.plusDays(18);
        DateTime ninethRun = currentMonth.plusDays(19);
        DateTime tenthRun = currentMonth.plusDays(27);
        controllerRunFormEntry.createRun("prevFirst",9, "mi",30, firstRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSecond",9, "mi",30, secondRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevThird",9, "mi",30, thirdRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFourth",9, "mi",30, fourthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFifth",9, "mi",30, fifthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSixth",9, "mi",30, sixthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSeventh",9, "mi",30, seventhRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevEighth",9, "mi",30, eighthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevNineth",9, "mi",30, ninethRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevTenth",9, "mi",30, tenthRun.getMillis(), 5, "notes");
    }

    //TODO: setupTrendDatabaseEven
    private void setupTrendDatabaseEvenCurrent(){
        DateTime currentMonth = new DateTime(2018, 2, 1, 1,1);
        DateTime firstRun = currentMonth.plusDays(1);
        DateTime secondRun = currentMonth.plusDays(2);
        DateTime thirdRun = currentMonth.plusDays(3);
        DateTime fourthRun = currentMonth.plusDays(10);
        DateTime fifthRun = currentMonth.plusDays(11);
        DateTime sixthRun = currentMonth.plusDays(12);
        DateTime seventhRun = currentMonth.plusDays(17);
        DateTime eighthRun = currentMonth.plusDays(18);
        DateTime ninethRun = currentMonth.plusDays(19);
        DateTime tenthRun = currentMonth.plusDays(27);
        controllerRunFormEntry.createRun("prevFirst",2.5, "mi",30, firstRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSecond",2.5, "mi",30, secondRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevThird",2.5, "mi",30, thirdRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFourth",2.5, "mi",30, fourthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFifth",2.5, "mi",30, fifthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSixth",2.5, "mi",30, sixthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSeventh",2.5, "mi",30, seventhRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevEighth",2.5, "mi",30, eighthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevNineth",2.5, "mi",30, ninethRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevTenth",2.5, "mi",30, tenthRun.getMillis(), 5, "notes");
    }
    //TODO: setupTrendDatabaseDown
    private void setupTrendDatabaseDownCurrent(){
        DateTime currentMonth = new DateTime(2018, 2, 1, 1,1);
        DateTime firstRun = currentMonth.plusDays(1);
        DateTime secondRun = currentMonth.plusDays(2);
        DateTime thirdRun = currentMonth.plusDays(3);
        DateTime fourthRun = currentMonth.plusDays(10);
        DateTime fifthRun = currentMonth.plusDays(11);
        DateTime sixthRun = currentMonth.plusDays(12);
        DateTime seventhRun = currentMonth.plusDays(17);
        DateTime eighthRun = currentMonth.plusDays(18);
        DateTime ninethRun = currentMonth.plusDays(19);
        DateTime tenthRun = currentMonth.plusDays(27);
        controllerRunFormEntry.createRun("prevFirst",0.5, "mi",30, firstRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSecond",0.5, "mi",30, secondRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevThird",0.5, "mi",30, thirdRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFourth",0.5, "mi",30, fourthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevFifth",0.5, "mi",30, fifthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSixth",0.5, "mi",30, sixthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevSeventh",0.5, "mi",30, seventhRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevEighth",0.5, "mi",30, eighthRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevNineth",0.5, "mi",30, ninethRun.getMillis(), 5, "notes");
        controllerRunFormEntry.createRun("prevTenth",0.5, "mi",30, tenthRun.getMillis(), 5, "notes");
    }
    @Test
    public void aTestTotalMileage(){
        setupDatabase();
        double total = 0;
        total = controllerAggregateStats.getTotalMileage("mi");
        assertThat(total, is((double)36.24274238447467));
        double totalInKm = 0;
        totalInKm = controllerAggregateStats.getTotalMileage("km");
        assertThat(totalInKm, is(58.327040000000004));

    }

    @Test
    public void bTestMileageThisMonth(){
        setupDatabase();
        double weekTotal;
        weekTotal = 0;
        weekTotal = controllerAggregateStats.getMonthlyMileage("mi");
        assertThat(weekTotal, is((double)35.62137119223733));
    }
    @Test
    public void cTestMileageThisWeek(){
        setupDatabase();
        double weekTotal;
        weekTotal = 0;
        weekTotal = controllerAggregateStats.getWeeklyMileage("mi");
        assertThat(weekTotal, is((double)11));
    }

    @Test
    public void dTestMonthTrendUp(){
        setupTrendDatabaseUp();
        setupTrendDatabaseUpCurrent();
        String rate = controllerAggregateStats.getMonthTrendRate("mi");
        assertThat(rate, is( "up"));
    }

    @Test
    public void eTestMonthTrendEven(){
        setupTrendDatabaseUp();
        setupTrendDatabaseEvenCurrent();
        String rate = controllerAggregateStats.getMonthTrendRate("mi");
        assertThat(rate, is( "even"));
    }
    @Test
    public void fTestMonthTrendDown(){
        setupTrendDatabaseUp();
        setupTrendDatabaseDownCurrent();
        String rate = controllerAggregateStats.getMonthTrendRate("mi");
        assertThat(rate, is( "even"));
    }
}
