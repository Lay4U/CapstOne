package com.example.mysubwayproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;


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
    model Model =new model();

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
                        + "\nTime: " + hour + "시 " + minute + "분\n"
                        + Model.modelPredict(startStationNM, day, hour, minute);;//추가

                int current_Hour = hour;
                int current_Minute = minute;
                for (int i = 0; i < stationArray.size(); i++) {
                    JsonObject object = (JsonObject) stationArray.get(i);
                    String current_Station = object.get("startName").getAsString();

                    station_in_course = station_in_course
                            + "\nStation: " + current_Station
                            + "\nDate: " + year + "년 " +  month + "월 " +  day + "일"
                            + "\nTime: " + current_Hour + "시 " + current_Minute + "분\n"
                            + Model.modelPredict(current_Station, day, current_Hour, current_Minute);;//추가;


                    current_Minute = minute + object.get("travelTime").getAsInt();
                    if(current_Minute >= 60){
                        current_Hour = hour + current_Minute/60;
                        current_Minute %= 60;
                    }
                }
                station_in_course = station_in_course
                        + "\nStation: " + endStationNM
                        + "\nDate: " + year + "년 " +  month + "월 " +  day + "일"
                        + "\nTime: " + current_Hour + "시 " + current_Minute + "분\n"
                        + Model.modelPredict(endStationNM, day, current_Hour, current_Minute);//추가;


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
}
