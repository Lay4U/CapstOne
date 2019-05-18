package kr.go.seoul.trafficsubway.Common;


public class FirstLastTimeInfo
{
  private String subwayId;
  
  private String subwayNm;
  private String lastcarDiv;
  private String updnLine;
  private String expressyn;
  private String subwayename;
  private String weekendTranHour;
  private String saturdayTranHour;
  private String holidayTranHour;
  
  public FirstLastTimeInfo(String subwayId, String subwayNm, String lastcarDiv, String updnLine, String expressyn, String subwayename, String weekendTranHour, String saturdayTranHour, String holidayTranHour)
  {
    this.subwayId = subwayId;
    this.subwayNm = subwayNm;
    this.lastcarDiv = lastcarDiv;
    this.updnLine = updnLine;
    this.expressyn = expressyn;
    this.subwayename = subwayename;
    this.weekendTranHour = weekendTranHour;
    this.saturdayTranHour = saturdayTranHour;
    this.holidayTranHour = holidayTranHour;
  }
  
  public String getSubwayId() {
    return subwayId;
  }
  
  public void setSubwayId(String subwayId) {
    this.subwayId = subwayId;
  }
  
  public String getSubwayNm() {
    return subwayNm;
  }
  
  public void setSubwayNm(String subwayNm) {
    this.subwayNm = subwayNm;
  }
  
  public String getLastcarDiv() {
    return lastcarDiv;
  }
  
  public void setLastcarDiv(String lastcarDiv) {
    this.lastcarDiv = lastcarDiv;
  }
  
  public String getUpdnLine() {
    return updnLine;
  }
  
  public void setUpdnLine(String updnLine) {
    this.updnLine = updnLine;
  }
  
  public String getExpressyn() {
    return expressyn;
  }
  
  public void setExpressyn(String expressyn) {
    this.expressyn = expressyn;
  }
  
  public String getSubwayename() {
    return subwayename;
  }
  
  public void setSubwayename(String subwayename) {
    this.subwayename = subwayename;
  }
  
  public String getWeekendTranHour() {
    return weekendTranHour;
  }
  
  public void setWeekendTranHour(String weekendTranHour) {
    this.weekendTranHour = weekendTranHour;
  }
  
  public String getSaturdayTranHour() {
    return saturdayTranHour;
  }
  
  public void setSaturdayTranHour(String saturdayTranHour) {
    this.saturdayTranHour = saturdayTranHour;
  }
  
  public String getHolidayTranHour() {
    return holidayTranHour;
  }
  
  public void setHolidayTranHour(String holidayTranHour) {
    this.holidayTranHour = holidayTranHour;
  }
}
