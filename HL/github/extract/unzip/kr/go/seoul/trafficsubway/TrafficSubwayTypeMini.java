package kr.go.seoul.trafficsubway;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import kr.go.seoul.trafficsubway.Common.FontUtils;
import kr.go.seoul.trafficsubway.Common.RealtimeStationArrivalInfo;
import kr.go.seoul.trafficsubway.Common.StationInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;




public class TrafficSubwayTypeMini
  extends LinearLayout
{
  private AlertDialog alert;
  private int selectedLine = 0;
  private int selectedStation = 0;
  
  private Context context;
  
  private LayoutInflater layoutInflater;
  private String[] lineNmList = { "1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선", "경의중앙선", "공항철도", "경춘선", "인천1호선", "수인선", "분당선", "신분당선" };
  
  private LinearLayout subwayLineNmLayout;
  
  private TextView subwayLineNm;
  
  private ImageView selectSubwayLineBtn;
  
  private LinearLayout subwayStationNmLayout;
  
  private TextView subwayStationNm;
  private ImageView selectSubwayStationBtn;
  private TextView selectInfo;
  private TextView prevStation;
  private TextView stopStation;
  private TextView nextStation;
  private TextView upLineArvl1Msg;
  private TextView upLineArvl2Msg;
  private TextView downLineArvl1Msg;
  private TextView downLineArvl2Msg;
  private TextView upLineArvl1MsgHeading;
  private TextView upLineArvl2MsgHeading;
  private TextView downLineArvl1MsgHeading;
  private TextView downLineArvl2MsgHeading;
  private String openAPIKey = "";
  private String subwayLocationAPIKey = "";
  
  private String tagName = "";
  private String subwayId;
  private String statnNm;
  private String statnFnm;
  private String statnTnm; private String rowNum; private String updnLine; private String trainLineNm; private String barvlDt; private String bstatnNm; private String arvlMsg2; private ArrayList<String[]> stationArrayList = new ArrayList();
  private HashMap<String, StationInfo> stationInfoHashMap = new HashMap();
  private ArrayList<RealtimeStationArrivalInfo> realtimeStationArrivalInfoArrayList = new ArrayList();
  
  public TrafficSubwayTypeMini(Context context) {
    super(context);
    this.context = context;
  }
  
  public TrafficSubwayTypeMini(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }
  
  public TrafficSubwayTypeMini(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
  }
  
  @TargetApi(21)
  public TrafficSubwayTypeMini(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.context = context;
  }
  
  public void setOpenAPIKey(String key) {
    openAPIKey = key;
    initView();
  }
  
  public void setsubwayLocationAPIKey(String key) {
    subwayLocationAPIKey = key;
    initView();
  }
  
  private void initView() {
    if (layoutInflater == null) {
      layoutInflater = ((LayoutInflater)getContext().getSystemService("layout_inflater"));
      View view = layoutInflater.inflate(R.layout.traffic_subway_type_mini, this, false);
      FontUtils.getInstance(context).setGlobalFont(view);
      
      subwayLineNmLayout = ((LinearLayout)view.findViewById(R.id.subway_line_nm_layout));
      subwayLineNmLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View view) {
          selectSubwayLineBtn.setSelected(true);
          AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context, 3);
          
          alert_confirm.setSingleChoiceItems(lineNmList, selectedLine, new DialogInterface.OnClickListener()
          {

            public void onClick(DialogInterface dialog, int whichButton) { selectedLine = whichButton; } })
          

            .setPositiveButton("확인", new DialogInterface.OnClickListener()
            {

              public void onClick(DialogInterface dialog, int whichButton)
              {
                if (selectedLine != -1) {
                  subwayLineNm.setText(lineNmList[selectedLine]);
                  subwayLineNm.setTextColor(Color.parseColor("#000000"));
                  stationArrayList.clear();
                  selectedStation = 0;
                  new TrafficSubwayTypeMini.ProcessNetworkSubwayStationByLineInfoThread(TrafficSubwayTypeMini.this).execute(new String[] { lineNmList[selectedLine] }); } } })
            


            .setNegativeButton("취소", new DialogInterface.OnClickListener()
            {


              public void onClick(DialogInterface dialog, int whichButton) {}


            });
          alert = alert_confirm.create();
          alert.setOnShowListener(new DialogInterface.OnShowListener()
          {
            public void onShow(DialogInterface dialogInterface) {
              FontUtils.getInstance(context).setGlobalFont(alert.getListView());
              alert.getButton(-2).setTypeface(FontUtils.getInstance(context).getmTypeface());
              alert.getButton(-1).setTypeface(FontUtils.getInstance(context).getmTypeface());
            }
          });
          alert.setOnDismissListener(new DialogInterface.OnDismissListener()
          {
            public void onDismiss(DialogInterface dialogInterface) {
              selectSubwayLineBtn.setSelected(false);
            }
          });
          alert.show();
        }
        
      });
      subwayLineNm = ((TextView)view.findViewById(R.id.subway_line_nm));
      selectSubwayLineBtn = ((ImageView)view.findViewById(R.id.select_subway_line_btn));
      
      subwayStationNmLayout = ((LinearLayout)view.findViewById(R.id.subway_station_nm_layout));
      subwayStationNmLayout.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View view) {
          if (stationArrayList.size() > 0) {
            selectSubwayStationBtn.setSelected(true);
            final String[] itemList = new String[stationArrayList.size()];
            for (int i = 0; i < stationArrayList.size(); i++) {
              itemList[i] = ((String[])stationArrayList.get(i))[1];
            }
            if (itemList.length > 0)
            {
              AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context, 3);
              
              alert_confirm.setSingleChoiceItems(itemList, selectedStation, new DialogInterface.OnClickListener()
              {

                public void onClick(DialogInterface dialog, int whichButton) { selectedStation = whichButton; } })
              

                .setPositiveButton("확인", new DialogInterface.OnClickListener()
                {

                  public void onClick(DialogInterface dialog, int whichButton)
                  {
                    if (selectedStation != -1) {
                      subwayStationNm.setText(itemList[selectedStation]);
                      selectInfo.setVisibility(8);
                      stopStation.setText(itemList[selectedStation]);
                      stopStation.setVisibility(0);
                      subwayStationNm.setTextColor(Color.parseColor("#000000"));
                      stationInfoHashMap.clear();
                      new TrafficSubwayTypeMini.ProcessNetworkSubwayStationInfoThread(TrafficSubwayTypeMini.this).execute(new String[] { itemList[selectedStation] }); } } })
                


                .setNegativeButton("취소", new DialogInterface.OnClickListener()
                {


                  public void onClick(DialogInterface dialog, int whichButton) {}


                });
              alert = alert_confirm.create();
              alert.setOnShowListener(new DialogInterface.OnShowListener()
              {
                public void onShow(DialogInterface dialogInterface) {
                  FontUtils.getInstance(context).setGlobalFont(alert.getListView());
                  alert.getButton(-2).setTypeface(FontUtils.getInstance(context).getmTypeface());
                  alert.getButton(-1).setTypeface(FontUtils.getInstance(context).getmTypeface());
                }
              });
              alert.setOnDismissListener(new DialogInterface.OnDismissListener()
              {
                public void onDismiss(DialogInterface dialogInterface) {
                  selectSubwayStationBtn.setSelected(false);
                }
              });
              alert.show();
            }
            
          }
        }
      });
      subwayStationNm = ((TextView)view.findViewById(R.id.subway_station_nm));
      selectSubwayStationBtn = ((ImageView)view.findViewById(R.id.select_subway_station_btn));
      
      selectInfo = ((TextView)view.findViewById(R.id.select_info));
      prevStation = ((TextView)view.findViewById(R.id.prev_station));
      stopStation = ((TextView)view.findViewById(R.id.stop_station_nm));
      nextStation = ((TextView)view.findViewById(R.id.next_station));
      
      upLineArvl1Msg = ((TextView)view.findViewById(R.id.up_line_arvl_1_msg));
      upLineArvl2Msg = ((TextView)view.findViewById(R.id.up_line_arvl_2_msg));
      downLineArvl1Msg = ((TextView)view.findViewById(R.id.down_line_arvl_1_msg));
      downLineArvl2Msg = ((TextView)view.findViewById(R.id.down_line_arvl_2_msg));
      
      upLineArvl1MsgHeading = ((TextView)view.findViewById(R.id.up_line_arvl_1_msg_heading));
      upLineArvl2MsgHeading = ((TextView)view.findViewById(R.id.up_line_arvl_2_msg_heading));
      downLineArvl1MsgHeading = ((TextView)view.findViewById(R.id.down_line_arvl_1_msg_heading));
      downLineArvl2MsgHeading = ((TextView)view.findViewById(R.id.down_line_arvl_2_msg_heading));
      
      addView(view);
    }
  }
  
  private void setRealtimeArrival() {
    if (stationInfoHashMap.size() > 0) {
      prevStation.setText(((StationInfo)stationInfoHashMap.get(((String[])stationArrayList.get(selectedStation))[0])).getStatnFnm());
      nextStation.setText(((StationInfo)stationInfoHashMap.get(((String[])stationArrayList.get(selectedStation))[0])).getStatnTnm());
    } else {
      if (selectedStation > 0) {
        prevStation.setText(((String[])stationArrayList.get(selectedStation - 1))[1]);
      } else {
        prevStation.setText(stopStation.getText().toString());
      }
      if (selectedStation < stationArrayList.size()) {
        nextStation.setText(((String[])stationArrayList.get(selectedStation + 1))[1]);
      } else {
        nextStation.setText(stopStation.getText().toString());
      }
    }
    prevStation.setVisibility(0);
    nextStation.setVisibility(0);
    
    upLineArvl1MsgHeading.setText("도착정보가 없습니다.");
    upLineArvl1Msg.setText("-");
    upLineArvl2MsgHeading.setText("도착정보가 없습니다.");
    upLineArvl2Msg.setText("-");
    downLineArvl1MsgHeading.setText("도착정보가 없습니다.");
    downLineArvl1Msg.setText("-");
    downLineArvl2MsgHeading.setText("도착정보가 없습니다.");
    downLineArvl2Msg.setText("-");
    
    int upLineNo = 0;
    int downLineNo = 0;
    if (realtimeStationArrivalInfoArrayList.size() > 0) {
      Collections.sort(realtimeStationArrivalInfoArrayList, new Comparator()
      {
        public int compare(RealtimeStationArrivalInfo s, RealtimeStationArrivalInfo t1)
        {
          return Integer.parseInt(s.getBarvlDt()) > Integer.parseInt(t1.getBarvlDt()) ? 1 : Integer.parseInt(s.getBarvlDt()) < Integer.parseInt(t1.getBarvlDt()) ? -1 : 0;
        }
      });
    }
    
    for (int i = 0; i < realtimeStationArrivalInfoArrayList.size(); i++) {
      if (((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getSubwayId().equals(((String[])stationArrayList.get(selectedStation))[0])) {
        if ((((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getUpdnLine().equals("상행")) || (((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getUpdnLine().equals("내선"))) {
          if (upLineNo < 2) {
            String arrivalMsg = ((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getArvlMsg2().replaceAll("\\[", "").replaceAll("\\]", "");
            arrivalMsg = arrivalMsg.split("\\(")[0];
            if ((!arrivalMsg.contains("도착")) && (!arrivalMsg.contains("출발")) && (!arrivalMsg.contains("진입"))) {
              arrivalMsg = arrivalMsg.concat("도착");
            }
            if (upLineNo == 0) {
              upLineArvl1MsgHeading.setText(((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getBstatnNm() + "행");
              upLineArvl1Msg.setText(arrivalMsg);
              upLineNo += 1;
            } else {
              upLineArvl2MsgHeading.setText(((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getBstatnNm() + "행");
              upLineArvl2Msg.setText(arrivalMsg);
              upLineNo += 1;
            }
          }
        }
        else if (downLineNo < 2) {
          String arrivalMsg = ((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getArvlMsg2().replaceAll("\\[", "").replaceAll("\\]", "");
          arrivalMsg = arrivalMsg.split("\\(")[0];
          if ((!arrivalMsg.contains("도착")) && (!arrivalMsg.contains("출발")) && (!arrivalMsg.contains("진입"))) {
            arrivalMsg = arrivalMsg.concat("도착");
          }
          if (downLineNo == 0) {
            downLineArvl1MsgHeading.setText(((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getBstatnNm() + "행");
            downLineArvl1Msg.setText(arrivalMsg);
            downLineNo += 1;
          } else {
            downLineArvl2MsgHeading.setText(((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getBstatnNm() + "행");
            downLineArvl2Msg.setText(arrivalMsg);
            downLineNo += 1;
          }
        }
      }
    }
  }
  
  public class ProcessNetworkSubwayRealtimeStationArrivalInfoThread extends AsyncTask<String, Void, String>
  {
    public ProcessNetworkSubwayRealtimeStationArrivalInfoThread() {}
    
    protected String doInBackground(String... strings) {
      executeClient(strings);
      return "";
    }
    
    protected void onPostExecute(String result) {
      TrafficSubwayTypeMini.this.setRealtimeArrival();
    }
    


    public void executeClient(String[] strings)
    {
      URL apiURL = null;
      InputStream in = null;
      XmlPullParserFactory factory = null;
      XmlPullParser xpp = null;
      int eventType = -1;
      try {
        String station = strings[0];
        if ((subwayLocationAPIKey == null) || (subwayLocationAPIKey.equals(""))) {
          apiURL = new URL("http://swopenapi.seoul.go.kr/api/subway/" + openAPIKey + "/xml/realtimeStationArrival/1/999/" + URLEncoder.encode(station));
        } else {
          apiURL = new URL("http://swopenapi.seoul.go.kr/api/subway/" + subwayLocationAPIKey + "/xml/realtimeStationArrival/1/999/" + URLEncoder.encode(station));
        }
        
        in = apiURL.openStream();
        
        factory = XmlPullParserFactory.newInstance();
        
        factory.setNamespaceAware(true);
        
        xpp = factory.newPullParser();
        
        xpp.setInput(in, "UTF-8");
        
        eventType = xpp.getEventType();
        boolean isItemTag = false;
        

        while (eventType != 1)
        {
          if (eventType == 2)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = true;
            }
          }
          else if (eventType == 4) {
            if ((isItemTag) && 
              (!tagName.equals("")) && (!xpp.getText().equals(""))) {
              if (tagName.equals("rowNum")) {
                rowNum = xpp.getText();
              } else if (tagName.equals("subwayId")) {
                subwayId = xpp.getText();
              } else if (tagName.equals("updnLine")) {
                updnLine = xpp.getText();
              } else if (tagName.equals("trainLineNm")) {
                trainLineNm = xpp.getText();
              } else if (tagName.equals("barvlDt")) {
                barvlDt = xpp.getText();
              } else if (tagName.equals("bstatnNm")) {
                bstatnNm = xpp.getText();
              } else if (tagName.equals("arvlMsg2")) {
                arvlMsg2 = xpp.getText();
              }
            }
          }
          else if (eventType == 3)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = false;
              RealtimeStationArrivalInfo info = new RealtimeStationArrivalInfo(rowNum, subwayId, updnLine, trainLineNm, barvlDt, bstatnNm, arvlMsg2);
              realtimeStationArrivalInfoArrayList.add(info);
              info = null;
            } else {
              tagName = "";
            }
          }
          eventType = xpp.next();
        }
        return;
      } catch (MalformedURLException e) { e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (XmlPullParserException e) {
        e.printStackTrace();
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
  
  public class ProcessNetworkSubwayStationInfoThread extends AsyncTask<String, Void, String> {
    public ProcessNetworkSubwayStationInfoThread() {}
    
    protected String doInBackground(String... strings) {
      executeClient(strings);
      return "";
    }
    
    protected void onPostExecute(String result) {
      realtimeStationArrivalInfoArrayList.clear();
      new TrafficSubwayTypeMini.ProcessNetworkSubwayRealtimeStationArrivalInfoThread(TrafficSubwayTypeMini.this).execute(new String[] { ((String[])stationArrayList.get(selectedStation))[1] });
    }
    


    public void executeClient(String[] strings)
    {
      URL apiURL = null;
      InputStream in = null;
      XmlPullParserFactory factory = null;
      XmlPullParser xpp = null;
      int eventType = -1;
      try {
        String station = strings[0];
        if (station.equals("서울역")) {
          station = "서울";
        }
        if (station.equals("천호(풍납토성)")) {
          station = "천호";
        }
        if (station.equals("굽은다리(강동구민회관앞)")) {
          station = "굽은다리";
        }
        if (station.equals("몽촌토성(평화의문)")) {
          station = "몽촌토성";
        }
        if (station.equals("신촌(경의.중앙선)")) {
          station = "신촌(경의중앙선)";
        }
        
        apiURL = new URL("http://swopenapi.seoul.go.kr/api/subway/" + openAPIKey + "/xml/stationInfo/1/999/" + URLEncoder.encode(station));
        
        in = apiURL.openStream();
        
        factory = XmlPullParserFactory.newInstance();
        
        factory.setNamespaceAware(true);
        
        xpp = factory.newPullParser();
        
        xpp.setInput(in, "UTF-8");
        
        eventType = xpp.getEventType();
        boolean isItemTag = false;
        

        while (eventType != 1)
        {
          if (eventType == 2)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = true;
            }
          }
          else if (eventType == 4) {
            if ((isItemTag) && 
              (!tagName.equals("")) && (!xpp.getText().equals(""))) {
              if (tagName.equals("subwayId")) {
                subwayId = xpp.getText();
              } else if (tagName.equals("statnFnm")) {
                statnFnm = xpp.getText();
              } else if (tagName.equals("statnTnm")) {
                statnTnm = xpp.getText();
              }
            }
          }
          else if (eventType == 3)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = false;
              StationInfo info;
              if (subwayId.equals("1065")) {
                info = new StationInfo(subwayId, statnTnm, statnFnm);
              } else {
                info = new StationInfo(subwayId, statnFnm, statnTnm);
              }
              stationInfoHashMap.put(info.getSubwayId(), info);
              StationInfo info = null;
            } else {
              tagName = "";
            }
          }
          eventType = xpp.next();
        }
        return;
      } catch (MalformedURLException e) { e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (XmlPullParserException e) {
        e.printStackTrace();
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
  
  public class ProcessNetworkSubwayStationByLineInfoThread extends AsyncTask<String, Void, String> {
    public ProcessNetworkSubwayStationByLineInfoThread() {}
    
    protected String doInBackground(String... strings) {
      executeClient(strings);
      return "";
    }
    


    protected void onPostExecute(String result) {}
    

    public void executeClient(String[] strings)
    {
      URL apiURL = null;
      InputStream in = null;
      XmlPullParserFactory factory = null;
      XmlPullParser xpp = null;
      int eventType = -1;
      try {
        String line = strings[0];
        apiURL = new URL("http://swopenapi.seoul.go.kr/api/subway/" + openAPIKey + "/xml/stationByLine/1/999/" + URLEncoder.encode(line));
        
        in = apiURL.openStream();
        
        factory = XmlPullParserFactory.newInstance();
        
        factory.setNamespaceAware(true);
        
        xpp = factory.newPullParser();
        
        xpp.setInput(in, "UTF-8");
        
        eventType = xpp.getEventType();
        boolean isItemTag = false;
        

        while (eventType != 1)
        {
          if (eventType == 2)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = true;
            }
          }
          else if (eventType == 4) {
            if ((isItemTag) && 
              (!tagName.equals("")) && (!xpp.getText().equals(""))) {
              if (tagName.equals("subwayId")) {
                subwayId = xpp.getText();
              } else if (tagName.equals("statnNm")) {
                statnNm = xpp.getText();
              }
            }
          }
          else if (eventType == 3)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = false;
              stationArrayList.add(new String[] { subwayId, statnNm });
            } else {
              tagName = "";
            }
          }
          eventType = xpp.next();
        }
        return;
      } catch (MalformedURLException e) { e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (XmlPullParserException e) {
        e.printStackTrace();
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
