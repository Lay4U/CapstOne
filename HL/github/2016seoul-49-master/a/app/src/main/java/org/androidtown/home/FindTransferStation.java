package org.androidtown.home;

import java.util.ArrayList;

public class FindTransferStation extends Thread {
    String path;
    GetOutCode GOC = new GetOutCode();
    ArrayList<stInformation> arrays = new ArrayList<stInformation>();
    ArrayList<TransInformation> TransferArrays = new ArrayList<TransInformation>();
    String startST, endST, startSTnm;
    String timeAll;
    public FindTransferStation(String startST, String endST, String startSTnm) {
        this.startST = startST;
        this.endST = endST;
        this.startSTnm = startSTnm;
    }

    public void run() {
        tryFTfS();
        findTrans();
    }

    public void tryFTfS() {
        GetShortestPath k = new GetShortestPath(startST, endST);
        k.start();
        try {
            k.join();
        } catch (Exception e) {

        }
        path = k.getPath();
        timeAll = k.getPathTime();
        String[] values = path.split(",");
        String StartLine = startSTnm;
        int flag = 0;
        for (int i = 0; i < values.length; i++) {
            GOC.setSl(values[i], StartLine);
            if (flag == 0) {
                GOC.start();
                try {
                    GOC.join();
                } catch (Exception e) {
                }
            } else {
                GOC.tryGOC();
            }

            arrays.add(new stInformation(GOC.getREALN(), GOC.getSTATION_NM(), GOC.getLineArray(), GOC.getREALFR()));
            if (i > 0) {
                for (int j = 0; j < arrays.get(i - 1).getLineArray().size(); j++) {

                    if (arrays.get(i).getLineArray().contains(arrays.get(i - 1).getLineArray().get(j))) {
                        arrays.get(i).setF(arrays.get(i - 1).getLineArray().get(j), arrays.get(i).getStationName());
                        break;
                    }
                }
            }
            flag = 1;
        }
    }

//    public void print(){
//        for(int i =0;i<TransferArrays.size();i++){
//            System.out.println(i+1+"번째 환승역 : "+TransferArrays.get(i).getStationName());
//        }
//        System.out.println("-----------------------------------------------------");
//
//    }


    public void findTrans() {
        for (int i = 1; i < arrays.size(); i++) {
            if (!arrays.get(i - 1).getLine().equals(arrays.get(i).getLine())) {
                GetOutCode go = new GetOutCode(arrays.get(i - 1).getStationName(), arrays.get(i - 1).getLine());
                go.start();

                try {
                    go.join();
                } catch (Exception e) {

                }
                GetOutCode od = new GetOutCode(arrays.get(i - 1).getStationName(), arrays.get(i).getLine());
                od.start();
                try {
                    od.join();
                } catch (Exception e) {

                }
                TransferArrays.add(new TransInformation(go.getREALN(), od.getREALN(), go.getREALFR(), od.getREALFR(), od.getSTATION_NM()));
            }
        }
    }

    public ArrayList<TransInformation> getTransferArrays() {
        return TransferArrays;
    }

    public ArrayList<stInformation> getArrays() {
        return arrays;
    }

    public String getTimeAll(){
        return timeAll;
    }
}