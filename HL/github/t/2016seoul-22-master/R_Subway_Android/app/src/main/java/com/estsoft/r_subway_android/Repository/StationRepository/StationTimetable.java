package com.estsoft.r_subway_android.Repository.StationRepository;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016-07-18.
 */
public class StationTimetable {
    private Context context;
    private int stationID;
    private String stationName;
    private String UpWay;
    private String DownWay;
    // 시간(5~25시)
    private ArrayList<Integer> stationHour = new ArrayList<>();
    private ArrayList<HashMap<String, Object>>[] ordUpWayLdx = new ArrayList[20];
    private ArrayList<HashMap<String, Object>>[] ordDownWayLdx = new ArrayList[20];
    private ArrayList<HashMap<String, Object>>[] satUpWayLdx = new ArrayList[20];
    private ArrayList<HashMap<String, Object>>[] satDownWayLdx = new ArrayList[20];
    private ArrayList<HashMap<String, Object>>[] sunUpWayLdx = new ArrayList[20];
    private ArrayList<HashMap<String, Object>>[] sunDownWayLdx = new ArrayList[20];

    // 시간(00행)
    private HashMap<String, Object> list = new HashMap<>();


    public StationTimetable() {
        for (int i = 0; i < 20; i++) {
            stationHour.add(i + 5);
            ordUpWayLdx[i] = new ArrayList<>();
            ordDownWayLdx[i] = new ArrayList<>();

            satUpWayLdx[i] = new ArrayList<>();
            satDownWayLdx[i] = new ArrayList<>();

            sunUpWayLdx[i] = new ArrayList<>();
            sunDownWayLdx[i] = new ArrayList<>();

        }

    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getUpWay() {
        return UpWay;
    }

    public void setUpWay(String UpWay) {
        this.UpWay = UpWay;
    }

    public String getDownWay() {
        return DownWay;
    }

    public void setDownWay(String DownWay) {
        this.DownWay = DownWay;
    }


    public HashMap<String, Object> getList() {
        return list;
    }

    public void setList(HashMap<String, Object> list) {
        this.list = list;
    }

    public ArrayList<HashMap<String, Object>>[] getOrdUpWayLdx() {
        return ordUpWayLdx;
    }

    public void setOrdUpWayLdx(ArrayList<HashMap<String, Object>>[] ordUpWayLdx) {
        this.ordUpWayLdx = ordUpWayLdx;
    }

    public ArrayList<HashMap<String, Object>>[] getOrdDownWayLdx() {
        return ordDownWayLdx;
    }

    public void setOrdDownWayLdx(ArrayList<HashMap<String, Object>>[] ordDownWayLdx) {
        this.ordDownWayLdx = ordDownWayLdx;
    }

    public ArrayList<HashMap<String, Object>>[] getSatUpWayLdx() {
        return satUpWayLdx;
    }

    public void setSatUpWayLdx(ArrayList<HashMap<String, Object>>[] satUpWayLdx) {
        this.satUpWayLdx = satUpWayLdx;
    }

    public ArrayList<HashMap<String, Object>>[] getSatDownWayLdx() {
        return satDownWayLdx;
    }

    public void setSatDownWayLdx(ArrayList<HashMap<String, Object>>[] satDownWayLdx) {
        this.satDownWayLdx = satDownWayLdx;
    }

    public ArrayList<HashMap<String, Object>>[] getSunUpWayLdx() {
        return sunUpWayLdx;
    }

    public void setSunUpWayLdx(ArrayList<HashMap<String, Object>>[] sunUpWayLdx) {
        this.sunUpWayLdx = sunUpWayLdx;
    }

    public ArrayList<HashMap<String, Object>>[] getSunDownWayLdx() {
        return sunDownWayLdx;
    }

    public void setSunDownWayLdx(ArrayList<HashMap<String, Object>>[] sunDownWayLdx) {
        this.sunDownWayLdx = sunDownWayLdx;
    }

    public ArrayList<Integer> getStationHour() {
        return stationHour;
    }

    public void setStationHour(ArrayList<Integer> stationHour) {
        this.stationHour = stationHour;
    }
}
