package com.estsoft.r_subway_android.Repository.StationRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by estsoft on 2016-06-30.
 */
public class Route extends TtfNode {

    List<TtfNode> stationList = null;

    List<Calendar> stationTime = null;

    List<TtfNode> transferStations = null;

    public Route(int conLevel, String stationId1, String stationId2) {
        super(conLevel, stationId1, stationId2);
        transferStations = new ArrayList<>();
    }

    public List<TtfNode> getStationList() {
        return stationList;
    }

    public void setStationList(List<TtfNode> stationList) {
        this.stationList = stationList;
    }

    public List<TtfNode> getTransferStations() {
        return transferStations;
    }

    public void setTransferStations(List<TtfNode> transferStations) {
        this.transferStations = transferStations;
    }

    public List<Calendar> getStationTime() {
        return stationTime;
    }

    public void setStationTime(List<Calendar> stationTime) {
        this.stationTime = stationTime;
    }
}
