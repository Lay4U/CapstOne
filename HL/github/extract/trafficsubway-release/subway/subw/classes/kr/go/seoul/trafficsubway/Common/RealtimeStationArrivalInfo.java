package kr.go.seoul.trafficsubway.Common;


public class RealtimeStationArrivalInfo
{
  private String rowNum;
  private String subwayId;
  private String updnLine;
  private String trainLineNm;
  private String barvlDt;
  private String bstatnNm;
  private String arvlMsg2;
  
  public String getRowNum()
  {
    return rowNum;
  }
  
  public void setRowNum(String rowNum) {
    this.rowNum = rowNum;
  }
  
  public String getSubwayId() {
    return subwayId;
  }
  
  public void setSubwayId(String subwayId) {
    this.subwayId = subwayId;
  }
  
  public String getUpdnLine() {
    return updnLine;
  }
  
  public void setUpdnLine(String updnLine) {
    this.updnLine = updnLine;
  }
  
  public String getTrainLineNm() {
    return trainLineNm;
  }
  
  public void setTrainLineNm(String trainLineNm) {
    this.trainLineNm = trainLineNm;
  }
  
  public String getBarvlDt() {
    return barvlDt;
  }
  
  public void setBarvlDt(String barvlDt) {
    this.barvlDt = barvlDt;
  }
  
  public String getBstatnNm() {
    return bstatnNm;
  }
  
  public void setBstatnNm(String bstatnNm) {
    this.bstatnNm = bstatnNm;
  }
  
  public String getArvlMsg2() {
    return arvlMsg2;
  }
  
  public void setArvlMsg2(String arvlMsg2) {
    this.arvlMsg2 = arvlMsg2;
  }
  
  public RealtimeStationArrivalInfo(String rowNum, String subwayId, String updnLine, String trainLineNm, String barvlDt, String bstatnNm, String arvlMsg2)
  {
    this.rowNum = rowNum;
    this.subwayId = subwayId;
    this.updnLine = updnLine;
    this.trainLineNm = trainLineNm;
    this.barvlDt = barvlDt;
    this.bstatnNm = bstatnNm;
    this.arvlMsg2 = arvlMsg2;
  }
}
