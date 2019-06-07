package com.example.mysubwayproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/*
 * 최종 화면을 구성할 예정인 소스코드 입니다!
 * */
public class ResultView extends AppCompatActivity implements View.OnClickListener {
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
    Button btnSubway;

    JsonArray stationArray;
    ArrayList<String> StationNMArray = new ArrayList<>();
    ArrayList<String> exStation = new ArrayList<>();
    ArrayList<String> wayNMArray = new ArrayList<>();
    ArrayList<String> wayCodeArray = new ArrayList<>();
    ArrayList<Integer> currentHourArray = new ArrayList<>();
    ArrayList<Integer> currentMinArray = new ArrayList<>();
    ArrayList<Float> currentPredict = new ArrayList<>();

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

        TextView textPageNM = (TextView) findViewById(R.id.start_and_end);
        textPageNM.setText(startStationNM + "    " + endStationNM);
        findStationID();

        btnSubway = (Button) findViewById(R.id.complete);
        btnSubway.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void findStationID() {
        odsayService = ODsayService.init(ResultView.this, getString(R.string.odsay_key));

        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {

            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                jsonObject = odsayData.getJson();

                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObj = (JsonObject) jsonParser.parse(jsonObject.toString());
                JsonObject resultObj = (JsonObject) jsonObj.get("result");
                JsonArray stationArray = (JsonArray) resultObj.get("station");
//                JsonObject driveInfoSetObj = (JsonObject) resultObj.get("driveInfoSet");
//                Log.i("gg", driveInfoSetObj+"\n");
                String stationName = "";
                String stationID = "";
                for (int i = 0; i < stationArray.size(); i++) {// 수정필요!!!!
                    JsonObject object = (JsonObject) stationArray.get(i);
                    stationName = object.get("stationName").getAsString();
                    stationID = object.get("stationID").getAsString();
                    if (StationID.equals("null") && stationName.equals(startStationNM)) {
                        break;
                    } else if (!StationID.equals("null") && stationName.equals(endStationNM)) {
                        break;
                    }
                }
                if (StationID.equals("null"))
                    StationID = stationID;
                else {
                    shortestPathSearching(StationID, stationID);
                }
            }

            // 호출 실패 시 실행
            @Override
            public void onError(int i, String s, API api) {
                tv_data2.setText("API : " + api.name() + "\n" + "error");
            }
        };
        if (startStationNM.equals("광교")) {
            odsayService.requestSearchStation("광교(경기대)", "1000", "2", "10", "0", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (startStationNM.equals("광교중앙")) {
            odsayService.requestSearchStation("광교중앙(아주대)", "1000", "2", "10", "0", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (startStationNM.equals("신촌(경의중앙선)")) {
            odsayService.requestSearchStation("신촌", "1000", "2", "10", "1", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (startStationNM.equals("녹사평")) {
            odsayService.requestSearchStation("녹사평(용산구청)", "1000", "2", "10", "1", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (startStationNM.equals("봉화산")) {
            odsayService.requestSearchStation("봉화산(서울의료원)", "1000", "2", "10", "1", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else {
            odsayService.requestSearchStation(startStationNM, "1000", "2", "10", "0", "127.0363583:37.5113295",
                    onResultCallbackListener);
        }

        if (endStationNM.equals("광교")) {
            odsayService.requestSearchStation("광교(경기대)", "1000", "2", "10", "0", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (endStationNM.equals("광교중앙")) {
            odsayService.requestSearchStation("광교중앙(아주대)", "1000", "2", "10", "0", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (endStationNM.equals("신촌(경의중앙선)")) {
            odsayService.requestSearchStation("신촌", "1000", "2", "10", "1", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (endStationNM.equals("녹사평")) {
            odsayService.requestSearchStation("녹사평(용산구청)", "1000", "2", "10", "1", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else if (endStationNM.equals("봉화산")) {
            odsayService.requestSearchStation("봉화산(서울의료원)", "1000", "2", "10", "1", "127.0363583:37.5113295",
                    onResultCallbackListener);
        } else {
            odsayService.requestSearchStation(endStationNM, "1000", "2", "10", "0", "127.0363583:37.5113295",
                    onResultCallbackListener);
        }
    }

    protected void shortestPathSearching(String startStationID, String endStationID) {
        odsayService = ODsayService.init(ResultView.this, getString(R.string.odsay_key));
        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                jsonObject = odsayData.getJson();

                JsonParser jsonParser = new JsonParser();

                JsonObject jsonObj = (JsonObject) jsonParser.parse(jsonObject.toString());

                JsonObject resultObj = (JsonObject) jsonObj.get("result");

                JsonObject driveInfoSetObj = (JsonObject) resultObj.get("driveInfoSet");

                // JsonObject exChangeInfoSetObj = (JsonObject)
                // resultObj.get("exChangeInfoSet");
                JsonObject stationSetObj = (JsonObject) resultObj.get("stationSet");

                JsonArray driveInfoArray = (JsonArray) driveInfoSetObj.get("driveInfo");

                // JsonArray exChangeInfoArray = (JsonArray)
                // exChangeInfoSetObj.get("exChangeInfo");
                stationArray = (JsonArray) stationSetObj.get("stations");

                String dayOfWeek;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month - 1, day);
                int dayNum = cal.get(Calendar.DAY_OF_WEEK);

                switch (dayNum) {
                    case 1:
                        dayOfWeek = "일요일";
                        break;
                    case 7:
                        dayOfWeek = "토요일";
                        break;
                    default:
                        dayOfWeek = "평일";
                        break;
                }
                JsonObject startObject = (JsonObject) stationArray.get(0);

                for (int i = 0; i < driveInfoArray.size(); i++) {
                    JsonObject object = (JsonObject) driveInfoArray.get(i);
                    exStation.add(object.get("startName").getAsString());
                    wayNMArray.add(object.get("wayName").getAsString());
                    wayCodeArray.add(object.get("wayCode").getAsString());
                }

                timeTable(hour,
                        minute,
                        startObject.get("startID").getAsString(),
                        wayCodeArray.get(0),
                        dayNum,
                        dayOfWeek);
                /*
                model m = new model();
                currentHourArray.add(hour);
                currentMinArray.add(minute);

                int setline = -1;
                for (int i = 0; i < stationArray.size(); i++) {
                    JsonObject object = (JsonObject) stationArray.get(i);
                    String current_Station = object.get("startName").getAsString();
                    for (String name : exStation)
                        if (current_Station.equals(name)) {
                            setline++;
                            break;
                        }
                    StationNMArray.add(current_Station);

                    currentPredict.add(modelPredict(current_Station, wayNMArray.get(setline), dayNum,
                            currentHourArray.get(i), currentMinArray.get(i)));


                    currentHourArray.add(hour);
                    currentMinArray.add(minute + object.get("travelTime").getAsInt());
                    if (currentMinArray.get(i + 1) >= 60) {
                        currentHourArray.remove(i + 1);
                        currentHourArray.add(hour + currentMinArray.get(i + 1) / 60);
                        currentMinArray.remove(i + 1);
                        currentMinArray
                                .add((minute + object.get("travelTime").getAsInt() % 60) - 60); // 60분 이상으로 나옴 수정
                    }
                }

                StationNMArray.add(endStationNM);
//                currentPredict.add(m.modelPredict(endStationNM, wayNMArray.get(setline), dayNum, currentHourArray.get(stationArray.size()), currentMinArray.get(stationArray.size()), ));
                currentPredict.add(modelPredict(endStationNM, wayNMArray.get(setline), dayNum,
                        currentHourArray.get(stationArray.size()), currentMinArray.get(stationArray.size())));
                makeCourse();
                */
            }

            @Override
            public void onError(int i, String s, API api) {
                tv_data2.setText("API : " + api.name() + "\n" + "error");
            }
        };
        odsayService.requestSubwayPath("1000", startStationID, endStationID, "1", onResultCallbackListener);

    }

    String dow;//요일
    String upDown;//상하행
    int timeTableDayNum;
    int inputHour, inputMinute;
    int resHour, resMinute;
    void timeTable(int thishour, int thisminute, String stationID, String wayCode, int dayNum,String dayOfWeek) {
        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                jsonObject = odsayData.getJson();

                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObj = (JsonObject) jsonParser.parse(jsonObject.toString());
                JsonObject resultObj = (JsonObject) jsonObj.get("result");
                JsonObject weekObj = (JsonObject) resultObj.get(dow);
                JsonObject upDownObj = (JsonObject) weekObj.get(upDown);
                JsonArray resArray = (JsonArray) upDownObj.get("time");

                for(JsonElement jElement : resArray) {
                    JsonObject timeElement = jElement.getAsJsonObject();
                    int timeTableHour = timeElement.get("Idx").getAsInt();//시간
                    String[] timeTableSplit = timeElement.get("list").getAsString().split("[()]");
                    ArrayList<Integer> timeTableMin = new ArrayList();
                    boolean toggle = true;
                    System.out.print(timeTableHour + " ");
                    for(String data : timeTableSplit) {
                        data = data.replace(" ", "");
                        System.out.print(data + " ");
                        if (toggle == true){
                            timeTableMin.add(Integer.parseInt(data));
                            toggle = false;
                        }
                        else toggle = true;
                    }
                    System.out.println();
                    //
                    boolean set = false;
                    if(timeTableHour < inputHour) continue;
                    else if(timeTableHour == inputHour){
                        for(int sub_minute : timeTableMin) {
                            if (sub_minute >= inputMinute) {
                                resHour = timeTableHour;
                                resMinute = sub_minute;
                                set = true;
                                break;
                            }
                        }
                    }
                    else{
                        resHour = timeTableHour;
                        resMinute = timeTableMin.get(0);
                        set = true;
                    }
                    if(set == true) break;
                }
                model m = new model();
                hour = resHour;
                minute = resMinute;
                currentHourArray.add(hour);
                currentMinArray.add(minute);

                int setline = -1;
                for (int i = 0; i < stationArray.size(); i++) {
                    JsonObject object = (JsonObject) stationArray.get(i);
                    String current_Station = object.get("startName").getAsString();
                    for (String name : exStation)
                        if (current_Station.equals(name)) {
                            setline++;
                            break;
                        }
                    StationNMArray.add(current_Station);

                    currentPredict.add(modelPredict(current_Station, wayNMArray.get(setline), timeTableDayNum,
                            currentHourArray.get(i), currentMinArray.get(i)));


                    currentHourArray.add(hour);
                    currentMinArray.add(minute + object.get("travelTime").getAsInt());
                    if (currentMinArray.get(i + 1) >= 60) {
                        currentHourArray.remove(i + 1);
                        currentHourArray.add(hour + currentMinArray.get(i + 1) / 60);
                        currentMinArray.remove(i + 1);
                        currentMinArray
                                .add((minute + object.get("travelTime").getAsInt()) % 60); // 60분 이상으로 나옴 수정
                    }
                }

                StationNMArray.add(endStationNM);
//                currentPredict.add(m.modelPredict(endStationNM, wayNMArray.get(setline), dayNum, currentHourArray.get(stationArray.size()), currentMinArray.get(stationArray.size()), ));
                currentPredict.add(modelPredict(endStationNM, wayNMArray.get(setline), timeTableDayNum,
                        currentHourArray.get(stationArray.size()), currentMinArray.get(stationArray.size())));
                makeCourse();
            }

            @Override
            public void onError(int i, String s, API api) {
                //tv_data2.setText("API : " + api.name() + "\n" + "error");
            }
        };

        switch(dayOfWeek){
            case "평일":
                dow = "OrdList";
                break;
            case "토요일":
                dow = "SatList";
                break;
            case "일요일":
                dow = "SunList";
                break;
        }
        if(wayCode.equals("1")) upDown = "up";
        else upDown = "down";
        timeTableDayNum = dayNum;
        inputHour = thishour;
        inputMinute = thisminute;
        odsayService.requestSubwayTimeTable(stationID, wayCode, onResultCallbackListener);
    }
    //////////////////////////////////

    public float modelPredict(String StationNM, String line, int day, int hour, int minute) {
        hour = (int)hour/2;
        AssetManager assetManager = getApplication().getAssets();
        ArrayList<String[]> records = new ArrayList<String[]>();
        ArrayList<Integer> model_list = new ArrayList<Integer>();
        Scanner scan = null;
        try {
            scan = new Scanner(assetManager.open(StationNM + ".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //홀수 내선 짝수 외선
        if (line.equals("외선순환"))
            scan.nextLine();
        while (scan.hasNext()) {
            String[] record;
            record = scan.nextLine().split(",");
            records.add(record);
            if (scan.hasNext())
                scan.nextLine();
        }

        //String -> int
        for (String[] temp : records) {
            for (String temp1 : temp) {
                int val = new BigDecimal(temp1).intValue();
                model_list.add(val);
            }
        }

        //model 생성 i는 x축
        int[] model = new int[model_list.size()];
        for (int i = 0; i < model.length; i++) {
            model[i] = model_list.get(i).intValue();
//            Log.i("model", "modle[i]:"+model[i]+"\n");
        }

        int x1 = 0;
        int x2 = 0;
        if (day == 2 || day == 3 || day == 4 || day == 5 || day == 6) { //평일
            x1 = hour;
            x2 = hour + 1;
        } else if (day == 7) {  //토요일
            x1 = 20 + hour;
            x2 = 20 + hour + 1;
        } else if (day == 1) {  //일요일
            x1 = 40 + hour;
            x2 = 40 + hour + 1;
        }

        float y1 = model[x1];
        float y2 = model[x2];
        float a = (y2 - y1); //(x2-x1)은 항상1
        float b = y2 - a;

        // 24분에 해당하는점을 구한다.

        float x = (float) (1.0 / 60.0 * minute);
        float y = a * x + b;



//        System.out.println("x1 :" + x1);
//        System.out.println("x2 :" + x2);
//        System.out.println("y1 :" + y1);
//        System.out.println("y2 :" + y2);
//        System.out.println("a :" + a);
//        System.out.println("b :" + b);
//        System.out.println("x :" + x);
//        System.out.print(y);

        return y;
    }

    void makeCourse() {

        ArrayList<String> stationRoute = new ArrayList<String>();
        /*
         * stationRoute.add(exStation+""); stationRoute.add(endStationNM + "\n");
         * stationRoute.add("\nStation: " + StationNMArray + "\nDate: " + year + "년 " +
         * month + "월 " + day + "일" + "\nTime: " + currentHourArray + "시 " +
         * currentMinArray + "분" + "\n예측값: " + currentPredict+ "\n");
         */

        String station_in_course = "";
        for (String data : exStation)
            station_in_course = station_in_course + data + " ";
        station_in_course = station_in_course + endStationNM + "\n";

        SpannableStringBuilder ssb;
        for (int i = 0; i < StationNMArray.size(); i++) {
            String current_Station = StationNMArray.get(i);
            station_in_course = station_in_course + "\n역명: " + current_Station + "\n날짜: " + year + "년 " + month + "월 "
                    + day + "일" + "\n시간: " + currentHourArray.get(i) + "시 " + currentMinArray.get(i) + "분" + "\n포화도: "
                    + Math.round(currentPredict.get(i)) + "\n";

        }
        ssb = new SpannableStringBuilder(station_in_course);
        String word = ssb.toString() + "   ";
        String find = "포화도: ";
        for (int index = word.indexOf(find); index >= 0; index = word.indexOf(find, index + 1)) {

            int start = index;
            int end = start + find.length() + 3;
            String t1 = word.substring(start, end);
            String t2 = word.substring(end - 3, end);
            t2 = t2.replaceAll("\\s+", "");
//            Log.i("start", start + "\n");
//            Log.i("end", end + "\n");
//            Log.i("t1", t1 + "\n");
//            Log.i("t2", t2 + "\n");
            int t = Integer.parseInt(t2);
//            Log.i("t", t + "\n");

            if (t > 0 && t <= 50) {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (t > 50 && t <= 100) {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcc66")), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (t > 100 && t < 150) {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#fdff00")), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (t < 200) {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff")), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), start, end,
            // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end,
            // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // ssb.setSpan(new RelativeSizeSpan(1.3f), start, end,
            // SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            // }
            tv_data2.setText(ssb);
            //
            // TextView textView = findViewById(R.id.tv_data2);
            // String content = textView.getText().toString();
            // SpannableString spannableString = new SpannableString(content);
            //
            //
            // String word = "예측값: ";
            // int start = content.indexOf(word);
            // int end = start + word.length()+3;
            //
            // spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")),
            // start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end,
            // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // spannableString.setSpan(new RelativeSizeSpan(1.3f), start, end,
            // SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            //
            // textView.setText(spannableString);
        }
    }
}
