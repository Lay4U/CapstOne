package com.estsoft.r_subway_android;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.estsoft.r_subway_android.Controller.RouteControllerNew;
import com.estsoft.r_subway_android.Controller.StationController;
import com.estsoft.r_subway_android.Crawling.InternetManager;
import com.estsoft.r_subway_android.Crawling.ServerConnection;
import com.estsoft.r_subway_android.Crawling.ServerConnectionSingle;
import com.estsoft.r_subway_android.Repository.StationRepository.InitializeRealm;
import com.estsoft.r_subway_android.Repository.StationRepository.RealmStation;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.UI.MapTouchView.TtfMapImageView;
import com.estsoft.r_subway_android.UI.RouteInfo.RoutePagerAdapter;
import com.estsoft.r_subway_android.UI.Settings.ExpandableListAdapter;
import com.estsoft.r_subway_android.UI.Settings.SearchSetting;
import com.estsoft.r_subway_android.UI.StationInfo.PagerAdapter;
import com.estsoft.r_subway_android.UI.Tutorial.TutorialActivity;
import com.estsoft.r_subway_android.listener.InteractionListener;
import com.estsoft.r_subway_android.listener.SearchListAdapterListener;
import com.estsoft.r_subway_android.listener.TtfMapImageViewListener;
import com.facebook.stetho.Stetho;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity
        implements TtfMapImageViewListener,
        SearchListAdapterListener {

    private static final String TAG = "MainActivity";

    public final int WAIT = 1;
    public final int FULL = 2;
    public int status = WAIT;

    public static final int ALL_MARKERS = 0;
    public static final int ACT_MARKER = 1;
    public static final int REINFLATE_MARKER = 2;
    public static final int REINFLATE_MARKER_SERVER = 3;

    public static final int START_MARKER = 21;
    public static final int TRANSFER_MARKER = 22;
    public static final int END_MARKER = 23;
    public static final int EXCEPT_ACTI_MARKER = 24;

    public static final int SHORT_ROUTE = 10;
    public static final int MIN_TRANSFER = 11;
    public static final int CUSTOM_ROUTE = 12;
    public static final int DEFAULT_ROUTE = 13;

    private InteractionListener interactionListener = null;

    private BottomSheetLayout stationBottomSheet = null;
    private BottomSheetLayout routeBottomSheet = null;

    private Toolbar toolbar = null;

    private DrawerLayout drawer = null;

    private SearchView mSearchView = null;

    private NavigationView navigationView = null;

    private ExpandableListAdapter expandableListAdapter = null;

    public RoutePagerAdapter mRoutePagerAdapter = null;

    private RouteControllerNew routeController = null;
    private StationController stationController = null;
    private Station activeStation = null;
    private Station startStation = null;
    private Station endStation = null;

    private RouteNew currentRoute = null;
    private RouteNew[] routes = null;
    private RelativeLayout passMarkerMother = null;
    private List<ImageView> routeMarkers = null;
    private TextView markerText = null;
    private List<View> markerList = null;

    private TtfMapImageView mapView = null;

    private RecyclerView searchListView = null;

    private EditText searchEditText = null;

    private float normalMarkerSize = -1;
    private float routeMarkerSize = -1;

    LayoutInflater inflater = null;

    ExpandableListView expListView;
    SearchSetting searchSetting;

    private int curPage;

//    @BindView(R.id.Start)
//    ImageView start;
//    @BindView(R.id.Arrive)
//    ImageView arrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(getApplicationContext(), TutorialActivity.class));

        ButterKnife.bind(this);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //역이름 텍스트
        markerText = ((TextView) findViewById(R.id.markerText));

        //마커기준 사이즈
        Drawable image = ((ImageView)findViewById(R.id.marker)).getDrawable();
        normalMarkerSize = (image.getIntrinsicWidth() + image.getIntrinsicHeight()) / 2;
        // f 값 작을수록 작아짐
        routeMarkerSize = normalMarkerSize / 0.6f;

        // passMarkerMother Relative View reference
        passMarkerMother = (RelativeLayout) findViewById(R.id.route_mother);

        // TtfMapImageView ... mapView 의 구현
        mapView = ((TtfMapImageView) findViewById(R.id.mapView));
        mapView.setImageResource(R.drawable.linemap_naver);
        // log.e(TAG, "onCreate: " + mapView.getDrawable().getIntrinsicWidth());
        mapView.setTtfMapImageViewListener(this);

        mapView.setImageResource(R.drawable.linemap_naver);
        mapView.setTtfMapImageViewListener(this);

        interactionListener = new InteractionListener(this, mapView.getSemiStationList());


        stationBottomSheet = (BottomSheetLayout) findViewById(R.id.station_bottomSheet);
        routeBottomSheet = (BottomSheetLayout) findViewById(R.id.route_bottomSheet1);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        //설정창 Sliding menu
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //search
        toolbar.inflateMenu(R.menu.search);

        mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setFocusableInTouchMode(true);

        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(interactionListener);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        toolbar.setNavigationOnClickListener(interactionListener);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchSetting = new SearchSetting();
        expandableListAdapter = new ExpandableListAdapter(this, searchSetting.getGroupList(), searchSetting.getSettingCollection());

        expListView = (ExpandableListView) findViewById(R.id.search_setting);
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnGroupClickListener(interactionListener);
        expListView.setOnChildClickListener(interactionListener);



        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        SharedPreferences initRealmPrefs = getSharedPreferences("initRealmPrefs", MODE_PRIVATE);
        String init = initRealmPrefs.getString("Init",null);
        if(init == null) {
            InitializeRealm initRealm = new InitializeRealm(this);
            for(int i=100, j=1; i<=20138; i++, j++) {
                String json = initRealm.getJSONFromAsset(i);
                if(json != null) {
                    initRealm.loadJSONToRealm(json, j);
                }
            }
            initRealm.connectStations();

            SharedPreferences.Editor editor = initRealmPrefs.edit();
            editor.putString("Init", "Done");
            editor.commit();
        }


        // log.d("\\\\\\\\\\\\\\", initRealmPrefs.getString("Init",null));
        Realm realm = Realm.getInstance(this);
        RealmResults<RealmStation> results = realm.where(RealmStation.class).findAll();
        for(RealmStation station : results) {
            // log.d("\\\\\\\\\\", station.getStationName() + station.getStationID() + "x : " + station.getxPos() + "y : " + station.getyPos());
        }

        try {
            stationController = new StationController(Realm.getInstance(this), getResources().openRawResource(R.raw.station_cost), this);
            // log.d(TAG, "initializeAdj: Successfully Finished ");
        } catch ( Exception e ) {
            e.printStackTrace();
        }

//        routeController = new RouteControllerNew( stationController, mapView );
        routeController = new RouteControllerNew( stationController, mapView, this );

//        mapView.setSemiStationLaneNumber( stationController );

        InternetManager.init(this);

        // log.d(TAG, "Flow Check: onCreate Finished");
    }


    //Search icon없이 바로뜨게
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchMenu = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchMenu.getActionView();

        interactionListener.setMenu(menu);
