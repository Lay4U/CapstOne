package org.androidtown.home;

import android.util.Log;

import java.util.ArrayList;

public class FindTimeTable extends Thread {
    FindTransferStation s;
    TimeTable a = new TimeTable();
    FindLast FL = new FindLast();
    int checking2line = 1;
    int start = 0;
    int end = 0;
    String FLAG;
    String startPlace;
    String endPlace;
    ArrayList<String> timet = new ArrayList<String>();
    String check = "?";
    String start1, arrive1, line1;
    int timetableflag = 0;
    long startTime = 0, endTime = 0;
    String date;

    public FindTimeTable(FindTransferStation s, String start, String arrive, String line , String date) {
        this.s = s;
        this.start1 = start;
        this.arrive1 = arrive;
        this.line1 = line;
        this.date = date;
    }

    public void run() {
        s = new FindTransferStation(start1, arrive1, line1);
        s.start();

        try {
            s.join();
        } catch (Exception e) {
        }

    }

    public void find() {
        endPlace = s.getArrays().get(s.getArrays().size() - 1).getOc();
        String endPlaceName = s.getArrays().get(s.getArrays().size() - 1).getStationName();
        if (s.getTransferArrays().size() == 0) {
            startPlace = s.getArrays().get(0).getOc();
            endPlace = s.getArrays().get(s.getArrays().size() - 1).getOc();
            try {
                end = Integer.parseInt(endPlace);
                start = Integer.parseInt(startPlace);
            } catch (NumberFormatException e) {
                end = Integer.parseInt(endPlace.substring(1));
                start = Integer.parseInt(startPlace.substring(1));
            }

            System.out.println(s.getArrays().get(0).getStationName() + "  " + "  ***  " + endPlaceName);
            checking2line = 1;
            checkingDir(end, start);
            if ((startPlace.substring(0, 1).equals("K")) && (end >= 300 || start >= 300 || (end <= start && FLAG.equals("2")))) {
                if (FLAG.equals("1")) {
                    FLAG = "2";
                    a.setTT(s.getArrays().get(0).getOc(), date, FLAG );
                } else {
                    FLAG = "1";
                    a.setTT(s.getArrays().get(0).getOc(),date, FLAG);
                }
            } else {
                a.setTT(s.getArrays().get(0).getOc(),date, FLAG);
            }
            a.start();
            try {
                a.join();
            } catch (Exception e) {

            }
            timet.add(a.getLEFTTIME());
            FL.setFL(timet, s.getArrays().get(0).getLine(), date, FLAG, startPlace, endPlace, s.getArrays().get(0).getStationName(), endPlaceName, checking2line);
            FL.findLast("?");
            check = FL.getObjtime();
        } else {
            startTime = System.currentTimeMillis();
            for (int j = s.getTransferArrays().size() - 1; j >= 0; j--) {
                startPlace = s.getTransferArrays().get(j).getRightOC();
                try {
                    end = Integer.parseInt(endPlace);
                    start = Integer.parseInt(startPlace);
                } catch (NumberFormatException e) {
                    end = Integer.parseInt(endPlace.substring(1));
                    start = Integer.parseInt(startPlace.substring(1));
                }
                System.out.println(s.getTransferArrays().get(j).getStationName() + "      **      " + endPlaceName);
                checking2line = 1;
                checkingDir(end, start);
                a.LEFTTIME = "";
                if (startPlace.substring(0, 1).equals("K") && start >= 300) {
                    if (FLAG.equals("1")) {
                        FLAG = "2";
                        a.setTT(s.getTransferArrays().get(j).getRightOC(), date, FLAG);
                    } else {

                        FLAG = "1";
                        a.setTT(s.getTransferArrays().get(j).getRightOC(), date, FLAG);
                    }
                } else {
                    a.setTT(s.getTransferArrays().get(j).getRightOC(), date, FLAG);
                }
                a.run();
                timet.add(a.getLEFTTIME());
                Log.e("hi", a.getLEFTTIME());
                FL.setFL(timet, s.getTransferArrays().get(j).getRightLine(), date, FLAG, s.getTransferArrays().get(0).getRightOC(), endPlace, s.getTransferArrays().get(j).getStationName(), endPlaceName, checking2line);
                if (check.equals("?")) {
                    FL.findLast(check);
                    check = FL.getObjtime();
                } else {
                    FL.findLast(check);
                    check = FL.getObjtime();
                }
                endPlace = s.getTransferArrays().get(j).getLeftOC();
                endPlaceName = s.getTransferArrays().get(j).getStationName();
                endTime = System.currentTimeMillis();
                if (j == 0) {
                    startPlace = s.getArrays().get(0).getOc();
                    System.out.println(s.getArrays().get(0).getStationName() + "    *  " + "   " + endPlaceName);
                    try {
                        end = Integer.parseInt(endPlace);
                        start = Integer.parseInt(startPlace);
                    } catch (NumberFormatException e) {
                        end = Integer.parseInt(endPlace.substring(1));
                        start = Integer.parseInt(startPlace.substring(1));
                    }
                    checking2line = 1;
                    checkingDir(end, start);
                    a.LEFTTIME = "";
                    if ((startPlace.substring(0, 1).equals("K") && (end > start || start >= 300))) {
                        if (FLAG.equals("1")) {
                            FLAG = "2";
                            a.setTT(s.getArrays().get(j).getOc(), date, FLAG);
                        } else {
                            FLAG = "1";
                            a.setTT(s.getArrays().get(j).getOc(), date, FLAG);
                        }
                    } else {
                        a.setTT(s.getArrays().get(j).getOc(), date, FLAG);
                    }

                    a.run();
                    timet.add(a.getLEFTTIME());
                    FL.setFL(timet, s.getArrays().get(0).getLine(), date, FLAG, s.getArrays().get(0).getOc(), endPlace, s.getArrays().get(0).getStationName(), endPlaceName, checking2line);
                    FL.findLast(check);
                    check = FL.getObjtime();


                }


            }
            endTime = System.currentTimeMillis();
        }
    }

    public void checkingDir(int a, int b) {
        //a : end
        //b : start

        //a-b < 0 : 반시계 (상행) a-b > 0 : 시계 (하행)
        if ((201 <= a && a <= 243)) {
            if ((-22 < a - b && a - b < 0) || a - b > 22) {
                FLAG = "2";
                checking2line = 2;
            } else if ((22 > a - b && a - b > 0) || a - b < -22) {
                FLAG = "1";
                checking2line = 2;
            }
        } else {
            if (a > b) {
                FLAG = "2";
            } else {
                FLAG = "1";
            }
        }
    }

    public ArrayList<String> getTimet() {
        return timet;
    }

    public String getCheck() {
        return check;
    }

    public ArrayList<TransInformation> getA() {
        return s.getTransferArrays();
    }

    public void setCheck(String a) {
        this.check = a;
    }

    public ArrayList<stInformation> getB() {
        return s.getArrays();
    }

    public String getTime(){ return s.getTimeAll(); }

}