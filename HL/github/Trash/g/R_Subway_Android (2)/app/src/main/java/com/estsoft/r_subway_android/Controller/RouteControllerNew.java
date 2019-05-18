package com.estsoft.r_subway_android.Controller;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;
import com.estsoft.r_subway_android.UI.Settings.SearchSetting;
import com.estsoft.r_subway_android.utility.ShortestPath;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by estsoft on 2016-07-18.
 */
public class RouteControllerNew {

    private static final String TAG = "RouteControllerNew";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
    private static final SimpleDateFormat sdftest = new SimpleDateFormat("d H:mm");
    private final static int SHORT_PATH = 0;
    private final static int MIN_TRANSFER = 1;
    private static final int CUSTOM_ROUTE = 2;

    private final int adjScale = 6;

    StationController stationController = null;
    TtfMapImageView mapView = null;

    private RouteNew lastRouteNew;

    private Context context;

    private Calendar inputCalendar;

    private Boolean isExpress;

    private Boolean expressFirst;

    private boolean[] activeLaneArr = null;

    private ArrayList<Pair<Station, Integer>>[] defaultAdj = null;


    public RouteControllerNew(StationController stationController, TtfMapImageView mapView, Context context) {
        this.stationController = stationController;
        this.mapView = mapView;
        this.context = context;

        this.defaultAdj = stationController.getAdj();
    }

    private void debugSections( List<List<Station>> sections ) {
        Log.d(TAG, "debugSections: section Size = " + sections.size());
        for (int i = 0; i < sections.size(); i ++ ) {
            List<Station> section = sections.get(i);
            Log.d(TAG, "debugSections: START AT " + section.get(0).getStationName() + " / " + section.get(0).getLaneName());
            for (int j = 0; j < section.size(); j ++ ) {
                Station st = section.get(j);
                Log.d(TAG, "debugSections: " + st.getStationName() + st.getStationID());
            }
            if ( i == sections.size() - 1) Log.d(TAG, "debugSections: END! ");
            else Log.d(TAG, "debugSections: TRANSFER! ");
        }
    }
    private void debugIsPrevWay( List<Station> section , boolean isPrevWay ) {
        Log.d(TAG, "debugIsPrevWay: " + section.get(0).getLaneName());
        Log.d(TAG, "debugIsPrevWay: " + section.get(0).getStationName() + " to " +
                section.get(section.size() -1 ).getStationName() + " is "
                + (isPrevWay ? "PrevWay" : "NextWay") );
    }
    private void debugTerminals( List<Station> section, List<Station> terminals ) {
        Log.d(TAG, "debugTerminals: " + section.get(0).getStationName() + " to " + section.get(section.size() - 1).getStationName() );
        Log.d(TAG, "debugTerminals: " + section.get(0).getLaneName());
        for ( Station st : terminals ) {
            Log.d(TAG, "debugTerminals: " + st.getStationName() );
        }
        Log.d(TAG, "debugTerminals: END ... ");
    }
    private void debugSectionCalendars(List<Station> section ) {
        Log.d(TAG, "debugSectionCalendars: " + section.get(0).getStationName() + " to " + section.get(section.size() - 1).getStationName() + " EX ? " + isExpress );
        for ( Station st : section ) {
            Log.d(TAG, "debugSectionCalendars: " + st.getStationName() + " /time = " + sdftest.format(st.getArriveTime().getTime()));
        }
        Log.d(TAG, "debugSectionCalendars: _________________________________________________________");
    }
    private void debugGetNoneExRemovedSection(List<Station> section ) {
        Log.d(TAG, "debugGetNoneExRemovedSection: " + section.get(0).getStationName() + " to " + section.get(section.size() - 1).getStationName() );
        for ( int i = 0; i < section.size(); i ++ ) {
            Log.d(TAG, "debugGetNoneExRemovedSection: " + section.get(i).getStationName());
        }
        Log.d(TAG, "debugGetNoneExRemovedSection:  ----------------------------------------------------");
    }
    private void debugMapPoints( List<Station> section ) {
        Log.d(TAG, "debugMapPoints: ");
        for ( int i = 0; i < section.size(); i ++ ) {
            Station station = section.get(i);
            Log.d(TAG, "debugMapPoints: " + station.getStationName() + " /Point  " + station.getMapPoint().toString());
        }
        Log.d(TAG, "debugMapPoints: _______________________________________________________");
    }
    private void debugActiveLanes () {
        for ( int i = 0; i < SearchSetting.getActiveLanes().size(); i ++ ) {
            Log.d(TAG, "debugActiveLanes: " + SearchSetting.getActiveLanes().get(i).getName() +
                    " /inactive? " + activeLaneArr[SearchSetting.getActiveLanes().get(i).getNumber()]);
        }
    }

