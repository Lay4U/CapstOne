package com.estsoft.r_subway_android.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-22.
 */
public class GroupTag {

    private List<StationTag> stationTags = null;
    private int laneNumber;
    private String laneName;

    public GroupTag(int laneNumber, String laneName) {
        this.laneNumber = laneNumber;
        this.laneName = laneName;
        this.stationTags = new ArrayList<>();
    }

    public void addStationTag ( String name, String id, List<String> next, List<String> prev, List<String> ex ){
        stationTags.add( new StationTag(name, id, next, prev,ex ) );
    }

    public List<StationTag> getStationTags() {
        return stationTags;
    }

    public void setStationTags(List<StationTag> stationTags) {
        this.stationTags = stationTags;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }
}
