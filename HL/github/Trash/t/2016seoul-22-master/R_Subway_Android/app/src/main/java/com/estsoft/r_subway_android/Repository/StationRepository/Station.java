package com.estsoft.r_subway_android.Repository.StationRepository;

import android.graphics.PointF;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by estsoft on 2016-06-30.
 */
public class Station extends TtfNode {

    private boolean accidentInfo;

    private int index;
    private int stationID;
    private String stationName = "";
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

    private boolean isTransfer;

    private List<Station> exStations;
    private List<Station> prevStations;
    private List<Station> nextStations;

    private List<Integer> exStationIDs;
    private List<Integer> prevStationIDs;
    private List<Integer> nextStationIDs;

    private PointF mapPoint = null;
    private int conLevel;

    private List<Integer> exLaneNumbers;

    private RealmStation realmStation;

    private Calendar arriveTime;
    private Calendar[] arriveTimes;
    private boolean isExpress;

    private List<String> timeStringList;

    private int TrainsPerHour;

    private Integer congestionNum = null;
    private Integer congestionFlag = null;
    private Boolean accidentFlag = null;


    public Station( RealmStation rst, PointF mapPoint, int conLevel ) {
        super( conLevel, rst.getStationID() + "", null );
        realmStation = rst;
        this.mapPoint = mapPoint;
        this.conLevel = conLevel;
        copyFromRealmStationLite( );
        copyExStationLite();
        copyNextStationLite();
        copyPrevStationLite();

        copyExStationID();
        copyPrevStationID();
        copyNextStationID();
    }

    public Station ( RealmStation rst, int conLevel ) {
        super ( conLevel, rst.getStationID() + "", null );
        realmStation = rst;
        this.conLevel = conLevel;
        copyFromRealmStationLite( );
    }

    private Station() {
        super( 0, 0+"", null );
    }
    public static Station getEmptyStation() {
        return new Station();
    }

    public void cleanStation() {
        this.accidentFlag = null;
        this.congestionFlag = null;
        this.congestionNum = null;
    }

    public void copyFromRealmStationLite(  ) {

        index = realmStation.getIndex();
        stationID = realmStation.getStationID();
        setStationId1( stationID + "" );

        stationName = realmStation.getStationName();
        laneType = realmStation.getLaneType();
        laneName = realmStation.getLaneName();
        xPos = realmStation.getxPos();
        yPos = realmStation.getyPos();
        address = realmStation.getAddress();
        tel = realmStation.getTel();
        platform = realmStation.getPlatform();
        meetingPlace = realmStation.getMeetingPlace();
        restroom = realmStation.getRestroom();
        offDoor = realmStation.getOffDoor();
        crossOver = realmStation.getCrossOver();
        publicPlace = realmStation.getPublicPlace();
        handicapCount = realmStation.getHandicapCount();
        parkingCount = realmStation.getParkingCount();
        bicycleCount = realmStation.getBicycleCount();
        civilCount = realmStation.getCivilCount();

//        List<Pair< Station, Integer >> pairList = new ArrayList<>();
//        Pair<Station, Integer> pair = new Pair<>(station, cost);
       // Pair< Station, Integer > pair = new Pair<>(  );

    }

    public void copyExStationID () {
        exStationIDs = new ArrayList<>();
        for (RealmStation loRst : realmStation.getExStations()) {
            //Lite Copy Only ID
            exStationIDs.add( loRst.getStationID() );
        }
    }

    public void copyPrevStationID () {
        prevStationIDs = new ArrayList<>();
        for (RealmStation loRst : realmStation.getPrevStations()) {
            //Lite Copy Only ID
            prevStationIDs.add( loRst.getStationID() );
        }
    }

    public void copyNextStationID () {
        nextStationIDs = new ArrayList<>();
        for (RealmStation loRst : realmStation.getNextStations()) {
            //Lite Copy Only ID
            nextStationIDs.add( loRst.getStationID() );
        }
    }


    public void copyPrevStationLite ( ) {
        prevStations = new ArrayList<>();
        for ( RealmStation lowRst : realmStation.getPrevStations() ) {
            //LiteCopy
            Station copied = new Station( lowRst, conLevel );
            prevStations.add(copied);
        }
    }
    public void copyNextStationLite ( ) {
        nextStations = new ArrayList<>();
        for ( RealmStation lowRst : realmStation.getNextStations() ) {
            //LiteCopy
            Station copied = new Station( lowRst, conLevel );
            nextStations.add(copied);
        }
    }

    public void copyExStationLite( ) {
        exStations = new ArrayList<>();
        boolean flag = false;
        for ( RealmStation lowRst : realmStation.getExStations() ) {
            //LiteCopy
            flag = true;
            Station copied = new Station( lowRst, conLevel );
            exStations.add(copied);
        }

        isTransfer = flag;

    }


    //getter setters.

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

    public PointF getGeoPoint() {
        return new PointF( (float)xPos, (float)yPos );
    }

    public void setGeoPoint( PointF geoPoint ){
        this.xPos = geoPoint.x;
        this.yPos = geoPoint.y;
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

    public List<Station> getExStations() {
        return exStations;
    }

    public void setExStations(List<Station> exStations) {
        this.exStations = exStations;
    }

    public List<Station> getPrevStations() {
        return prevStations;
    }

    public void setPrevStations(List<Station> prevStations) {
        this.prevStations = prevStations;
    }

    public List<Station> getNextStations() {
        return nextStations;
    }

    public void setNextStations(List<Station> nextStations) {
        this.nextStations = nextStations;
    }

    public List<Integer> getPrevStationIDs() {        return prevStationIDs;    }

    public List<Integer> getNextStationIDs() {        return nextStationIDs;    }

    public List<Integer> getExStationIDs() {        return exStationIDs;    }

    //Ignore Variable

    public PointF getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(PointF mapPoint) {
        this.mapPoint = mapPoint;
    }

    public Calendar getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Calendar arriveTime) {
        this.arriveTime = arriveTime;
    }

    public boolean isExpress() {
        return isExpress;
    }

    public void setExpress(boolean express) {
        isExpress = express;
    }

    public boolean isAccidentInfo() {
        return accidentInfo;
    }

    public void setAccidentInfo(boolean accidentInfo) {
        this.accidentInfo = accidentInfo;
    }

    public List<String> getTimeStringList() {
        return timeStringList;
    }

    public void setTimeStringList(List<String> timeStringList) {
        this.timeStringList = timeStringList;
    }

    public int getTrainsPerHour() {
        return TrainsPerHour;
    }

    public void setTrainsPerHour(int trainsPerHour) {
        TrainsPerHour = trainsPerHour;
    }

    @Override
    public int getConLevel() {
        return conLevel;
    }

    public void setConLevel(int conLevel) {
        this.conLevel = conLevel;
    }

    public Integer getCongestionNum() {
        return congestionNum;
    }

    public void setCongestionNum(Integer congestionNum) {
        this.congestionNum = congestionNum;
    }

    public int getCongestionFlag() {
        return congestionFlag;
    }

    public void setCongestionFlag(int congestionFlag) {
        this.congestionFlag = congestionFlag;
    }

    public Boolean getAccidentFlag() {
        return accidentFlag;
    }

    public void setAccidentFlag(Boolean accidentFlag) {
        this.accidentFlag = accidentFlag;
    }

    public Calendar[] getArriveTimes() {
        if (arriveTimes == null) arriveTimes = new GregorianCalendar[3];
        return arriveTimes;
    }
}

