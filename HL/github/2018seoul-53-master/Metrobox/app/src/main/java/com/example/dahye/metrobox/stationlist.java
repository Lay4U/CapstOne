package com.example.dahye.metrobox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class stationlist extends AppCompatActivity {
    ArrayList<String> station_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mybox);
    }
    public boolean contains(String s){
        station_list.add("강남");
        station_list.add("건대입구A");
        station_list.add("건대입구B");
        station_list.add("건대입구C");
        station_list.add("건대입구D");
        station_list.add("건대입구E");
        station_list.add("경복궁A");
        station_list.add("경복궁B");
        station_list.add("고속터미널B");
        station_list.add("광화문A");
        station_list.add("광화문B");
        station_list.add("동대문역사문화공원A");
        station_list.add("동대문역사문화공원B");
        station_list.add("동대문역사문화공원C");
        station_list.add("상봉");
        station_list.add("서울역A");
        station_list.add("서울역B");
        station_list.add("서울역C");
        station_list.add("서울역D");
        station_list.add("서울역E");
        station_list.add("수서");
        station_list.add("시청A");
        station_list.add("시청B");
        station_list.add("시청C");
        station_list.add("안국A");
        station_list.add("안국B");
        station_list.add("어린이대공원");
        station_list.add("여의도");
        station_list.add("왕십리");
        station_list.add("을지로3가B");
        station_list.add("을지로4가");
        station_list.add("을지로입구A");
        station_list.add("을지로입구B");
        station_list.add("이태원A");
        station_list.add("이태원B");
        station_list.add("잠실A");
        station_list.add("잠실B");
        station_list.add("잠실C");
        station_list.add("잠실D");
        station_list.add("잠실E");
        station_list.add("종로3가A");
        station_list.add("종로3가B");
        station_list.add("종로3가C");
        station_list.add("청량리");
        station_list.add("홍대입구A");
        station_list.add("홍대입구B");
        station_list.add("홍대입구C");
        station_list.add("홍대입구D");
        return station_list.contains(s);
    }
}
