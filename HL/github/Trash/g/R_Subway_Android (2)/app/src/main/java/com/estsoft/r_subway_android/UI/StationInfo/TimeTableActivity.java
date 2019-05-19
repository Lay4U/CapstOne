package com.estsoft.r_subway_android.UI.StationInfo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;

import java.util.ArrayList;

public class TimeTableActivity extends AppCompatActivity {
    String TAG = "TimeTableActivity";
    StationTimetable stationTimetable;
    ArrayList<Integer> stationIDs;
    int page;
    int curStationID;
    TimetableAdapter timetableAdapter;
    RecyclerView timetableRecyclerView;
    //   Staggered
    GridLayoutManager timetableGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // StationInfoFragment로부터 stationID와 현재 page받기
        stationIDs = getIntent().getIntegerArrayListExtra("stationIDs");
//        Log.d(TAG, "stationIDs: " + stationIDs.toString());
        page = getIntent().getIntExtra("page", 0);
//        Log.d(TAG, "page: " + page);

        //현재 page의 id 받기
        curStationID = stationIDs.get(page);
//        Log.d(TAG, "curStationID: " + curStationID);

        //timetable 정보 받기
        JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(this, curStationID);
        stationTimetable = jsonTimetableParser.getStationTimetable();
//        Log.d(TAG, "stationTimetable_stationName: " + stationTimetable.getStationName());
//        Log.d(TAG, "stationTimetable_Upway: " + stationTimetable.getUpWay());
//        Log.d(TAG, "stationTimetable_Downway: " + stationTimetable.getDownWay());
//        Log.d(TAG, "stationTimetable_OrdUp: " + stationTimetable.getOrdUpWayLdx()[0]);
//        Log.d(TAG, "stationTimetable_OrdDown: " + stationTimetable.getOrdDownWayLdx().length);
//        Log.d(TAG, "stationTimetable_SatUp: " + stationTimetable.getSatUpWayLdx().length);
//        Log.d(TAG, "stationTimetable_SatDown: " + stationTimetable.getSatDownWayLdx().length);
//        Log.d(TAG, "stationTimetable_SunUp: " + stationTimetable.getSunUpWayLdx().length);
//        Log.d(TAG, "stationTimetable_SunDown: " + stationTimetable.getSunDownWayLdx().length);
//        Log.d(TAG, "stationTimetable_stationhour" + stationTimetable.getStationHour().size());
//        Log.d(TAG, "stationTimetable_stationminute" + ":" + stationTimetable.getOrdUpWayLdx().length);


        ImageView timetableFinish = (ImageView) findViewById(R.id.timetable_finish);
        LinearLayout ord = (LinearLayout) findViewById(R.id.ord_layout);
        LinearLayout sat = (LinearLayout) findViewById(R.id.sat_layout);
        LinearLayout sun = (LinearLayout) findViewById(R.id.sun_layout);
        final ImageView ordUnCheck = (ImageView) findViewById(R.id.ord_unchecked);
        final ImageView ordCheck = (ImageView) findViewById(R.id.ord_checked);
        final ImageView satUnCheck = (ImageView) findViewById(R.id.sat_unchecked);
        final ImageView satCheck = (ImageView) findViewById(R.id.sat_checked);
        final ImageView sunUnCheck = (ImageView) findViewById(R.id.sun_unchecked);
        final ImageView sunCheck = (ImageView) findViewById(R.id.sun_checked);

        //default: ord
        TextView upWay = (TextView) findViewById(R.id.upway);
        TextView downWay = (TextView) findViewById(R.id.downway);

        upWay.setText(stationTimetable.getUpWay());
        downWay.setText(stationTimetable.getDownWay());


        timetableRecyclerView = (RecyclerView) findViewById(R.id.timetable);
        //staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        timetableGridLayoutManager = new GridLayoutManager(this, 2);
        timetableRecyclerView.setLayoutManager(timetableGridLayoutManager);

        ArrayList<String> timeOrd = timetable(stationTimetable, 0);

//        Log.d(TAG, "last_upway:" + stationTimetable.getUpWay());
//        Log.d(TAG, "last_downway:" + stationTimetable.getDownWay());


