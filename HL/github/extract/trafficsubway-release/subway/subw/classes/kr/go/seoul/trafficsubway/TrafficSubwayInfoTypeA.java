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
import java.util.HashSet;
import kr.go.seoul.trafficsubway.Common.BaseActivity;
import kr.go.seoul.trafficsubway.Common.FirstLastTimeInfo;
import kr.go.seoul.trafficsubway.Common.FontUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



public class TrafficSubwayInfoTypeA
  extends BaseActivity
{
  private int heightPx;
  private String openAPIKey = "";
  private String stationNM = "";
  private TextView stationNm;
  private LinearLayout lineGroupLayout;
  private String selectedSubwayId = "";
  
  private RadioGroup intervalRadioGroup;
  
  private RadioButton intervalWeekday;
  private RadioButton intervalsat;
  private RadioButton intervalHoli;
  private LinearLayout upLineFirst;
  private LinearLayout upLineLast;
  private LinearLayout downLineFirst;
  private LinearLayout downLineLast;
  private String tagName = "";
  private String subwayId;
  private String subwayNm;
  private String lastcarDiv; private String updnLine; private String expressyn; private String subwayename; private String weekendTranHour; private String saturdayTranHour; private String holidayTranHour; private ArrayList<FirstLastTimeInfo> firstLastTimeInfoArrayList = new ArrayList();
  private ArrayList<String> lineArrayList = new ArrayList();
  private HashSet<String> lineHashSet = new HashSet();
  
  public TrafficSubwayInfoTypeA() {}
  
  protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    requestWindowFeature(1);
    getWindow().setBackgroundDrawable(new ColorDrawable(0));
    setContentView(R.layout.traffic_subway_info_type_a);
    heightPx = ((int)TypedValue.applyDimension(1, 31.0F, getResources().getDisplayMetrics()));
    if ((getIntent() != null) && (getIntent().getStringExtra("OpenAPIKey") != null)) {
      openAPIKey = getIntent().getStringExtra("OpenAPIKey");
    }
    if ((getIntent() != null) && (getIntent().getStringExtra("StationNM") != null)) {
      stationNM = getIntent().getStringExtra("StationNM");
    }
    initView();
    new ProcessNetworkSubwayFirstLastTimeInfoThread().execute(new String[] { stationNM });
  }
  
  private void initView() {
    stationNm = ((TextView)findViewById(R.id.station_nm));
    stationNm.setText(stationNM);
    lineGroupLayout = ((LinearLayout)findViewById(R.id.line_group_layout));
    intervalRadioGroup = ((RadioGroup)findViewById(R.id.interval_radio_group));
    intervalWeekday = ((RadioButton)findViewById(R.id.interval_weekday));
    intervalsat = ((RadioButton)findViewById(R.id.interval_sat));
    intervalHoli = ((RadioButton)findViewById(R.id.interval_holi));
    intervalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
    {
      public void onCheckedChanged(RadioGroup radioGroup, int id) {
        if (id == R.id.interval_weekday) {
          TrafficSubwayInfoTypeA.this.setWeekData();
        } else if (id == R.id.interval_sat) {
          TrafficSubwayInfoTypeA.this.setSatData();
        } else if (id == R.id.interval_holi) {
          TrafficSubwayInfoTypeA.this.setHoliData();
        }
        
      }
    });
    upLineFirst = ((LinearLayout)findViewById(R.id.up_line_first));
    upLineLast = ((LinearLayout)findViewById(R.id.up_line_last));
    downLineFirst = ((LinearLayout)findViewById(R.id.down_line_first));
    downLineLast = ((LinearLayout)findViewById(R.id.down_line_last));
  }
  
  private void setLineIcon() {
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
      LinearLayout lineIconLayout = (LinearLayout)layoutInflater.inflate(R.layout.traffic_subway_info_type_a_line_icon, null);
      switch (Integer.parseInt((String)lineArrayList.get(i))) {
      case 1001: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_1;
        break;
      case 1002: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_2;
        break;
      case 1003: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_3;
        break;
      case 1004: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_4;
        break;
      case 1005: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_5;
        break;
      case 1006: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_6;
        break;
      case 1007: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_7;
        break;
      case 1008: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_8;
        break;
      case 1009: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_9;
        break;
      case 1063: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_kyeong;
        break;
      case 1065: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_konghang;
        break;
      case 1067: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_kyeongchun;
        break;
      case 1069: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_incheon;
        break;
      case 1071: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_sooin;
        break;
      case 1075: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_bundang;
        break;
      case 1077: 
        drawableId = R.drawable.selector_trafficsub_pop_radio_sinbundang;
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
              TrafficSubwayInfoTypeA.this.setWeekData();
            } else {
              intervalWeekday.setChecked(true);
            }
            
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
    }
  }
  
  private void setWeekData() {
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
          time.setTextSize(2, 14.0F);
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
          time.setTextSize(2, 14.0F);
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
          time.setTextSize(2, 14.0F);
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
          time.setTextSize(2, 14.0F);
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
          time.setTextSize(2, 14.0F);
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
          time.setTextSize(2, 14.0F);
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
  
  public class ProcessNetworkSubwayFirstLastTimeInfoThread extends AsyncTask<String, Void, String> {
    public ProcessNetworkSubwayFirstLastTimeInfoThread() {}
    
    protected String doInBackground(String... strings) {
      executeClient(strings);
      return "";
    }
    
    protected void onPostExecute(String result) {
      lineHashSet.addAll(lineArrayList);
      lineArrayList.clear();
      lineArrayList.addAll(lineHashSet);
      TrafficSubwayInfoTypeA.this.setLineIcon();
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
  
  public void finishActivity(View view) {
    finish();
  }
}
