package org.androidtown.home;

import java.util.ArrayList;

public class stInformation {

    private String line;
    private String stationName;
    private String oc;
    ArrayList<String> linearray = new ArrayList<String>();
    public stInformation(String line,String stationName,ArrayList linelist,String oc){
        this.line=line;
        this.stationName = stationName;
        this.linearray = linelist;
        this.oc= oc;
    }

    public String getLine(){
        return line;
    }
    public String getOc(){
        return oc;
    }
    public String getStationName(){
        return stationName;
    }
    public ArrayList<String> getLineArray(){
        return linearray;
    }

    public void setF(String line,String stname){
        GetOutCode newc = new GetOutCode(stname, line);
        newc.start();
        try{
            newc.join();
        }catch(Exception e){

        }
        this.line= line; this.stationName = stname; this.oc = newc.getREALFR();
    }

}