    public RouteNew[] getRoutes( Station start, Station end ) {
        RouteNew[] routes = new RouteNew[3];
        // getShortRout
//        Log.d(TAG, "getRoutes: " + "SHORT_ROUTE");
        routes[0] = getRouteNew( start, end, SHORT_PATH );
        // getShortRoute done

        // getMinTransferRoute
//        defaultAdj = stationController.getMinTransferAdj();
//        Log.d(TAG, "getRoutes: " + "MIN_TRANSFER");
        routes[1] = getRouteNew( start, end, MIN_TRANSFER );
        // getMinTransferRoute done

        // getCustomRoute
//        defaultAdj = stationController.getShortestPathAdj();
//        Log.d(TAG, "getRoutes: " + "CUSTOM_ROUTE");
        routes[2] = getRouteNew( start, end, CUSTOM_ROUTE );
        // getCustomRoute done

//        Log.d(TAG, "getRoutes: " + "returning Route");
        return routes;

    }


    private RouteNew getRouteNew( Station start, Station end, int mode  ) {

//        Log.d(TAG, "getRouteNew: " + start.getIndex() + start.getStationName());
//        Log.d(TAG, "getRouteNew: " + end.getIndex() + end.getStationName());

        inputCalendar = null;

        int arriveTimeIndex = 0;

        //SearchSetting
        initializeSettings( mode );
        ShortestPath.setLineRange( activeLaneArr );
//        debugActiveLanes();
        //SearchSetting done

        // raw section making
        int[] path ;
        if ( mode == SHORT_PATH ){
            path = ShortestPath.getShortestPathByIntArray(defaultAdj, start, end);
            arriveTimeIndex = 0;
        } else if ( mode == MIN_TRANSFER ) {
            path = ShortestPath.getMinimumTransferPathByIntArray(defaultAdj, start, end);
            arriveTimeIndex = 1;
        } else {
            path = ShortestPath.getShortestPathByIntArray(defaultAdj, start, end);
            arriveTimeIndex = 2;
        }

        // if could not find Route
        if (path.length == 0) return null;
        // if could not find Route
        // listing path[]
        List<Integer> listPath = getListPath(path);
        // listing path[] end
        List<List<Station>> sections = getSections(listPath);
//        debugSections(sections);
        // raw section making done

        // before process
        List<List<Station>> processedSections = new ArrayList<>();

        // prevWay result of each section
        List<Boolean> sectionIsPrevWay = new ArrayList<>();

        // terminal Stations of each section
        List<List<Station>> sectionTerminals = new ArrayList<>();

        // isExpress of each section
        List<Boolean> isExpressSection = new ArrayList<>();

        for ( int i = 0; i < sections.size(); i ++ ) {

            List<Station> section = sections.get(i);

            // isPrevWay check of section
            boolean isPrevWay = isPrevWay( section.get(0), section.get(1) );
            sectionIsPrevWay.add(isPrevWay);
//            debugIsPrevWay( section, isPrevWay );
            // isPrevWay check Done

            // finding terminals of section
            List<Station> terminals = findTerminals( section, isPrevWay );
            sectionTerminals.add( terminals );
//            debugTerminals( section, terminals );
            // finding terminals done

            // get Calendar List.
            if (inputCalendar == null) inputCalendar = new GregorianCalendar();
            List<Calendar> sectionCalendars = getSectionCalendars(section, terminals, isPrevWay, inputCalendar);
            for ( int j = 0; j < section.size(); j ++ ) {
                Station station = section.get(j);
                Calendar stationCalendar = sectionCalendars.get(j);
//                station.setArriveTime( stationCalendar );
                station.getArriveTimes()[arriveTimeIndex] = (Calendar)stationCalendar.clone();
                // sharing Calendar for Each section
                inputCalendar = stationCalendar;
            }
//            debugSectionCalendars( section );
            // Calendar Mapping done.

            // isExpress check (in Function getTimeTable)
            if (isExpress) isExpress = RouteNew.isExpressStation(section.get(0).getStationID()) && RouteNew.isExpressStation(section.get(section.size()-1).getStationID());
            isExpressSection.add(isExpress);
            // isExpress done

            // deleting none Express Stations
            Log.d(TAG, "getRouteNew: " + isExpress);
            if (isExpress) {
                section = getNoneExRemovedSection( section );
            }
//            debugGetNoneExRemovedSection( section );
            // deleting none Express Stations done

            // mapping mapPoints
            setMapPoint( section );
            // mapping mapPoints done
//            debugMapPoints( section );

            processedSections.add(section);
        }

        // Ex processed Sections
        lastRouteNew = new RouteNew( processedSections, isExpressSection );
        // Ex none processed Sections
//        lastRouteNew = new RouteNew(sections, isExpressSection);

        return  lastRouteNew;

//        return new RouteNew( new ArrayList<List<Station>>(), null );
    }


