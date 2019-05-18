package org.androidtown.home;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by 민섭 on 2016-08-12.
 */
public class SeoulThread extends Thread{
    String seoulAPIkey = "4d52505543616c7337346b71535357";
    StringBuffer strBuilder = new StringBuffer();
    StringBuffer sBuffer = new StringBuffer();
    String gpsX,gpsY;
    String nearStation;
    String nearsX,nearsY;
    public SeoulThread(String a,String b) {
        this.gpsX = a;
        this.gpsY = b;
    }
    public void run(){
        connectSeoulNearData();
        parseNearStation(strBuilder.toString());
    }
    public void connectSeoulNearData() {

        try{
            String strUrl = "http://swopenapi.seoul.go.kr/api/subway/"+seoulAPIkey+"/"+"xml/nearBy/"+"0/5/"+gpsX+"/"+gpsY;
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if(conn!=null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                int resCode = conn.getResponseCode();
                if(resCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    while((line=br.readLine())!=null) {
                        strBuilder.append(line);
                    }
                    br.close();
                    conn.disconnect();
                }
            }
        }catch(Exception ex) {
            ex.printStackTrace();
            Log.e("접속오류", ex.toString());
        }

    }
    public void parseNearStation(String str){
        String xml = str;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            Document doc = documentBuilder.parse(is);
            Element element = doc.getDocumentElement();
            NodeList stationNm = element.getElementsByTagName("statnNm");
            NodeList subwayX = element.getElementsByTagName("subwayXcnts");
            NodeList subwayY = element.getElementsByTagName("subwayYcnts");
            Node sitem = stationNm.item(0);
            Node ditem = subwayX.item(0);
            Node fitem = subwayY.item(0);
            Node stext = sitem.getFirstChild();
            Node dtext = ditem.getFirstChild();
            Node ftext = fitem.getFirstChild();

            nearStation = stext.getNodeValue();
            nearsX = dtext.getNodeValue();
            nearsY = ftext.getNodeValue();



        }catch (Exception e){
            Log.e("파싱 중 에러 발생" , e.getMessage());
        }
    }
    public String getXML(){
        return sBuffer.toString();
    }
    public String getNearStation(){
        return nearStation;
    }
    public String getNearsX(){return nearsX;}
    public String getNearsY(){return nearsY;}

}

