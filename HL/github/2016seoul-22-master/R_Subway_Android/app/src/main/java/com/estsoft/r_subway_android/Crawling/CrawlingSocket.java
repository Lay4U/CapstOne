package com.estsoft.r_subway_android.Crawling;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by estsoft on 2016-08-10.
 */
public class CrawlingSocket extends Thread {

    private static final String TAG = "CrawlingSocket";

    private String serverAddr = "222.239.250.207";
    private int port = 11011;
    private String stationName;
    private String accidentInfo;

    public CrawlingSocket(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(serverAddr, port);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(stationName);
            outputStream.flush();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            accidentInfo = input.readLine();
//            Log.d(TAG, "run: " + accidentInfo);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex ) {
            ex.printStackTrace();
        } finally {
            accidentInfo = "Server Connection Fail";
//            Log.d(TAG, "run: " + accidentInfo);
        }
    }

    /*
    <사용법>
    CrawlingSocket crawlingSocket = new CrawlingSocket(역이름);
    crawlingSocket.start();

    try {
        crawlingSocket.join();
    } catch(InterruptedException ex) {
        ex.printStackTrace();
    }
    crawlingSocket.getAccidentInfo(); // String 으로 결과나옴
                                      // 해당역 사고 정보 있으면 "T", 없으면 "F"
     */
}