    private void setMapPoint( List<Station> section ) {
        for ( Station st : section ) {
            //MapPoint Setting...
            if (st.getMapPoint() == null ) {
                if (st.getStationID() == 1611) {
                    st.setMapPoint(mapView.getStationPoint(String.valueOf(1611)));
                    break;
                }
                st.setMapPoint(mapView.getStationPointByName( st.getStationName() ));
            }
        }
    }

    private List<Station> getNoneExRemovedSection( List<Station> section ) {
        List<Station> removedSection = new ArrayList<>();
        for ( int i = 0; i < section.size(); i ++ ) {
            Station station = section.get(i);
            if ( RouteNew.isExpressStation( station.getStationID() ) )
                removedSection.add( station );
            else {
                Log.d(TAG, "getNoneExRemovedSection: " + station.getStationName() + "is deleting");
            }
        }
        return removedSection;
    }

    private List<Calendar> getSectionCalendars (List<Station> section, List<Station> terminals, boolean isPrevWay, Calendar selectedTime ) {
        Calendar sharedTime;
        if (selectedTime == null) {
            sharedTime = new GregorianCalendar();
        } else {
            sharedTime = selectedTime;
        }
        List<Calendar> sectionCalendars = new ArrayList<>();
        for (int i = 0; i < section.size(); i ++) {
            Station station = section.get(i);
            Calendar newCal;
            if (i == 0) {
                // adding transfer Cost...
//                Log.d(TAG, "getSectionCalendars: Shared" + station.getStationName() + " 시간 : " + sharedTime.get(Calendar.HOUR) + ":" + sharedTime.get(Calendar.MINUTE));
                sharedTime.add(Calendar.MINUTE, getTransferCost(null, null));
//                Log.d(TAG, "getSectionCalendars: Shared" + station.getStationName() + " 시간 : " + sharedTime.get(Calendar.HOUR) + ":" + sharedTime.get(Calendar.MINUTE));
                // adding done
                newCal = getTimeTable( station, terminals, isPrevWay, sharedTime );
            } else {
                newCal =  getTimeGap( section.get( i - 1 ).getIndex(), section.get(i).getIndex(), sharedTime ) ;
            }
//            Log.d(TAG, "getSectionCalendars: " + station.getStationName() + " 시간 : " + newCal.get(Calendar.HOUR) + ":" + newCal.get(Calendar.MINUTE));
            sectionCalendars.add( newCal );
            sharedTime = newCal;
        }
        return sectionCalendars;
    }
    private Calendar getTimeGap( int stationIndex, int nextStationIndex, Calendar sharedTime ) {
        ArrayList<Pair<Station, Integer>>[] adj = defaultAdj;
        ArrayList<Pair<Station, Integer>> pairs = adj[stationIndex];

        Calendar caledGapCal = (Calendar)sharedTime.clone();
        for ( Pair<Station, Integer> pair : pairs ) {
            if (pair.first.getIndex() == nextStationIndex ) {
                int edgeCost = pair.second;
                caledGapCal.set(Calendar.MINUTE, caledGapCal.get(Calendar.MINUTE) +  Math.round(edgeCost / adjScale) );
                return caledGapCal;
            }
        }
        return caledGapCal;
    }
    private int getTransferCost( Station station, Station exStaton ) {
        return 5;
    }
    private Calendar getTimeTable ( Station station, List<Station> terminals, boolean isPrevWay, Calendar sharedTime ) {
        Calendar newCal = (Calendar)sharedTime.clone();
        Calendar compareCal = (Calendar)sharedTime.clone();
//        Log.d(TAG, "getTimeTable__:0  " + station.getStationName() +  " " + newCal.get(Calendar.HOUR) +":"+ newCal.get(Calendar.MINUTE));

        JSONTimetableParser jsonTimetableParser = new JSONTimetableParser(context , station.getStationID());
        StationTimetable stt = jsonTimetableParser.getStationTimetable();

        ArrayList<HashMap<String, Object>>[] timeTable;
        String key;
        int day = newCal.get(Calendar.DAY_OF_WEEK);
        switch ( day ) {
            case 1 :
                if (isPrevWay) {
                    timeTable = stt.getSunUpWayLdx();
                    key = "sunUpWayLdx";
                }
                else {
                    timeTable = stt.getSunDownWayLdx();
                    key = "sunDownWayLdx";
                }
                break;
            case 7 :
                if (isPrevWay) {
                    timeTable = stt.getSatUpWayLdx();
                    key = "satUpWayLdx";
                }
                else{
                    timeTable = stt.getSatDownWayLdx();
                    key = "satDownWayLdx";
                }
                break;
            default:
                if (isPrevWay){
                    timeTable = stt.getOrdUpWayLdx();
                    key = "ordUpWayLdx";
                }
                else {
                    timeTable = stt.getOrdDownWayLdx();
                    key = "ordDownWayLdx";
                }
                break;
        }
        int hour = newCal.get(Calendar.HOUR_OF_DAY);
        int hourIndex = hour - 5 < 0 ? hour - 5 + 19 : hour - 5;
        ArrayList<HashMap<String, Object>> timeList = timeTable[hourIndex];

        int minute = newCal.get(Calendar.MINUTE);

//        Log.d(TAG, "getTimeTable: " + timeList.size());
        for ( int i = 0; i < timeList.size(); i ++ ) {
            HashMap<String, Object> timeMap = timeList.get(i);
            String timeString[] = ((String)timeMap.get( key )).split("\\(");
            int timeMinute = Integer.parseInt(timeString[0]);
//            Log.d(TAG, "getTimeTable: " + timeMinute);
            String terminalName = timeString[1].replace(")", "");
//            Log.d(TAG, "getTimeTable: " + terminalName );
//            Log.d(TAG, "checkTerminalName: " + station.getStationName());

            boolean tmpIsExpress = (Boolean)timeMap.get("isExpress");
//            Log.d(TAG, "getTimeTable: expressFirst ? " + expressFirst + " timeMinute : "+ timeMinute + " Minute : " + minute);
//            Log.d(TAG, "getTimeTable: terminalName ? " + terminalName);
            if (expressFirst && RouteNew.isExpressStation(station.getStationID())) {
                if ( timeMinute > minute && checkTerminalName(terminals, terminalName) && tmpIsExpress ) {
                    newCal.set(Calendar.MINUTE, timeMinute);
                    // 전역변수 isExpress
                    isExpress = tmpIsExpress;
                    break;
                }
            } else {
                if ( timeMinute > minute && checkTerminalName(terminals, terminalName) ) {
//                    Log.d(TAG, "getTimeTable__:... " + newCal.get(Calendar.HOUR) +":" + newCal.get(Calendar.MINUTE));
                    newCal.set(Calendar.MINUTE, timeMinute);
//                    Log.d(TAG, "getTimeTable__:... " + newCal.get(Calendar.HOUR) +":" + newCal.get(Calendar.MINUTE));
                    // 전역변수 isExpress
                    isExpress = tmpIsExpress;
                    break;
                }
            }
        }

        if (!newCal.equals(compareCal)) {
//            Log.d(TAG, "getTimeTable__:2  " + station.getStationName() +  " " + newCal.get(Calendar.HOUR) +":"+ newCal.get(Calendar.MINUTE));
//            Log.d(TAG, "getTimeTable: --- " + station.getStationName());
//            Log.d(TAG, "getTimeTable: --- " + newCal.get(Calendar.HOUR) + " : " + newCal.get(Calendar.MINUTE));
            return newCal;
        }
        else {
            newCal.set( Calendar.MINUTE, 60 );
//            Log.d(TAG, "getTimeTable__:1  " + station.getStationName() +  " " + newCal.get(Calendar.HOUR) +":"+ newCal.get(Calendar.MINUTE));
//            newCal.set( Calendar.MINUTE, 0 );
            return getTimeTable(station, terminals, isPrevWay, newCal);
        }

    }
    private Boolean checkTerminalName ( List<Station> terminals, String timeTerminalName ) {
        for ( int i = 0; i < terminals.size(); i ++ ) {
            String terminalName = terminals.get(i).getStationName();
            String[] tmp = terminalName.split("\\(");
            String justTerminalName = tmp[0];
//            Log.d(TAG, "checkTerminalName: /" + timeTerminalName + "/  finding  /" + justTerminalName +"/");
//            Log.d(TAG, "checkTerminalName: /" + timeTerminalName.getClass() +  " // " + justTerminalName.getClass());
//            if (justTerminalName.equals(timeTerminalName)){
//            if (justTerminalName.contains(timeTerminalName)) {


            if (timeTerminalName.equals(justTerminalName)) {
//                Log.d(TAG, "checkTerminalName: / in true /" + timeTerminalName + "/  finding  /" + justTerminalName +"/");
                return true;
            }
        }
        return false;
    }

