package com.estsoft.r_subway_android.Parser;

import java.util.List;

/**
 * Created by estsoft on 2016-07-19.
 */
public class StationTag {

    private static final String TAG = "StationTag";

    private String name;
    private String id;
    private List<String> nextCost;
    private List<String> prevCost;
    private List<String> exCost;


    public StationTag(String name, String id, List<String> nextCost, List<String> prevCost, List<String> exCost) {
        this.name = name;
        this.id = id;
        this.nextCost = nextCost;
        this.prevCost = prevCost;
        this.exCost = exCost;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getNextCost() {
        return nextCost;
    }

    public void setNextCost(List<String> nextCost) {
        this.nextCost = nextCost;
    }

    public List<String> getPrevCost() {
        return prevCost;
    }

    public void setPrevCost(List<String> prevCost) {
        this.prevCost = prevCost;
    }

    public List<String> getExCost() {
        return exCost;
    }

    public void setExCost(List<String> exCost) {
        this.exCost = exCost;
    }


    @Override
    public String toString() {
        return "StationTag{" +
                "exCost=" + exCost +
                ", prevCost=" + prevCost +
                ", nextCost=" + nextCost +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
