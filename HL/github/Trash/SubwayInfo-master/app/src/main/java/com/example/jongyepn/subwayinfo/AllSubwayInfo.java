package com.example.jongyepn.subwayinfo;

public class AllSubwayInfo {

    public AllSubwayInfo() {

    }



    private static String KEY = "79694c4f4874616538356f70436a69";
    private static String TYPE = "xml";
    private static String SERVICE = "realtimeStationArrival";
    private static Integer START_INDEX = 0;
    private static Integer END_INDEX = 10;
    private static String list_total_count;

    private static boolean inrow = false;
    private static boolean inrowNum = false;
    private static boolean inselectedCount = false;
    private static boolean intotalCount = false;
    private static boolean insubwayId = false;
    private static boolean inupdnLine = false;
    private static boolean intrainLineNm = false;
    private static boolean insubwayHeading = false;
    private static boolean instatnFid = false;
    private static boolean instatnTid = false;
    private static boolean instatnId = false;
    private static boolean instatnNm = false;
    private static boolean inordkey = false;
    private static boolean insubwayList = false;
    private static boolean instatnList = false;
    private static boolean inbarvlDt = false;
    private static boolean inbtrainNo = false;
    private static boolean inbstatnId = false;
    private static boolean inbstatnNm = false;
    private static boolean inrecptnDt = false;
    private static boolean inarvlMsg2 = false;
    private static boolean inarvlMsg3 = false;
    private static boolean inarvlCd = false;

    private static String rowNum = null;
    private static String selectedCount = null;
    private static String totalCount = null;
    private static String subwayId = null;
    private static String updnLine = null;
    private static String trainLineNm = null;
    private static String subwayHeading = null;
    private static String statnFid = null;
    private static String statnTid = null;
    private static String statnId = null;
    private static String statnNm = null;
    private static String ordkey = null;
    private static String subwayList = null;
    private static String statnList = null;
    private static String barvlDt = null;
    private static String btrainNo = null;
    private static String bstatnId = null;
    private static String bstatnNm = null;
    private static String recptnDt = null;
    private static String arvlMsg2 = null;
    private static String arvlMsg3 = null;
    private static String arvlCd = null;

    public static String getKEY() {
        return KEY;
    }

    public static void setKEY(String KEY) {
        AllSubwayInfo.KEY = KEY;
    }

    public static String getTYPE() {
        return TYPE;
    }

    public static void setTYPE(String TYPE) {
        AllSubwayInfo.TYPE = TYPE;
    }

    public static String getSERVICE() {
        return SERVICE;
    }

    public static void setSERVICE(String SERVICE) {
        AllSubwayInfo.SERVICE = SERVICE;
    }

    public static Integer getStartIndex() {
        return START_INDEX;
    }

    public static void setStartIndex(Integer startIndex) {
        START_INDEX = startIndex;
    }

    public static Integer getEndIndex() {
        return END_INDEX;
    }

    public static void setEndIndex(Integer endIndex) {
        END_INDEX = endIndex;
    }

    public static String getList_total_count() {
        return list_total_count;
    }

    public static void setList_total_count(String list_total_count) {
        AllSubwayInfo.list_total_count = list_total_count;
    }

    public static boolean isInrow() {
        return inrow;
    }

    public static void setInrow(boolean inrow) {
        AllSubwayInfo.inrow = inrow;
    }

    public static boolean isInrowNum() {
        return inrowNum;
    }

    public static void setInrowNum(boolean inrowNum) {
        AllSubwayInfo.inrowNum = inrowNum;
    }

    public static boolean isInselectedCount() {
        return inselectedCount;
    }

    public static void setInselectedCount(boolean inselectedCount) {
        AllSubwayInfo.inselectedCount = inselectedCount;
    }

    public static boolean isIntotalCount() {
        return intotalCount;
    }

    public static void setIntotalCount(boolean intotalCount) {
        AllSubwayInfo.intotalCount = intotalCount;
    }

