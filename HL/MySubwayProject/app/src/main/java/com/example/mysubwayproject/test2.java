package com.example.mysubwayproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class test2 extends AppCompatActivity {
    private String StationNM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        if (getIntent() != null && getIntent().getStringExtra("StationNM") != null) {
            this.StationNM = getIntent().getStringExtra("StationNM");
        }
        TextView textView = (TextView) findViewById(R.id.textView) ;
        textView.setText(StationNM + " 출력값");
        //쿼리날리고 = https://api.odsay.com/v1/eDieEbNzQwonBt3ajgZQqAjhIcDlQSCAqpmRhLMtETA/searchStation?lang=0&stationName=StationNM
        //사용자에게 시간, 도착역받고
        //출발역, 도착역 경로받아서
        //각 역의 시간을 모델에 던진다.
        //받은 값을 시각화해서 뿌려준다.

    }
}
