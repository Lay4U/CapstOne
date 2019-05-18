package org.androidtown.home;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class GetOutCode extends Thread{
    private String STATION_CD= null;
    private String STATION_NM= null;
    private String LINE_NUM= null;
    private String FR_CODE= null;
    private String REALN = null;
    private String REALFR = null;
    ArrayList<String> linearray;
    String StationName,line_nm="?";
    public GetOutCode(String StationName,String line_nm){
        this.StationName = StationName;
        this.line_nm = line_nm;
    }
    public GetOutCode(String StationName){
        this.StationName = StationName;
    }
    public GetOutCode(){

    }
    public void setSl(String StationName,String line_nm){
        this.StationName = StationName;
        this.line_nm = line_nm;
    }

    public void run(){
        tryGOC();
    }
    public void tryGOC(){
        try{
            String urlc = "http://openapi.seoul.go.kr:8088/"+ MainActivity.api_key+"/xml/SearchInfoBySubwayNameService/1/5/"+ URLEncoder.encode(StationName, "UTF-8")+"/";

            URL url = new URL(urlc);
            URLConnection conn = url.openConnection();
            Document doc = parseXML(conn.getInputStream());
            NodeList descNodes = doc.getElementsByTagName("row");
            linearray = new ArrayList<String>();
            for(int i=0; i<descNodes.getLength();i++){
                for(Node node = descNodes.item(i).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
                    if(node.getNodeName().equals("STATION_CD")){
                        STATION_CD = node.getTextContent();
                    }else if(node.getNodeName().equals("STATION_NM")){
                        STATION_NM = node.getTextContent();
                    }else if(node.getNodeName().equals("LINE_NUM")){
                        LINE_NUM = node.getTextContent();
                        linearray.add(LINE_NUM);
                    }else if(node.getNodeName().equals("FR_CODE")){
                        FR_CODE = node.getTextContent();
                    }

                }

                if(line_nm.equals(LINE_NUM)){
                    REALN = LINE_NUM;
                    REALFR = FR_CODE;
                }
            }

        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Document parseXML(InputStream inputStream) {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;

        try{

            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(inputStream);

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return doc;
    }

    public String getSTATION_CD() {
        return STATION_CD;
    }


    public String getSTATION_NM() {
        return STATION_NM;
    }


    public String getLINE_NM() {
        return LINE_NUM;
    }


    public String getFR_CODE() {
        return FR_CODE;
    }
    public String getREALFR(){
        return REALFR;
    }
    public String getREALN(){
        return REALN;
    }
    public ArrayList<String> getLineArray(){
        return linearray;
    }
    public void setStation(String STCD,String STNM,String LNM,String FRC, String REALN) {
        this.STATION_CD = STCD;
        this.STATION_NM = STNM;
        this.LINE_NUM = LNM;
        this.FR_CODE = FRC;
        this.REALN = REALN;
    }



}