    public static boolean isInsubwayId() {
        return insubwayId;
    }

    public static void setInsubwayId(boolean insubwayId) {
        AllSubwayInfo.insubwayId = insubwayId;
    }

    public static boolean isInupdnLine() {
        return inupdnLine;
    }

    public static void setInupdnLine(boolean inupdnLine) {
        AllSubwayInfo.inupdnLine = inupdnLine;
    }

    public static boolean isIntrainLineNm() {
        return intrainLineNm;
    }

    public static void setIntrainLineNm(boolean intrainLineNm) {
        AllSubwayInfo.intrainLineNm = intrainLineNm;
    }

    public static boolean isInsubwayHeading() {
        return insubwayHeading;
    }

    public static void setInsubwayHeading(boolean insubwayHeading) {
        AllSubwayInfo.insubwayHeading = insubwayHeading;
    }

    public static boolean isInstatnFid() {
        return instatnFid;
    }

    public static void setInstatnFid(boolean instatnFid) {
        AllSubwayInfo.instatnFid = instatnFid;
    }

    public static boolean isInstatnTid() {
        return instatnTid;
    }

    public static void setInstatnTid(boolean instatnTid) {
        AllSubwayInfo.instatnTid = instatnTid;
    }

    public static boolean isInstatnId() {
        return instatnId;
    }

    public static void setInstatnId(boolean instatnId) {
        AllSubwayInfo.instatnId = instatnId;
    }

    public static boolean isInstatnNm() {
        return instatnNm;
    }

    public static void setInstatnNm(boolean instatnNm) {
        AllSubwayInfo.instatnNm = instatnNm;
    }

    public static boolean isInordkey() {
        return inordkey;
    }

    public static void setInordkey(boolean inordkey) {
        AllSubwayInfo.inordkey = inordkey;
    }

    public static boolean isInsubwayList() {
        return insubwayList;
    }

    public static void setInsubwayList(boolean insubwayList) {
        AllSubwayInfo.insubwayList = insubwayList;
    }

    public static boolean isInstatnList() {
        return instatnList;
    }

    public static void setInstatnList(boolean instatnList) {
        AllSubwayInfo.instatnList = instatnList;
    }

    public static boolean isInbarvlDt() {
        return inbarvlDt;
    }

    public static void setInbarvlDt(boolean inbarvlDt) {
        AllSubwayInfo.inbarvlDt = inbarvlDt;
    }

    public static boolean isInbtrainNo() {
        return inbtrainNo;
    }

    public static void setInbtrainNo(boolean inbtrainNo) {
        AllSubwayInfo.inbtrainNo = inbtrainNo;
    }

    public static boolean isInbstatnId() {
        return inbstatnId;
    }

    public static void setInbstatnId(boolean inbstatnId) {
        AllSubwayInfo.inbstatnId = inbstatnId;
    }

    public static boolean isInbstatnNm() {
        return inbstatnNm;
    }

    public static void setInbstatnNm(boolean inbstatnNm) {
        AllSubwayInfo.inbstatnNm = inbstatnNm;
    }

    public static boolean isInrecptnDt() {
        return inrecptnDt;
    }

    public static void setInrecptnDt(boolean inrecptnDt) {
        AllSubwayInfo.inrecptnDt = inrecptnDt;
    }

    public static boolean isInarvlMsg2() {
        return inarvlMsg2;
    }

    public static void setInarvlMsg2(boolean inarvlMsg2) {
        AllSubwayInfo.inarvlMsg2 = inarvlMsg2;
    }

    public static boolean isInarvlMsg3() {
        return inarvlMsg3;
    }

    public static void setInarvlMsg3(boolean inarvlMsg3) {
        AllSubwayInfo.inarvlMsg3 = inarvlMsg3;
    }

    public static boolean isInarvlCd() {
        return inarvlCd;
    }

    public static void setInarvlCd(boolean inarvlCd) {
        AllSubwayInfo.inarvlCd = inarvlCd;
    }

    public static String getRowNum() {
        return rowNum;
    }

