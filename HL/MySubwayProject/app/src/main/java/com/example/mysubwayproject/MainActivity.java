package com.example.mysubwayproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

/*
프로그램 시작을 위한 소스코드 입니다. 절대 건드리지 마시오.
* */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, StartStation.class);
        startActivity(intent);
    }
}
