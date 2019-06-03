package com.example.mysubwayproject;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
/*
 * 도착역선택 후 정보를 전달하기 위한 소스코드 입니다!
 * */
public class InterfaceEdSt {
    private Activity mContext;
    private String startStationNM;
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
        intent.putExtra("EndStationNM", station);
        intent.putExtra("Year", year);
        intent.putExtra("Month", month);
        intent.putExtra("Day", day);
        intent.putExtra("Hour", hour);
        intent.putExtra("Minute", minute);
        this.mContext.finish();
        this.mContext.startActivity(intent);
    }
}
