package com.sholop.sholopstaff.objects;

import com.sholop.sholopstaff.utilities.CalendarTool;

/**
 * Created by Pooyan on 8/18/2016.
 */
public class Day {
    CalendarTool day;

    public Day(CalendarTool ct){
        this.day = ct;
    }

    public CalendarTool getDay() {
        return day;
    }

    public void setDay(CalendarTool day) {
        this.day = day;
    }
}
