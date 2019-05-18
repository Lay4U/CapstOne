package com.estsoft.r_subway_android.Repository.StationRepository;

import android.graphics.PointF;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by estsoft on 2016-07-13.
 */
public class RealmStation extends RealmObject {
    private int index;
    private int stationID;
    private String stationName;
    private int laneType;
    private String laneName;
    private double xPos;
    private double yPos;
    private String address;
    private String tel;
    private int platform;
    private int meetingPlace;
    private int restroom;
    private int offDoor;
    private int crossOver;
    private int publicPlace;
    private int handicapCount;
    private int parkingCount;
    private int bicycleCount;
    private int civilCount;
    private RealmList<RealmStation> exStations;
    private RealmList<RealmStation> prevStations;
    private RealmList<RealmStation> nextStations;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStationID() {
        return stationID;
    }

    public void setStationID(int stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getLaneType() {
        return laneType;
    }

    public void setLaneType(int laneType) {
        this.laneType = laneType;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(int meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public int getRestroom() {
        return restroom;
    }

    public void setRestroom(int restroom) {
        this.restroom = restroom;
    }

    public int getOffDoor() {
        return offDoor;
    }

    public void setOffDoor(int offDoor) {
        this.offDoor = offDoor;
    }

    public int getCrossOver() {
        return crossOver;
    }

    public void setCrossOver(int crossOver) {
        this.crossOver = crossOver;
    }

    public int getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(int publicPlace) {
        this.publicPlace = publicPlace;
    }

    public int getHandicapCount() {
        return handicapCount;
    }

    public void setHandicapCount(int handicapCount) {
        this.handicapCount = handicapCount;
    }

    public int getParkingCount() {
        return parkingCount;
    }

    public void setParkingCount(int parkingCount) {
        this.parkingCount = parkingCount;
    }

    public int getBicycleCount() {
        return bicycleCount;
    }

    public void setBicycleCount(int bicycleCount) {
        this.bicycleCount = bicycleCount;
    }

    public int getCivilCount() {
        return civilCount;
    }

    public void setCivilCount(int civilCount) {
        this.civilCount = civilCount;
    }

    public RealmList<RealmStation> getExStations() {
        return exStations;
    }

    public void setExStations(RealmList<RealmStation> exStations) {
        this.exStations = exStations;
    }

    public RealmList<RealmStation> getPrevStations() {
        return prevStations;
    }

    public void setPrevStations(RealmList<RealmStation> prevStations) {
        this.prevStations = prevStations;
    }

    public RealmList<RealmStation> getNextStations() {
        return nextStations;
    }

    public void setNextStations(RealmList<RealmStation> nextStations) {
        this.nextStations = nextStations;
    }

}
