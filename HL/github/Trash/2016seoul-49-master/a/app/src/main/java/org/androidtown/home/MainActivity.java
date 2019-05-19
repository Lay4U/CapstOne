package org.androidtown.home;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapTapi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    public static String api_key = "4a5764746f616c733238624b7a7476";
    static ImageButton bellBtn;
    static SharedPreferences setonOff;
    static SharedPreferences.Editor editor;
    static String Latitude, Longitude; //위도(Y) , 경도(X)
    static String nearSubwayCoordX, nearSubwayCoordY; //X = Longi Y = Lati
    static String pTime; //이동거리 소요시간
    double myX, myY, subwayX, subwayY;
    static String nearStationOnmyPlace;            // 현위치에서 가장 가까운 역 이름
    TMapTapi tmaptapi;
    String dateID;
    GpsInfo gps;
    public static AlarmManager am;
    public static PendingIntent pi;
    public static int mCrouteCode = 0;
    FindTransferStation s;
    static String mTime; //막차 시간
    Date alarmTime, now;
    long ringTime;
    static int alar;
    static boolean a = true;
    ImageButton selectBtn, settingBtn, mapBtn, lastBtn, retryBtn;
    static String forRail = "1";
    static String forRailtrans = "2";
    static String fortime = "3";
    static String forAlarm = "4";
    static ImageButton timeAlways;
    static int FLAG_service = 0;
    String myHome = "";
    TextView myhome;
    ImageView homeImg;
    int retryFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bellBtn = (ImageButton) findViewById(R.id.offButton);
        selectBtn = (ImageButton) findViewById(R.id.selectButton);
        mapBtn = (ImageButton) findViewById(R.id.mapButton);
        lastBtn = (ImageButton) findViewById(R.id.onLast);
        settingBtn = (ImageButton) findViewById(R.id.setButton);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.dayradio);
        timeAlways = (ImageButton) findViewById(R.id.checkingService);
        myhome = (TextView) findViewById(R.id.myhome);
        homeImg = (ImageView) findViewById(R.id.imageView2);
        retryBtn = (ImageButton) findViewById(R.id.retryBtn);

        setonOff = getSharedPreferences("setonOff", 0);
        editor = setonOff.edit();
        if (setonOff.getBoolean("checkButton", false)) {
            bellBtn.setBackgroundResource(R.drawable.onbutton);
        } else if (setonOff.getBoolean("checkButton", true)) {
            bellBtn.setBackgroundResource(R.drawable.offbutton);
        }
        try {
            myHome = setonOff.getString("desN", "");
            if (myHome.equals("")) {
                myhome.setText(" - " + "설정안됨");
            } else {
                myhome.setText(" - " + setonOff.getString("desN", "") + "역");
            }
            homeImg.setVisibility(View.VISIBLE);

        } catch (Exception e) {

        }

        if (bellBtn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.onbutton).getConstantState())) {
            timeAlways.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
        }

        if (setonOff.getBoolean("checking", false)) {
            timeAlways.setBackgroundResource(R.drawable.timeon);
            retryBtn.setVisibility(View.INVISIBLE);

        } else if (setonOff.getBoolean("checking", true)) {
            timeAlways.setBackgroundResource(R.drawable.timeoff);
        }




        timeAlways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeAlways.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.timeoff).getConstantState())) {
                    if (bellBtn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.offbutton).getConstantState())) {
                        Toast.makeText(getApplicationContext(), "알람을 먼저 실행시켜 주세요.", Toast.LENGTH_LONG).show();
                    } else {
                        timeAlways.setBackgroundResource(R.drawable.timeon);
                        editor.putBoolean("checking", true);
                        retryBtn.setVisibility(View.INVISIBLE);
                        editor.commit();
                        unregisterAlarm2();
                        startServiceMethod();
                        FLAG_service = 1;

                    }

                } else {
                    //실시간 끌 때
                    timeAlways.setBackgroundResource(R.drawable.timeoff);
                    Toast.makeText(getApplicationContext(), "실시간 OFF", Toast.LENGTH_LONG).show();
                    editor.remove("checking");
                    retryBtn.setVisibility(View.VISIBLE);
                    editor.commit();
                    stopServiceMethod();
                    FLAG_service = 2;

                }
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onButton 시 하는 act
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                String date = rb.getText().toString();
                dateID = "";
                if (date.equals("평일")) {
                    dateID = "1";
                } else if (date.equals("주말")) {
                    dateID = "2";
                } else if (date.equals("공휴일")) {
                    dateID = "3";
                }
                if (bellBtn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.offbutton).getConstantState())) {
                    if (myHome.equals("")) {
                        Toast.makeText(getApplicationContext(), "우리집을 설정해 주세요.", Toast.LENGTH_LONG).show();
                    }
                    else {

                        FLAG_service = 2;
                        timeAlways.setVisibility(View.VISIBLE);
                        retryBtn.setVisibility(View.VISIBLE);
                        findNearStation(dateID);
                    }

                } else {
                    //offButton 시 하는 act
                    FLAG_service = 2;
                    timeAlways.setVisibility(View.INVISIBLE);
                    retryBtn.setVisibility(View.INVISIBLE);
                    bellBtn.setBackgroundResource(R.drawable.offbutton);
                    editor.remove("checkButton");
                    editor.commit();
                    unregisterAlarm();
                    if (timeAlways.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.timeon).getConstantState())) {
                        timeAlways.setBackgroundResource(R.drawable.timeoff);
                        editor.remove("checking");
                        editor.commit();
                        stopServiceMethod();
                    }
                }
            }

        });


        selectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                String date = rb.getText().toString();
                String dateID = "";
                if (date.equals("평일")) {
                    dateID = "1";
                } else if (date.equals("주말")) {
                    dateID = "2";
                } else if (date.equals("공휴일")) {
                    dateID = "3";
                }
                Intent intent = new Intent(getApplicationContext(), selectStartEnd.class);
                intent.putExtra("dateid", dateID);
                startActivity(intent);


            }

        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnMap();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((bellBtn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.onbutton).getConstantState()) && !myHome.equals("1"))) {
                    Intent intent = new Intent(getApplicationContext(), select_LastBtn.class);
                    intent.putExtra("trans", forRailtrans);
                    intent.putExtra("rail", forRail);
                    intent.putExtra("stime", fortime);
                    intent.putExtra("mtime", forAlarm);
                    intent.putExtra("nearS", nearStationOnmyPlace);
                    intent.putExtra("arriveS", myHome);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "알람을 먼저 켜주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });


        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FLAG_service == 1) {
                    Toast.makeText(getApplicationContext(), "실시간 중 에는 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                } else if (bellBtn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.offbutton).getConstantState())) {
                    Toast.makeText(getApplicationContext(), "알람을 먼저 실행시켜 주세요.", Toast.LENGTH_LONG).show();
                } else {
                    retryFlag = 1;
                    unregisterAlarm();
                    findNearStation(dateID);
                }
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////////가장가까운역 지도보기///////////////////
    public void turnOnMap() {
        String context = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(context);
        boolean netEnabled = setNetwork();
        if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER)) == false) {
            setGps();
        } else if (!netEnabled) {
            chkNet();
        } else {
            turnOnGps();
            UseThread(); //여기서 현재에서 가장 가까운 역을 알아낸다.
            Intent intent = new Intent(MainActivity.this, TmapOpen.class);
            startActivity(intent);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void findNearStation(String dated) {
        String context = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(context);
        boolean netEnabled = setNetwork();
        if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER)) == false) {
            setGps();
        } else if (!netEnabled) {
            chkNet();
        } else {
            turnOnGps();
            UseThread(); //여기서 현재에서 가장 가까운 역을 알아낸다.
            if(nearStationOnmyPlace.equals(myHome)){
                Toast.makeText(getApplicationContext(),"집까지 도보가 더 빠릅니다.",Toast.LENGTH_LONG).show();
            }else {
                CheckTypesTask task = new CheckTypesTask();
                task.execute(nearStationOnmyPlace, myHome, dated);
            }
        }
    }


    // gps 와 network 확인 코드///////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean setNetwork() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected() || mobile.isConnected()) {
            return true;
        } else
            return false;
    }

    private void chkNet() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setMessage("네트워크 연결을 확인해주세요.");
        alert.show();

    }

    private void setGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS를 켜주세요. GPS설정창으로 가시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                        })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //gps를 사용해 자신의 현재 위치를 받아오는 함수 ///////////////////////////////////////////////////////////////////////////////////////////////
    public void turnOnGps() {
        gps = new GpsInfo(MainActivity.this);
        if (gps.isGetLocation()) {
            double Lat = gps.getLatitude();
            double Lon = gps.getLongitude();
            Latitude = Double.toString(Lat);
            Longitude = Double.toString(Lon);
            myX = Double.parseDouble(Longitude);
            myY = Double.parseDouble(Latitude);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //알람을 세팅 하는 함수////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void AlarmSetting(String result, String K) {
        //K = 역까지의 소요시간(단위 : 초) , result : 막차시간

        Calendar calendarNow = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
        SimpleDateFormat dateFormatResult = new SimpleDateFormat("yyyy:MM:dd");

        now = calendarNow.getTime(); //now : 현재시간의 DATE
        String n = dateFormat.format(now);
        int km = Integer.parseInt(n.substring(11, 13));

        try {
            if (n.substring(11, 13).equals("00") && result.substring(0, 2).equals("24")) {
                result = result.replaceAll("24", "00");
            } else if (n.substring(11, 13).equals("00") && result.substring(0, 2).equals("25")) {
                result = result.replaceAll("25", "01");
            } else if (n.substring(11, 13).equals("01") && result.substring(0, 2).equals("25")) {
                result = result.replaceAll("25", "01");
            }
            alarmTime = dateFormat.parse(dateFormatResult.format(calendarNow.getTime()) + "-" + result);
        } catch (Exception e) {

        }

        int sec = Integer.parseInt(K); //역까지의 소요시간 형변환
        int min = sec / 60; // 역까지의 소요 분
        Calendar calFuture = Calendar.getInstance();
        calFuture.setTime(alarmTime);
        calFuture.add(Calendar.MINUTE, -min);
        ringTime = (calFuture.getTimeInMillis() - calendarNow.getTimeInMillis()) / 1000;

        alar = Integer.parseInt(Long.toString(ringTime));


        if (alar > 0) {
            Intent intent2 = new Intent(getApplicationContext(), offPressBell.class);
            startActivity(intent2);
            // 알람 매니저에 등록할 인텐트를 만듬
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, mCrouteCode, intent, 0);

            // 알람을 받을 시간을 alar초 뒤로 설정
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, alar);
            // 알람 매니저에 알람을 등록
            am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        } else {
            Intent intent = new Intent(getApplicationContext(), onPressBell.class);
            startActivity(intent);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //알람을 해제 하는 함수//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void unregisterAlarm() {
        if (retryFlag == 1) {
            Toast.makeText(getApplicationContext(), "이전 알람이 취소되었습니다.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "알람이 취소되었습니다.", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, mCrouteCode, intent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.cancel(sender);
    }

    public void unregisterAlarm2() {
        Toast.makeText(getApplicationContext(), "실시간 ON", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, mCrouteCode, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(sender);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //현재위치와 가장 가까운 역을 고르는 함수//////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void UseThread() {
        DaumThread changeMyPositionCoords = new DaumThread(Latitude, Longitude, "WGS84");
        changeMyPositionCoords.start();          //다음 api를 사용하여 현재 위치를 서울 open api 에 맞는 좌표로 변환
        try {
            changeMyPositionCoords.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SeoulThread findNearStation = new SeoulThread(changeMyPositionCoords.getX(), changeMyPositionCoords.getY());
        findNearStation.start();                 //서울 api를 이용해 가장 가까운 역 검색
        try {
            findNearStation.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DaumThread changeNearStationCoords = new DaumThread(findNearStation.getNearsX(), findNearStation.getNearsY(), "WTM");
        changeNearStationCoords.start();        //다음 api를 이용해 서울 api로 나온 가장 가까운역의 좌표를 gps 좌표로 변환
        try {
            changeNearStationCoords.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nearSubwayCoordX = changeNearStationCoords.getX();    //가장 가까운 역 x좌표
        nearSubwayCoordY = changeNearStationCoords.getY();    //가장 가까운 역 y좌표


        subwayX = Double.parseDouble(nearSubwayCoordX);       //형변환
        subwayY = Double.parseDouble(nearSubwayCoordY);

        nearStationOnmyPlace = findNearStation.getNearStation();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///역까지 가는데 걸리는 시간 구하는 함수////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void TotaltimeSet(String result) {
        this.mTime = result;
        tmaptapi = new TMapTapi(this);
        tmaptapi.setSKPMapAuthentication("6180bf43-e723-3211-991e-e116b8cc9732");
        TMapData tmapdata = new TMapData();

        TMapPoint startpoint = new TMapPoint(myY, myX);
        TMapPoint endpoint = new TMapPoint(subwayY, subwayX);

        tmapdata.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, startpoint, endpoint, new TMapData.FindPathDataAllListenerCallback() {
            String p;

            @Override
            public void onFindPathDataAll(Document document) {

                Document doc = document;
                Element element = doc.getDocumentElement();
                NodeList time = element.getElementsByTagName("tmap:totalTime");
                Node timenode = time.item(0).getFirstChild();
                pTime = timenode.getNodeValue();
                AlarmSetting(mTime, pTime);
                //역까지 가는데 걸리는 시간 구하는 함수를 거치면 소요시간이 나오는데 이 소요시간을 alarmSetting에 넣어주면
                //alarmsetting에서 소요시간이랑 막차시간을 받아서 알람 호출

            }
        });


    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////AsyncTask 에서 수행///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class CheckTypesTask extends AsyncTask<String, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(
                MainActivity.this);

        @Override
        protected void onPreExecute() {

            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            if (retryFlag == 1) {
                asyncDialog.setMessage("현 위치에서 재탐색 합니다..");
                retryFlag = 0;
            } else {
                asyncDialog.setMessage("막차를 확인중 입니다..");
            }
            // show dialog
            asyncDialog.show();
            bellBtn.setBackgroundResource(R.drawable.onbutton);
            editor.putBoolean("checkButton", true);
            editor.commit();
            super.onPreExecute();
            //막차 바로 확인에는 progressDialog , 알람 바로 설정에는 toast message
        }

        @Override
        protected String doInBackground(String... arg0) {

            GetOutCode gsc = new GetOutCode(arg0[0].toString());
            gsc.start();
            try {
                gsc.join();
            } catch (Exception e) {

            }
            FindTimeTable t = new FindTimeTable(s, arg0[0], arg0[1], gsc.getLINE_NM(), arg0[2]);

            t.start();
            try {
                t.join();
            } catch (Exception e) {
            }
            try {
                t.find();
            } catch (Exception e) {
                t.check = null;
            }

            try {
                if (t.getCheck().substring(0, 2).equals("00")) {
                    String k1 = t.getCheck().replaceAll("00", "24");
                    t.setCheck(k1);
                }

                TotaltimeSet(t.getCheck());
            } catch (Exception e) {

            }
            String K = ",";

            for (int i = 0; i < t.getA().size(); i++) {
                K = K + t.getA().get(i).getStationName() + ",";
            }
            String P = "-";

            for (int i = 0; i < t.getB().size(); i++) {
                P = P + t.getB().get(i).getStationName() + "-";
            }

            String Q = "+";
            Q = Q + t.getTime();

            return t.getCheck() + K + P + Q;
        }

        @Override
        protected void onPostExecute(String result) {
            asyncDialog.dismiss();
            int k = result.indexOf(",");
            int p = result.indexOf("-");
            int q = result.indexOf("+");
            forAlarm = result.substring(0, k);
            forRail = result.substring(p, q);
            fortime = result.substring(q + 1);


            if (p != k + 1) {
                forRailtrans = result.substring(k + 1, p - 1);
            } else {
                forRailtrans = "";
            }
            try {
                if (result.contains("null") || result.equals(null)) {
                    Toast.makeText(getApplicationContext(), "해당 경로는 업데이트 중입니다..", Toast.LENGTH_LONG).show();
                    bellBtn.setBackgroundResource(R.drawable.offbutton);
                    editor.remove("checkButton");
                    editor.commit();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "해당 경로는 업데이트 중입니다..", Toast.LENGTH_LONG).show();
                bellBtn.setBackgroundResource(R.drawable.offbutton);
                editor.remove("checkButton");
                editor.commit();
            }
            super.onPostExecute(result);
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////서비스 실행/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void startServiceMethod() {
        Intent Service = new Intent(this, MainService.class);
        Service.putExtra("dateID", dateID);
        Service.putExtra("myHome", myHome);
        startService(Service);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////서비스 중지//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void stopServiceMethod() {
        Intent Service = new Intent(this, MainService.class);
        stopService(Service);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///서비스 안쓰면? ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
