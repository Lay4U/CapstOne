package com.estsoft.r_subway_android.UI.Settings;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.estsoft.r_subway_android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-07-07.
 */
public class SearchSetting {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> settingCollection;

    public SearchSetting() {
        createGroupList();
        createCollection();

        avoidCongestStations = false;
  //      avoidDangerStations = false;
        activeExpressOnly = false;
        activeLanes = makeTmpLaneList();

    }

    public void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("급행 열차");
        groupList.add("검색노선 설정");

    }

    public List<String> getGroupList() {
        return groupList;
    }

    public List<String> getChildList() {
        return childList;
    }

    public Map<String, List<String>> getSettingCollection() {
        return settingCollection;
    }

    public void createCollection() {

        String[] lines = {"1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선", "분당선", "인천", "신분당선", "경의중앙선", "경춘선", "공항", "의정부", "수인선", "에버라인", "자기부상"};
        settingCollection = new LinkedHashMap<String, List<String>>();
        for (String setting : groupList) {
            childList = new ArrayList<String>();
            if (setting.equals("검색노선 설정")) {
                loadChild(lines);

            }
            settingCollection.put(setting, childList);
        }

    }

    private void loadChild(String[] settingChild) {

        for (String settingcontent : settingChild) {
            childList.add(settingcontent);
        }
    }

    //Inkiu

    private static final String TAG = "SearchSetting";
    // 역별 혼잡도
    // ?
    static boolean avoidCongestStations ;

    // 위험역 ?
 //   static  boolean avoidDangerStations ;

    // 급행열차만
    static boolean activeExpressOnly ;

    // 검색노선 설정
    static List< Lane > activeLanes ;

    public static boolean isAvoidCongestStations() {
        return avoidCongestStations;
    }

    public static void setAvoidCongestStations(boolean avoidCongestStations) {
        SearchSetting.avoidCongestStations = avoidCongestStations;
    }

/*
    public static boolean isAvoidDangerStations() {
        return avoidDangerStations;
    }

    public static void setAvoidDangerStations(boolean avoidDangerStations) {
        SearchSetting.avoidDangerStations = avoidDangerStations;
    }
*/

    public static boolean isActiveExpressOnly() {
        return activeExpressOnly;
    }

    public static void setActiveExpressOnly(boolean activeExpressOnly) {
        SearchSetting.activeExpressOnly = activeExpressOnly;
    }

    public static List<Lane> getActiveLanes() {
        return activeLanes;
    }

    public static void setActiveLanes(List<Lane> activeLanes) {
        SearchSetting.activeLanes = activeLanes;
    }

    public static void checkSettings() {
//        Log.d(TAG, "checkSettings: 혼잡도 ? " + isAvoidCongestStations());
//        Log.d(TAG, "checkSettings: 급행차 ? " + isActiveExpressOnly());

//        Log.d(TAG, "checkSettings: 검색노선");
        for (int i = 0 ; i < getActiveLanes().size(); i ++ ) {
            Lane lane = getActiveLanes().get(i);
//            Log.d(TAG, "checkSettings: " + lane.getName() + "/Status " + lane.isActive());
        }
//        Log.d(TAG, "checkSettings: -------------------------------------------------------");

    }

    private static List< Lane > makeTmpLaneList(){
        List< Lane > tmp = new ArrayList<>();


        String[] lines = {"1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선",
                "분당선", "인천", "신분당선", "경의중앙선", "경춘선", "공항", "의정부", "수인선", "에버라인", "자기부상"};
        tmp.add( new Lane("1호선", 1, true) );
        tmp.add( new Lane("2호선", 2, true) );
        tmp.add( new Lane("3호선", 3, true) );
        tmp.add( new Lane("4호선", 4, true) );
        tmp.add( new Lane("5호선", 5, true) );
        tmp.add( new Lane("6호선", 6, true) );
        tmp.add( new Lane("7호선", 7, true) );
        tmp.add( new Lane("8호선", 8, true) );
        tmp.add( new Lane("9호선", 9, true) );
        tmp.add( new Lane("분당선", 100, true) ); // 분당선
        tmp.add( new Lane("인천", 21, true) ); // 인천1호선
        tmp.add( new Lane("신분당선", 109, true) ); // 신분당선
        tmp.add( new Lane("경의중앙선", 104, true) ); // 경의중앙
        tmp.add( new Lane("경춘선", 108, true) ); // 경춘선
        tmp.add( new Lane("공항", 101, true) ); // 공항철도
        tmp.add( new Lane("의정부", 110, true) ); // 의정부경전철
        tmp.add( new Lane("수인선", 111, true) ); // 수인선
        tmp.add( new Lane("에버라인", 107, true) ); // 에버라인
        tmp.add( new Lane("자기부상", 102, true) ); // 자기부상


        return  tmp;
    }

    public static class Lane {
        String name;
        int number;
        boolean active;

        public Lane(String name, int number, boolean active) {
            this.name = name;
            this.number = number;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

}
