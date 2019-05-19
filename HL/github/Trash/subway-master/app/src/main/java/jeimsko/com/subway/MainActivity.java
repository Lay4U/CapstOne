package jeimsko.com.subway;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.InputStream;

import jeimsko.com.subway.DB.SubwayDbAdapter;
import jeimsko.com.subway.fragment.FirstPageFragment;
import jeimsko.com.subway.fragment.SecondPageFragment;
import jeimsko.com.subway.fragment.ThirdPageFragment;
import jeimsko.com.subway.utils.ComUtils;
import jeimsko.com.subway.utils.Conf;
import jeimsko.com.subway.utils.PreferenceManager;
import jxl.Sheet;
import jxl.Workbook;


public class MainActivity extends AppCompatActivity implements LocationListener, GpsStatus.Listener {


    private String TAG = MainActivity.class.getSimpleName();
    //TODO: 필요 퍼미션 설정
    private static final String[] PERMISSION_LIST_NECESSARY = {Manifest.permission.ACCESS_FINE_LOCATION};

    private Context mContext;

    private LocationManager mService;
    private GpsStatus mStatus;

    private LocationListener locationListener = null;

    private SubwayDbAdapter dbAdapter;
    private Cursor mCursor;

    private String mGPSStatus;

    private double latmodi;
    private double lngmodi;

    private LinearLayout mLinearlodingfr;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private boolean GPS_Flag = false;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private boolean reload_flag = true;

    private Fragment mFirstFragment  = null;
    private Fragment mSecondFragment = null;
    private Fragment mThridFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mService = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLinearlodingfr = (LinearLayout) findViewById(R.id.lodingfr);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mFirstFragment = FirstPageFragment.newInstance();
        mSecondFragment = SecondPageFragment.newInstance();
        mThridFragment  = ThirdPageFragment.newInstance();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //TODO: 퍼미션 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean result = necessaryPermissionCheck();

