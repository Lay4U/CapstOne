package com.estsoft.r_subway_android.Parser;

/**
 * Created by estsoft on 2016-06-22.
 */
public class CircleTag {

    private final static String TAG = "CircleTag";

    private Float positionX;
    private Float positionY;
    private Float radius;
    private int laneNumber;
    private String id;

    //잠시만 지하철 이름
    private String otherFactor;

    public int getLaneNumber() {
        return laneNumber;
    }

    public Float getPositionX() {
        return positionX;
    }

    public Float getPositionY() {
        return positionY;
    }

    public String getId() {
        return id;
    }

    public Float getRadius() {
        return radius;
    }

    public String getOtherFactor() {
        return otherFactor;
    }

    public CircleTag(Float positionX, Float positionY, Float radius, String id, int laneNumber, String otherFactor) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.id = id;
        this.radius = radius;
        this.laneNumber = laneNumber;
        this.otherFactor = otherFactor;
    }

    public CircleTag(Float positionX, Float positionY, int laneNumber, String id) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.laneNumber = laneNumber;
        this.id = id;
    }

    public CircleTag(Float positionX, Float positionY, int laneNumber, String id, String otherFactor) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.laneNumber = laneNumber;
        this.id = id;
        this.otherFactor = otherFactor;
    }

    @Override
    public String toString() {
        return "CircleTag{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", radius=" + radius +
                ", fill='" + laneNumber + '\'' +
                ", id='" + id + '\'' +
                ", otherFactor='" + otherFactor + '\'' +
                '}';
    }
}
