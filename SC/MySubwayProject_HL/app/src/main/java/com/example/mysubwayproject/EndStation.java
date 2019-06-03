package com.example.mysubwayproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
/*
* 도착역을 선택하는 화면을 구성하는 소스코드 입니다!
* */
public class EndStation extends MapMaker {


    private TextView textPageNM;
    private InterfaceEdSt mWebViewInterface;

    private String startStationNM;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.textPageNM = (TextView)findViewById(R.id.my_textView);
        textPageNM.setText("도착역을 선택하시오");
        this.startStationNM = getIntent().getStringExtra("StartStationNM");
        this.year = getIntent().getIntExtra("Year", year);
        this.month = getIntent().getIntExtra("Month", month);
        this.day = getIntent().getIntExtra("Day", day);
        this.hour = getIntent().getIntExtra("Hour", hour);
        this.minute = getIntent().getIntExtra("Minute", minute);
        this.initView();
    }

    private void initView() {
        this.mWebViewInterface = new InterfaceEdSt(this,
                startStationNM,
                year,
                month,
                day,
                hour,
                minute);// 역 선택시 정보전달
        super.lineMapWebview.addJavascriptInterface(this.mWebViewInterface, "Android");
    }
}
