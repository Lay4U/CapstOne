package org.androidtown.home;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.ImageButton;

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
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    MainActivity MAIN = new MainActivity();
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
    Date alarmTime;
    long ringTime;
    static int alar;
    int alarmflag =0;
    String myHome="1";
    static int service_flag=0;
    static String forRail = "1";
    static String forRailtrans = "2";
    static String fortime = "3";
    static String forAlarm = "4";
    Handler mHandler = new Handler(Looper.getMainLooper());

    TimerTask adTast = new TimerTask(){
        public void run(){
            service_flag =1;
            if(alarmflag==0){
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        turnOnGps();
                        findNearStation(dateID);
                    }
                }, 0);

            }else{
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        turnOnGps();
                        unregisterAlarm();
                        findNearStation(dateID);
                    }
                }, 0);

            }
        }
    };
    Timer timer;
    @Override
    public void onCreate() {
        timer = new Timer();

        super.onCreate();
    }

    public void turnOnGps() {
        gps = new GpsInfo(MainService.this);
        if (gps.isGetLocation()) {
            double Lat = gps.getLatitude();
            double Lon = gps.getLongitude();
            Latitude = Double.toString(Lat);
            Longitude = Double.toString(Lon);
            myX = Double.parseDouble(Longitude);
            myY = Double.parseDouble(Latitude);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dateID = intent.getExtras().getString("dateID");
        myHome = intent.getExtras().getString("myHome");
        timer.schedule(adTast, 0, 300000);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    public void findNearStation(String dated) {
        String context = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(context);
        boolean netEnabled = setNetwork();
        if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER)) == false) {
            setGps();
        } else if (!netEnabled) {
            chkNet();
        } else {
            UseThread(); //여기서 현재에서 가장 가까운 역을 알아낸다.
            alarmflag=1;
            CheckTypesTask task = new CheckTypesTask();
            task.execute(nearStationOnmyPlace,myHome,dated);
        }
    }

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



    public void AlarmSetting(String result, String K) {
        //K = 역까지의 소요시간(단위 : 초) , result : 막차시간

        Calendar calendarNow = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
        SimpleDateFormat dateFormatResult = new SimpleDateFormat("yyyy:MM:dd");

        Date now = calendarNow.getTime(); //now : 현재시간의 DATE
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

        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //알람을 해제 하는 함수//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void unregisterAlarm() {
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
    private class CheckTypesTask extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {

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
            FindTimeTable t = new FindTimeTable(s, arg0[0], arg0[1], gsc.getLINE_NM(),arg0[2]);

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
            Q = Q+t.getTime();

            return t.getCheck()+K+P+Q;
        }

        @Override
        protected void onPostExecute(String result) {


            super.onPostExecute(result);
        }

    }

}
