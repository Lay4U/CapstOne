package com.estsoft.r_subway_android.Controller;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.Parser.StationTag;
import com.estsoft.r_subway_android.Parser.TtfXmlParserCost;
import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by estsoft on 2016-07-13.
 */
public class StationController {

    private static final String TAG = "StationController";

    private static final int SHORT_ROUTE_WEIGHT = 0;

    private RealmResults<RealmStation> realmStationList = null;
    private Realm mRealm = null;

    private List<Station> deepCopiedStations = null;

    private ArrayList<Pair<Station, Integer>> adj[] = null;
    private TtfXmlParserCost costParser;
    private InputStream inputStream;

    private Context mContext;

    public StationController(Realm mRealm, InputStream in, Context context) throws IOException, XmlPullParserException {
        this.mRealm = mRealm;
        this.inputStream = in;
        realmStationList = mRealm.where(RealmStation.class).findAll();

        deepCopyRealmStation();

        adj = initializeAdj(SHORT_ROUTE_WEIGHT);

        mContext = context;

    }

    public void cleanStations() {
        if (deepCopiedStations == null) return;
        Iterator<Station> iter = deepCopiedStations.iterator();
        while(iter.hasNext()) {
            iter.next().cleanStation();
        }
    }

    private ArrayList<Pair<Station, Integer>>[] initializeAdj(int transferWeight) throws IOException, XmlPullParserException {
        if (costParser == null) {
            costParser = new TtfXmlParserCost(inputStream);
//            stationTags = costParser.getStationTags();
        }

        ArrayList<Pair<Station, Integer>> adj[] = new ArrayList[deepCopiedStations.size()];

        for (int i = 0; i < adj.length; i++) {

            adj[i] = new ArrayList<>();
            Station station = deepCopiedStations.get(i);
            String laneType = station.getLaneType() + "";

            StationTag stationTag = findMatchedStationTag(station.getStationID(), laneType);

//                Log.d(TAG, "initializeAdj: " + station.getLaneName() + station.getStationName());

            for (int j = 0; j < station.getPrevStations().size(); j++) {
//                Log.d(TAG, "initializeAdj: PREV " + station.getPrevStations().get(j).getStationName());
                int cost = Integer.parseInt(stationTag.getPrevCost().get(j));
                if (!(cost < 0)) {
                    adj[i].add(new Pair<Station, Integer>(station.getPrevStations().get(j), cost));
                }
            }
            for (int j = 0; j < station.getNextStations().size(); j++) {
//                Log.d(TAG, "initializeAdj: NEXT " + station.getNextStations().get(j).getStationName());
                int cost = Integer.parseInt(stationTag.getNextCost().get(j));
                if (!(cost < 0)) {
                    adj[i].add(new Pair<Station, Integer>(station.getNextStations().get(j), cost));
                }
            }
            for (int j = 0; j < station.getExStations().size(); j++) {
//                Log.d(TAG, "initializeAdj: EX " + station.getExStations().get(j).getStationName());
                adj[i].add(new Pair<Station, Integer>(station.getExStations().get(j), transferWeight));
//                Log.d(TAG, "initializeAdj: " + transferWeight);
            }

            if (adj[i].size() == 0) {
//                Log.d(TAG, "StationController: " + station.getStationName());
            } else {
//                Log.d(TAG, "StationController: " + adj[i].get(adj[i].size() - 1).second);
            }
        }
        return adj;
    }

    private StationTag findMatchedStationTag(int stationId, String laneType) {
        List<StationTag> boundary = costParser.getStationTags(Integer.parseInt(laneType));
        for (int i = 0; i < boundary.size(); i++) {
            if (stationId == Integer.parseInt(boundary.get(i).getId())) {
                return boundary.get(i);
            }
        }
//        Log.d(TAG, "initializeAdj: NULL!! " + stationId);
        return null;
    }

