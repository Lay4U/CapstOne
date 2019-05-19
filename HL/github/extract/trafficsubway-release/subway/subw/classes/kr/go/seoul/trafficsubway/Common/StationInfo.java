package kr.go.seoul.trafficsubway.Common;


public class StationInfo
{
  private String subwayId;
  private String statnFnm;
  private String statnTnm;
  
  public String getSubwayId()
  {
    return subwayId;
  }
  
  public void setSubwayId(String subwayId) {
    this.subwayId = subwayId;
  }
  
  public String getStatnFnm() {
    return statnFnm;
  }
  
  public void setStatnFnm(String statnFnm) {
    this.statnFnm = statnFnm;
  }
  
  public String getStatnTnm() {
    return statnTnm;
  }
  
  public void setStatnTnm(String statnTnm) {
    this.statnTnm = statnTnm;
  }
  
  public StationInfo(String subwayId, String statnFnm, String statnTnm) {
    this.subwayId = subwayId;
    this.statnFnm = statnFnm;
    this.statnTnm = statnTnm;
  }
}
