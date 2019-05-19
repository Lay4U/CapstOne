package org.androidtown.home;

/**
 * Created by 민섭 on 2016-09-21.
 */


public class TransInformation {
    private String LEFT_line;
    private String RIGHT_line;
    private String stationName;
    private String LEFT_oc;
    private String RIGHT_oc;
    public TransInformation(String LEFT_line,String RIGHT_line,String LEFT_oc,String RIGHT_oc,String stationName){
        this.LEFT_line = LEFT_line;
        this.RIGHT_line = RIGHT_line;
        this.LEFT_oc = LEFT_oc;
        this.RIGHT_oc = RIGHT_oc;
        this.stationName = stationName;
    }

    public String getLeftOC(){
        return LEFT_oc;
    }
    public String getRightOC(){
        return RIGHT_oc;
    }
    public String getRightLine(){ //환승하기 이후의 line
        return RIGHT_line;
    }
    public String getStationName(){
        return stationName;
    }
}