            if (result) {
                GPS_Flag = true;
                PreferenceManager.put("gpsflag", GPS_Flag);

                dbAdapter = new SubwayDbAdapter(this);
                dbAdapter.open();

                mCursor = dbAdapter.fetchAllSubway();

                ComUtils.printLog(TAG, " 최초 확인 카운트Count = " + mCursor.getCount());

                if (mCursor.getCount() < 1) {
                    copyExcelDataToDatabase();
                } else {
                    ComUtils.printLog(TAG, "DB 완료 되어 있음");
                    ServiceInit();
                }
                mLinearlodingfr.setVisibility(View.GONE);

            } else {
                GPS_Flag = false;
                PreferenceManager.put("gpsflag", GPS_Flag);
                Toast.makeText(mContext, "GPS를 사용할수 없습니다", Toast.LENGTH_LONG);
                mLinearlodingfr.setVisibility(View.GONE);

            }
        } else { //TODO: 6.0  이하버전용

            dbAdapter = new SubwayDbAdapter(this);
            dbAdapter.open();

            mCursor = dbAdapter.fetchAllSubway();

            ComUtils.printLog(TAG, " 최초 확인 카운트Count = " + mCursor.getCount());

            if (mCursor.getCount() < 1) {
                copyExcelDataToDatabase();
            } else {
                ComUtils.printLog(TAG, "DB 완료 되어 있음");
                ServiceInit();
            }

        }

    }

    private void ServiceInit() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mService.addGpsStatusListener(this);
        }

        isGPSEnabled = mService.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mService.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {

            locationListener = new MainActivity();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mService.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0.0f, locationListener);
                mService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0.0f, locationListener);
            }

        } else {

            alertbox("GPS 상태", "GPS 상태가 OFF 입니다");
        }


    }


    //TODO: GPS 데이터와 역 데이터 비교 하여 역찾기
    private void subWaystationFind(double latmodi, double lngmodi) {
        ComUtils.printLog(TAG, "subWaySystationFind");
        String save_code = null;
        String save_stationname = null;
        String save_id = null;
        String save_trainnum = null;
        double save_xwgps = 0;
        double save_ywgps = 0;
        double tempdistance = 0;
        //float distance = 0;
        //TODO: DB data 값 읽어 오는 부분

        if (dbAdapter == null) {
            dbAdapter = new SubwayDbAdapter(mContext);
            ComUtils.printLog(TAG, "dbadapter == null");
            mCursor = dbAdapter.fetchAllSubway();

        } else {
            mCursor = dbAdapter.fetchAllSubway();
        }

        ComUtils.printLog(TAG, "Count = " + mCursor.getCount());
        ComUtils.printLog(TAG, "latmodi = " + latmodi);
        ComUtils.printLog(TAG, "lngmodi = " + lngmodi);
        Location current_lo = new Location("");
        current_lo.setLatitude(latmodi);
        current_lo.setLongitude(lngmodi);

        //TODO: DB 데이터 모든 값을 읽어 오는부분 여기서 최소 값 찾아서 역 선택이 되어야 한다
        int i = 0;
        while (mCursor.moveToNext()) {
            String id, stationname, code, trainnum;
            double xwgps = 0;
            double ywgps = 0;

            id = mCursor.getString(mCursor.getColumnIndex("_id"));
            xwgps = mCursor.getDouble(mCursor.getColumnIndex("xwgs"));
            ywgps = mCursor.getDouble(mCursor.getColumnIndex("ywgs"));
            stationname = mCursor.getString(mCursor.getColumnIndex("stationname"));
            code = mCursor.getString(mCursor.getColumnIndex("code"));
            trainnum = mCursor.getString(mCursor.getColumnIndex("trainnum"));
            ComUtils.printLog(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            ComUtils.printLog(TAG, "id = " + id);
            ComUtils.printLog(TAG, "latmodi = " + latmodi);
            ComUtils.printLog(TAG, "lngmodi = " + lngmodi);
            ComUtils.printLog(TAG, "xwgps = " + xwgps);
            ComUtils.printLog(TAG, "ywgps = " + ywgps);
            ComUtils.printLog(TAG, "stationname = " + stationname);
            ComUtils.printLog(TAG, "code = " + code);

            Location station_lo = new Location("");
            station_lo.setLatitude(xwgps);
            station_lo.setLongitude(ywgps);

            double R = 6371000f;
            double x = (ywgps - lngmodi) * Math.cos((latmodi + xwgps) / 2);
            double y = (xwgps - latmodi);
            double distance = Math.sqrt(x * x + y * y) * R;


            //distance = current_lo.distanceTo(station_lo);

            ComUtils.printLog(TAG, "modi distance = " + distance);

            if (i < 1) {
                tempdistance = distance;
                save_code = code;
                save_stationname = stationname;
                save_id = id;
                save_trainnum = trainnum;
                save_xwgps = xwgps;
                save_ywgps = ywgps;

            } else {

                if (tempdistance > distance) {
                    ComUtils.printLog(TAG, "값 변경");
                    tempdistance = distance;
                    save_code = code;
                    save_stationname = stationname;
                    save_id = id;
                    save_xwgps = xwgps;
                    save_ywgps = ywgps;
                    save_trainnum = trainnum;

                    ComUtils.printLog(TAG, "save_id = " + save_id);
                    ComUtils.printLog(TAG, "save_xwgps = " + save_xwgps);
                    ComUtils.printLog(TAG, "save_ywgps = " + save_ywgps);
                    ComUtils.printLog(TAG, "save_stationname = " + save_stationname);
                    ComUtils.printLog(TAG, "save_code = " + save_code);
                    ComUtils.printLog(TAG, "save_trainnum = " + save_trainnum);
                    ComUtils.printLog(TAG, "tempdistance = " + tempdistance);
                }

            }
            ComUtils.printLog(TAG, "i = " + i);
            i++;

        }

        ComUtils.printLog(TAG, "===================================");
        ComUtils.printLog(TAG, "save_id = " + save_id);
        ComUtils.printLog(TAG, "save_xwgps = " + save_xwgps);
        ComUtils.printLog(TAG, "save_ywgps = " + save_ywgps);
        ComUtils.printLog(TAG, "save_stationname = " + save_stationname);
        ComUtils.printLog(TAG, "save_code = " + save_code);
        ComUtils.printLog(TAG, "save_trainnum = " + save_trainnum);
        ComUtils.printLog(TAG, "tempdistance = " + tempdistance);

        PreferenceManager.put("save_id", save_id);
//        PreferenceManager.put("save_xwgps", save_xwgps);
//        PreferenceManager.put("save_ywgps", save_ywgps);
        PreferenceManager.put("save_stationname", save_stationname);
        PreferenceManager.put("save_code", save_code);
        PreferenceManager.put("save_trainnum", save_trainnum);
//        PreferenceManager.put("tempdistance", tempdistance);

    }


    protected void alertbox(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("GPS On", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        startActivity(intent);
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //TODO: GPS 상태 체크
    @Override
    public void onGpsStatusChanged(int event) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mStatus = mService.getGpsStatus(mStatus);

        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                ComUtils.printLog(TAG, "GPS_EVENT_STARTED");
                mGPSStatus = "GPS_EVENT_STARTED";
                //subWaySystationFind();
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                ComUtils.printLog(TAG, "GPS_EVENT_STOPPED");
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                ComUtils.printLog(TAG, "GPS_EVENT_FIRST_FIX");
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                ComUtils.printLog(TAG, "GPS_EVENT_SATELLITE_STATUS");
                break;
        }
    }


    //TODO: 실제 좌표값 얻어 오는 부분
    @Override
    public void onLocationChanged(Location location) {


        double lat = location.getLatitude();
        double lng = location.getLongitude();

        ComUtils.printLog(TAG, "lat = " + lat);
        ComUtils.printLog(TAG, "lng = " + lng);

        latmodi = lat;
        lngmodi = lng;

        ComUtils.printLog(TAG, "latmodi = " + latmodi);
        ComUtils.printLog(TAG, "lngmodi = " + lngmodi);

        if (reload_flag) {
            reload_flag = false;
            subWaystationFind(latmodi, lngmodi);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        ComUtils.printLog(TAG, "onProviderEnabled !!");
    }

    @Override
    public void onProviderDisabled(String provider) {
        ComUtils.printLog(TAG, "onProviderDisabled !!");
    }


    //TODO: Pager Adapter

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
                return mFirstFragment;
                //return FirstPageFragment.newInstance();
            } else if (position == 1) {
                return mSecondFragment;
                //return SecondPageFragment.newInstance();
            } else {
                return mThridFragment;
                //return ThirdPageFragment.newInstance();
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.section_number1);
                case 1:
                    return getString(R.string.section_number2);
                case 2:
                    return getString(R.string.section_number3);
            }
            return null;
        }
    }


    //TODO:  퍼미션 체크
    public boolean necessaryPermissionCheck() {

        boolean isGranted = true;
        for (String permission : PERMISSION_LIST_NECESSARY) {
            if (checkPermision(this, permission) == false) {
                showNecessaryPermissionPopup(this, permission, PERMISSION_LIST_NECESSARY);
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }


    public boolean checkPermision(final Activity activity, final String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }


    private void showNecessaryPermissionPopup(final Activity activity, String permission, final String[] request_permission) {
        boolean firsetPermission = false;
        //1회 거부 이후
        //최초실행 , 다시보지않기 클릭후 거부
        //필수 권한은 구분없이 팝업 띄움
        firsetPermission = PreferenceManager.getBoolean("firsetPermissionNecessary", false);
        if (firsetPermission) {

            goSystemSetting(this, Conf.REQ_CODE_REQUEST_SETTING);

        } else {
            ActivityCompat.requestPermissions(activity, request_permission, Conf.MY_PERMISSIONS_REQUEST_NECESSARY);
            PreferenceManager.put("firsetPermissionNecessary", true);
        }
    }


    public void goSystemSetting(final Activity activity, int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            activity.startActivityForResult(intent, requestCode);
        }
    }


    //TODO: 퍼미션결과
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ComUtils.printLog(TAG, "권한 결과");
        switch (requestCode) {
            case Conf.MY_PERMISSIONS_REQUEST_NECESSARY:
                boolean result = necessaryPermissionCheck();
                if (result) {
                    GPS_Flag = true;
                    PreferenceManager.put("gpsflag", GPS_Flag);
                    ComUtils.printLog(TAG, "permission result true");
                    //gps on 시키자

                    dbAdapter = new SubwayDbAdapter(this);
                    dbAdapter.open();

                    mCursor = dbAdapter.fetchAllSubway();

                    ComUtils.printLog(TAG, " 최초 확인 카운트Count = " + mCursor.getCount());

                    if (mCursor.getCount() < 1) {
                        copyExcelDataToDatabase();
                        mLinearlodingfr.setVisibility(View.GONE);
                    } else {
                        ComUtils.printLog(TAG, "DB 완료 되어 있음");
                        mLinearlodingfr.setVisibility(View.GONE);
                        ServiceInit();
                    }


                } else {
                    GPS_Flag = false;
                    PreferenceManager.put("gpsflag", GPS_Flag);
                    ComUtils.printLog(TAG, "permission result false");

                    dbAdapter = new SubwayDbAdapter(this);
                    dbAdapter.open();

                    mCursor = dbAdapter.fetchAllSubway();

                    ComUtils.printLog(TAG, " 최초 확인 카운트Count = " + mCursor.getCount());

                    if (mCursor.getCount() < 1) {
                        copyExcelDataToDatabase();
                        mLinearlodingfr.setVisibility(View.GONE);
                    } else {
                        ComUtils.printLog(TAG, "DB 완료 되어 있음");
                        ServiceInit();
                        mLinearlodingfr.setVisibility(View.GONE);
                    }

                }
                break;

        }


    }


    //TODO: 엑셀 -> DB
    private void copyExcelDataToDatabase() {

        ComUtils.printLog(TAG, "copyExcelDataToDatabase()");


        mLinearlodingfr.setVisibility(View.VISIBLE);

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("seoulsubway.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {

                ComUtils.printLog(TAG, "workbook != null");
                sheet = workbook.getSheet(0);

                ComUtils.printLog(TAG, "sheet.getRows()= " + sheet.getRows());
                ComUtils.printLog(TAG, "sheet.getColumns()= " + sheet.getColumns());

                if (sheet != null) {

                    int nMaxColum = 9;
                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumns();
                    int nColumnStarIndex = 0;
                    int nColumnEndIndex = 671; //sheet.getRows();

                    boolean error_data = false;
                    for (int nRow = nRowStartIndex; nRow <= nColumnEndIndex; nRow++) {
                        ComUtils.printLog(TAG, "nRow =" + nRow);

                        error_data = false;

                        String code = sheet.getCell(nColumnStarIndex, nRow).getContents();
                        ComUtils.printLog(TAG, "code =" + code);

                        String stationname = sheet.getCell(nColumnStarIndex + 1, nRow).getContents();
                        ComUtils.printLog(TAG, "stationname =" + stationname);

                        String trainnum = sheet.getCell(nColumnStarIndex + 2, nRow).getContents();
                        ComUtils.printLog(TAG, "trainnum =" + trainnum);

                        String outcode = sheet.getCell(nColumnStarIndex + 3, nRow).getContents();
                        ComUtils.printLog(TAG, "outcode =" + outcode);

                        String cyber = sheet.getCell(nColumnStarIndex + 4, nRow).getContents();
                        ComUtils.printLog(TAG, "cyber =" + cyber);

                        double x;
                        String double_x = (sheet.getCell(nColumnStarIndex + 5, nRow).getContents());
                        if (double_x.equals("")) {
                            x = (double) 0.0;
                            error_data = true;
                        } else {
                            x = Double.parseDouble(sheet.getCell(nColumnStarIndex + 5, nRow).getContents());
                        }
                        ComUtils.printLog(TAG, "x =" + x);

                        double y;
                        String double_y = sheet.getCell(nColumnStarIndex + 6, nRow).getContents();
                        if (double_y.equals("")) {
                            y = (double) 0.0;
                            error_data = true;
                        } else {
                            y = Double.parseDouble(sheet.getCell(nColumnStarIndex + 6, nRow).getContents());
                        }
                        ComUtils.printLog(TAG, "y =" + y);

                        double xwgs;
                        String double_xwgs = sheet.getCell(nColumnStarIndex + 7, nRow).getContents();
                        if (double_xwgs.equals("")) {
                            xwgs = (double) 0.0;
                            error_data = true;
                        } else {
                            xwgs = Double.parseDouble(sheet.getCell(nColumnStarIndex + 7, nRow).getContents());
                        }
                        ComUtils.printLog(TAG, "xwgs =" + xwgs);

                        double ywgs;
                        String double_ywgs = sheet.getCell(nColumnStarIndex + 8, nRow).getContents();
                        if (double_ywgs.equals("")) {
                            error_data = true;
                            ywgs = (double) 0.0;
                        } else {
                            ywgs = Double.parseDouble(sheet.getCell(nColumnStarIndex + 8, nRow).getContents());
                        }

                        ComUtils.printLog(TAG, "ywgs =" + ywgs);

                        if (!error_data)
                            dbAdapter.createSubway(code, stationname, trainnum, outcode, cyber, x, y, xwgs, ywgs);
                    }
                    //dbAdapter.close();

                } else {
                    System.out.println("sheet is null");
                }
            } else {
                System.out.println("workbook is null");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }


        ServiceInit(); // 서비스 초기화 함수
        mLinearlodingfr.setVisibility(View.GONE);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ComUtils.printLog(TAG, "on onBackPressed ");

        if (locationListener != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            } else {
                mService.removeUpdates(locationListener);  //업데이트 리스너 제거
                locationListener = null;
            }

        }

        finish();

    }

    @Override
    public void onStop() {
        super.onStop();
        ComUtils.printLog(TAG, "on stop");
        mService.removeGpsStatusListener((GpsStatus.Listener) locationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        if (locationListener != null) {
            mService.removeUpdates(locationListener);  //업데이트 리스너 제거
            locationListener = null;
        }
        ComUtils.printLog(TAG, "on stop2 ");

        reload_flag = false;

        finish();
    }


}
