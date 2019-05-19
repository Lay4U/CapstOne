package com.estsoft.r_subway_android.Crawling;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by estsoft on 2016-08-18.
 */
public class DateManager {

    private static final String TAG = "DateManager";

    private static DateManager ourInstance = null;

    public static DateManager getInstance() {
//        Log.d(TAG, "getInstance: " + "instance");
        if (ourInstance == null) ourInstance = new DateManager();
        return ourInstance;
    }

    private DateManager() {

        redDayList = new ArrayList<>();

        redDayList.add("1-1");
        redDayList.add("2-7");
        redDayList.add("2-8");
        redDayList.add("2-9");
        redDayList.add("2-10");
        redDayList.add("3-1");
        redDayList.add("4-13");
        redDayList.add("5-5");
        redDayList.add("5-6");
        redDayList.add("5-14");
        redDayList.add("6-6");
        redDayList.add("8-15");
        redDayList.add("9-14");
        redDayList.add("9-15");
        redDayList.add("9-16");
        redDayList.add("10-3");
        redDayList.add("10-9");
        redDayList.add("12-25");
//        Log.d(TAG, "DateManager: "  + "init");

    }

    private List<String> redDayList = null;

    public String getDayAndTime() {
        Calendar curTime = new GregorianCalendar();
        int hour = curTime.get(Calendar.HOUR_OF_DAY);
        int yDay = curTime.get(Calendar.DAY_OF_YEAR);
        if ( checkYunYear(curTime.get(Calendar.YEAR)) && yDay == 59 ) yDay = 60;

        int redDay = -1;
        if ( checkRedDay(curTime) ) redDay = 2;
        else if (curTime.get(Calendar.DAY_OF_WEEK) == 7) redDay = 1;
        else redDay = 0;

//        Log.d(TAG, "getDayAndTime: " + (curTime.get(Calendar.MONTH) + 1) + "." + curTime.get(Calendar.DAY_OF_MONTH) + " / " + curTime.get(Calendar.HOUR));
//        Log.d(TAG, "getDayAndTime: " + yDay + " " + redDay + " " + hour);

        return yDay + "-" + redDay + "-" + hour;
    }
    private boolean checkRedDay( Calendar calendar ) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) return true;
        for ( String day : redDayList ) {
            String[] dayString = day.split("-");
            int month = Integer.parseInt(dayString[0]);
            int dayint = Integer.parseInt(dayString[1]);
//            Log.d(TAG, "checkRedDay: "+ (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH) );
            if ((calendar.get(Calendar.MONTH) + 1) == month && calendar.get(Calendar.DAY_OF_MONTH) == dayint ) return true;
        }
        return false;
    }

    private boolean checkYunYear( int year ) {
        boolean result = ( year % 4 == 0 ) && ( year % 100 != 0 || ( year % 400 == 0 ) ) ;
        return result;
    }

}