    public static void setRowNum(String rowNum) {
        AllSubwayInfo.rowNum = rowNum;
    }

    public static String getSelectedCount() {
        return selectedCount;
    }

    public static void setSelectedCount(String selectedCount) {
        AllSubwayInfo.selectedCount = selectedCount;
    }

    public static String getTotalCount() {
        return totalCount;
    }

    public static void setTotalCount(String totalCount) {
        AllSubwayInfo.totalCount = totalCount;
    }

    public static String getSubwayId() {
        return subwayId;
    }

    public static void setSubwayId(String subwayId) {
        AllSubwayInfo.subwayId = subwayId;
    }

    public static String getUpdnLine() {
        return updnLine;
    }

    public static void setUpdnLine(String updnLine) {
        AllSubwayInfo.updnLine = updnLine;
    }

    public static String getTrainLineNm() {
        return trainLineNm;
    }

    public static void setTrainLineNm(String trainLineNm) {
        AllSubwayInfo.trainLineNm = trainLineNm;
    }

    public static String getSubwayHeading() {
        return subwayHeading;
    }

    public static void setSubwayHeading(String subwayHeading) {
        AllSubwayInfo.subwayHeading = subwayHeading;
    }

    public static String getStatnFid() {
        return statnFid;
    }

    public static void setStatnFid(String statnFid) {
        AllSubwayInfo.statnFid = statnFid;
    }

    public static String getStatnTid() {
        return statnTid;
    }

    public static void setStatnTid(String statnTid) {
        AllSubwayInfo.statnTid = statnTid;
    }

    public static String getStatnId() {
        return statnId;
    }

    public static void setStatnId(String statnId) {
        AllSubwayInfo.statnId = statnId;
    }

    public static String getStatnNm() {
        return statnNm;
    }

    public static void setStatnNm(String statnNm) {
        AllSubwayInfo.statnNm = statnNm;
    }

    public static String getOrdkey() {
        return ordkey;
    }

    public static void setOrdkey(String ordkey) {
        AllSubwayInfo.ordkey = ordkey;
    }

    public static String getSubwayList() {
        return subwayList;
    }

    public static void setSubwayList(String subwayList) {
        AllSubwayInfo.subwayList = subwayList;
    }

    public static String getStatnList() {
        return statnList;
    }

    public static void setStatnList(String statnList) {
        AllSubwayInfo.statnList = statnList;
    }

    public static String getBarvlDt() {
        return barvlDt;
    }

    public static void setBarvlDt(String barvlDt) {
        AllSubwayInfo.barvlDt = barvlDt;
    }

    public static String getBtrainNo() {
        return btrainNo;
    }

    public static void setBtrainNo(String btrainNo) {
        AllSubwayInfo.btrainNo = btrainNo;
    }

    public static String getBstatnId() {
        return bstatnId;
    }

    public static void setBstatnId(String bstatnId) {
        AllSubwayInfo.bstatnId = bstatnId;
    }

    public static String getBstatnNm() {
        return bstatnNm;
    }

    public static void setBstatnNm(String bstatnNm) {
        AllSubwayInfo.bstatnNm = bstatnNm;
    }

    public static String getRecptnDt() {
        return recptnDt;
    }

    public static void setRecptnDt(String recptnDt) {
        AllSubwayInfo.recptnDt = recptnDt;
    }

    public static String getArvlMsg2() {
        return arvlMsg2;
    }

    public static void setArvlMsg2(String arvlMsg2) {
        AllSubwayInfo.arvlMsg2 = arvlMsg2;
    }

    public static String getArvlMsg3() {
        return arvlMsg3;
    }

    public static void setArvlMsg3(String arvlMsg3) {
        AllSubwayInfo.arvlMsg3 = arvlMsg3;
    }

    public static String getArvlCd() {
        return arvlCd;
    }

    public static void setArvlCd(String arvlCd) {
        AllSubwayInfo.arvlCd = arvlCd;
    }
}