    public Station getStation(SemiStation semiStation) {

        // Sever Communication
        int conLevel = 0;
        // API

        //Realm
//        RealmStation matchRealmStation = null;
//        for ( RealmStation rst : realmStationList ) {
//            if ( rst.getStationName() == semiStation.getIntId() ) {
//                matchRealmStation = rst;
//                break;
//            }
//        }
//
//        Station station = new Station( matchRealmStation, semiStation.getPosition(), getConLevel(semiStation.getIntId()) );

        Station station = null;
        for (Station ss : deepCopiedStations) {
            if (semiStation.getIntId() == ss.getStationID()) {
                ss.setMapPoint(semiStation.getPosition());
                return ss;
            }
        }

        return null;

    }

//    public Station getLiteStation( SemiStation semiStation ) {
//        // Sever Communication
//        int conLevel = 0;
//        // API
//
//        //Realm
//        RealmStation matchRealmStation = null;
//        for ( RealmStation rst : realmStationList ) {
//            if ( rst.getStationName() == semiStation.getIntId() ) {
//                matchRealmStation = rst;
//                break;
//            }
//        }
//
//        Station station = new Station( matchRealmStation, getConLevel(semiStation.getIntId()) );
//
//        return  station;
//    }

//    public List<Station> getExStations( SemiStation semiStation ) {
//        List<Station> ExStations = new ArrayList<>();
//        RealmStation seedRealmStation = getRealmStationByID( semiStation.getIntId() );
//        ExStations.add( new Station(seedRealmStation, semiStation.getPosition(), getConLevel(semiStation.getIntId())) );
//        for ( RealmStation rst : seedRealmStation.getExStations() ) {
//            ExStations.add( new Station(rst, semiStation.getPosition(), getConLevel(semiStation.getIntId())) );
//        }
//
//        return ExStations;
//    }

//    public List<Station> getStationList (List<SemiStation> semiStationList) {
//        List<Station> stationList = new ArrayList<>();
//        for ( SemiStation ss : semiStationList ) {
//            stationList.add(getLiteStation( ss ));
//        }
//        return stationList;
//    }

    public Station getDeepStation(int id) {
        for (Station st : deepCopiedStations) {
            if (st.getStationID() == id) return st;
        }
        return null;
    }

    public void setSemiStationLaneNumber(List<SemiStation> ssl) {
        for (SemiStation ss : ssl) {
            ss.setLaneNumbers(getExNumbers(ss));
//            Log.d(TAG, "setSemiStationLaneNumber: " + ss.getName() + " // " + ss.getLaneNumbers());
        }
    }

    public List<Integer> getExNumbers(SemiStation semiStation) {
        List<Integer> exNumbers = new ArrayList<>();
        //아래 한줄은 XML index 를 Station Index 랑 싱크맞추면 빠름
        Station st = getDeepStation(semiStation.getIntId());
        exNumbers.add(st.getLaneType());
//        Log.d(TAG, "getExNumbers: " + st.getStationName() + " // " + st.getLaneName());
        for (int i = 0; i < st.getExStations().size(); i++) {
//            Log.d(TAG, "getExNumbers: " + st.getExStations().get(i).getStationName() + " // " + st.getExStations().get(i).getLaneName());
            exNumbers.add(st.getExStations().get(i).getLaneType());
        }
        return exNumbers;
    }

    private RealmStation getRealmStationByID(int ID) {
        RealmStation matchRealmStation = null;
        for (RealmStation rst : realmStationList) {
//            Log.d(TAG, "getRealmStationByID: " + rst.getStationID());
//            Log.d(TAG, "getRealmStationByID: ID = " + ID);
            if (rst.getStationID() == ID) {
                matchRealmStation = rst;
                break;
            }
        }
//        Log.d(TAG, "getRealmStationByID: ---------------------------------------------------------- ");
        return matchRealmStation;
    }

    private void deepCopyRealmStation() {

        deepCopiedStations = new ArrayList<>();

        for (RealmStation rst : realmStationList) {
            Station st = new Station(rst, null, getConLevel(rst.getStationID()));
            deepCopiedStations.add(st);
//            Log.e(TAG, "deepCopyRealmStation: ," + st.getStationID() + ", " + st.getStationName() + ", " + st.getAddress());
        }
//        for ( Station st : deepCopiedStations ) {
//            Log.d(TAG, "deepCopyRealmStation: " + st.getIndex());
//            if (st.getPrevStations().size() != 0)
//            Log.d(TAG, "deepCopyRealmStation: " +st.getStationName() + " / " +  st.getPrevStations().get(0).getIndex() );
//        }
    }


    public List<Station> getExStations(Station station) {
        int debugCount = 0;
        List<Station> exStations = new ArrayList<>();
        exStations.add(station);
        List<Integer> stationIDs = station.getExStationIDs();
        for (Station st : deepCopiedStations) {
            for (Integer exStID : stationIDs) {
                debugCount++;
                if (st.getStationID() == exStID) {
                    exStations.add(st);
                    break;
                }
            }
        }
//        Log.d(TAG, "getExStations: for count = " + debugCount);

        for (Station stationForTime : exStations) {
            stationForTime.setTimeStringList(getPrevNextStationTime(stationForTime));
        }

        return exStations;
    }

