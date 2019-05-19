package kr.go.seoul.trafficsubway;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import java.util.HashSet;
import kr.go.seoul.trafficsubway.Common.BaseActivity;
import kr.go.seoul.trafficsubway.Common.CustomProgressDialog;
import kr.go.seoul.trafficsubway.Common.FirstLastTimeInfo;
import kr.go.seoul.trafficsubway.Common.FontUtils;
import kr.go.seoul.trafficsubway.Common.RealtimeStationArrivalInfo;
import kr.go.seoul.trafficsubway.Common.StationInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



public class TrafficSubwayInfoTypeB
  extends BaseActivity
{
  private int heightPx;
  private String openAPIKey = "";
  private String subwayLocationAPIKey = "";
  private ImageView btnBackSubway;
  private String stationNM = "";
  private TextView stationNm;
  private LinearLayout lineGroupLayout;
  private String selectedSubwayId = "";
  
  private ImageView btnRealTimeRefresh;
  
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
  private RadioGroup intervalRadioGroup;
  private RadioButton intervalWeekday;
  private RadioButton intervalsat;
  private RadioButton intervalHoli;
  private LinearLayout upLineLast;
  private LinearLayout downLineFirst;
  private LinearLayout upLineFirst;
  private LinearLayout downLineLast;
  private String tagName = "";
  private String statnFnm;
  private String statnTnm; private HashMap<String, StationInfo> stationInfoHashMap = new HashMap();
  private String rowNum;
  private String trainLineNm;
  private String barvlDt; private String bstatnNm; private String arvlMsg2; private ArrayList<RealtimeStationArrivalInfo> realtimeStationArrivalInfoArrayList = new ArrayList();
  private String subwayId;
  private String subwayNm;
  private String lastcarDiv; private String updnLine; private String expressyn; private String subwayename; private String weekendTranHour; private String saturdayTranHour; private String holidayTranHour; private ArrayList<FirstLastTimeInfo> firstLastTimeInfoArrayList = new ArrayList();
  private ArrayList<String> lineArrayList = new ArrayList();
  private HashSet<String> lineHashSet = new HashSet();
  private CustomProgressDialog customProgressDialog;
  
  public TrafficSubwayInfoTypeB() {}
  
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(1);
    getWindow().setBackgroundDrawable(new ColorDrawable(0));
    setContentView(R.layout.traffic_subway_info_type_b);
    heightPx = ((int)TypedValue.applyDimension(1, 27.0F, getResources().getDisplayMetrics()));
    if ((getIntent() != null) && (getIntent().getStringExtra("OpenAPIKey") != null)) {
      openAPIKey = getIntent().getStringExtra("OpenAPIKey");
    }
    if ((getIntent() != null) && (getIntent().getStringExtra("SubwayLocationAPIKey") != null)) {
      subwayLocationAPIKey = getIntent().getStringExtra("SubwayLocationAPIKey");
    }
    if ((getIntent() != null) && (getIntent().getStringExtra("StationNM") != null)) {
      stationNM = getIntent().getStringExtra("StationNM");
    }
    
    initView();
    showCustomProgressDialog();
    new ProcessNetworkSubwayStationInfoThread().execute(new String[] { stationNM });
  }
  
  private void showCustomProgressDialog() {
    if (customProgressDialog == null) {
      customProgressDialog = new CustomProgressDialog(this);
      customProgressDialog.setCancelable(false);
    }
    if (customProgressDialog != null) {
      customProgressDialog.show();
    }
  }
  
  private void cancelCustomProgressDialog() {
    if ((customProgressDialog != null) && (customProgressDialog.isShowing())) {
      customProgressDialog.cancel();
    }
  }
  
  private void initView() {
    btnBackSubway = ((ImageView)findViewById(R.id.btn_back_subway));
    btnBackSubway.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View view) {
        finish();
      }
      
    });
    stationNm = ((TextView)findViewById(R.id.station_nm));
    stationNm.setText(stationNM);
    lineGroupLayout = ((LinearLayout)findViewById(R.id.line_group_layout));
    
    btnRealTimeRefresh = ((ImageView)findViewById(R.id.btn_real_time_refresh));
    btnRealTimeRefresh.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View view) {
        realtimeStationArrivalInfoArrayList.clear();
        TrafficSubwayInfoTypeB.this.showCustomProgressDialog();
        new TrafficSubwayInfoTypeB.ProcessNetworkSubwayRealtimeStationArrivalInfoThread(TrafficSubwayInfoTypeB.this).execute(new String[] { stationNM });
      }
    });
    prevStation = ((TextView)findViewById(R.id.prev_station));
    stopStation = ((TextView)findViewById(R.id.stop_station_nm));
    nextStation = ((TextView)findViewById(R.id.next_station));
    
    stopStation.setText(stationNM);
    
    upLineArvl1Msg = ((TextView)findViewById(R.id.up_line_arvl_1_msg));
    upLineArvl2Msg = ((TextView)findViewById(R.id.up_line_arvl_2_msg));
    downLineArvl1Msg = ((TextView)findViewById(R.id.down_line_arvl_1_msg));
    downLineArvl2Msg = ((TextView)findViewById(R.id.down_line_arvl_2_msg));
    
    upLineArvl1MsgHeading = ((TextView)findViewById(R.id.up_line_arvl_1_msg_heading));
    upLineArvl2MsgHeading = ((TextView)findViewById(R.id.up_line_arvl_2_msg_heading));
    downLineArvl1MsgHeading = ((TextView)findViewById(R.id.down_line_arvl_1_msg_heading));
    downLineArvl2MsgHeading = ((TextView)findViewById(R.id.down_line_arvl_2_msg_heading));
    
    intervalRadioGroup = ((RadioGroup)findViewById(R.id.interval_radio_group));
    intervalWeekday = ((RadioButton)findViewById(R.id.interval_weekday));
    intervalsat = ((RadioButton)findViewById(R.id.interval_sat));
    intervalHoli = ((RadioButton)findViewById(R.id.interval_holi));
    intervalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
    {
      public void onCheckedChanged(RadioGroup radioGroup, int id) {
        if (id == R.id.interval_weekday) {
          TrafficSubwayInfoTypeB.this.setWeekData();
        } else if (id == R.id.interval_sat) {
          TrafficSubwayInfoTypeB.this.setSatData();
        } else if (id == R.id.interval_holi) {
          TrafficSubwayInfoTypeB.this.setHoliData();
        }
        
      }
    });
    upLineFirst = ((LinearLayout)findViewById(R.id.up_line_first));
    upLineLast = ((LinearLayout)findViewById(R.id.up_line_last));
    downLineFirst = ((LinearLayout)findViewById(R.id.down_line_first));
    downLineLast = ((LinearLayout)findViewById(R.id.down_line_last));
  }
  
  private void setLineIcon() {
    if (lineGroupLayout.getChildCount() == 0) {
      LayoutInflater layoutInflater = (LayoutInflater)getSystemService("layout_inflater");
      
      int drawableId = 0;
      if (lineArrayList.size() > 0) {
        Collections.sort(lineArrayList, new Comparator()
        {
          public int compare(String s, String t1) {
            return s.compareToIgnoreCase(t1);
          }
        });
      }
      for (int i = 0; i < lineArrayList.size(); i++) {
        LinearLayout lineIconLayout = (LinearLayout)layoutInflater.inflate(R.layout.traffic_subway_info_type_b_line_icon, null);
        switch (Integer.parseInt((String)lineArrayList.get(i))) {
        case 1001: 
          drawableId = R.drawable.selector_trafficsub_radio_1;
          break;
        case 1002: 
          drawableId = R.drawable.selector_trafficsub_radio_2;
          break;
        case 1003: 
          drawableId = R.drawable.selector_trafficsub_radio_3;
          break;
        case 1004: 
          drawableId = R.drawable.selector_trafficsub_radio_4;
          break;
        case 1005: 
          drawableId = R.drawable.selector_trafficsub_radio_5;
          break;
        case 1006: 
          drawableId = R.drawable.selector_trafficsub_radio_6;
          break;
        case 1007: 
          drawableId = R.drawable.selector_trafficsub_radio_7;
          break;
        case 1008: 
          drawableId = R.drawable.selector_trafficsub_radio_8;
          break;
        case 1009: 
          drawableId = R.drawable.selector_trafficsub_radio_9;
          break;
        case 1063: 
          drawableId = R.drawable.selector_trafficsub_radio_kyeong;
          break;
        case 1065: 
          drawableId = R.drawable.selector_trafficsub_radio_konghang;
          break;
        case 1067: 
          drawableId = R.drawable.selector_trafficsub_radio_kyeongchun;
          break;
        case 1069: 
          drawableId = R.drawable.selector_trafficsub_radio_incheon;
          break;
        case 1071: 
          drawableId = R.drawable.selector_trafficsub_radio_sooin;
          break;
        case 1075: 
          drawableId = R.drawable.selector_trafficsub_radio_bundang;
          break;
        case 1077: 
          drawableId = R.drawable.selector_trafficsub_radio_sinbundang;
        }
        
        lineIconLayout.setId(Integer.parseInt((String)lineArrayList.get(i)));
        if (Build.VERSION.SDK_INT >= 23) {
          ((ImageView)lineIconLayout.findViewById(R.id.line_icon)).setBackground(getResources().getDrawable(drawableId, null));
        }
        else if (Build.VERSION.SDK_INT >= 16) {
          ((ImageView)lineIconLayout.findViewById(R.id.line_icon)).setBackground(getResources().getDrawable(drawableId));
        } else {
          ((ImageView)lineIconLayout.findViewById(R.id.line_icon)).setBackgroundResource(drawableId);
        }
        
        lineIconLayout.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View view) {
            if (lineGroupLayout.getChildCount() > 0) {
              for (int i = 0; i < lineGroupLayout.getChildCount(); i++) {
                lineGroupLayout.getChildAt(i).setSelected(false);
                lineGroupLayout.getChildAt(0).findViewById(R.id.line_icon).setSelected(false);
              }
              view.setSelected(true);
              view.findViewById(R.id.line_icon).setSelected(true);
              selectedSubwayId = String.valueOf(view.getId());
              if (intervalWeekday.isChecked()) {
                TrafficSubwayInfoTypeB.this.setWeekData();
              } else {
                intervalWeekday.setChecked(true);
              }
              
              TrafficSubwayInfoTypeB.this.setRealtimeArrival();
            }
          }
        });
        lineGroupLayout.addView(lineIconLayout);
      }
      if (lineGroupLayout.getChildCount() > 0) {
        lineGroupLayout.getChildAt(0).setSelected(true);
        lineGroupLayout.getChildAt(0).findViewById(R.id.line_icon).setSelected(true);
        selectedSubwayId = String.valueOf(lineGroupLayout.getChildAt(0).getId());
        if (intervalWeekday.isChecked()) {
          setWeekData();
        } else {
          intervalWeekday.setChecked(true);
        }
        setRealtimeArrival();
      }
    } else {
      setRealtimeArrival();
    }
  }
  
  private void setRealtimeArrival() {
    if (stationInfoHashMap.size() > 0) {
      prevStation.setText(((StationInfo)stationInfoHashMap.get(selectedSubwayId)).getStatnFnm());
      nextStation.setText(((StationInfo)stationInfoHashMap.get(selectedSubwayId)).getStatnTnm());
      
      ((TextView)findViewById(R.id.up_line_heading)).setText(((StationInfo)stationInfoHashMap.get(selectedSubwayId)).getStatnFnm() + " 방향");
      ((TextView)findViewById(R.id.down_line_heading)).setText(((StationInfo)stationInfoHashMap.get(selectedSubwayId)).getStatnTnm() + " 방향");
      ((TextView)findViewById(R.id.up_line_heading_1)).setText(((StationInfo)stationInfoHashMap.get(selectedSubwayId)).getStatnFnm() + " 방향");
      ((TextView)findViewById(R.id.down_line_heading_1)).setText(((StationInfo)stationInfoHashMap.get(selectedSubwayId)).getStatnTnm() + " 방향");
    }
    
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
      if (((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getSubwayId().equals(selectedSubwayId)) {
        if ((((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getUpdnLine().equals("상행")) || (((RealtimeStationArrivalInfo)realtimeStationArrivalInfoArrayList.get(i)).getUpdnLine().equals("외선"))) {
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
  
  private void setWeekData()
  {
    upLineFirst.removeAllViews();
    upLineLast.removeAllViews();
    downLineFirst.removeAllViews();
    downLineLast.removeAllViews();
    
    if (firstLastTimeInfoArrayList.size() > 0) {
      Collections.sort(firstLastTimeInfoArrayList, new Comparator()
      {
        public int compare(FirstLastTimeInfo s, FirstLastTimeInfo t1) {
          return s.getWeekendTranHour().compareToIgnoreCase(t1.getWeekendTranHour());
        }
      });
    }
    
    for (int i = 0; i < firstLastTimeInfoArrayList.size(); i++) {
      if ((((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayId().equals(selectedSubwayId)) && (!((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getWeekendTranHour().equals("-"))) {
        if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getUpdnLine().equals("1")) {
          TextView time = new TextView(this);
          time.setHeight(heightPx);
          time.setGravity(17);
          time.setTextSize(2, 12.0F);
          time.setTextColor(Color.parseColor("#000000"));
          if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getLastcarDiv().equals("1")) {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getWeekendTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            upLineFirst.addView(time);
          } else {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getWeekendTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            upLineLast.addView(time);
          }
        } else {
          TextView time = new TextView(this);
          time.setHeight(heightPx);
          time.setGravity(17);
          time.setTextSize(2, 12.0F);
          time.setTextColor(Color.parseColor("#000000"));
          if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getLastcarDiv().equals("1")) {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getWeekendTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            downLineFirst.addView(time);
          } else {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getWeekendTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            downLineLast.addView(time);
          }
        }
      }
    }
    FontUtils.getInstance(this).setGlobalFont(upLineFirst);
    FontUtils.getInstance(this).setGlobalFont(upLineLast);
    FontUtils.getInstance(this).setGlobalFont(downLineFirst);
    FontUtils.getInstance(this).setGlobalFont(downLineLast);
  }
  
  private void setSatData() {
    upLineFirst.removeAllViews();
    upLineLast.removeAllViews();
    downLineFirst.removeAllViews();
    downLineLast.removeAllViews();
    
    if (firstLastTimeInfoArrayList.size() > 0) {
      Collections.sort(firstLastTimeInfoArrayList, new Comparator()
      {
        public int compare(FirstLastTimeInfo s, FirstLastTimeInfo t1) {
          return s.getSaturdayTranHour().compareToIgnoreCase(t1.getSaturdayTranHour());
        }
      });
    }
    
    for (int i = 0; i < firstLastTimeInfoArrayList.size(); i++) {
      if ((((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayId().equals(selectedSubwayId)) && (!((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSaturdayTranHour().equals("-"))) {
        if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getUpdnLine().equals("1")) {
          TextView time = new TextView(this);
          time.setHeight(heightPx);
          time.setGravity(17);
          time.setTextSize(2, 12.0F);
          time.setTextColor(Color.parseColor("#000000"));
          if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getLastcarDiv().equals("1")) {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSaturdayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            upLineFirst.addView(time);
          } else {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSaturdayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            upLineLast.addView(time);
          }
        } else {
          TextView time = new TextView(this);
          time.setHeight(heightPx);
          time.setGravity(17);
          time.setTextSize(2, 12.0F);
          time.setTextColor(Color.parseColor("#000000"));
          if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getLastcarDiv().equals("1")) {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSaturdayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            downLineFirst.addView(time);
          } else {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSaturdayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            downLineLast.addView(time);
          }
        }
      }
    }
    FontUtils.getInstance(this).setGlobalFont(upLineFirst);
    FontUtils.getInstance(this).setGlobalFont(upLineLast);
    FontUtils.getInstance(this).setGlobalFont(downLineFirst);
    FontUtils.getInstance(this).setGlobalFont(downLineLast);
  }
  
  private void setHoliData() {
    upLineFirst.removeAllViews();
    upLineLast.removeAllViews();
    downLineFirst.removeAllViews();
    downLineLast.removeAllViews();
    
    if (firstLastTimeInfoArrayList.size() > 0) {
      Collections.sort(firstLastTimeInfoArrayList, new Comparator()
      {
        public int compare(FirstLastTimeInfo s, FirstLastTimeInfo t1) {
          return s.getHolidayTranHour().compareToIgnoreCase(t1.getHolidayTranHour());
        }
      });
    }
    
    for (int i = 0; i < firstLastTimeInfoArrayList.size(); i++) {
      if ((((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayId().equals(selectedSubwayId)) && (!((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getHolidayTranHour().equals("-"))) {
        if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getUpdnLine().equals("1")) {
          TextView time = new TextView(this);
          time.setHeight(heightPx);
          time.setGravity(17);
          time.setTextSize(2, 12.0F);
          time.setTextColor(Color.parseColor("#000000"));
          if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getLastcarDiv().equals("1")) {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getHolidayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            upLineFirst.addView(time);
          } else {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getHolidayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            upLineLast.addView(time);
          }
        } else {
          TextView time = new TextView(this);
          time.setHeight(heightPx);
          time.setGravity(17);
          time.setTextSize(2, 12.0F);
          time.setTextColor(Color.parseColor("#000000"));
          if (((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getLastcarDiv().equals("1")) {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getHolidayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            downLineFirst.addView(time);
          } else {
            time.setText(((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getHolidayTranHour() + " " + ((FirstLastTimeInfo)firstLastTimeInfoArrayList.get(i)).getSubwayename());
            downLineLast.addView(time);
          }
        }
      }
    }
    FontUtils.getInstance(this).setGlobalFont(upLineFirst);
    FontUtils.getInstance(this).setGlobalFont(upLineLast);
    FontUtils.getInstance(this).setGlobalFont(downLineFirst);
    FontUtils.getInstance(this).setGlobalFont(downLineLast);
  }
  
  public class ProcessNetworkSubwayRealtimeStationArrivalInfoThread extends AsyncTask<String, Void, String> {
    public ProcessNetworkSubwayRealtimeStationArrivalInfoThread() {}
    
    protected String doInBackground(String... strings) {
      executeClient(strings);
      return "";
    }
    
    protected void onPostExecute(String result) {
      lineHashSet.addAll(lineArrayList);
      lineArrayList.clear();
      lineArrayList.addAll(lineHashSet);
      TrafficSubwayInfoTypeB.this.setLineIcon();
      TrafficSubwayInfoTypeB.this.cancelCustomProgressDialog();
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
        if (station.equals("신촌(경의중앙선)")) {
          station = "신촌(경의.중앙선)";
        }
        if (station.equals("천호")) {
          station = "천호(풍납토성)";
        }
        if (station.equals("굽은다리")) {
          station = "굽은다리(강동구민회관앞)";
        }
        if (station.equals("몽촌토성")) {
          station = "몽촌토성(평화의문)";
        }
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
              if (lineArrayList.size() == 0) {
                lineArrayList.add(subwayId);
              }
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
  
  public class ProcessNetworkSubwayFirstLastTimeInfoThread extends AsyncTask<String, Void, String> {
    public ProcessNetworkSubwayFirstLastTimeInfoThread() {}
    
    protected String doInBackground(String... strings) {
      executeClient(strings);
      return "";
    }
    
    protected void onPostExecute(String result) {
      new TrafficSubwayInfoTypeB.ProcessNetworkSubwayRealtimeStationArrivalInfoThread(TrafficSubwayInfoTypeB.this).execute(new String[] { stationNM });
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
        apiURL = new URL("http://swopenapi.seoul.go.kr/api/subway/" + openAPIKey + "/xml/firstLastTimetable/1/999/" + URLEncoder.encode(station));
        
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
              } else if (tagName.equals("subwayNm")) {
                subwayNm = xpp.getText();
              } else if (tagName.equals("lastcarDiv")) {
                lastcarDiv = xpp.getText();
              } else if (tagName.equals("updnLine")) {
                updnLine = xpp.getText();
              } else if (tagName.equals("expressyn")) {
                expressyn = xpp.getText();
              } else if (tagName.equals("subwayename")) {
                subwayename = xpp.getText();
              } else if (tagName.equals("weekendTranHour")) {
                weekendTranHour = xpp.getText();
              } else if (tagName.equals("saturdayTranHour")) {
                saturdayTranHour = xpp.getText();
              } else if (tagName.equals("holidayTranHour")) {
                holidayTranHour = xpp.getText();
              }
            }
          }
          else if (eventType == 3)
          {
            tagName = xpp.getName();
            if (tagName.equals("row")) {
              isItemTag = false;
              if (subwayId.equals("1065")) {
                if (updnLine.equals("0")) {
                  updnLine = "1";
                } else {
                  updnLine = "0";
                }
              }
              FirstLastTimeInfo info = new FirstLastTimeInfo(subwayId, subwayNm, lastcarDiv, updnLine, expressyn, subwayename, weekendTranHour, saturdayTranHour, holidayTranHour);
              firstLastTimeInfoArrayList.add(info);
              lineArrayList.add(subwayId);
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
      new TrafficSubwayInfoTypeB.ProcessNetworkSubwayFirstLastTimeInfoThread(TrafficSubwayInfoTypeB.this).execute(new String[] { stationNM });
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
              if ((subwayId.equals("1002")) || (subwayId.equals("1065"))) {
                info = new StationInfo(subwayId, statnTnm, statnFnm);
              } else {
                info = new StationInfo(subwayId, statnFnm, statnTnm);
              }
              stationInfoHashMap.put(info.getSubwayId(), info);
              lineArrayList.add(subwayId);
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
}
