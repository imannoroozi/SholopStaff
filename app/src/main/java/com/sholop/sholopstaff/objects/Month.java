package com.sholop.sholopstaff.objects;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.utilities.CalendarTool;

/**
 * Created by Pooyan on 8/18/2016.
 */
public class Month {

    Context context;
    int []jalaliMonthLength = {0,31,31,31,31,31,31,30,30,30,30,30,29};
    int []gregorianMonthLength = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    int selectedDayIndex, todayIndex = -1;

    ArrayList<Day> days;
    String jalaliMonthTitle;
    String gregorianMonthTitle;

    public Month(Context ctx, int diff){
        context = ctx;
        days = new ArrayList<>();

        CalendarTool today = new CalendarTool();

        int diffYear, diffMonth, diffDay;
        if( today.getGregorianMonth()+diff < 1){
            diffYear = (int) (today.getGregorianYear() + Math.floor((today.getGregorianMonth()+diff-1)/12.00));
        }else if(today.getGregorianMonth() + diff > 12 ){
            diffYear = (int) (today.getGregorianYear() + Math.floor((today.getGregorianMonth()+diff)/12));
        }else{
            diffYear = today.getGregorianYear();
        }

        int tempMonth = today.getGregorianMonth() -1 + diff;
        while(tempMonth<0) tempMonth += 12;
        diffMonth = (tempMonth%12) + 1;

        if(today.getGregorianDay() > gregorianMonthLength[diffMonth]){
            diffDay = gregorianMonthLength[diffMonth];
        }else{
            diffDay = today.getGregorianDay();
        }

        CalendarTool todayTwin = new CalendarTool();
        todayTwin.setGregorianDate(diffYear, diffMonth, diffDay);

        CalendarTool startCal = new CalendarTool();
        startCal.setGregorianDate(todayTwin.getGregorianYear(), todayTwin.getGregorianMonth(), 1);

        Resources res = context.getResources();
        String []gregorianMonthNames = res.getStringArray(R.array.gregorian_month_names);

        gregorianMonthTitle = gregorianMonthNames[todayTwin.getGregorianMonth()-1] +", " + todayTwin.getGregorianYear();

        int currDayNum = 1;
        while( currDayNum <= gregorianMonthLength[startCal.getGregorianMonth()]){
            if( currDayNum == 1 && days.size() != startCal.getDayOfWeek()){
                //insert null date
                days.add(new Day(null));
                continue;
            }

            //make today twin as selected day in calendar
            if( currDayNum == todayTwin.getGregorianDay()){
                this.setSelectedDayIndex(days.size());
            }

            //set today index
            if(todayTwin.getGregorianYear() == today.getGregorianYear() &&
                 todayTwin.getGregorianMonth()== today.getGregorianMonth() &&
                    today.getGregorianDay() == currDayNum ){
                 this.setTodayIndex(days.size());
            }

            CalendarTool currDay = new CalendarTool();
            currDay.setGregorianDate(todayTwin.getGregorianYear(), todayTwin.getGregorianMonth(), currDayNum++);

            days.add(new Day(currDay));
        }
    }

    public Day getSelectedDay(){
        return days.get(selectedDayIndex);
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public String getGregorianMonthTitle() {
        return gregorianMonthTitle;
    }

    public void setGregorianMonthTitle(String gregorianMonthTitle) {
        this.gregorianMonthTitle = gregorianMonthTitle;
    }

    public String getJalaliMonthTitle() {
        return jalaliMonthTitle;
    }

    public void setJalaliMonthTitle(String jalaliMonthTitle) {
        this.jalaliMonthTitle = jalaliMonthTitle;
    }

    public int getSelectedDayIndex() {
        return selectedDayIndex;
    }

    public void setSelectedDayIndex(int selectedDayIndex) {
        this.selectedDayIndex = selectedDayIndex;
    }

    public int getTodayIndex() {
        return todayIndex;
    }

    public void setTodayIndex(int todayIndex) {
        this.todayIndex = todayIndex;
    }

    public String getSelectedDateString() {
        return getSelectedDay().getDay().getDateString();
    }

    public String getGregorianDateString() {
        return getSelectedDay().getDay().getGregorianDisplayDate(context);
    }

    public String getJalaliDateString() {
        return getSelectedDay().getDay().getJalaliDisplayDate(context);
    }

}
