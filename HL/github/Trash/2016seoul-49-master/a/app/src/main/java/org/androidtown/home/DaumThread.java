package org.androidtown.home;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 민섭 on 2016-08-12.
 */
public class DaumThread extends Thread{
    StringBuffer strBuffer2 = new StringBuffer();
    String x,y;
    String Latitude,Longitude;
    String selectCoords;
    String strUrl;
    public DaumThread(String a,String b,String selectCoords){
        this.Latitude = a; //Y
        this.Longitude = b; //X
        this.selectCoords = selectCoords;


    }

    public void run() {
        connectDaumTranslateCoord();
        parseTransCoord(strBuffer2.toString());
    }

    public void connectDaumTranslateCoord(){

        try {
            if(selectCoords.equals("WGS84")) {
                strUrl = "https://apis.daum.net/local/geo/transcoord?apikey=f0cd1ddcae53ef8ea47dd881deb65fdd&fromCoord=WGS84&y=" + Latitude + "&x=" + Longitude + "&toCoord=WTM&output=json";
            }
            else if(selectCoords.equals("WTM")){
                strUrl = "https://apis.daum.net/local/geo/transcoord?apikey=f0cd1ddcae53ef8ea47dd881deb65fdd&fromCoord=WTM&y=" + Longitude + "&x=" + Latitude + "&toCoord=WGS84&output=json";
            }
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        strBuffer2.append(line);

                    }

                    br.close();
                    conn.disconnect();
                }
            }

        } catch (Exception e) {
            Log.e("parse error", e.toString());
        }
    }
    public void parseTransCoord(String str){
        String json = str;
        try {
            JSONArray jAr = new JSONArray("["+json+"]");
            JSONObject A = jAr.getJSONObject(0);
            x = A.getString("x");
            y = A.getString("y");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getResult(){
        return strBuffer2.toString();
    }
    public String getX(){
        return x;
    }
    public String getY(){
        return y;
    }
}