//        searchView.setIconifiedByDefault(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();


        if (searchMenu!= null) {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            if(searchView !=null){
                LinearLayout searchPlate = (LinearLayout)searchView.findViewById(R.id.search_plate);
                if(searchPlate != null){
                    EditText mSearchEditText = (EditText)searchPlate.findViewById(R.id.search_src_text);
                    searchEditText = mSearchEditText;
                    if(mSearchEditText != null){
                        mSearchEditText.clearFocus();     // This fixes the keyboard from popping up each time
                        mSearchEditText.setCursorVisible(true);
                    }
                }
            }
        }

        searchView.setOnQueryTextListener(interactionListener);

        ImageView closebtn = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout searchPlate = (LinearLayout)searchView.findViewById(R.id.search_plate);
                EditText mSearchEditText = (EditText)searchPlate.findViewById(R.id.search_src_text);
                RecyclerView list = (RecyclerView) findViewById(R.id.list_test_view);
                mSearchEditText.setText("");

                hideSoftKeyboard(v);
                list.setVisibility(View.GONE);

            }
        });


        LinearLayout searchPlate = (LinearLayout)searchView.findViewById(R.id.search_plate);
        EditText mSearchEditText = (EditText)searchPlate.findViewById(R.id.search_src_text);
        mSearchEditText.setOnClickListener(interactionListener);

        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("역검색");

        searchMenu.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        hideSoftKeyboard(mapView);

        // log.d(TAG, "Flow Check: onCreateOptionsMenu Finished");

        return true;
    }

    public void onComposeAction(MenuItem mi) {
        // handle click here
        // log.d("123","123");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                // log.d("1234534534534533","12334534534534");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
            Hide Keyboard
             */
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if  (searchEditText != null) {
            // log.d(TAG, "hideSoftKeyboard: setting focus");
            searchEditText.setFocusable(false);
            searchEditText.setFocusableInTouchMode(true);
        } else {
            // log.d(TAG, "hideSoftKeyboard: searchEditText is null");
        }
    }

    //설정창 navigation items
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        return true;
//    } 수정

    /*
        Down Here - SearchListAdapter
        Implemented Listener Override Methods
    */

    @Override
    public void itemClick(SemiStation semiStation) {
        RecyclerView list = (RecyclerView)findViewById(R.id.list_test_view);
        ((EditText)findViewById(interactionListener.getSearchTextContext())).setText("");
        findViewById(interactionListener.getSearchTextContext()).clearFocus();
        searchListView.setVisibility(View.GONE);
        if (status == FULL) {
            routeBottomSheet.dismissSheet();
            setMarkerDefault(ALL_MARKERS);
        }
        setActiveStation( semiStation );
        // log.d(TAG, "itemClick: ");
    }


    /*
        Down Here - TtfMapImageView
        Implemented Listener Override Methods
    */

    @Override
    public void setMarkerDefault(int markerMode) {
        if (markerList == null) {
            markerList = new ArrayList<>();
            markerList.add((ImageView) findViewById(R.id.marker));
            markerList.add((ImageView) findViewById(R.id.startMarker));
            markerList.add((ImageView) findViewById(R.id.endMarker));
            markerList.add((TextView) findViewById(R.id.markerText));
        }

        if(markerMode == EXCEPT_ACTI_MARKER ) {
            for (View marker : markerList) {
                if (markerList.get(3).getId() == marker.getId()) {
                    // log.d(TAG, "setMarkerDefault: " + markerList.size() + " / " + markerList.get(3).getId() + " / " + marker.getId());
                    continue;
                }
                if (markerList.get(0).getId() == marker.getId()) {
                    // log.d(TAG, "setMarkerDefault: " + markerList.size() + " / " + markerList.get(0).getId() + " / " + marker.getId());
                    continue;
                }
                setMarkerVisibility(marker, false);
            }
            startStation = null;
            endStation = null;
            status = WAIT;
            currentRoute = null;

            if (routeMarkers != null) {
                for (ImageView view : routeMarkers) {
                    view.setVisibility(View.GONE);
                    passMarkerMother.removeView(view);
                }
            }
            routeMarkers = null;

            applyMapScaleChange();

            return;
        }



        if (markerMode == ALL_MARKERS) {
            for (View marker : markerList) {
                setMarkerVisibility(marker, false);
            }
//            setMarkerVisibility( (ImageView)findViewById(R.id.route_image_source), false);
            activeStation = null;
            startStation = null;
            endStation = null;
            status = WAIT;
            currentRoute = null;

            if (routeMarkers != null) {
                for (ImageView view : routeMarkers) {
                    view.setVisibility(View.GONE);
                    passMarkerMother.removeView(view);
                }
            }
            routeMarkers = null;

            applyMapScaleChange();

        } else if ( markerMode == ACT_MARKER ) {
            setMarkerVisibility(markerList.get(0), false);
            activeStation = null;
            if (findViewById(R.id.station_bottomSheet) != null) {
                stationBottomSheet.dismissSheet();
            }
        } else if ( markerMode == REINFLATE_MARKER ){
            for (View marker : markerList) {
                setMarkerVisibility(marker, false);
            }
            activeStation = null;
            currentRoute = null;

            if (routeMarkers != null) {
                for (ImageView view : routeMarkers) {
                    view.setVisibility(View.GONE);
                    passMarkerMother.removeView(view);
                }
            }
            routeMarkers = null;
            applyMapScaleChange();
        } else if (markerMode == REINFLATE_MARKER_SERVER) {
            for (View marker : markerList) {
                setMarkerVisibility(marker, false);
            }
            activeStation = null;

            if (routeMarkers != null) {
                for (ImageView view : routeMarkers) {
                    view.setVisibility(View.GONE);
                    passMarkerMother.removeView(view);
                }
            }
            routeMarkers = null;
            applyMapScaleChange();
        }

    }

    @Override
    public void setActiveStation(SemiStation semiStation) {
        // log.d(TAG, "setActiveStation: " + semiStation.toString());
        ServerConnectionSingle.killThread();

        if (status != FULL) {
            activeStation = stationController.getStation(semiStation);
            // log.d(TAG, "setActiveStation: " + activeStation.getStationID());

            mapView.moveToMapCenter( semiStation.getPosition() );

            boolean flag = false;
            List<Station> checkList = new ArrayList<>();
            checkList.add(startStation);
            checkList.add(endStation);
            for (Station station : checkList) {
                if (station == null) continue;
                else {
                    if ( activeStation.getStationID() == station.getStationID() ) {
//                    if (activeStation.getStationId1().equals(station.getStationId1())) {
                        activeStation = station;
                        ImageView activeMarker = (ImageView)findViewById(R.id.marker);
                        setMarkerVisibility(activeMarker, false);
                        flag = true;
                        break;
                    }
                }
            }

            if (flag == false) {
                ImageView activeMarker = (ImageView)findViewById(R.id.marker);
                setMarkerVisibility(activeMarker, true);
                setMarkerPosition(0, null, null);
            }


            runBottomSheet(stationController.getExStations(activeStation), null);
//            runBottomSheet(activeStation, null);
//            stationController.getExStations(activeStation);
        }
    }

    private void setMarkerVisibility(View marker, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        if (marker.getId() == R.id.marker) {
            markerText.setVisibility(visibility);
//            marker ImageView 삭제해야함! 임시방편
            visibility = View.INVISIBLE;
        }
        marker.setVisibility(visibility);

    }

    @Override
    public void applyMapScaleChange() {
        // 맵뷰 스케일이 바뀔때마다 Call
        setMarkerPosition(0, null, null);

        hideSoftKeyboard(mapView);

        if (currentRoute != null) {
            setRouteMarkerPosition();
        }

    }

    private void setMarkerPosition(float markerRatio, PointF markerPosition1, String stationName1) {

        for (View marker : markerList) {
            if (marker.getVisibility() == View.VISIBLE) {

                PointF markerPosition;
                switch (marker.getId()) {
                    case R.id.marker:
                        markerPosition = activeStation.getMapPoint();
                        break;
                    case R.id.startMarker:
                        markerPosition = startStation.getMapPoint();
                        break;
                    case R.id.endMarker:
                        markerPosition = endStation.getMapPoint();
                        break;
                    case R.id.markerText:
                        markerPosition = activeStation.getMapPoint();
                        markerText = (TextView) findViewById(R.id.markerText);
                        markerText.setText(activeStation.getStationName());
                        markerText.setTextSize( mapView.getMarkerRatio() / 10  );
                        markerText.measure(0, 0);
                        markerText.setX(markerPosition.x - markerText.getMeasuredWidth() / 2);
                        markerText.setY(markerPosition.y - markerText.getMeasuredHeight());
                        break;
                    default:
                        markerPosition = new PointF(0, 0);
                }
                // log.d(TAG, "setMarkerPosition: " + markerPosition.toString());
                if (marker instanceof ImageView ) {
                    setImageMatrix((ImageView)marker, mapView.getMarkerRatio(), markerPosition);
                }

            }

        }

    }

    private void setRouteMarkerPosition() {

        if (routeMarkers == null || currentRoute == null) return;
        for (int i = 0; i < routeMarkers.size(); i++) {
//            if (i == 0 || i == routeMarkers.size() - 1) continue;
            setImageMatrix(
//                    routeMarkers.get(i - 1),
                    routeMarkers.get(i),
                    mapView.getMarkerRatio(),
                    currentRoute.getStationByOrder(i).getMapPoint()
            );

        }
//        System.gc();

    }

    private void setImageMatrix(ImageView view, float markerRatio, PointF point) {
        if (view.getScaleType() != ImageView.ScaleType.MATRIX)
            view.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix = new Matrix();
        float[] values = new float[9];
        matrix.getValues(values);
        Drawable image = view.getDrawable();
//        float magnification = markerRatio / ((image.getIntrinsicWidth() + image.getIntrinsicHeight()) / 2);
        float magnification ;
        float width = image.getIntrinsicWidth();
        float height = image.getIntrinsicHeight();
        if (view.getId() / 1000 == 3){
            magnification = markerRatio / routeMarkerSize;
            width = width * magnification;
            height = height * magnification;
            values[0] = values[4] = magnification;
            values[2] = point.x - width / 2;
            values[5] = point.y - height / 2;
        } else {
            magnification = markerRatio / normalMarkerSize;
            width = width * magnification;
            height = height * magnification;
            values[0] = values[4] = magnification;
            values[2] = point.x - width / 2;
            values[5] = point.y - height;
        }

//        if (view.getId() == R.id.marker) {
//            markerText = (TextView) findViewById(R.id.markerText);
//            markerText.setText(activeStation.getStationName());
//            markerText.setTextSize( mapView.getMarkerRatio() / 10  );
//            markerText.measure(0, 0);
//            markerText.setX(point.x - markerText.getMeasuredWidth() / 2);
//            markerText.setY(point.y - markerText.getMeasuredHeight());
//        }

        matrix.setValues(values);
        view.setImageMatrix(matrix);
    }


    /*
    BottomSheets
    */
    public void runBottomSheet(List<Station> exStations, RouteNew[] route) {
//    public void runBottomSheet(Station station, Route route) {
        BottomSheetLayout stationBottomSheet = (BottomSheetLayout) findViewById(R.id.station_bottomSheet);
        stationBottomSheet.setPeekSheetTranslation(490);
        stationBottomSheet.addOnSheetDismissedListener(interactionListener);


//        final BottomSheetLayout routeBottomSheet = (BottomSheetLayout) findViewById(R.id.route_bottomSheet1);
        BottomSheetLayout routeBottomSheet = (BottomSheetLayout) findViewById(R.id.route_bottomSheet1);
        routeBottomSheet.addOnSheetDismissedListener(interactionListener);


        if (status == WAIT) {         // Station 정보
            if (stationBottomSheet.isSheetShowing()) {
                 LayoutInflater.from(this).inflate(R.layout.layout_subwayinfo_bottomsheet, stationBottomSheet, false);
            } else {
                stationBottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_subwayinfo_bottomsheet, stationBottomSheet, false));
            }
            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
 //           // log.d(TAG,"pager : station:"+exStations.size());
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), exStations));
    //   viewPager.setOffscreenPageLimit(3);
            // log.d("pager", "------------->" + viewPager.toString());
            // Give the PagerSlidingTabStrip the ViewPager
            PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabsStrip.setTabPaddingLeftRight(25);
            tabsStrip.setIndicatorHeight(5);

            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);

            stationBottomSheet.setShouldDimContentView(false);
            stationBottomSheet.setInterceptContentTouch(false);

            // log.d(TAG, "runBottomSheet: " + ((LinearLayout) stationBottomSheet.getSheetView()).getChildAt(0).getClass());

            ImageView start = (ImageView) findViewById(R.id.Start);
            ImageView arrive = (ImageView) findViewById(R.id.Arrive);
            start.setOnClickListener(interactionListener);
            arrive.setOnClickListener(interactionListener);

            // getting Server AccidentInfo
