package com.example.jongyepn.subwayinfo;

import java.util.ArrayList;

public class Variable {

    private static Variable variable;
    private static ArrayList<SubwayInfo> SubwayInfo = new ArrayList();
    private static ArrayList<SubwayInfo> UPSubwayInfo = new ArrayList();
    private static ArrayList<SubwayInfo> DNSubwayInfo = new ArrayList();

    private static String field1 = "";
    private static String field2 = "";
    private static String field3 = "";
    private static String field4 = "";
    private static String field5 = "";
    private static String field6 = "";
    private static String field7 = "";
    private static String field8 = "";


    public static String getField1() {
        return field1;
    }

    public static void setField1(String field1) {
        Variable.field1 = field1;
    }

    public static String getField2() {
        return field2;
    }

    public static void setField2(String field2) {
        Variable.field2 = field2;
    }

    public static String getField3() {
        return field3;
    }

    public static void setField3(String field3) {
        Variable.field3 = field3;
    }

    public static String getField4() {
        return field4;
    }

    public static void setField4(String field4) {
        Variable.field4 = field4;
    }

    public static String getField5() {
        return field5;
    }

    public static void setField5(String field5) {
        Variable.field1 = field5;
    }

    public static String getField6() {
        return field6;
    }

    public static void setField6(String field6) {
        Variable.field6 = field6;
    }

    public static String getField7() {
        return field7;
    }

    public static void setField7(String field7) {
        Variable.field7 = field7;
    }

    public static String getField8() {
        return field8;
    }

    public static void setField8(String field8) {
        Variable.field8 = field8;
    }

    private static ArrayList<String> Line4 = new ArrayList<>();

    private Variable() {
        variable = new Variable();
    }

    public static Variable getInstance(){
        return variable;
    }

    public static ArrayList<SubwayInfo> getSubwayInfo() {
        return SubwayInfo;
    }

    public static void setSubwayInfo(ArrayList<SubwayInfo> SubwayInfo) {
        Variable.SubwayInfo = SubwayInfo;
    }


    public static ArrayList<String> getLine4() {
        return Line4;
    }

    public static void setLine4(ArrayList<String> line4) {
        Line4 = line4;
    }

    public static ArrayList<SubwayInfo> getUPSubwayInfo() {
        return UPSubwayInfo;
    }

    public static void setUPSubwayInfo(ArrayList<SubwayInfo> UPSubwayInfo) {
        Variable.UPSubwayInfo = UPSubwayInfo;
    }

    public static ArrayList<SubwayInfo> getDNSubwayInfo() {
        return DNSubwayInfo;
    }

    public static void setDNSubwayInfo(ArrayList<SubwayInfo> DNSubwayInfo) {
        Variable.DNSubwayInfo = DNSubwayInfo;
    }



}
