package com.example.mysubwayproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.content.res.AssetManager;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
/*
프로그램 시작을 위한 소스코드 입니다. 절대 건드리지 마시오.
* */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.finish();
        Intent intent = new Intent(this, StartStation.class);
        startActivity(intent);
    }
}