//            mServerConnection.getAccidentInfo(activeStation);
            // log.d(TAG, "runBottomSheet: " + activeStation.isAccidentInfo());

//            // log.d("----------->", start.getText().toString());
//            // log.d("----------->", arrive.getText().toString());

            // 리스너로 감
            /*start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartClick(v);
                }
            });
            arrive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onArriveClick(v);
                }
            });*/


        } else if (status == FULL) {          // Route 정보

//            if(bottomSheet!= null) bottomSheet.dismissSheet();
            routeBottomSheet.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.layout_routeinfo_bottomsheet, stationBottomSheet, false));
            // Get the ViewPager and set it's RoutePagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.route_viewpager);

            mRoutePagerAdapter = new RoutePagerAdapter(getSupportFragmentManager(), route);

            viewPager.setAdapter( mRoutePagerAdapter );


//        viewPager.setOffscreenPageLimit(3);
            // log.d("pager", "------------->" + viewPager.toString());
            // Give the PagerSlidingTabStrip the ViewPager
            PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.route_tabs);
            tabsStrip.setTabPaddingLeftRight(25);
            tabsStrip.setIndicatorHeight(5);

            // Attach the view pager to the tab strip
            tabsStrip.setViewPager(viewPager);

            routeBottomSheet.setPeekSheetTranslation(450);
            routeBottomSheet.findViewById(R.id.end_info).setOnClickListener(interactionListener);

            viewPager.addOnPageChangeListener(interactionListener);
            // 리스너로 감
            /*routeBottomSheet.findViewById(R.id.endinfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMarkerDefault( ALL_MARKERS );
                    routeBottomSheet.dismissSheet();
                }
            });*/

            routeBottomSheet.setShouldDimContentView(false);
            routeBottomSheet.setInterceptContentTouch(false);
            stationBottomSheet.dismissSheet();

        }

    }

    /*
        Down Here - BottomSheet
        Implemented Listener Override Methods
    */

    public void onStartClick(View v) {
        ((BottomSheetLayout) findViewById(R.id.station_bottomSheet)).dismissSheet();
        Station tmp = activeStation;
        if (activeStation == endStation) setMarkerDefault(ALL_MARKERS);
        activeStation = tmp;
        startStation = activeStation;
//        activeStation = null;
        // 0 :defaultMarker, 1 : startMarker, 2 : endMarker
        ImageView startMarker = (ImageView)markerList.get(1);
        setMarkerVisibility(startMarker, true);
        setMarkerVisibility(markerList.get(0), false);
        setMarkerPosition(0, null, null);
        setStatus();
    }

    public void onArriveClick(View v) {
        ((BottomSheetLayout) findViewById(R.id.station_bottomSheet)).dismissSheet();
        Station tmp = activeStation;
        if (activeStation == startStation) setMarkerDefault(ALL_MARKERS);
        activeStation = tmp;
        endStation = activeStation;
//        activeStation = null;
        // 0 :defaultMarker, 1 : startMarker, 2 : endMarker
        ImageView startMarker = (ImageView)markerList.get(2);
        setMarkerVisibility(startMarker, true);
        setMarkerVisibility(markerList.get(0), false);
        setMarkerPosition(0, null, null);

        setStatus();
    }

    private void setStatus() {
        if (startStation != null && endStation != null) {
            status = FULL;

//            progressBar.setVisibility(View.VISIBLE);

//            currentRoute = routeController.getRouteNew(startStation, endStation);

            routes = routeController.getRoutes(startStation, endStation);

            currentRoute = routes[0];

            inflateRouteNew(currentRoute);
            runBottomSheet(null, routes);

//            if (currentRoute == null) {
//                Toast.makeText(getApplication().getBaseContext(), "결과를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
//                setMarkerDefault(ALL_MARKERS);
//            } else {
//                inflateRouteNew(currentRoute);
//                runBottomSheet(null, routes);
//            }
        } else {
            status = WAIT;
        }
    }


    private void inflateRouteNew(RouteNew route) {

        // log.d(TAG, "inflateRouteNew: route is null" );

        if (route == null) return;

        // log.d(TAG, "inflateRouteNew: under if " + route.getSections().size());

        if (routeMarkers == null) routeMarkers = new ArrayList<>();

        int count = 0;
        for ( List<Station> section : route.getSections() ) {
            for ( int i = 0 ; i < section.size(); i ++ ) {
                // log.d(TAG, "inflateRouteNew: inflating");
                ImageView marker = (ImageView) inflater.inflate(R.layout.content_main_route, null);
                if ( count == 0 ) {
                    marker.setImageResource( R.drawable.route_start_normal_marker);
                    marker.setId( 5000 + count );
                    marker.setAlpha( 1f );
                } else if ( i == 0 ){
                    marker.setImageResource( R.drawable.route_transfer_normal_marker);
                    marker.setId( 4000 + count );
                    marker.setAlpha( 1f );
                } else if (count == route.getTotalSize() - 1){
                    marker.setImageResource( R.drawable.route_end_normal_marker);
                    marker.setId( 5000 + count );
                    marker.setAlpha( 1f );
                } else if ( i != section.size() - 1 ) {
                    marker.setImageResource( R.drawable.blue_route_icon );
                    marker.setId( 3000 + count );
                    marker.setAlpha(0.7f);
                }
                routeMarkers.add(marker);
                count ++;
            }
        }

        for (int i = 3; i < 6; i++) {
            for ( ImageView mark : routeMarkers ) {
                if (mark.getId() / 1000 == i) {
                    // log.d(TAG, "inflateRouteNew: " + mark.getId());
                    passMarkerMother.addView(mark);
                    mark.setVisibility(View.VISIBLE);
                    // marker set Layout width, height using startMarker's LayoutParam
                    mark.setLayoutParams(markerList.get(0).getLayoutParams());
                }
            }
        }

        setRouteMarkerPosition();

        //진행중~!!!!
        // TrainsPerHour setting... and check null
        for ( Station station : route.getETSStation() ) {
            station.setTrainsPerHour( stationController.getTrainsPerHour(station, new GregorianCalendar()) );
        }
        new ServerConnectionSingle().getRouteCongestion(route.getETSStation(), this);
    }

    public void reInflateRouteMarker( List<Pair<Integer, Integer>> conList ) {

        setMarkerDefault(REINFLATE_MARKER_SERVER);

        if (routeMarkers == null) routeMarkers = new ArrayList<>();

        int count = 0;
        for ( List<Station> section : currentRoute.getSections() ) {
            for ( int i = 0 ; i < section.size(); i ++ ) {
                // log.d(TAG, "inflateRouteNew: inflating");
                ImageView marker = (ImageView) inflater.inflate(R.layout.content_main_route, null);
                if ( count == 0 ) {
//                    marker.setImageResource( R.drawable.route_start_normal_marker);
                    marker.setImageResource(getCongestionDrawble(section.get(i), conList, START_MARKER));
                    marker.setId( 5000 + count );
                    marker.setAlpha( 1f );
                } else if ( i == 0 ){
//                    marker.setImageResource( R.drawable.route_transfer_normal_marker);
                    marker.setImageResource(getCongestionDrawble(section.get(i), conList, TRANSFER_MARKER));
                    marker.setId( 4000 + count );
                    marker.setAlpha( 1f );
                } else if (count == currentRoute.getTotalSize() - 1){
//                    marker.setImageResource( R.drawable.route_end_normal_marker);
                    marker.setImageResource(getCongestionDrawble(section.get(i), conList, END_MARKER));
                    marker.setId( 5000 + count );
                    marker.setAlpha( 1f );
                } else if ( i != section.size() - 1 ) {
                    marker.setImageResource( R.drawable.blue_route_icon );
                    marker.setId( 3000 + count );
                    marker.setAlpha(0.7f);
                }
                routeMarkers.add(marker);
                count ++;
            }
        }

        for (int i = 3; i < 6; i++) {
            for ( ImageView mark : routeMarkers ) {
                if (mark.getId() / 1000 == i) {
                    // log.d(TAG, "inflateRouteNew: " + mark.getId());
                    passMarkerMother.addView(mark);
                    mark.setVisibility(View.VISIBLE);
                    // marker set Layout width, height using startMarker's LayoutParam
                    mark.setLayoutParams(markerList.get(0).getLayoutParams());
                }
            }
        }

        setRouteMarkerPosition();
        // log.d(TAG, "reInflateRouteMarker: " + getCurPage());
        mRoutePagerAdapter.reinflateRouteCongestion(getCurPage());

    }
    private int getCongestionDrawble (Station station, List<Pair<Integer, Integer>> conList, int etsStatus  ) {
        int conStatus = -1;
        for ( Pair<Integer, Integer> pair : conList  ) {
            if (pair.first == station.getStationID()) {
                conStatus = new ServerConnectionSingle().getRYGByCongestion(pair.second, station);
                break;
            }
        }


        if (conStatus == ServerConnectionSingle.CON_GREEN) {
            // log.d(TAG, "getCongestionDrawble: GREEN");
            if (etsStatus == START_MARKER) return R.drawable.route_start_green_marker;
            if (etsStatus == TRANSFER_MARKER) return R.drawable.route_transfer_green_marker;
            if (etsStatus == END_MARKER) return R.drawable.route_end_green_marker;
        } else if (conStatus == ServerConnectionSingle.CON_YELLOW) {
            // log.d(TAG, "getCongestionDrawble: CON_YELLOW");
            if (etsStatus == START_MARKER) return R.drawable.route_start_yellow_marker;
            if (etsStatus == TRANSFER_MARKER) return R.drawable.route_transfer_yellow_marker;
            if (etsStatus == END_MARKER) return R.drawable.route_end_yellow_marker;
        } else if (conStatus == ServerConnectionSingle.CON_RED) {
            // log.d(TAG, "getCongestionDrawble: CON_RED");
            if (etsStatus == START_MARKER) return R.drawable.route_start_red_marker;
            if (etsStatus == TRANSFER_MARKER) return R.drawable.route_transfer_red_marker;
            if (etsStatus == END_MARKER) return R.drawable.route_end_red_marker;
        } else {
            // log.d(TAG, "getCongestionDrawble: DEFAULT");
            if (etsStatus == START_MARKER) return R.drawable.route_start_normal_marker;
            if (etsStatus == TRANSFER_MARKER) return R.drawable.route_transfer_normal_marker;
            if (etsStatus == END_MARKER) return R.drawable.route_end_normal_marker;
        }
        return R.drawable.pass_marker;
    }


    /*
    getters
    */

    public RecyclerView getSearchListView() {
        return searchListView;
    }

    public void setSearchListView(RecyclerView searchListView) {
        this.searchListView = searchListView;
    }

    public BottomSheetLayout getRouteBottomSheet() {
        return routeBottomSheet;
    }

    public BottomSheetLayout getStationBottomSheet() {
        return stationBottomSheet;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        return expandableListAdapter;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public StationController getStationController() {
        return stationController;
    }

    public int getCurPage() {
        return curPage;
    }
    public void setCurPage(int page ) {
        this.curPage = page;
    }

    public TtfMapImageView getMapView() {
        return mapView;
    }

    public void setCurrentRoute(int curPage, int mode ) {

        this.curPage = curPage;

        setMarkerDefault(REINFLATE_MARKER);
//        getRouteBottomSheet().dismissSheet();

        switch ( mode ) {
            case SHORT_ROUTE:
                currentRoute = routes[0];
                break;
            case MIN_TRANSFER:
                currentRoute = routes[1];
                break;
            case CUSTOM_ROUTE:
                currentRoute = routes[2];
                break;
            default:
                currentRoute = routes[0];
                break;

        }

        inflateRouteNew(currentRoute);
        // 수정
//        runBottomSheet(null, routes);
    }

    public ExpandableListView getExpListView() {
        return expListView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // log.d(TAG, "onResume: ");

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (getDrawer().isDrawerOpen(GravityCompat.START)) {
            getDrawer().closeDrawer(GravityCompat.START);
            return;
        }
        if (stationBottomSheet.isSheetShowing()) {
            stationBottomSheet.dismissSheet();
            return;
        }
        if (routeBottomSheet.isSheetShowing()) {
            routeBottomSheet.dismissSheet();
            return;
        }
        if (searchListView != null && searchListView.getVisibility() == View.VISIBLE) {
            ((EditText)findViewById(interactionListener.getSearchTextContext())).setText("");
            searchListView.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
