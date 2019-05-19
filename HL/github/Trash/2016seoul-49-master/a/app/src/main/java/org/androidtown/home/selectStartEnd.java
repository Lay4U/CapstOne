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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by WooHee on 2016-09-26.
 */
public class selectStartEnd extends Activity {
    CharSequence start;
    CharSequence end;
    ImageButton btn;
    TextView trans;
    TextView railinfo;
    public static int mCrouteCode = 1;
    static SharedPreferences setSonoff;
    static SharedPreferences.Editor editor;
    TextView times;
    Date alarmTime;
    long ringTime;
    static String forRail = "1";
    static String forRailtrans = "2";
    static String foralarm = "3";
    private long lastTimeBackPressed;
    FindTransferStation s;
    static ImageButton btn2;
    ImageButton rail;
    String Dateid;
    public static AlarmManager am2;
    public static PendingIntent pi;
    public static String[] names = new String[]{"교대", "남부터미널", "양재", "매봉", "도곡", "대치", "대청", "일원", "수서", "가락시장", "경찰병원", "오금", "당고개", "상계", "노원", "쌍문", "수유", "미아", "미아사거리", "길음", "한성대입구", "혜화", "동대문", "동대문역사문화공원", "충무로", "명동", "회현", "서울", "삼각지", "신용산", "이촌", "동작", "총신대입구(이수)", "남태령", "남영", "노량진", "영등포", "신도림", "서빙고", "한남", "응봉", "왕십리", "외대앞", "신이문", "석계", "월계", "녹천", "시청", "종각", "종로3가", "종로5가", "신설동", "제기동", "동묘앞", "을지로입구", "을지로3가", "을지로4가", "상왕십리", "한양대", "뚝섬", "건대입구", "구의", "강변", "잠실나루", "신천", "삼성", "선릉", "강남", "서초", "방배", "사당", "서울대입구", "봉천", "신림", "신대방", "구로디지털단지", "문래", "영등포구청", "당산", "홍대입구", "신촌", "이대", "아현", "충정로", "신답", "도림천", "양천구청", "신정네거리", "용두", "까치산", "지축", "구파발", "연신내", "불광", "녹번", "홍제", "무악재", "독립문", "동대입구", "금호", "옥수", "압구정", "신사", "고속터미널", "일산", "탄현", "금릉", "금촌", "파주", "문산", "광운대", "상봉", "망우", "갈매", "별내", "사릉", "금곡", "천마산", "마석", "청평", "상천", "굴봉산", "백양리", "남춘천", "쌍용(나사렛대)", "아산", "탕정", "풍기", "온양온천", "신창", "원당", "백석", "마두", "주엽", "대화", "방화", "개화산", "김포공항", "마곡", "발산", "우장산", "화곡", "신정", "목동", "오목교", "신길", "여의나루", "마포", "공덕", "애오개", "서대문", "광화문", "마포구청", "망원", "합정", "상수", "대흥", "효창공원앞", "이태원", "한강진", "버티고개", "약수", "신당", "창신", "보문", "고려대", "상월곡", "태릉입구", "화랑대", "장암", "도봉산", "수락산", "마들", "청구", "신금호", "행당", "마장", "장한평", "군자", "아차산", "천호", "강동", "길동", "명일", "고덕", "둔촌동", "방이", "개롱", "거여", "응암", "역촌", "구산", "새절", "증산", "대공원", "과천", "인덕원", "평촌", "금정", "가산디지털단지", "관악", "안양", "군포", "성균관대", "화서", "독산", "세류", "세마", "오산대", "오산", "송탄", "서정리", "평택", "직산", "당정", "서동탄", "광명", "산본", "대야미", "상록수", "중앙", "고잔", "안산", "신길온천", "오이도", "수리산", "오류동", "역곡", "송내", "부평", "주안", "제물포", "인천", "구일", "소사", "간석", "도원", "중동", "도화", "수원", "서울숲", "압구정로데오", "선정릉", "가천대", "야탑", "수내", "정자", "오리", "이매", "죽전", "구성", "기흥", "상갈", "영통", "망포", "수원시청", "월곶", "소래포구", "인천논현", "남동인더스파크", "원인재", "연수", "도봉", "회룡", "의정부", "녹양", "양주", "덕계", "지행", "동두천중앙", "소요산", "마전(무정차)", "구룡", "개포동", "대모산입구", "복정", "회기", "중랑", "양원", "구리", "양정", "덕소", "팔당", "운길산", "아신", "오빈", "용문", "대곡", "신촌(경의중앙선)", "서강대", "가좌", "디지털미디어시티", "화전", "행신", "곡산", "백마", "발곡", "범골", "경전철의정부", "의정부시청", "흥선", "의정부중앙", "동오", "새말", "효자", "곤제", "어룡", "탑석", "판교", "원흥", "청량리", "성수", "잠실", "역삼", "낙성대", "대림", "삼동", "경기광주", "초월", "청라국제도시", "언주", "삼성중앙", "봉은사", "종합운동장", "대방", "창동", "봉명", "배방", "구로", "석수", "명학", "의왕", "병점", "진위", "지제", "성환", "천안", "개봉", "부천", "백운", "동인천", "부개", "강매", "온수", "방학", "망월사", "가능", "덕정", "보산", "삼송", "정발산", "성신여대입구", "숙대입구", "선바위", "경마공원", "정부과천청사", "범계", "반월", "한대앞", "초지", "정왕", "송정", "양평", "중계", "하계", "공릉", "중화", "면목", "용마산", "중곡", "뚝섬유원지", "청담", "강남구청", "논현", "반포", "남성", "숭실대입구", "신대방삼거리", "보라매", "남구로", "철산", "광명사거리", "천왕", "부천종합운동장", "춘의", "부천시청", "상동", "삼산체육관", "부평구청", "강동구청", "석촌", "송파", "문정", "장지", "산성", "단대오거리", "신흥", "모란", "계양", "박촌", "임학", "작전", "갈산", "동수", "간석오거리", "인천시청", "예술회관", "문학경기장", "선학", "신연수", "동춘", "동막", "지식정보단지", "인천대입구", "국제업무지구", "개화", "공항시장", "신방화", "마곡나루", "가양", "증미", "등촌", "신목동", "선유도", "여의도", "샛강", "흑석", "구반포", "신반포", "사평", "신논현", "검암", "운서", "인천국제공항", "강남대", "지석", "동백", "초당", "시청·용인대", "명지대", "운동장·송담대", "고진", "둔전", "전대·에버랜드", "태평", "서현", "미금", "보정", "신갈", "청명", "매탄권선", "달월", "호구포", "송도", "먹골", "사가정", "어린이대공원", "학동", "내방", "상도", "신풍", "까치울", "신중동", "굴포천", "몽촌토성", "남한산성입구", "용답", "경복궁", "안국", "잠원", "학여울", "용산", "한티", "신원", "양평(경의중앙선)", "김유정", "금천구청", "두정", "동암", "매교", "동두천", "화정", "영등포시장", "상일동", "월드컵경기장", "월곡", "장승배기", "암사", "테크노파크", "노들", "양재시민의숲", "테스트", "검단오류", "왕길", "검단사거리", "마전", "완정", "독정", "검바위", "아시아드경기장", "서구청", "가정", "가정중앙시장", "석남", "서부여성회관", "인천가좌", "가재울", "주안국가산단", "인하대", "숭의", "신포", "시민공원", "석바위시장", "석천사거리", "모래내시장", "만수", "남동구청", "인천대공원", "운연", "곤지암", "신둔도예촌", "이천", "부발", "세종대왕릉", "여주", "야당", "답십리", "광나루", "굽은다리", "올림픽공원", "마천", "독바위", "광흥창", "녹사평", "안암", "돌곶이", "봉화산", "동천", "수지구청", "성복", "상현", "광교중앙", "광교", "수진", "귤현", "계산", "경인교대입구", "부평시장", "부평삼거리", "인천터미널", "캠퍼스타운", "센트럴파크", "양천향교", "염창", "국회의사당", "공항화물청사", "청계산입구", "어정", "삼가", "김량장", "보평", "경기도청북부청사", "송산", "영종", "도농", "도심", "양수", "국수", "원덕", "서울(경의중앙선)", "수색", "능곡", "풍산", "운정", "월롱", "신내", "퇴계원", "평내호평", "대성리", "가평", "강촌", "춘천"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_start_end);
        btn = (ImageButton) findViewById(R.id.search);
        btn2 = (ImageButton) findViewById(R.id.stbtalarmbt);
        trans = (TextView) findViewById(R.id.trans);
        times = (TextView) findViewById(R.id.time);
        railinfo = (TextView) findViewById(R.id.tx6);
        rail = (ImageButton) findViewById(R.id.rail);
        ArrayAdapter<String> name1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        ArrayAdapter<String> name = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        //////////////////////////////////////////////////////////////////
        Intent intent = getIntent();
        Dateid = intent.getExtras().getString("dateid");

