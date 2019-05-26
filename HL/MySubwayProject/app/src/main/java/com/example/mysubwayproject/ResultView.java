package com.example.mysubwayproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * 최종 화면을 구성할 예정인 소스코드 입니다!
 * */
public class ResultView extends AppCompatActivity {
    String startStationNM;
    private String StationID = "null";
    private String endStationNM;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private JSONObject jsonObject;
    private TextView tv_data2;
    private ODsayService odsayService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);

        tv_data2 = (TextView) findViewById(R.id.tv_data2);
        this.startStationNM = getIntent().getStringExtra("StartStationNM");
        this.endStationNM = getIntent().getStringExtra("EndStationNM");
        this.year = getIntent().getIntExtra("Year", year);
        this.month = getIntent().getIntExtra("Month", month);
        this.day = getIntent().getIntExtra("Day", day);
        this.hour = getIntent().getIntExtra("Hour", hour);
        this.minute = getIntent().getIntExtra("Minute", minute);
        findStationID();
    }

    protected void findStationID(){
        odsayService = ODsayService.init(ResultView.this, getString(R.string.odsay_key));

        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener(){

            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                jsonObject = odsayData.getJson();

                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObj = (JsonObject) jsonParser.parse(jsonObject.toString());
                JsonObject resultObj = (JsonObject) jsonObj.get("result");
                JsonArray stationArray = (JsonArray) resultObj.get("station");

                String stationName = "";
                String stationID = "";
                for (int i = 0; i < stationArray.size(); i++) {
                    JsonObject object = (JsonObject) stationArray.get(i);
                    stationName = object.get("stationName").getAsString();
                    stationID = object.get("stationID").getAsString();
                    if(stationName.equals(startStationNM) || stationName.equals(endStationNM))
                        break;
                }
                if(StationID.equals("null")) StationID = stationID;
                else shortestPathSearching(StationID, stationID);
            }
            // 호출 실패 시 실행
            @Override
            public void onError(int i, String s, API api) {
                tv_data2.setText("API : " + api.name() + "\n" + "error");
            }
        };

        odsayService.requestSearchStation(startStationNM, "1000", "2","10","0","127.0363583:37.5113295", onResultCallbackListener);
        odsayService.requestSearchStation(endStationNM, "1000", "2","10","0","127.0363583:37.5113295", onResultCallbackListener);
    }
    protected void shortestPathSearching(String startStationID, String endStationID) {

        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener(){

            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                jsonObject = odsayData.getJson();
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObj = (JsonObject) jsonParser.parse(jsonObject.toString());
                JsonObject resultObj = (JsonObject) jsonObj.get("result");

                //JsonObject driveInfoSetObj = (JsonObject) resultObj.get("driveInfoSet");
                //JsonObject exChangeInfoSetObj = (JsonObject) resultObj.get("exChangeInfoSet");
                JsonObject stationSetObj = (JsonObject) resultObj.get("stationSet");

                //JsonArray driveInfoArray = (JsonArray) driveInfoSetObj.get("driveInfo");
                //JsonArray exChangeInfoArray = (JsonArray) exChangeInfoSetObj.get("exChangeInfo");
                JsonArray stationArray = (JsonArray) stationSetObj.get("stations");

                String station_in_course = "startStation: " + startStationNM + " endStation: " + endStationNM
                        + "\nDate: " + year + "년 " +  month + "월 " +  day + "일"
                        + "\nTime: " + hour + "시 " + minute + "분\n";;
                int current_Hour = hour;
                int current_Minute = minute;
                for (int i = 0; i < stationArray.size(); i++) {
                    JsonObject object = (JsonObject) stationArray.get(i);
                    String current_Station = object.get("startName").getAsString();

                    station_in_course = station_in_course
                            + "\nStation: " + current_Station
                            + "\nDate: " + year + "년 " +  month + "월 " +  day + "일"
                            + "\nTime: " + current_Hour + "시 " + current_Minute + "분\n";

                    current_Minute = minute + object.get("travelTime").getAsInt();
                    if(current_Minute >= 60){
                        current_Hour = hour + current_Minute/60;
                        current_Minute %= 60;
                    }
                }
                station_in_course = station_in_course
                        + "\nStation: " + endStationNM
                        + "\nDate: " + year + "년 " +  month + "월 " +  day + "일"
                        + "\nTime: " + current_Hour + "시 " + current_Minute + "분\n";

                tv_data2.setText(station_in_course);

                //분해해야함!!!!
            }
            // 호출 실패 시 실행
            @Override
            public void onError(int i, String s, API api) {
                tv_data2.setText("API : " + api.name() + "\n" + "error");
            }
        };

        odsayService.requestSubwayPath("1000", startStationID, endStationID,"1", onResultCallbackListener);
    }

    public float modelPredict(String StationNM, int day, int hour, int minute)
    {
        Scanner scan = null;
        try {
            scan = new Scanner(new File("file:///android_lib/"+StationNM+".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String[]> records = new ArrayList<String[]>();
        ArrayList<Integer> model_list = new ArrayList<Integer>();

        while (scan.hasNext()) {
            String[] record;
            record = scan.nextLine().split(",");
            records.add(record);
        }

        // 확인용 출력 확인완료
//		int i = 0;
//		for (String[] temp : records) {
//			for (String temp1 : temp) {
//				System.out.print(temp1 + " ");
//				i++;
//			}
//			System.out.print("\n");
//		}
//		System.out.println(i);
        // 과학적표기법 -> 실수

        for (String[] temp : records) {
            for (String temp1 : temp) {
                int val = new BigDecimal(temp1).intValue();
                model_list.add(val);
            }
        }
//		for(int t : model_list) {
//			System.out.println(t);
//		}	//확인완료
        int[] model = new int[model_list.size()];
        for (int i = 0; i < model.length; i++) {
            model[i] = model_list.get(i).intValue();
        }
//		for(int t : model) {
//		System.out.println(t);
//	}	//확인완료

        //가정
        //day 기준점으로부터 8
        //시간은 7시 24분일때
        //0부터 8까지 떨어져있는 8*20 + 7, 8*20 + 8을 직선의 방정식으로 구한다.
        // input 기준점으로부터 day(8)과 시각(7)을 받는다.

        int x1 = day * 20 + hour;
        int x2 = day * 20 + hour + 1;
        float y1 = model[x1];
        float y2 = model[x2];


        float a = (y2 - y1); //(x2-x1)은 항상1
        float b = y2 - a;

        // 24분에 해당하는점을 구한다.

        float x = (float) (1.0 / 60.0 * minute); // input은 24를 받는다.
        float y = a * x + b;
        System.out.println("x1 :" + x1);
        System.out.println("x2 :" + x2);
        System.out.println("y1 :" + y1);
        System.out.println("y2 :" + y2);
        System.out.println("a :" + a);
        System.out.println("b :" + b);
        System.out.println("x :" + x);
        System.out.print(y);

        return y;
    }
}
