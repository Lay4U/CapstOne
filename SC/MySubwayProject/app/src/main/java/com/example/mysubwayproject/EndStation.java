package com.example.mysubwayproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

public class EndStation extends MapMaker {

    private ImageView btnBackSubway;
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
        //this.textPageNM = (TextView)findViewById(R.id.showTitle);
        this.btnBackSubway = (ImageView)findViewById(R.id.my_btn_back_subway);
        this.btnBackSubway.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                EndStation.this.finish();
            }
        });
        this.mWebViewInterface = new InterfaceEdSt(this,
                startStationNM,
                year,
                month,
                day,
                hour,
                minute);
        super.lineMapWebview.addJavascriptInterface(this.mWebViewInterface, "Android");
    }
}
