package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

abstract class MainActivitya extends AppCompatActivity {
    public void onClick(View view) {
        //여기에 InputBox 추가
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.click_event);
    }
}