    public List<String> getPrevNextStationTime(Station station) {
        List<String> result = new ArrayList<>();

        JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(mContext, station.getStationID());
        StationTimetable stt = jsonTimetableParser.getStationTimetable();


        //지금 시간 세팅
        Calendar newCal = new GregorianCalendar();
//        newCal.set(Calendar.HOUR, 9);
//        newCal.set(Calendar.MINUTE, 55);
        int day = newCal.get(Calendar.DAY_OF_WEEK);
        String prevKey, nextKey;
        ArrayList<HashMap<String, Object>>[] prevTimeTable, nextTimeTable;
        switch (day) {
            case 1:
                prevTimeTable = stt.getSunUpWayLdx();
                nextTimeTable = stt.getSunDownWayLdx();
                prevKey = "sunUpWayLdx";
                nextKey = "sunDownWayLdx";
                break;
            case 7:
                prevTimeTable = stt.getSatUpWayLdx();
                nextTimeTable = stt.getSatDownWayLdx();
                prevKey = "satUpWayLdx";
                nextKey = "satDownWayLdx";
                break;
            default:
                prevTimeTable = stt.getOrdUpWayLdx();
                nextTimeTable = stt.getOrdDownWayLdx();
                prevKey = "ordUpWayLdx";
                nextKey = "ordDownWayLdx";
                break;
        }


//        Log.d(TAG,"prev :"+prevTimeTable[19]);
//        Log.d(TAG,"next :"+nextTimeTable[19]);

        int hour = newCal.get(Calendar.HOUR_OF_DAY);
        int hourIndexFirst = hour - 5 < 0 ? hour + 19 : hour - 5;

        int prevTrainsSize = prevTimeTable[hourIndexFirst].size();
        int nextTrainsSize = nextTimeTable[hourIndexFirst].size();

        station.setTrainsPerHour( prevTrainsSize + nextTrainsSize );

//        Log.d(TAG, "getPrevNextStationTime: TEST " + hour + " / " + station.getTrainsPerHour());

        Map prevFirst = null;
        Calendar prevCalendarFirst = null;
        Map prevSecond = null;
        String prevTerminalFirst = null;
        int prevHourFirst = -1;

        //station.getPrevStationIDs().size() > 0 && prevTimeTable[hourIndex].size() > 0 ???
        if (station.getPrevStations().size() > 0 && prevTimeTable.length > hourIndexFirst) {
            prevFirst = getCalendar(prevTimeTable, hourIndexFirst, prevKey, (Calendar) newCal.clone());

            if (prevFirst != null) {
                prevCalendarFirst = (Calendar) prevFirst.get("calendar");
                prevTerminalFirst = (String) prevFirst.get("terminal");
//                int prevHourIndexFirst = (int) prevFirst.get("hourIndex");
                prevHourFirst = prevCalendarFirst.get(Calendar.HOUR_OF_DAY);
                int prevHourIndex = prevHourFirst - 5 < 0 ? prevHourFirst + 19 : prevHourFirst - 5;
                //       Log.d(TAG,"prevHour"+prevHourFirst+"prevHourIndex"+prevhourIndexFirst );
                prevSecond = getCalendar(prevTimeTable, prevHourIndex, prevKey, (Calendar) prevCalendarFirst.clone());
            }
        }
        if (prevFirst != null && prevSecond != null) {
            // 둘다 안빔
            Calendar prevCalendarSecond = (Calendar) prevSecond.get("calendar");
            String prevTerminalSecond = (String) prevSecond.get("terminal");

            int hourGapFirst = prevCalendarFirst.get(Calendar.HOUR_OF_DAY) - newCal.get(Calendar.HOUR_OF_DAY);
            int timeGapFirst = prevCalendarFirst.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapFirst < 0) {
                timeGapFirst += 60;
                hourGapFirst -= 1;
            }
            if(hourGapFirst> 0) {
                result.add(prevTerminalFirst + "행 "+ hourGapFirst +"시간 "+ timeGapFirst + "분 후 ");
            }else{
//                Log.d(TAG, "Hour" +  prevCalendarFirst.get(Calendar.HOUR_OF_DAY));
//                Log.d(TAG, "Hour" +  prevCalendarSecond.get(Calendar.HOUR_OF_DAY));
                result.add(prevTerminalFirst + "행 "+ timeGapFirst + "분 후 ");

            }

            int hourGapSecond = prevCalendarSecond.get(Calendar.HOUR_OF_DAY) - newCal.get(Calendar.HOUR_OF_DAY);
            int timeGapSecond = prevCalendarSecond.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapSecond < 0) {
                hourGapSecond -= 1;
                timeGapSecond += 60;
            }

