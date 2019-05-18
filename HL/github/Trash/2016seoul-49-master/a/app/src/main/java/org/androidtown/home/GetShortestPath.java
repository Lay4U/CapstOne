package org.androidtown.home;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by 민섭 on 2016-09-21.
 */

public class GetShortestPath extends Thread{
    private String Path;
    private String PathTime;
    private String minPath;
    private String minPathtime;
    String startST,endST;
    public GetShortestPath(String startST, String endST){
        this.startST = startST;
        this.endST = endST;


    }
    public GetShortestPath(){

    }
    public void setGSP(String startST,String endST){
        this.startST = startST;
        this.endST = endST;

    }
    public void tryGetSTP(){
        try{
            String urlc = "http://swopenAPI.seoul.go.kr/api/subway/"+MainActivity.api_key+"/xml/shortestRoute/0/5/"+ URLEncoder.encode(startST, "UTF-8")+"/"+URLEncoder.encode(endST,"UTF-8");
            URL url = new URL(urlc);
            URLConnection conn = url.openConnection();
            Document doc = GetOutCode.parseXML(conn.getInputStream());
            NodeList descNodes = doc.getElementsByTagName("row");

            for(int i=0; i<descNodes.getLength();i++){

                for(Node node = descNodes.item(i).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
                    if(node.getNodeName().equals("shtStatnNm")){
                        Path=node.getTextContent();
                    }else if (node.getNodeName().equals("shtTravelTm")){
                        PathTime = node.getTextContent();
                    }else if (node.getNodeName().equals("minshtStatnNm")){
                        minPath = node.getTextContent();
                    }else if (node.getNodeName().equals("minshtTravelTm")){
                        minPathtime = node.getTextContent();
                    }
                }
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public String getPath() {
        return Path;
    }


    public String getPathTime() {
        return PathTime;
    }

    public void run(){
        tryGetSTP();
    }

}

