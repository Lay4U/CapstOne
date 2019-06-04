package com.example.mysubwayproject;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

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

    ArrayList<String> StationNMArray = new ArrayList<>();
    ArrayList<String> exStation = new ArrayList<>();
    ArrayList<String> wayNMArray = new ArrayList<>();
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

        TextView textPageNM = (TextView)findViewById(R.id.start_and_end);
        textPageNM.setText(startStationNM + "    "+   endStationNM);
        findStationID();

        btnSubway = (Button)findViewById(R.id.complete);
        btnSubway.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        this.finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
                for (int i = 0; i < stationArray.size(); i++) {//수정필요!!!!
                    JsonObject object = (JsonObject) stationArray.get(i);
                    stationName = object.get("stationName").getAsString();
                    stationID = object.get("stationID").getAsString();
                    if(StationID.equals("null") && stationName.equals(startStationNM)) {
                        break;
                    }
                    else if(!StationID.equals("null") && stationName.equals(endStationNM)){
                        break;
                    }
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
        if(startStationNM.equals("광교")){
            odsayService.requestSearchStation("광교(경기대)", "1000", "2", "10", "0", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(startStationNM.equals("광교중앙")){
            odsayService.requestSearchStation("광교중앙(아주대)", "1000", "2", "10", "0", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(startStationNM.equals("신촌(경의중앙선)")){
            odsayService.requestSearchStation("신촌", "1000", "2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(startStationNM.equals("녹사평")){
            odsayService.requestSearchStation("녹사평(용산구청)", "1000", "2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(startStationNM.equals("봉화산")){
            odsayService.requestSearchStation("봉화산(서울의료원)", "1000", "2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else {
            odsayService.requestSearchStation(startStationNM, "1000", "2", "10", "0", "127.0363583:37.5113295", onResultCallbackListener);
        }

        if(endStationNM.equals("광교")){
            odsayService.requestSearchStation("광교(경기대)", "1000", "2", "10", "0", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(endStationNM.equals("광교중앙")) {
            odsayService.requestSearchStation("광교중앙(아주대)", "1000", "2","10","0","127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(endStationNM.equals("신촌(경의중앙선)")){
            odsayService.requestSearchStation("신촌", "1000", "2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(endStationNM.equals("녹사평")){
            odsayService.requestSearchStation("녹사평(용산구청)", "1000", "2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else if(endStationNM.equals("봉화산")){
            odsayService.requestSearchStation("봉화산(서울의료원)", "1000", "2", "10", "1", "127.0363583:37.5113295", onResultCallbackListener);
        }
        else {
            odsayService.requestSearchStation(endStationNM, "1000", "2","10","0","127.0363583:37.5113295", onResultCallbackListener);
        }
    }
    protected void shortestPathSearching(String startStationID, String endStationID) {

        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener(){
            @Override
            public void onSuccess(ODsayData odsayData, API api) {
                jsonObject = odsayData.getJson();
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObj = (JsonObject) jsonParser.parse(jsonObject.toString());
                JsonObject resultObj = (JsonObject) jsonObj.get("result");

                JsonObject driveInfoSetObj = (JsonObject) resultObj.get("driveInfoSet");
                //JsonObject exChangeInfoSetObj = (JsonObject) resultObj.get("exChangeInfoSet");
                JsonObject stationSetObj = (JsonObject) resultObj.get("stationSet");

                JsonArray driveInfoArray = (JsonArray) driveInfoSetObj.get("driveInfo");
                //JsonArray exChangeInfoArray = (JsonArray) exChangeInfoSetObj.get("exChangeInfo");
                JsonArray stationArray = (JsonArray) stationSetObj.get("stations");

                String dayOfWeek;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month-1, day);
                int dayNum = cal.get(Calendar.DAY_OF_WEEK);

                switch(dayNum){
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
                System.out.println(dayOfWeek);
                for (int i = 0; i < driveInfoArray.size(); i++) {
                    JsonObject object = (JsonObject) driveInfoArray.get(i);
                    exStation.add(object.get("startName").getAsString());
                    wayNMArray.add(object.get("wayName").getAsString());
                }

                currentHourArray.add(hour);
                currentMinArray.add(minute);

                int setline = -1;
                for (int i = 0; i < stationArray.size(); i++) {
                    JsonObject object = (JsonObject) stationArray.get(i);
                    String current_Station = object.get("startName").getAsString();
                    for(String name : exStation)
                        if(current_Station.equals(name)){
                            setline++;
                            break;
                        }
                    StationNMArray.add(current_Station);
                    currentPredict.add(modelPredict(current_Station, dayOfWeek, wayNMArray.get(setline),currentHourArray.get(i), currentMinArray.get(i)));
                    currentHourArray.add(hour);
                    currentMinArray.add(minute + object.get("travelTime").getAsInt());
                    if(currentMinArray.get(i+1) >= 60) {
                        currentHourArray.remove(i + 1);
                        currentHourArray.add(hour + currentMinArray.get(i + 1) / 60);
                        currentMinArray.remove(i + 1);
                        currentMinArray.add((minute + object.get("travelTime").getAsInt() % 60)-60); /*60분 이상으로 나옴 수정*/
                    }
                }
                StationNMArray.add(endStationNM);
                currentPredict.add(modelPredict(endStationNM, dayOfWeek, wayNMArray.get(setline),currentHourArray.get(stationArray.size()), currentMinArray.get(stationArray.size())));
                makeCourse();
            }

            @Override
            public void onError(int i, String s, API api) {
                tv_data2.setText("API : " + api.name() + "\n" + "error");
            }
        };

        odsayService.requestSubwayPath("1000", startStationID, endStationID, "1", onResultCallbackListener);

    }
    //////////////////////////////////

    public float modelPredict(String StationNM, String dayOfWeek, String line, int hour, int minute)
    {
        AssetManager assetManager = getApplication().getAssets();
        ArrayList<String[]> model_list = new ArrayList<>();
        try {

            InputStreamReader is = new InputStreamReader(assetManager.open("line2.csv"));
            BufferedReader br = new BufferedReader(is);

            CSVReader reader = new CSVReader(br);
            for(String[] data : reader.readAll()){
                model_list.add(data);
            }
        } catch (IOException e) {
            System.out.println("can not found .csv");
        }

        ArrayList<String[]> model_list_setStationNM = new ArrayList<>();
        for(String[] data : model_list){
            if(data[2].equals(StationNM))
                model_list_setStationNM.add(data);
        }
        if(model_list_setStationNM.size() == 0)
            return 0;

        ArrayList<String[]> model_list_setDate = new ArrayList<>();
        for(String[] data : model_list_setStationNM){
            if(data[0].equals(dayOfWeek))
                model_list_setDate.add(data);
        }

        String[] model_setComplet = new String[29];
        for(String[] data : model_list_setDate){
            if(data[1].equals(line))
                model_setComplet = data;
        }

        float res;
        res = Float.parseFloat(model_setComplet[hour+3]) + Float.parseFloat(model_setComplet[hour+4]);
        res = res/60*minute;
        return res;
    }

    void makeCourse() {

        ArrayList<String> stationRoute = new ArrayList<String>();
/*
        stationRoute.add(exStation+"");
        stationRoute.add(endStationNM + "\n");
        stationRoute.add("\nStation: " + StationNMArray
                + "\nDate: " + year + "년 " +  month + "월 " +  day + "일"
                + "\nTime: " + currentHourArray + "시 " + currentMinArray + "분"
                + "\n예측값: " + currentPredict+ "\n");
                */


        String station_in_course = "";
        for (String data : exStation)
            station_in_course = station_in_course + data + " ";
        station_in_course = station_in_course + endStationNM + "\n";


        SpannableStringBuilder ssb;
        for (int i = 0; i < StationNMArray.size(); i++) {
            String current_Station = StationNMArray.get(i);
            station_in_course = station_in_course + "\n역명: " + current_Station
                    + "\n날짜: " + year + "년 " + month + "월 " + day + "일"
                    + "\n시간: " + currentHourArray.get(i) + "시 " + currentMinArray.get(i) + "분"
                    + "\n포화도: " + Math.round(currentPredict.get(i)) + "\n";

        }
        ssb = new SpannableStringBuilder(station_in_course);
        String word = ssb.toString();
        String find = "포화도: ";
        for (int index = word.indexOf(find); index >= 0; index = word.indexOf(find, index + 1)) {

            int start = index;
            int end = start + find.length() + 3;

            String t2 = word.substring(end-3, end-1);
            int t = Integer.parseInt(t2);
            System.out.println(t);

            if (t >0 && t <=50) {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(t >50 && t <=100)
            {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcc66")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(t>100 && t < 150)
            {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#fdff00")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(t < 200)
            {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.5f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else
            {
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(1.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            
            
//            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ssb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ssb.setSpan(new RelativeSizeSpan(1.3f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

//        }
            tv_data2.setText(ssb);
//
//        TextView textView = findViewById(R.id.tv_data2);
//        String content = textView.getText().toString();
//        SpannableString spannableString = new SpannableString(content);
//
//
//        String word = "예측값: ";
//        int start = content.indexOf(word);
//        int end = start + word.length()+3;
//
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        textView.setText(spannableString);
        }
    }
}