            if(hourGapSecond> 0) {
                result.add(prevTerminalSecond + "행 "+ hourGapSecond +"시간 "+ timeGapSecond + "분 후 ");
            }else{

                result.add(prevTerminalSecond + "행 " + timeGapSecond + "분 후 ");
            }

        } else if (prevFirst != null && prevSecond == null) {
            //하나만 빔
            int hourGapFirst = prevCalendarFirst.get(Calendar.HOUR_OF_DAY) - newCal.get(Calendar.HOUR_OF_DAY);
            int timeGapFirst = prevCalendarFirst.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapFirst < 0) {
                hourGapFirst -= 1;
                timeGapFirst += 60;
            }
            if(hourGapFirst> 0) {
                result.add(prevTerminalFirst + "행 "+ hourGapFirst +"시간 "+ timeGapFirst + "분 후 ");
            }else{
                result.add(prevTerminalFirst + "행 "+ timeGapFirst + "분 후 ");

            }

//            Log.d(TAG, "getPrevNextStationTime: " + prevCalendarFirst.get(Calendar.HOUR) + ":" + prevCalendarFirst.get(Calendar.MINUTE) + " to " + prevTerminalFirst );
//            Log.d(TAG, "getPrevNextStationTime: " + prevCalendarSecond.get(Calendar.HOUR) + ":" + prevCalendarSecond.get(Calendar.MINUTE) + " to " + prevTerminalSecond );

            result.add("-");
        } else {
            //둘다 빔
            result.add("-");
            result.add("-");
        }


        Map nextFirst = null;
        Calendar nextCalendarFirst = null;
        Map nextSecond = null;
        String nextTerminalFirst = null;
        if (station.getNextStationIDs().size() > 0 && nextTimeTable.length >  hourIndexFirst) {
            nextFirst = getCalendar(nextTimeTable, hourIndexFirst, nextKey, (Calendar) newCal.clone());
            if (nextFirst != null) {
                nextCalendarFirst = (Calendar) nextFirst.get("calendar");
                nextTerminalFirst = (String) nextFirst.get("terminal");
                int nextHour = nextCalendarFirst.get(Calendar.HOUR_OF_DAY);
                int nextHourIndex = nextHour - 5 < 0 ? nextHour  + 19 : nextHour - 5;
//                Log.d(TAG, "nextHour" + nextHour + "nextHourIndex" + nextHourIndex);
                nextSecond = getCalendar(nextTimeTable, nextHourIndex, nextKey, (Calendar) nextCalendarFirst.clone());
            }

        }
        if (nextFirst != null && nextSecond != null) {
            // 둘다 안빔

            Calendar nextCalendarSecond = (Calendar) nextSecond.get("calendar");
            String nextTerminalSecond = (String) nextSecond.get("terminal");
            int hourGapFirst = nextCalendarFirst.get(Calendar.HOUR_OF_DAY) - newCal.get(Calendar.HOUR_OF_DAY);
            int timeGapFirst = nextCalendarFirst.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapFirst < 0) {
                hourGapFirst -= 1;
                timeGapFirst += 60;
            }

            if(hourGapFirst> 0) {
                result.add(nextTerminalFirst + "행 "+ hourGapFirst +"시간 "+ timeGapFirst + "분 후 ");
            }else{
//                Log.d(TAG, "Hour" +  nextCalendarFirst.get(Calendar.HOUR_OF_DAY));
//                Log.d(TAG, "Hour" +  nextCalendarSecond.get(Calendar.HOUR_OF_DAY));
                result.add(nextTerminalFirst + "행 "+ timeGapFirst + "분 후 ");

            }

            int hourGapSecond = nextCalendarSecond.get(Calendar.HOUR_OF_DAY) - newCal.get(Calendar.HOUR_OF_DAY);
            int timeGapSecond = nextCalendarSecond.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapSecond < 0){
                hourGapSecond -= 1;
                timeGapSecond += 60;
            }

