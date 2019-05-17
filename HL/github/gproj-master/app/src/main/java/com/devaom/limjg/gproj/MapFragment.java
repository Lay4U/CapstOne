package com.devaom.limjg.gproj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.NMapViewerResourceProvider;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.NMapPOIflagType;

public class MapFragment extends Fragment {
    private NMapContext mMapContext;
    int mapX, mapY;
    NMapView mapView;
    NMapController controller;
    NGeoPoint point;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment1, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (NMapView)getView().findViewById(R.id.mapView);
        mapView.setClientId(getString(R.string.client_id));// 클라이언트 아이디 설정
        mMapContext.setupMapView(mapView);
        controller = mapView.getMapController();
        //controller.animateTo( new NGeoPoint(126.31,37.1666091) ); // 해당 좌표로 NMapView 객체 중심점 이동

        Bundle bundle = getArguments();

        GeoTransPoint katechPt = new GeoTransPoint( bundle.getInt("mapX"), bundle.getInt("mapY") );
        GeoTransPoint geoPt = GeoTrans.convert(GeoTrans.KATEC,GeoTrans.GEO,katechPt);
        NGeoPoint nGeoPoint = new NGeoPoint( geoPt.getX(), geoPt.getY() );
        controller.animateTo( nGeoPoint );
        //controller.animateTo( new NGeoPoint( NGeoPoint.toLatitude(bundle.getInt("mapX")), NGeoPoint.toLongitude(bundle.getInt("mapY")) ) );

        NMapViewerResourceProvider nMapViewerResourceProvider = new NMapViewerResourceProvider(super.getContext());
        NMapOverlayManager nMapOverlayManager = new NMapOverlayManager(super.getContext(), mapView, nMapViewerResourceProvider);

        NMapPOIdata poiData = new NMapPOIdata(2, nMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem( geoPt.getX(), geoPt.getY(), bundle.getString("title"), NMapPOIflagType.PIN, 0 ); // 258은 NMapflagType.PIN // 두번째 파라미터는 Marker의 텍스트임.
        poiData.endPOIdata();
        NMapPOIdataOverlay poiDataOverlay = nMapOverlayManager.createPOIdataOverlay(poiData, null);

        mapView.setClickable(true); // NMapView 객체의 터치 활성화(중심좌표 이동 및 핀치줌)
        mapView.setScalingFactor(2.5f); // NMapView 객체의 지도 텍스트 사이즈 조절

    }
    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() { // 잠시 다른 어플로 이동했다가 다시 돌아오면 이 콜백메소드가 호출됨.
        mMapContext.onStop();
        //Bundle bundle = getArguments();
        //mapX = bundle.getInt("mapX");
        //mapY = bundle.getInt("mapY");
        //point.set(mapX,mapY);
        //controller = mapView.getMapController();
        //controller.animateTo(point);
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();

        super.onDestroy();
    }
}