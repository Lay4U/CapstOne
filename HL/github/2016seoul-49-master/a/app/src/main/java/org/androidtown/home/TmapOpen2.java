package org.androidtown.home;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

/**
 * Created by 민섭 on 2016-08-14.
 */
public class TmapOpen2 extends Activity {                                        //UseThread 함수를 호출한다면 가장 가까운 역까지의 경로 확인 가능.
    // Activity Start
    TMapView tmapview;
    RelativeLayout relativeLayout;
    double myX=Double.parseDouble(MainService.Longitude);                      //내 위치 x좌표
    double myY = Double.parseDouble(MainService.Latitude);                     //내 위치 y좌표
    double subwayX = Double.parseDouble(MainService.nearSubwayCoordX);         //가장 가까운 역 x좌표
    double subwayY = Double.parseDouble(MainService.nearSubwayCoordY);         //가장 가까운 역 y좌표
    String TmapApi = "6180bf43-e723-3211-991e-e116b8cc9732";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relativeLayout = new RelativeLayout(this);

        UIset();

        TMapData tmapdata = new TMapData();
        TMapPoint startpoint = new TMapPoint(myY, myX);
        TMapPoint endpoint = new TMapPoint(subwayY, subwayX);

        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startpoint, endpoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(9);
                tmapview.addTMapPath(tMapPolyLine);
            }


        });



        relativeLayout.addView(tmapview);
        setContentView(relativeLayout);
    }

    public void UIset(){
        tmapview = new TMapView(this);
        tmapview.setSKPMapApiKey(TmapApi);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(16);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setCenterPoint(myX, myY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
