package com.estsoft.r_subway_android.Repository.StationRepository;

import android.graphics.PointF;
import android.util.Log;

import java.util.Collections;
import java.util.List;

/**
 * Created by estsoft on 2016-06-30.
 */
public class SemiStation {

    private static final String TAG = "SemiStation";

    private String id = "";
    private int laneNumber = -1;
    private PointF position = null;
    private String name = "";
    private List<Integer> laneNumbers = null;

    public SemiStation(String id, int laneNumber, PointF position, String name) {
        this.id = id;
        this.laneNumber = laneNumber;
        this.position = position;
        this.name = name;
    }

    public void sortLaneNumbers() {
        Collections.sort(this.laneNumbers);
        Collections.reverse(this.laneNumbers);
        if (this.getIntId() == 133) {
            for ( int num : laneNumbers  ) {
//                Log.d(TAG, "sortLaneNumbers: + " + num);
            }
        }
    }

    public List<Integer> getLaneNumbers() {
        return laneNumbers;
    }

    public void setLaneNumbers(List<Integer> laneNumbers) {
        this.laneNumbers = laneNumbers;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    public int getIntId(){
        return Integer.parseInt(id);
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
//        Log.e("TEST", position.toString());
    }


}
