package org.androidtown.home;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 민섭 on 2016-09-21.
 * <p/>
 * Created by 민섭 on 2016-09-21.
 */


public class FindLast extends Thread {
    private ArrayList<String> timeTable;
    private String startline, date, dir, endOC, stOC;
    private String[] values;
    private ArrayList<String> Station;
    private ArrayList<String> StationOC;
    private String and, or;
    private GetOutCode getOutforLast = new GetOutCode(), getOutforLast2 = new GetOutCode();
    private GetShortestPath gsp = new GetShortestPath(), gspforSS = new GetShortestPath();
    public int start = 0;
    public int end = 0;
    public ArrayList<String> ltime, ltimeStation, ltimeStationOC;
    public String[] ltimeResult;
    public int arrive = 0;
    public String time, SStime;
    private String startName;
    private String endName;
    public String duringtime, duringtimeSS1;
    public String objtime;
    public int checking2, b = 0;
    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    DateFormat dg = new SimpleDateFormat("HH:mm:ss");
    int getOutforLastflag = 0;
    int getOutforLast2flag = 0;
    int gspforSSflag = 0;
    int gspflag = 0;
    Date jo, forSSjo;
    Date to, forSSto;
    int cnt = 0;
    TimeTable for2line;
    int cnt2 = 0;
    static int line1_Flag=0;
    static int checkLine1_Flag=0;

    public FindLast(ArrayList<String> k, String startline, String date, String dir, String endOC, String stOC, String startName, String endName, int checking) {
        this.timeTable = k;
        this.startline = startline;
        this.date = date;
        this.dir = dir;
        this.endOC = endOC;
        this.stOC = stOC;
        this.startName = startName;
        this.endName = endName;
        this.checking2 = checking;
    }

    public FindLast() {

    }

    public void setFL(ArrayList<String> k, String startline, String date, String dir, String endOC, String stOC, String startName, String endName, int checking) {
        this.timeTable = k;
        this.startline = startline;
        this.date = date;
        this.dir = dir;
        this.endOC = endOC;
        this.stOC = stOC;
        this.startName = startName;
        this.endName = endName;
        this.checking2 = checking;
    }

    public String getObjtime() {
        return objtime;
    }

