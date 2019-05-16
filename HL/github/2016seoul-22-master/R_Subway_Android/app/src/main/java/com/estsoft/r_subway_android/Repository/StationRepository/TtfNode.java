package com.estsoft.r_subway_android.Repository.StationRepository;

/**
 * Created by estsoft on 2016-06-30.
 */
public class TtfNode {

    //if the object is Route Class, Level should be -1
    private int conLevel = 0;

    // if the object id Route Class, both stationId should be not null
    // in case station, only stationId1 will be filled
    private String stationId1 = null;
    private String stationId2 = null;

    public TtfNode(int conLevel, String stationId1, String stationId2) {
        this.conLevel = conLevel;
        this.stationId1 = stationId1;
        this.stationId2 = stationId2;
    }

    public int getConLevel() {
        return conLevel;
    }

    public String getStationId1() {
        return stationId1;
    }

    public String getStationId2() {
        return stationId2;
    }

    public void setConLevel(int conLevel) {
        this.conLevel = conLevel;
    }

    public void setStationId1(String stationId1) {
        this.stationId1 = stationId1;
    }

    public void setStationId2(String stationId2) {
        this.stationId2 = stationId2;
    }
}