    private List<Station> fTerminals;
    private List<Station> findTerminals ( List<Station> section, boolean isPrevWay ) {
        fTerminals = new ArrayList<>();

        if (section.size() > 0) {
            // 종착역 도착은 재귀 안들어가고 여기서 해결
            findingTerminal( section.get( section.size() - 1 ) );

            recursiveFindingTerminal(section.get( section.size() - 1 ), isPrevWay);
        }

        return fTerminals;
    }
    private void recursiveFindingTerminal ( Station curStation, boolean isPrevWay ) {
        Station deepCurStation = stationController.getStation(curStation.getIndex());
        List<Station> nextStations = isPrevWay ? deepCurStation.getPrevStations() : deepCurStation.getNextStations();

        for ( int i = 0; i < nextStations.size(); i ++ ) {
            Station nextStation = nextStations.get(i);
            findingTerminal( nextStation );

//            // 원형 역 탈출 .. fTerminal 에 중복역에 있을 경우!
//            for ( Station terminal : fTerminals ) {
//                if (terminal.getStationName().equals(nextStation.getStationName())) return;
//            }

            // 2호선 원형 성수역 탈출
            if ( nextStation.getStationID() == 211 ) return;
            // 6호선 원형 응암 탈출
            if ( nextStation.getStationID() == 610 ) {
                Station tmpStation = Station.getEmptyStation();
                tmpStation.setLaneType(6);
                if (isPrevWay) tmpStation.setStationName("봉화산-역촌");
                else tmpStation.setStationName("봉화산-새절");
                findingTerminal( tmpStation );
                // 6호선일 경우, 무조건 봉화산 Terminal 입력
                findingTerminal( stationController.getDeepStation( 647 ) );
                return;
            }

            recursiveFindingTerminal(nextStation, isPrevWay);
        }
    }
    private void findingTerminal ( Station nextStation ) {
            if (RouteNew.compareTerminalName(nextStation.getStationName(), nextStation.getLaneType())) {
//                Log.d(TAG, "findingTerminal: " + nextStation.getStationName());
                fTerminals.add(nextStation);
            }
    }