    public boolean getSS(int end1, int start1, int arrive1, String dir1, int a, String stName) {
        //성수행 시계 , arrive 는 성수를 지나쳐서 가는 곳 즉 하행의 경우는 성수를 지나쳐서 가게되면 오류가 나기때문에 여기서 조건을 만족시킨다
        //하행의 경우도 성수를 지나쳐서 하행을 하게되면 오류가 나기때문에 여기서 조건을 만족시킨다.
        //2호선의 경우 dir1이 1일때 하행 dir1이 2일때 상행
        //a는 station[a]로 사용
        if (dir1.equals("1")) { //하행
            if (arrive1 < 223 && arrive1 > end1 && end1 == 211) {
                //oc,date,inout
                for2line = new TimeTable("211", date, "1", "245");
                for2line.start();
                try {
                    for2line.join();
                } catch (Exception e) {

                }
                ltime = new ArrayList<String>();
                ltime.add(for2line.getLEFTTIME());
                ltimeStation = new ArrayList<String>();
                for (int i = ltime.size() - 1; i >= 0; i--) {
                    ltimeResult = ltime.get(ltime.size() - 1).split(",");
                    for (int j = ltimeResult.length - 1; j > 0; j--) {
                        int k = ltimeResult[j].indexOf("=");
                        or = ltimeResult[j].substring(k + 1);
                        ltimeStation.add(or);
                    }
                }
                ltimeStationOC = new ArrayList<String>();
                for (int h = 0; h < ltimeStation.size(); h++) {
                    getOutforLast2.setSl(ltimeStation.get(h), "2");
                    if (getOutforLast2flag == 0) {
                        getOutforLast2.start();
                        try {
                            getOutforLast2.join();
                        } catch (Exception e) {

                        }
                    } else {
                        getOutforLast2.run();
                    }
                    ltimeStationOC.add(getOutforLast2.getREALFR());
                    getOutforLast2flag = 1;
                }
                //성수역의 시간표를 얻어온다
                //성수 -> 종합운동장 이기 때문에
                // 종합운동장이 도착지 즉 성수에서 종합운동장 갈 수 있는지 봐야한다.
                for (int q = 0; q < ltimeStation.size(); q++) {
                    int hang = Integer.parseInt(ltimeStationOC.get(q));
                    //start : 이대 // end : 성수 // arrive : 잠실운동장
                    if (hang > arrive1) {
                        SStime = ltimeResult[ltimeResult.length - 1 - q].substring(0, 8);
                        gspforSS.setGSP(stName, "성수");
                        if (gspforSSflag == 0) {
                            gspforSS.start();
                            try {
                                gspforSS.join();
                            } catch (Exception e) {

                            }
                        } else {
                            gspforSS.run();
                        }
                        gspforSSflag = 1;
                        duringtimeSS1 = gspforSS.getPathTime();
                        try {
                            forSSto = dg.parse(SStime);
                        } catch (Exception e) {

                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(forSSto);
                        cal.add(Calendar.MINUTE, -((Integer.parseInt(duringtimeSS1)) + 3));
                        SStime = dg.format(cal.getTime());
                        cnt2 = 1;
                        break;
                    } else {
                    }
                }
                return true;
            }
        } else if (dir1.equals("2") && end1 != 202 && end1 != 228 && end != 234 && end != 239) { //상행

            for2line = new TimeTable("211", date, "2", "358");
            for2line.start();
            try {
                for2line.join();
            } catch (Exception e) {

            }
            ltime = new ArrayList<String>();
            ltime.add(for2line.getLEFTTIME());
            ltimeStation = new ArrayList<String>();
            for (int i = ltime.size() - 1; i >= 0; i--) {
                ltimeResult = ltime.get(ltime.size() - 1).split(",");
                for (int j = ltimeResult.length - 1; j > 0; j--) {
                    int k = ltimeResult[j].indexOf("=");
                    or = ltimeResult[j].substring(k + 1);
                    ltimeStation.add(or);
                }
            }
            ltimeStationOC = new ArrayList<String>();
            for (int h = 0; h < ltimeStation.size(); h++) {
                getOutforLast2.setSl(ltimeStation.get(h), "2");
                if (getOutforLast2flag == 0) {
                    getOutforLast2.start();
                    try {
                        getOutforLast2.join();
                    } catch (Exception e) {

                    }
                } else {
                    getOutforLast2.run();
                }
                ltimeStationOC.add(getOutforLast2.getREALFR());
                getOutforLast2flag = 1;
            }
            //성수역의 시간표를 얻어온다
            //성수 -> 종합운동장 이기 때문에
            // 종합운동장이 도착지 즉 성수에서 종합운동장 갈 수 있는지 봐야한다.
            for (int q = 0; q < ltimeStation.size(); q++) {
                int hang;
                b = 0;
                try {
                    hang = Integer.parseInt(ltimeStationOC.get(q)); //211-4

                } catch (Exception e) {
                    hang = Integer.parseInt(ltimeStationOC.get(q).substring(0, 3));
                    b++;
                }
                //start : 건대입구 // end : 성수 // arrive : 홍대입구  //(start<=arrive && arrive<=end )


                if ((hang >= arrive1 && (202 <= arrive1 || arrive1 < 211) && b != 1) || (hang <= arrive1 && b != 1 && hang > 223) || (hang <= arrive1 && hang == 202 && start1 >= arrive1)) {
                    SStime = ltimeResult[ltimeResult.length - 1 - q].substring(0, 8);
                    gspforSS.setGSP(stName, "성수");
                    if (gspforSSflag == 0) {
                        gspforSS.start();
                        try {
                            gspforSS.join();
                        } catch (Exception e) {

                        }
                    } else {
                        gspforSS.run();
                    }
                    gspforSSflag = 1;
                    duringtimeSS1 = gspforSS.getPathTime();
                    try {
                        forSSto = dg.parse(SStime);
                    } catch (Exception e) {

                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(forSSto);
                    cal.add(Calendar.MINUTE, -((Integer.parseInt(duringtimeSS1)) + 3));
                    SStime = dg.format(cal.getTime());
                    cnt2 = 1;

                    break;
                } else {
                }
            }
            return true;
        }
        return false;
    }

    public void findLast(String objtime) {
        this.objtime = objtime;
        Station = new ArrayList<String>();
        if (timeTable.size() - 1 == 0) {
            cnt++;
        }
        for (int i = timeTable.size() - 1; i >= 0; i--) {

            values = timeTable.get(timeTable.size() - 1).split(",");
            for (int j = values.length - 1; j > 0; j--) {
                int k = values[j].indexOf("=");
                and = values[j].substring(k + 1);
                Station.add(and);
            }
        }
        //외부코드 를 얻고 그 외부코드 안에 들어오는 지 확인하기
        StationOC = new ArrayList<String>();
        for (int j = 0; j < Station.size(); j++) {
            getOutforLast.setSl(Station.get(j), startline);
            if (getOutforLastflag == 0) {
                getOutforLast.start();
                try {
                    getOutforLast.join();
                } catch (Exception e) {

                }
            } else {
                getOutforLast.run();
            }
            StationOC.add(getOutforLast.getREALFR());
            getOutforLastflag = 1;
        }
        checkLast(objtime);
    }

    public void checkLast(String objtime1) {
        try {
            start = Integer.parseInt(endOC);
            arrive = Integer.parseInt(stOC);
        } catch (NumberFormatException e) {
            if(stOC.substring(0,1).equals("P")){
                checkLine1_Flag=1;
            }
            start = Integer.parseInt(endOC.substring(1));
            arrive = Integer.parseInt(stOC.substring(1));

        }

        if (objtime1.equals("?")) {

            for (int k = 0; k < StationOC.size(); k++) {

                try {
                    end = Integer.parseInt(StationOC.get(k));
                } catch (Exception e) {
                    end = Integer.parseInt(StationOC.get(k).substring(1));
                    if(StationOC.get(k).substring(0, 1).equals("P")){
                        line1_Flag = 1;
                    }
                }
                if (dir.equals("1")) {
                    if (checking2 == 2) {
                        long startTime = System.currentTimeMillis();
                        if ((start <= arrive && end == 202 && start != end) || ((start <= arrive && arrive <= end)) ||
                                (arrive >= end && (arrive == 202)) || (arrive <= end && (end == 211 || (arrive == 201 && end != 239)))
                                || getSS(end, start, arrive, dir, k, startName)) {
                            long endTime = System.currentTimeMillis();
                            time = values[values.length - 1 - k].substring(0, 8);
                            if (cnt2 == 1) {

                                try {
                                    SStime = SStime.replaceAll("00", "24");
                                    forSSjo = dg.parse(SStime);

                                } catch (Exception e) {

                                }
                                try {
                                    forSSto = dg.parse(time);
                                } catch (Exception e) {

                                }
                                if (forSSjo.after(forSSto)) {
                                    gsp.setGSP(startName, endName);
                                    if (gspflag == 0) {
                                        gsp.start();
                                        try {
                                            gsp.join();
                                        } catch (Exception e) {

                                        }
                                    } else {
                                        gsp.run();
                                    }
                                    gspflag = 1;
                                    duringtime = gsp.getPathTime();

                                    try {
                                        to = dg.parse(time);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    objtime = time;

                                    break;
                                } else {
                                }
                            }
                            if (cnt2 == 0) {

                                gsp.setGSP(startName, endName);
                                if (gspflag == 0) {
                                    gsp.start();
                                    try {
                                        gsp.join();
                                    } catch (Exception e) {

                                    }
                                } else {
                                    gsp.run();
                                }
                                gspflag = 1;
                                duringtime = gsp.getPathTime();

                                try {
                                    to = dg.parse(time);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                objtime = time;

                                break;

                            }
                        } else {

                        }
                    } else {
                        if (((!endOC.substring(0, 1).equals("K") && end <= arrive)) || ((endOC.substring(0, 1).equals("K") && end >= arrive && end > 300))
                                || ((endOC.substring(0, 1).equals("K") && end <= arrive && end == 117))) {
                            time = values[values.length - 1 - k].substring(0, 8);
                            gsp.setGSP(startName, endName);
                            if (gspflag == 0) {
                                gsp.start();
                                try {
                                    gsp.join();
                                } catch (Exception e) {

                                }
                            } else {
                                gsp.run();
                            }
                            gspflag = 1;
                            duringtime = gsp.getPathTime();

                            try {
                                to = dg.parse(time);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            objtime = time;
                            break;
                        } else {
                        }
                    }// (end >= arrive && end==202) || ((start<=arrive && arrive<=end && arrive<=246)) || start-end == start-arrive
                } else {
                    if (checking2 == 2) {
                        // 뚝섬 - 을입 > 뚝섬 - 이대    210 - 202    210 - 242     210 - 239  210  - 242
//						checking2line=2;									// 출발지 - ~행 > 출발지 - 도착지 == 갈수없다. 성수까진 무조건 가고 성수~ 도착지 - 출발지~성수 시간 !! 막차!!
                        //2호선상행  //243 end 239              출발지보단 도착지가 작고 도착지가 종착점보단 크다.

                        if ((end <= arrive && arrive >= 223 && end != 202 && end != 211) || (start >= arrive && arrive >= end)
                                || (start >= arrive && arrive >= end) || (end != 202 && end <= arrive && arrive != 201 && end != 239 && end != 234 && end != 211)
                                || (end >= start && end <= arrive && arrive < 223) || (end <= arrive && start >= arrive)
                                || getSS(end, start, arrive, dir, k, startName)) {
                            time = values[values.length - 1 - k].substring(0, 8);
                            if (cnt2 == 1) {
                                try {
                                    SStime = SStime.replaceAll("00", "24");
                                    forSSjo = dg.parse(SStime);

                                } catch (Exception e) {

                                }
                                try {
                                    forSSto = dg.parse(time);
                                } catch (Exception e) {

                                }
                                if (forSSjo.after(forSSto)) {
                                    gsp.setGSP(startName, endName);
                                    if (gspflag == 0) {
                                        gsp.start();
                                        try {
                                            gsp.join();
                                        } catch (Exception e) {

                                        }
                                    } else {
                                        gsp.run();
                                    }
                                    gspflag = 1;
                                    duringtime = gsp.getPathTime();

                                    try {
                                        to = dg.parse(time);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }


                                    objtime = time;

                                    break;
                                } else {
                                }
                            }
                            if (cnt2 == 0) {
                                gsp.setGSP(startName, endName);
                                if (gspflag == 0) {
                                    gsp.start();
                                    try {
                                        gsp.join();
                                    } catch (Exception e) {

                                    }
                                } else {
                                    gsp.run();
                                }
                                gspflag = 1;
                                duringtime = gsp.getPathTime();

                                try {
                                    to = dg.parse(time);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                objtime = time;
                                break;
                            } else {
                            }
                        }
                    } else {


                                           //(line1_Flag!=1 && checkLine1_Flag!=1 && arrive<=end) || ((endOC.substring(0, 1).equals("K")) && start>300 && arrive>=313) || (line1_Flag==1&&checkLine1_Flag==1 && arrive<=end)
                                                                         //하행일때
                        if ((line1_Flag!=1 && checkLine1_Flag!=1 && arrive<=end) || ((endOC.substring(0, 1).equals("K")) && start>300 && arrive>=313) || (line1_Flag==1&&checkLine1_Flag==1 && arrive<=end)) {
                            time = values[values.length - 1 - k].substring(0, 8);
                            gsp.setGSP(startName, endName);
                            if (gspflag == 0) {
                                gsp.start();
                                try {
                                    gsp.join();
                                } catch (Exception e) {

                                }
                            } else {
                                gsp.run();
                            }
                            gspflag = 1;
                            duringtime = gsp.getPathTime();

                            try {
                                to = dg.parse(time);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                            objtime = time;
                            break;
                        } else {
                        }
                    }
                }
            }
        } else { //환승경우
            if (endName.equals("총신대입구(이수)")) {
                endName = endName.substring(0, 5);
            } else if (startName.equals("총신대입구(이수)")) {
                startName = startName.substring(0, 5);
            }
            gsp.setGSP(startName, endName);
            if (gspflag == 0) {
                gsp.start();
                try {
                    gsp.join();
                } catch (Exception e) {

                }
            } else {
                gsp.run();
            }
            gspflag = 1;
            if (startName.equals(endName)) {
                duringtime = "0";

            } else {
                duringtime = gsp.getPathTime();
                try {
                    to = dg.parse(objtime);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(to);
                cal.add(Calendar.MINUTE, -((Integer.parseInt(duringtime)) + 5));
                time = dg.format(cal.getTime());
                if (time.substring(0, 2).equals("00")) {
                    String K = time.replaceAll("00", "24");
                    try {
                        to = df.parse(K);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        to = dg.parse(time);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                try {
                    start = Integer.parseInt(endOC);
                    arrive = Integer.parseInt(stOC);
                } catch (NumberFormatException e) {
                    start = Integer.parseInt(endOC.substring(1));
                    arrive = Integer.parseInt(stOC.substring(1));
                }
                for (int k = 0; k < StationOC.size(); k++) {
                    try {
                        end = Integer.parseInt(StationOC.get(k));
                    } catch (Exception e) {
                        end = Integer.parseInt(StationOC.get(k).substring(1));
                    }
                    if (dir.equals("1")) {
                        try {
                            jo = dg.parse(values[values.length - 1 - k].substring(0, 8));
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (checking2 == 2) {
                            //2호선 하행일경우
                            if (((end <= arrive && end == 202 && start <= arrive) || ((start <= arrive && arrive <= end)) || (arrive >= end && (arrive == 202)) || (arrive <= end && !(221 < end) && (end == 211 || arrive == 201)) || getSS(end, start, arrive, dir, k, startName)) && to.after(jo)) {
                                time = values[values.length - 1 - k].substring(0, 8);
                                gsp.setGSP(startName, endName);
                                if (gspflag == 0) {
                                    gsp.start();
                                    try {
                                        gsp.join();
                                    } catch (Exception e) {

                                    }
                                } else {
                                    gsp.run();
                                }
                                gspflag = 1;
                                duringtime = gsp.getPathTime();

                                try {
                                    to = dg.parse(time);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                objtime = time;
                                break;
                            } else {
                            }

                        } else {
                            if ((((!endOC.substring(0, 1).equals("K") && end <= arrive)) || ((endOC.substring(0, 1).equals("K") && end >= arrive && end > 300))
                                    || ((endOC.substring(0, 1).equals("K") && end <= arrive && end == 117))) && to.after(jo)) {
                                time = values[values.length - 1 - k].substring(0, 8);
                                try {
                                    to = dg.parse(time);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                Calendar cal1 = Calendar.getInstance();
                                cal.setTime(to);
                                objtime = dg.format(cal1.getTime());
                                objtime = time;

                                break;
                            } else {
                            }
                        }
                    } else {
                        try {
                            jo = dg.parse(values[values.length - 1 - k].substring(0, 8));
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (checking2 == 2) { //2호선 하행
                            if (((end >= start && end <= arrive && arrive <= 246) || start - end == start - arrive
                                    || (end <= arrive && start >= arrive) || getSS(end, start, arrive, dir, k, startName)) && to.after(jo)) {
                                time = values[values.length - 1 - k].substring(0, 8);
                                gsp.setGSP(startName, endName);
                                if (gspflag == 0) {
                                    gsp.start();
                                    try {
                                        gsp.join();
                                    } catch (Exception e) {

                                    }
                                } else {
                                    gsp.run();
                                }
                                gspflag = 1;
                                duringtime = gsp.getPathTime();

                                try {
                                    to = dg.parse(time);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                objtime = time;
                                break;
                            } else {
                            }

                        } else {
                            if (((arrive <= end || ((endOC.substring(0, 1).equals("K")) && start > 300 && arrive >= 313)) && to.after(jo))) {
                                time = values[values.length - 1 - k].substring(0, 8);
                                try {
                                    to = dg.parse(time);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                objtime = time;

                                break;
                            } else {
                            }
                        }


                    }
                }
            }

        }

    }
}

