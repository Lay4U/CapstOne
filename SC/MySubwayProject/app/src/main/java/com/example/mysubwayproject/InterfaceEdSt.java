package com.example.mysubwayproject;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class InterfaceEdSt {
    private Activity mContext;
    private String startStationNM;
    private String endStationNM;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public InterfaceEdSt(Activity activity
            , String StartStationNM
            , int Year
            , int Month
            , int Day
            , int Hour
            , int Minute){
        this.mContext = activity;
        this.startStationNM = StartStationNM;
        this.year = Year;
        this.month = Month;
        this.day = Day;
        this.hour = Hour;
        this.minute = Minute;
    }

    @JavascriptInterface
    public void showSubwayInfo(String station){
        Intent intent = new Intent(this.mContext, ResultView.class);
        intent.putExtra("StartStationNM", startStationNM);
        intent.putExtra("Year", year);
        intent.putExtra("Month", month);
        intent.putExtra("Day", day);
        intent.putExtra("Hour", hour);
        intent.putExtra("Minute", minute);
        intent.putExtra("EndStationNM", station);
        this.mContext.startActivity(intent);
    }
}