        timetableAdapter = new TimetableAdapter(getApplicationContext(), timeOrd);
        timetableRecyclerView.setAdapter(timetableAdapter);

        ordUnCheck.setVisibility(View.GONE);
        ordCheck.setVisibility(View.VISIBLE);
        satUnCheck.setVisibility(View.VISIBLE);
        satCheck.setVisibility(View.GONE);
        sunUnCheck.setVisibility(View.VISIBLE);
        sunCheck.setVisibility(View.GONE);


        ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordUnCheck.setVisibility(View.GONE);
                ordCheck.setVisibility(View.VISIBLE);
                satUnCheck.setVisibility(View.VISIBLE);
                satCheck.setVisibility(View.GONE);
                sunUnCheck.setVisibility(View.VISIBLE);
                sunCheck.setVisibility(View.GONE);


                ArrayList<String> timeOrd = timetable(stationTimetable, 0);

                timetableAdapter = new TimetableAdapter(getApplicationContext(), timeOrd);
                timetableRecyclerView.setAdapter(timetableAdapter);
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordUnCheck.setVisibility(View.VISIBLE);
                ordCheck.setVisibility(View.GONE);
                satUnCheck.setVisibility(View.GONE);
                satCheck.setVisibility(View.VISIBLE);
                sunUnCheck.setVisibility(View.VISIBLE);
                sunCheck.setVisibility(View.GONE);

                ArrayList<String> timeSat = timetable(stationTimetable, 1);


                timetableAdapter = new TimetableAdapter(getApplicationContext(), timeSat);
                timetableRecyclerView.setAdapter(timetableAdapter);
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordUnCheck.setVisibility(View.VISIBLE);
                ordCheck.setVisibility(View.GONE);
                satUnCheck.setVisibility(View.VISIBLE);
                satCheck.setVisibility(View.GONE);
                sunUnCheck.setVisibility(View.GONE);
                sunCheck.setVisibility(View.VISIBLE);


                ArrayList<String> timeSun = timetable(stationTimetable, 2);

