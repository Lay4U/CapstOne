package com.example.test222;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import libs.classes.kr.go.seoul.trafficsubway.TrafficSubwayButtonTypeA;
import libs.classes.kr.go.seoul.trafficsubway.TrafficSubwayButtonTypeB;
import libs.classes.kr.go.seoul.trafficsubway.TrafficSubwayTypeMini;
public class MainActivity extends AppCompatActivity {

    private String subwayKey = "sample";      //지하철 실시간 정보 사용가능
    private String key = "sample";      //지하철 실시간 정보 제외하고 사용가능

    private TrafficSubwayButtonTypeA subwayButtonTypeA;
    private TrafficSubwayButtonTypeB subwayButtonTypeB;

    private TrafficSubwayTypeMini subwayTypeMini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subwayButtonTypeA = (TrafficSubwayButtonTypeA) findViewById(R.id.subway_type_a);
        subwayButtonTypeB = (TrafficSubwayButtonTypeB) findViewById(R.id.subway_type_b);
        subwayTypeMini = (TrafficSubwayTypeMini) findViewById(R.id.subway_type_mini);

        subwayButtonTypeA.setOpenAPIKey(key);
        subwayButtonTypeB.setOpenAPIKey(key);                           //지하철 기본 apikey(실시간 도착정보 조회가 가능한 키로 사용시 setsubwayLocationAPIKey 생략가능
        subwayButtonTypeB.setsubwayLocationAPIKey(subwayKey);           //지하철 실시간 도착정보용 apikey(B타입과 Mini타입에서만 사용)
        subwayTypeMini.setOpenAPIKey(key);
        subwayTypeMini.setsubwayLocationAPIKey(subwayKey);
    }
}