        setSonoff = getSharedPreferences("setSonoff", 0);
        editor = setSonoff.edit();
        if (setSonoff.getBoolean("check", false)) {
            btn2.setVisibility(View.VISIBLE);
            btn2.setBackgroundResource(R.drawable.stendalarmbton);
        } else if (setSonoff.getBoolean("check", true)) {
            btn2.setVisibility(View.INVISIBLE);
            btn2.setBackgroundResource(R.drawable.stendalarmbt);
        }

        ////////////////////////////////////////////////////////////
        final AutoCompleteTextView autoEdit = (AutoCompleteTextView) findViewById(R.id.autoedit);
        final AutoCompleteTextView autoEdit1 = (AutoCompleteTextView) findViewById(R.id.autoedit1);

        autoEdit.setAdapter(name);

        autoEdit1.setAdapter(name1);


        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int k = 0;
                start = (CharSequence) autoEdit.getText();
                end = (CharSequence) autoEdit1.getText();
                for (int i = 0; i < names.length; i++) {
                    if (names[i].equals(start.toString())) {
                        k++;
                        continue;
                    } else if (names[i].equals(end.toString())) {
                        k++;
                    }
                }
                if (k != 2) {
                    Toast.makeText(getApplicationContext(), "역명을 확인하세요.", Toast.LENGTH_LONG).show();
                } else {
                    boolean netEnabled = setNetwork();
                    if (!netEnabled) {
                        chkNet();
                    } else {
                        CheckTypesTask task = new CheckTypesTask();
                        task.execute(start.toString(), end.toString(), Dateid);
                    }
                    btn2.setVisibility(View.VISIBLE);
                }
                ////////////////////////////////////////////////


            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foralarm.equals("3")) {
                    Toast.makeText(getApplicationContext(), "먼저 경로를 검색해 주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if (btn2.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.stendalarmbt).getConstantState())) {
                        btn2.setBackgroundResource(R.drawable.stendalarmbton);
                        editor.putBoolean("check", true);
                        editor.commit();
                        AlarmSetting(foralarm);
                    } else {
                        btn2.setBackgroundResource(R.drawable.stendalarmbt);
                        editor.remove("check");
                        editor.commit();
                        unregisterAlarm();
                    }
                }
            }
        });

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


    public void onBackPressed() {
        //if(System.currentTimeMillis()-lastTimeBackPressed<1500){
        finish();
        return;

    }

    ;

    private class CheckTypesTask extends AsyncTask<String, Void, String> {

        ProgressDialog asyncDialog = new ProgressDialog(
                selectStartEnd.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("막차에 대한 정보를 불러옵니다..");

            // show dialog
            asyncDialog.show();

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


            } catch (Exception e) {

            }
            String K = ",";
            for (int i = 0; i < t.getA().size(); i++) {
                if (!t.getA().get(i).getStationName().equals(arg0[0]) && !t.getA().get(i).getStationName().equals(arg0[1])) {
                    K = K + t.getA().get(i).getStationName() + ",";
                }
            }

            String P = "-";
            for (int i = 0; i < t.getB().size(); i++) {
                P = P + t.getB().get(i).getStationName() + "-";
            }

            return t.getCheck() + K + P;
        }

        @Override
        protected void onPostExecute(String result) {
            asyncDialog.dismiss();
            int k = result.indexOf(",");
            int p = result.indexOf("-");
            forRail = result.substring(p);
            if (p != k + 1) {
                forRailtrans = result.substring(k + 1, p - 1);
            }
            foralarm = result.substring(0, k);
            try {
                if (result.contains("null") || result.equals(null)) {
                    Toast.makeText(getApplicationContext(), "해당 경로는 업데이트 중입니다..", Toast.LENGTH_LONG).show();
                } else {
                    if (result.substring(k + 1, p).isEmpty()) {
                        trans.setText("환승역 - ");
                    } else {
                        trans.setText("환승역 - " + result.substring(k + 1, p - 1));
                    }
                    times.setText("막차시간 - " + result.substring(0, k));
                    rail.setVisibility(View.VISIBLE);
                    railinfo.setVisibility(View.VISIBLE);
                    rail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(selectStartEnd.this);
                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();     //닫기
                                }
                            });
                            String railArray[] = forRail.substring(1, forRail.length() - 1).split("-");
                            ArrayList<String> onr = new ArrayList<String>();
                            for (int i = 0; i < railArray.length; i++) {
                                onr.add(railArray[i]);
                            }
                            String message = "";
                            for (int i = 0; i < onr.size(); i++) {
                                if (forRailtrans.contains(onr.get(i))) {
                                    message = message + onr.get(i) + "------------------((환승))" + "\n" + " *" + "\n";
                                } else {
                                    message = message + onr.get(i) + "\n" + " *" + "\n";
                                }
                            }
                            alert.setMessage(message.substring(0, message.length() - 2));
                            alert.show();


                        }
                    });

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "해당 경로는 업데이트 중입니다..", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

    }


    public void AlarmSetting(String result) {
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


        Calendar calFuture = Calendar.getInstance();
        calFuture.setTime(alarmTime);
        calFuture.add(Calendar.MINUTE, -15);
        ringTime = (calFuture.getTimeInMillis() - calendarNow.getTimeInMillis()) / 1000;

        int alar = Integer.parseInt(Long.toString(ringTime));
        if (alar > 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(selectStartEnd.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert.setMessage("막차 도착 15분전으로 알람을 설정합니다.");
            alert.show();

            // 알람 매니저에 등록할 인텐트를 만듬
            Intent intent = new Intent(this, AlarmReceiver2.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, mCrouteCode, intent, 0);

            // 알람을 받을 시간을 alar초 뒤로 설정
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, alar);
            // 알람 매니저에 알람을 등록

            am2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am2.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(selectStartEnd.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert.setMessage("오늘의 운행은 종료되었습니다.");
            alert.show();
        }
    }

    public void unregisterAlarm() {
        Toast.makeText(getApplicationContext(), "알람이 취소되었습니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AlarmReceiver2.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, mCrouteCode, intent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.cancel(sender);
    }

    /////서비스 중지//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void stopServiceMethod() {
        Intent Service = new Intent(this, MainService.class);
        stopService(Service);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}



