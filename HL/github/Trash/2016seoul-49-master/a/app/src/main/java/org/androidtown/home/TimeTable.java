package org.androidtown.home;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;

public class TimeTable extends Thread{
    public String LEFTTIME;
    public String outCode;
    public String WEEK_TAG;
    public String INOUT_TAG;
    public String page="1";
    public TimeTable(String outCode,String WEEK_TAG,String INOUT_TAG){
        this.outCode = outCode;
        this.WEEK_TAG = WEEK_TAG;
        this.INOUT_TAG = INOUT_TAG;

    }
    public void run(){
        if(page.equals("1")){
            tryNotPage(outCode,WEEK_TAG,INOUT_TAG);
        }else{
            tryPage(outCode,WEEK_TAG,INOUT_TAG,page);
        }
    }
    public TimeTable(){

    }
    public void setTT(String outCode,String WEEK_TAG,String INOUT_TAG){
        this.outCode = outCode;
        this.WEEK_TAG = WEEK_TAG;
        this.INOUT_TAG = INOUT_TAG;
    }
    public void setTTs(String outCode,String WEEK_TAG,String INOUT_TAG,String page){
        this.outCode = outCode;
        this.WEEK_TAG = WEEK_TAG;
        this.INOUT_TAG = INOUT_TAG;
        this.page = page;
    }
    public void tryNotPage(String outCode,String WEEK_TAG,String INOUT_TAG){

        try{
            String urlc = "http://openapi.seoul.go.kr:8088/"+MainActivity.api_key+"/xml/SearchSTNTimeTableByFRCodeService/1/258/"+outCode+"/"+WEEK_TAG+"/"+INOUT_TAG+"/";
            URL url = new URL(urlc);
            URLConnection conn = url.openConnection();
            Document doc = GetOutCode.parseXML(conn.getInputStream());
            NodeList descNodes = doc.getElementsByTagName("row");

            for(int i=0; i<descNodes.getLength();i++){
                for(Node node = descNodes.item(i).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
                    if(i>=descNodes.getLength()-30){
                        if(INOUT_TAG.equals("1")){
                            if(node.getNodeName().equals("ARRIVETIME")){
                                LEFTTIME = LEFTTIME + "," +node.getTextContent();
                            }else if(node.getNodeName().equals("SUBWAYENAME")){
                                LEFTTIME = LEFTTIME + "=" +node.getTextContent();
                            }
                        }else{
                            if(node.getNodeName().equals("LEFTTIME")){
                                LEFTTIME = LEFTTIME + "," +node.getTextContent();
                            }else if(node.getNodeName().equals("SUBWAYENAME")){
                                LEFTTIME = LEFTTIME + "=" +node.getTextContent();

                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public TimeTable(String outCode,String WEEK_TAG,String INOUT_TAG,String page){
        this.outCode = outCode;
        this.WEEK_TAG = WEEK_TAG;
        this.INOUT_TAG = INOUT_TAG;
        this.page = page;

    }
    public void tryPage(String outCode, String WEEK_TAG, String INOUT_TAG, String page){
        try{
            String urlc = "http://openapi.seoul.go.kr:8088/"+MainActivity.api_key+"/xml/SearchSTNTimeTableByFRCodeService/1/"+page+"/"+outCode+"/"+WEEK_TAG+"/"+INOUT_TAG+"/";
            URL url = new URL(urlc);
            URLConnection conn = url.openConnection();
            Document doc = GetOutCode.parseXML(conn.getInputStream());
            NodeList descNodes = doc.getElementsByTagName("row");

            for(int i=0; i<descNodes.getLength();i++){

                for(Node node = descNodes.item(i).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
                    if(i>=descNodes.getLength()-30){
                        if(INOUT_TAG.equals("2")||(INOUT_TAG.equals("1"))){
                            if(node.getNodeName().equals("LEFTTIME")){
                                LEFTTIME = LEFTTIME + "," +node.getTextContent();
                            }else if(node.getNodeName().equals("SUBWAYENAME")){
                                LEFTTIME = LEFTTIME + "=" +node.getTextContent();

                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String getLEFTTIME() {
        return LEFTTIME;
    }
}

