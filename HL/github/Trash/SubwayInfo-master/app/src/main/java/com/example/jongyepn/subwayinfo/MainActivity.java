package com.example.jongyepn.subwayinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener/*, View.OnTouchListener*/ {
    static StaionAdapter adapter;
    public static boolean loading;
    static Activity mActivity;
    static Context mContext;
    private static HorizontalScrollView Scroll_Horizontal;
    private static ScrollView Scroll_Vertical;
    protected static int currentX = 0;
    protected static int currentY = 0;
    String infostaion = "";
    AllSubwayInfo allSubwayInfo;  // 원하는 역의 정보를 셋하기 위해 호출하는 역정보 클래스객체
    Variable variable;  // 역정보클래스 객체의 arrayList를 사용하기위한 클래스객체

    ArrayList<SubwayInfo> SubwayInfoArrayList = new ArrayList<>();


    public static String url1 = "https://api.thingspeak.com/channels/553850/feeds.json?results=1";
    public static String url2 = "https://api.thingspeak.com/channels/553851/feeds.json?results=1";
    public static String url3 = "https://api.thingspeak.com/channels/640587/feeds.json?api_key=BNYUQ3H2YY8UF68D&results=1";
    public static String url4 = "https://api.thingspeak.com/channels/499591/feeds.json?results=1";
    public static String mainurl = "null";
    private Bitmap imagebitmap = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mActivity = this;

        variable.getLine4().add("길음");
        variable.getLine4().add("성신여대입구");
        variable.getLine4().add("한성대입구");
        variable.getLine4().add("혜화");
        variable.getLine4().add("동대문");
        variable.getLine4().add("동대문역사문화공원");


        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.replace, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        AsyncTask.execute(new Runnable() { // 네트워크작업을 수행하기 위한 asyncTask사용
            @Override
            public void run() {
                // All your networking logic
                // should be here
            }
        });


//줌뷰 객체 생성후
        ZoomView zoomView = new ZoomView(this);

//줌컨텐츠를 줌뷰에 넣어준다
        zoomView.addView(v);

//줌뷰를 컨테이너에 붙여준다
        RelativeLayout main_container = (RelativeLayout) findViewById(R.id.container);
        main_container.addView(zoomView);

        Scroll_Vertical = (ScrollView) findViewById(R.id.ScrollView);
        Scroll_Horizontal = (HorizontalScrollView) findViewById(R.id.horScrollView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Scroll_Horizontal.scrollTo((int) Scroll_Horizontal.getWidth() / 2, 0);
            }
        }, 100);

       /* Scroll_Vertical.setOnTouchListener(this);
        Scroll_Horizontal.setOnTouchListener(this);*/

        Resources res = getResources();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        zoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unseeButton();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final Button ibtn2 = (Button) findViewById(R.id.ibtn2);
        final Button ibtn1 = (Button) findViewById(R.id.ibtn1);
        final Button ibtn3 = (Button) findViewById(R.id.ibtn3);
        final Button b1 = (Button) findViewById(R.id.btn1);
        final Button b2 = (Button) findViewById(R.id.btn2);
        final Button b3 = (Button) findViewById(R.id.btn3);
        final Button b4 = (Button) findViewById(R.id.btn4);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeButton(b1);
                mainurl = url1;
                new GetData(MainActivity.this).execute();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeButton(b2);
                mainurl = url2;
                new GetData(MainActivity.this).execute();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeButton(b3);
                mainurl = url3;
                new GetData(MainActivity.this).execute();
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeButton(b4);
                mainurl = url4;
                new GetData(MainActivity.this).execute();
            }
        });

        ibtn2.setOnClickListener(new View.OnClickListener() {   // 요게 상세 정보로 보내는 버튼에 달린 리스너 동대문 누르면 동대문역 정보가 뜸
            @Override
            public void onClick(View v) {

                // 역을 입력하면 10개를 가져옴
                allSubwayInfo.setStatnNm(ibtn2.getText().toString());  // 버튼에서 역정보를 받아와서 검색함


                if (variable.getSubwayInfo() != null) {
                    variable.getSubwayInfo().clear();  // 모든데이터 지우기
                    variable.getUPSubwayInfo().clear();  // 상행 지우기
                    variable.getDNSubwayInfo().clear();  // 하행 지우기
                }
                 new GetSubwayData().execute();

//                SubwayInfo subwayInfo = new SubwayInfo("1", "4", "4", "1004", "상행", "당고개행 - 한성대입구방면", "오른쪽", "1004000419",
//                        "1004000421", "1004000420", "혜화", "01000당고개0", "1004", "1004000420", "0", "4120", "11",
//                        "당고개", "2018-12-04 16:02:13.0", "혜화 도착", "혜화", "1");
//                SubwayInfoArrayList.add(subwayInfo);
//
//                SubwayInfo subwayInfo2 = new SubwayInfo("1", "4", "4", "1004", "상행", "당고개행 - 한성대입구방면", "오른쪽", "1004000419",
//                        "1004000421", "1004000420", "혜화", "02002당고개0", "1004", "1004000420", "260", "4620", "13",
//                        "당고개", "2018-12-04 16:02:13.0", "4분 20초 후 (동대문역사문화공원)", "동대문역사문화공원", "99");
//                SubwayInfoArrayList.add(subwayInfo2);
//
//                SubwayInfo subwayInfo3 = new SubwayInfo("1", "4", "4", "1004", "하행", "오이도행 - 동대문방면", "왼쪽", "1004000419",
//                        "1004000421", "1004000420", "혜화", "11000오이도0", "1004", "1004000420", "0", "4637", "11",
//                        "오이도", "2018-12-04 16:01:57.0", "혜화 도착", "혜화", "1");
//                SubwayInfoArrayList.add(subwayInfo3);
//
//                SubwayInfo subwayInfo4 = new SubwayInfo("1", "4", "4", "1004", "상행", "사당행 - 동대문방면", "왼쪽", "1004000419",
//                        "1004000421", "1004000420", "혜화", "12003사당0", "1004", "1004000420", "345", "4125", "8",
//                        "사당", "2018-12-04 16:01:57.0", "5분 45초 후 (길음)", "길음", "99");
//
//                SubwayInfoArrayList.add(subwayInfo4);
//
//                variable.setSubwayInfo(SubwayInfoArrayList);
//
//                for (int i = 0; i < variable.getSubwayInfo().size(); i++) {
//                    String UpDn = variable.getSubwayInfo().get(i).getUpdnLine();
//                    Log.e("상하행", UpDn);
//                    if (UpDn.equals("상행")) {
//                        variable.getUPSubwayInfo().add(variable.getSubwayInfo().get(i));
//                    } else if (UpDn.equals("하행")) {
//                        variable.getDNSubwayInfo().add(variable.getSubwayInfo().get(i));
//                    } else if (UpDn.equals("0")) {  // 내선
//                        variable.getUPSubwayInfo().add(variable.getSubwayInfo().get(i));
//                    } else if (UpDn.equals("1")) {  // 외선
//                        variable.getDNSubwayInfo().add(variable.getSubwayInfo().get(i));
//                    }
//                }  // 반복되니까 다 저장이 될거다.
//
//                             Intent intent = new Intent(getApplicationContext(),
//                        DetailLineinfo.class);
//                startActivity(intent);


                //                variable.getAllSubwayInfo().clear();  // 모든데이터 지우기
//                variable.getUPSubwayInfo().clear();  // 상행 지우기
//                variable.getDNSubwayInfo().clear();  // 하행 지우기


            }
        });

    }

    public void seeButton(Button a) {
        final Button ibtn2 = (Button) findViewById(R.id.ibtn2);
        final Button ibtn1 = (Button) findViewById(R.id.ibtn1);
        final Button ibtn3 = (Button) findViewById(R.id.ibtn3);
        ibtn1.setVisibility(View.VISIBLE);
        ibtn2.setVisibility(View.VISIBLE);
        ibtn3.setVisibility(View.VISIBLE);
        ibtn1.setEnabled(true);
        ibtn2.setEnabled(true);
        ibtn3.setEnabled(true);
        ibtn2.setText(a.getText());
        infostaion = a.getText().toString();
    }

    public void unseeButton() {
        final Button ibtn2 = (Button) findViewById(R.id.ibtn2);
        final Button ibtn1 = (Button) findViewById(R.id.ibtn1);
        final Button ibtn3 = (Button) findViewById(R.id.ibtn3);
        ibtn1.setVisibility(View.INVISIBLE);
        ibtn2.setVisibility(View.INVISIBLE);
        ibtn3.setVisibility(View.INVISIBLE);
        ibtn1.setEnabled(false);
        ibtn2.setEnabled(false);
        ibtn3.setEnabled(false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /// if (id == R.id.action_settings) {
        //   return true;
        // }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action


        } else if (id == R.id.nav_gallery) {
            Intent intent1 = new Intent(getApplicationContext(),
                    SeeLine.class);
            startActivity(intent1);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  public static void scrollBy(int x, int y)
    {
        Scroll_Horizontal.scrollBy(x, 0);
        Scroll_Vertical.scrollBy(0, y);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                currentX = (int)event.getRawX();
                currentY = (int)event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int x2 = (int)event.getRawX();
                int y2 = (int)event.getRawY();
                scrollBy(currentX-x2, currentY-y2);
                currentX = x2;
                currentY = y2;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                currentX = (int)event.getRawX();
                currentY = (int)event.getRawY();
                break;
        }
        currentX = (int)event.getRawX();
        currentY = (int)event.getRawY();
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        loading = true;
    }
}
