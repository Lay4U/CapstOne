package com.estsoft.r_subway_android.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-22.
 */
public class GTag {

    private List<CircleTag> circleList = null;
    private String fill = "";
    private int laneNumber;

    public List<CircleTag> getCircleList() {
        return circleList;
    }

    public String getFill() {
        return fill;
    }

    public GTag(String fill, int laneNumber) {
        this.fill = fill;
        this.laneNumber = laneNumber;
        this.circleList = new ArrayList<>();
    }

    public void addCircle(String[] circleFactors) {

        CircleTag circle = new CircleTag(
                Float.parseFloat(circleFactors[0]),
                Float.parseFloat(circleFactors[1]),
                laneNumber,
                circleFactors[3],
                circleFactors[4]
        );
        circleList.add(circle);
    }

}
