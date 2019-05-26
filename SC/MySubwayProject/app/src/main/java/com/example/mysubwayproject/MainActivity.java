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
        readAll();
        Intent intent = new Intent(this, StartStation.class);
        startActivity(intent);
    }
//CSV파일 읽기 테스트
    void readAll() {
        AssetManager assetManager = getApplication().getAssets();
        try {

            InputStreamReader is = new InputStreamReader(assetManager.open("강남구청.csv"));
            BufferedReader br = new BufferedReader(is);

            CSVReader reader = new CSVReader(br);
            for(String[] data : reader.readAll()){
                System.out.println(data[0]);
            }
        } catch (IOException e) {
            System.out.println("can not found .csv");
        }

    }
}