//            Log.d(TAG, "getPrevNextStationTime: " + nextCalendarFirst.get(Calendar.HOUR) + ":" + nextCalendarFirst.get(Calendar.MINUTE) + " to " + nextTerminalFirst );
            //           Log.d(TAG, "getPrevNextStationTime: " + nextCalendarSecond.get(Calendar.HOUR) + ":" + nextCalendarSecond.get(Calendar.MINUTE) + " to " + nextTerminalSecond );
            if(hourGapSecond> 0) {
                result.add(nextTerminalSecond + "행 "+ hourGapSecond +"시간 "+ timeGapSecond + "분 후 ");
            }else{
                result.add(nextTerminalSecond + "행 " + timeGapSecond + "분 후 ");
            }


        } else if (nextFirst != null && nextSecond == null) {
            //하나만 빔

            int hourGapFirst = nextCalendarFirst.get(Calendar.HOUR_OF_DAY) - newCal.get(Calendar.HOUR_OF_DAY);
            int timeGapFirst = nextCalendarFirst.get(Calendar.MINUTE) - newCal.get(Calendar.MINUTE);
            if (timeGapFirst < 0) {
                hourGapFirst -= 1;
                timeGapFirst += 60;
            }

            if(hourGapFirst> 0) {
                result.add(nextTerminalFirst + "행 "+ hourGapFirst +"시간 "+ timeGapFirst + "분 후 ");
            }else{
                result.add(nextTerminalFirst + "행 "+ timeGapFirst + "분 후 ");

            }
            result.add("-");
        } else {
            //둘다 빔
            result.add("-");
            result.add("-");
        }

//            Log.d(TAG, "getPrevNextStationTime: " + nextCalendarFirst.get(Calendar.HOUR) + ":" + nextCalendarFirst.get(Calendar.MINUTE) + " to " + nextTerminalFirst );
//            Log.d(TAG, "getPrevNextStationTime: " + nextCalendarSecond.get(Calendar.HOUR) + ":" + nextCalendarSecond.get(Calendar.MINUTE) + " to " + nextTerminalSecond );
        return result;
    }

    public int getTrainsPerHour( Station station, Calendar cal ) {

        int stationID = station.getStationID();
        JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(mContext, stationID);
        StationTimetable stt = jsonTimetableParser.getStationTimetable();

        Calendar newCal = (Calendar)cal.clone();
        int day = newCal.get(Calendar.DAY_OF_WEEK);
        ArrayList<HashMap<String, Object>>[] prevTimeTable, nextTimeTable;

        switch (day) {
            case 1:
                prevTimeTable = stt.getSunUpWayLdx();
                nextTimeTable = stt.getSunDownWayLdx();
                break;
            case 7:
                prevTimeTable = stt.getSatUpWayLdx();
                nextTimeTable = stt.getSatDownWayLdx();
                break;
            default:
                prevTimeTable = stt.getOrdUpWayLdx();
                nextTimeTable = stt.getOrdDownWayLdx();
                break;
        }

        int hour = newCal.get(Calendar.HOUR_OF_DAY);
        int hourIndexFirst = hour - 5 < 0 ? hour + 19 : hour - 5;

        int prevTrainsSize = prevTimeTable[hourIndexFirst].size();
        int nextTrainsSize = nextTimeTable[hourIndexFirst].size();

        return prevTrainsSize + nextTrainsSize ;

    }
//    private Map getCalendar( ArrayList<HashMap<String, Object>> timeList, String key, Calendar cal) {

    private Map getCalendar(ArrayList<HashMap<String, Object>>[] timeTable, int hourIndex, String key, Calendar cal) {
        // out of boundary 24시 등
        if( hourIndex >= timeTable.length) return null;

//        Log.d(TAG, "getCalendar: " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));
        Map<String, Object> result = new HashMap<>();
        ArrayList<HashMap<String, Object>> timeList = timeTable[hourIndex];
//        Log.d(TAG,"timelist size: "+timeList.size());
//        Log.d(TAG,"timelist tostring"+timeList.toString());
        // there is no ele in table
        if (timeList.size() == 0) return null;

        String terminalName = "";
        int timeMinute = 0;
        boolean flag = false;
        int minute = cal.get(Calendar.MINUTE);
        for (int i = 0; i < timeList.size(); i++) {
            HashMap<String, Object> timeMap = timeList.get(i);
            String timeString[] = ((String) timeMap.get(key)).split("\\(");
            timeMinute = Integer.parseInt(timeString[0]);
            if (timeMinute > minute) {
                terminalName = timeString[1].replace(")", "");
                cal.set(Calendar.MINUTE, timeMinute);
                flag = true;
                break;
            }
        }

        if (flag) {
            cal.set(Calendar.MINUTE, timeMinute);
            result.put("calendar", cal);
            result.put("terminal", terminalName);
            return result;
        } else {
            cal.set(Calendar.MINUTE, 60);
            return getCalendar(timeTable, hourIndex + 1, key, cal);
        }

    }

    public ArrayList<Pair<Station, Integer>>[] getAdj() {
        return adj;
    }

    public Station getStation(int index) {
        return deepCopiedStations.get(index);
    }


    //Server Communication
    private int getConLevel(int ID) {
        // 머신러닝 후 적용!
        return 0;
    }


}