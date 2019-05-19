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
    }
}