    private boolean isPrevWay( Station start, Station next ) {
        List<Station> prevStations = start.getPrevStations();
        for ( int i = 0; i < prevStations.size() ; i ++) {
            if (next.getStationName().equals( prevStations.get(i).getStationName() )) {
                return true;
            }
        }
        return false;
    }

    private List<List<Station>> getSections ( List<Integer> listPath ){
        List<List<Station>> sections = new ArrayList<>();
        int startIndex = 0;

        for ( int i = 0; i < listPath.size() - 1 ; i ++ ) {
            Station curSt = stationController.getStation( listPath.get(i) );
            Station nextSt = stationController.getStation( listPath.get(i + 1) );

            if ( i == 0 && curSt.getStationName().equals( nextSt.getStationName() ) ) {
                startIndex ++;
            } else if ( i != 0 && curSt.getStationName().equals( nextSt.getStationName() ) ) {
                sections.addAll( getSection( listPath , startIndex, i ) );
                startIndex = i + 1;
            } else if ( i == listPath.size() - 2 && curSt.getStationName().equals( nextSt.getStationName() )){
                sections.addAll( getSection( listPath, startIndex, listPath.size() - 2 ) );
                return sections;
            } else if ( i == listPath.size() - 2 ){
                sections.addAll( getSection( listPath, startIndex, listPath.size() - 1 ) );
                return sections;
            }
        }
        return sections;
    }
    private List<List<Station>> getSection ( List<Integer> listPath, int start, int end ) {
        List<List<Station>> sections = new ArrayList<>();
        List<Station> section = new ArrayList<>();
//        Log.d(TAG, "getSection: LaneType : " +  stationController.getStation(listPath.get(start)).getLaneType());
        // 처음과 끝 역
        int startStationID = stationController.getStation(listPath.get(start)).getStationID();
        int endStationID = stationController.getStation(listPath.get(end)).getStationID();

        // 2호선이 아닐때, 미친 구간이 아닐때
        if (stationController.getStation(listPath.get(start)).getLaneType() != 2
                || !(RouteNew.isCrazyStation(stationController.getStation(listPath.get(start)).getStationID())
                ^ RouteNew.isCrazyStation(stationController.getStation(listPath.get(end)).getStationID()))
                ) {
            //1호선일때, 구로때문에
            //수정!!
//            if (false) {
//            Log.d(TAG, "getSection: into the space good night");
            if ( stationController.getStation(listPath.get(start)).getLaneType() == 1
                    && RouteNew.checkFirstLaneException(startStationID)
                    && RouteNew.checkFirstLaneException(endStationID)) {
                for (int i = start; i <= end; i ++ ) {
                    Station curStation = stationController.getStation(listPath.get(i));
                    section.add( curStation );
                    if ( curStation.getStationID() == 141 ){
                        sections.add(section);
                        // new Section and add crazy station one more
                        section = new ArrayList<>();
                        section.add( curStation );
                    }
                }
                sections.add(section);
            } else {
                for (int i = start; i <= end; i++) {
                    section.add(stationController.getStation(listPath.get(i)));
                }
                sections.add(section);
            }
        }  else {
            // 미친구간 일때
            for (int i = start; i <= end; i ++ ) {
                Station curStation = stationController.getStation(listPath.get(i));
                section.add( curStation );
                if ( curStation.getStationID() == 211 || curStation.getStationID() == 234 ){
                    sections.add(section);
                    // new Section and add crazy station one more
                    section = new ArrayList<>();
                    section.add( curStation );
                }
            }
            sections.add(section);
        }

        // size 1 section delete !
        for ( List<Station> tmpSection : sections ) {
            if (tmpSection.size() == 1){
                sections.remove(tmpSection);
                break;
            }
        }

//        Log.d(TAG, "getSection: ----------------------------------------");
        for ( List<Station> list : sections ) {
            for ( Station st : list ) {
//                Log.d(TAG, "getSection: " + st.getStationName());
            }
//            Log.d(TAG, "getSection:  // ");
        }

        return sections;
    }