                timetableAdapter = new TimetableAdapter(getApplicationContext(), timeSun);
                timetableRecyclerView.setAdapter(timetableAdapter);
            }
        });


        timetableFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public ArrayList<String> timetable(StationTimetable stationTimetable, int ordSatSun) {
        String upMinute = "";
        String downMinute = "";
        ArrayList<String> time = new ArrayList<>();

        switch (ordSatSun) {
            //sat
            case 1:
                for (int j = 0; j < 40; j++) {
//Up
                    if (upMinute != null) upMinute = "";
                    if (downMinute != null) downMinute = "";
                    if (j % 2 == 0) {
                        for (int i = 0; i < stationTimetable.getSatUpWayLdx()[j / 2].size(); i++) {
                            upMinute += stationTimetable.getStationHour().get(j / 2) + ":" + stationTimetable.getSatUpWayLdx()[j / 2].get(i).get("satUpWayLdx");
                            if((boolean)stationTimetable.getSatUpWayLdx()[j / 2].get(i).get("isExpress") == true) upMinute+="  급행";
                            if (i == stationTimetable.getSatUpWayLdx()[j / 2].size() - 1) {
                                upMinute +=  System.lineSeparator() + System.lineSeparator() + System.lineSeparator();

                            } else {
                                upMinute +=  System.lineSeparator()+ System.lineSeparator() ;
                            }
                        }

                        time.add(upMinute);
                    }
                    //         Down
                    else {
                        for (int i = 0; i < stationTimetable.getSatDownWayLdx()[(j - 1) / 2].size(); i++) {
                            downMinute += stationTimetable.getStationHour().get((j - 1) / 2) + ":" + stationTimetable.getSatDownWayLdx()[(j - 1) / 2].get(i).get("satDownWayLdx");
                            if((boolean)stationTimetable.getSatDownWayLdx()[(j - 1) / 2].get(i).get("isExpress") == true) downMinute+="  급행";
                            if (i == stationTimetable.getSatDownWayLdx()[(j - 1) / 2].size() - 1) {
                                downMinute +=  System.lineSeparator() + System.lineSeparator() + System.lineSeparator();
                            } else {
                                downMinute += System.lineSeparator()+ System.lineSeparator() ;
                            }
                        }
                        time.add(downMinute);
                    }
                }
                break;
            //sun
            case 2:
                for (int j = 0; j < 40; j++) {

                    if (upMinute != null) upMinute = "";
                    if (downMinute != null) downMinute = "";
                    if (j % 2 == 0) {
                        for (int i = 0; i < stationTimetable.getSunUpWayLdx()[j / 2].size(); i++) {
                            upMinute += stationTimetable.getStationHour().get(j / 2) + ":" + stationTimetable.getSunUpWayLdx()[j / 2].get(i).get("sunUpWayLdx");
                            if((boolean)stationTimetable.getSunUpWayLdx()[j / 2].get(i).get("isExpress") == true) upMinute+="  급행";
                            if (i == stationTimetable.getSunUpWayLdx()[j / 2].size() - 1) {
                                upMinute +=  System.lineSeparator() + System.lineSeparator() + System.lineSeparator();

                            } else {
                                upMinute +=  System.lineSeparator()+ System.lineSeparator() ;
                            }
                        }

                        time.add(upMinute);
                    }
                    //         Down
                    else {
                        for (int i = 0; i < stationTimetable.getSunDownWayLdx()[(j - 1) / 2].size(); i++) {
                            downMinute += stationTimetable.getStationHour().get((j - 1) / 2) + ":" + stationTimetable.getSunDownWayLdx()[(j - 1) / 2].get(i).get("sunDownWayLdx");
                            if((boolean)stationTimetable.getSunDownWayLdx()[(j - 1) / 2].get(i).get("isExpress") == true) downMinute+="  급행";
                            if (i == stationTimetable.getSunDownWayLdx()[(j - 1) / 2].size() - 1) {
                                downMinute +=  System.lineSeparator() + System.lineSeparator() + System.lineSeparator();
                            } else {
                                downMinute += System.lineSeparator()+ System.lineSeparator() ;
                            }
                        }
                        time.add(downMinute);
                    }
                }
                break;
            //ord
            default:
                for (int j = 0; j < 40; j++) {
                    if (upMinute != null) upMinute = "";
                    if (downMinute != null) downMinute = "";

                    if (j % 2 == 0) {
                        for (int i = 0; i < stationTimetable.getOrdUpWayLdx()[j / 2].size(); i++) {
                            upMinute += stationTimetable.getStationHour().get(j / 2) + ":" + stationTimetable.getOrdUpWayLdx()[j / 2].get(i).get("ordUpWayLdx");
                            if((boolean)stationTimetable.getOrdUpWayLdx()[j / 2].get(i).get("isExpress") == true) upMinute+="  급행";
                            if (i == stationTimetable.getOrdUpWayLdx()[j / 2].size() - 1) {
                                upMinute +=  System.lineSeparator() + System.lineSeparator() + System.lineSeparator();

                            } else {
                                upMinute +=  System.lineSeparator()+ System.lineSeparator() ;
                            }
                        }

                        time.add(upMinute);
                    }
                    //         Down
                    else {
                        for (int i = 0; i < stationTimetable.getOrdDownWayLdx()[(j - 1) / 2].size(); i++) {
                            downMinute += stationTimetable.getStationHour().get((j - 1) / 2) + ":" + stationTimetable.getOrdDownWayLdx()[(j - 1) / 2].get(i).get("ordDownWayLdx");
                            if((boolean)stationTimetable.getOrdDownWayLdx()[(j - 1) / 2].get(i).get("isExpress") == true) downMinute+="  급행";
                            if (i == stationTimetable.getOrdDownWayLdx()[(j - 1) / 2].size() - 1) {
                                downMinute +=  System.lineSeparator() + System.lineSeparator() + System.lineSeparator();
                            } else {
                                downMinute += System.lineSeparator()+ System.lineSeparator() ;
                            }
                        }
                        time.add(downMinute);
                    }
                }
                break;
        }
        return time;
    }

}