    private List<Integer> getListPath( int[] path ) {
        List<Integer> listPath = new ArrayList<>();
        for ( int i = 0; i <path.length; i ++ ) {
            listPath.add(path[i]);
        }
        return listPath;
    }

    private void initActiveLaneArr( int mode ) {
        if ( activeLaneArr == null || mode != CUSTOM_ROUTE)  activeLaneArr = new boolean[112]; // false init
//        for (int i = 0; i < activeLaneArr.length; i ++ ) {
//            activeLaneArr[i] = true;
//        }
        if ( mode == CUSTOM_ROUTE) {
//            Log.d(TAG, "initActiveLaneArr: CUSTOM_ROUTE");
            for (int i = 0; i < SearchSetting.getActiveLanes().size(); i++) {
                activeLaneArr[SearchSetting.getActiveLanes().get(i).getNumber()] =
                        !(SearchSetting.getActiveLanes().get(i).isActive());
            }
        }
    }

    private void initializeSettings( int mode ){
        initActiveLaneArr( mode );
//        Log.d(TAG, "initializeSettings: " + mode);
        if (mode == CUSTOM_ROUTE) {
            expressFirst = SearchSetting.isActiveExpressOnly();
        } else {
            expressFirst = false;
        }
//        Log.d(TAG, "initializeSettings: " + expressFirst);
    }

}
