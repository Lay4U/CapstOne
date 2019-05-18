package com.estsoft.r_subway_android.UI.MapTouchView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.estsoft.r_subway_android.Controller.StationController;
import com.estsoft.r_subway_android.Parser.CircleTag;
import com.estsoft.r_subway_android.Parser.TtfXmlParser;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;
import com.estsoft.r_subway_android.listener.TtfMapImageViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-24.
 */
public class TtfMapImageView extends MapTouchImageView {

    private static final String TAG = "TtfMapImageView";
    private static final int TOUCH_RADIUS = 50;

    // smaller number, bigger marker.
    private static final int MARKER_SCALE_RATIO = 20;

    private static final int ALL_MARKERS = 0;
    private static final int ACTI_MARKER = 1;


    // Listeners...
    protected TtfMapImageViewListener ttfMapImageViewListener;

    private TtfXmlParser parser = null;
    private List<PointF> savedStationPointList = null;
    private List<SemiStation> semiStationList = null;

    private int currentImageWidth = 0;
    private int currentImageHeight = 0;
    private int currentImageX = 0;
    private int currentImageY = 0;
    private int svgWidth = 0;
    private int svgHeight = 0;

    private PointF mapCenter = null;



    public void setTtfMapImageViewListener(TtfMapImageViewListener ttfMapImageViewListener) {
        this.ttfMapImageViewListener = ttfMapImageViewListener;
    }

    //context, attr, defStyleAttr 이 뭔지 궁금함
    public TtfMapImageView(Context context, AttributeSet attrs, int defStyleAttr)  throws Exception  {

        super(context, attrs, defStyleAttr);
        setScaleType( ScaleType.MATRIX );

        parser = new TtfXmlParser( getResources().openRawResource( R.raw.linemap_naver ) );
        semiStationList = new ArrayList<>();
        for ( CircleTag circle : parser.getCircleList() ) {
            SemiStation semiStation = new SemiStation(
                    circle.getId(),
                    circle.getLaneNumber(),
                    new PointF( circle.getPositionX(), circle.getPositionY() ),
                    circle.getOtherFactor()
            );

            saveSemiStation(semiStation);
        }


        saveStationPosition();

        //최대 배율 설정
        setMaxMagnification( 20 );
        svgWidth = parser.getSvgWidth();
        svgHeight = parser.getSvgHeight();
    }

    //중복 역 제거 & 호선 입력
    private void saveSemiStation( SemiStation semiStation ) {
        if (semiStation.getLaneNumbers() == null){
            semiStation.setLaneNumbers(new ArrayList<Integer>());
        }
        semiStation.getLaneNumbers().add( semiStation.getLaneNumber() );
        for ( SemiStation ss : semiStationList ) {
            if (ss.getName().equals( semiStation.getName() )) {
                ss.getLaneNumbers().add( semiStation.getLaneNumber() );
//                Log.d(TAG, "saveSemiStation: " + semiStation.getLaneNumber());
                return;
            }
        }

        semiStation.sortLaneNumbers();
        semiStationList.add(semiStation);
    }

    public TtfMapImageView(Context context, AttributeSet attrs)  throws Exception  {
        this(context, attrs, 0);
    }

    public TtfMapImageView(Context context) throws Exception {
        this( context, null );
    }

    private void saveStationPosition() {
        savedStationPointList = new ArrayList<>();
        for ( SemiStation semiStation : semiStationList) {
            PointF savePointF = new PointF();
            savePointF.set( semiStation.getPosition() );
            savedStationPointList.add(savePointF);
        }
    }

    @Override
    public void beforeTouch(int mode, Matrix matrix, MotionEvent event) {
        super.beforeTouch(mode, matrix, event);

//        Log.d(TAG, event.getX() + " : " + event.getY() );
//        Log.d(TAG, d.getIntrinsicWidth() + " : " + d.getIntrinsicHeight() );
//        Log.d(TAG, getWidth() + " : " + getHeight());
//        Log.d(TAG, matrix.toString());
//        Log.d(TAG, stations.toString());
//        Log.d(TAG, "Touch : " + event.getX() + " / " + event.getY() );
    }

    @Override
    public void afterTouch(int mode, Matrix matrix, MotionEvent event) {
        super.afterTouch(mode, matrix, event);

//        Log.d(TAG, getMovedImageX() + " / " + getMovedImageY());
//        Log.d(TAG, getScaledImageWidth() + " / " + getScaledImageHeight());

        if ( select == DONE ) {
            checkSelectStation(semiStationList, event );
        }
        if ( mode != NONE && select != DONE) {
            setStationsPosition(matrix);
            ttfMapImageViewListener.applyMapScaleChange();
//            Log.d(TAG, "afterTouch: " + semiStationList.get(0).getPosition().toString() );
//            Log.d(TAG, "afterTouch: " + System.identityHashCode( semiStationList.get(0).getPosition() ));
        }
    }

    private void setStationsPosition( Matrix matrix ) {

        currentImageWidth = getScaledImageWidth();
        currentImageHeight = getScaledImageHeight();
        currentImageX = getMovedImageX();
        currentImageY = getMovedImageY();

//        Log.e(TAG, currentImageX + " / " + currentImageY);

        float movedX = 0;
        float movedY = 0;
        for (int i = 0; i < semiStationList.size() ; i ++ ) {
            SemiStation semiStation = semiStationList.get(i);
            PointF originStationPoint = savedStationPointList.get(i);
            movedX = ((float) currentImageWidth / svgWidth) * originStationPoint.x + currentImageX;
            movedY = ((float) currentImageHeight / svgHeight) * originStationPoint.y + currentImageY;

//            semiStation.setPosition( new PointF(movedX, movedY) );
            semiStation.getPosition().set( movedX, movedY );
//            Log.d(TAG, "setStationsPosition: origin - " + originStationPoint.x + " / " + originStationPoint.y);

//            Log.d(TAG, "Y : " + value[5]);
//            Log.d(TAG, "setStationsPosition : " + semiStation.getPosition().toString() );
//            Log.d(TAG, "saved : " + originStationPoint.toString());
        }
    }

    private SemiStation checkSelectStation(List<SemiStation> semiStationList, MotionEvent event ) {

        for (SemiStation semiStation  : semiStationList) {
            if ( semiStation.getPosition().x + TOUCH_RADIUS > event.getX() &&
                    semiStation.getPosition().x - TOUCH_RADIUS < event.getX() &&
                    semiStation.getPosition().y + TOUCH_RADIUS > event.getY() &&
                    semiStation.getPosition().y - TOUCH_RADIUS < event.getY() ) {

                touchedStationAction( semiStation );
                return semiStation;
            }
        }
        unTouchedStationAction();
        return null;
    }

    private void unTouchedStationAction() {
        ttfMapImageViewListener.setMarkerDefault( ACTI_MARKER );
    }


    private void touchedStationAction( SemiStation semiStation ) {

        ttfMapImageViewListener.setActiveStation( semiStation );

    }

    public float getMarkerRatio() {
        return (getWidth() + getHeight()) / MARKER_SCALE_RATIO;
    }

    public PointF getStationPointByName( String name ) {
        for (SemiStation sst : semiStationList ) {
            if (sst.getName().equals(name))
                return sst.getPosition();
        }
        return null;
    }

    public PointF getStationPoint ( String stationId ) {
        for ( SemiStation semiStation : semiStationList ) {
            if (semiStation.getId().equals( stationId ))
                return semiStation.getPosition();
        }
        return null;
//        return new PointF(0,0);
    }

    public void setSemiStationLaneNumber( StationController sc ){
        sc.setSemiStationLaneNumber( semiStationList );
    }

    public void moveToMapCenter( PointF point ) {
        if (mapCenter == null) {
            mapCenter = new PointF( getViewWidth() / 2 , getViewHeight() / 2);
        }
//        Log.d(TAG, "moveToMapCenter: " + mapCenter.toString());
//        Log.d(TAG, "moveToMapCenter: " + point );
        float moveX = (mapCenter.x - point.x) / 10;
        float moveY = (mapCenter.y - point.y) / 10;
//        Log.d(TAG, "moveToMapCenter: " + moveX + " / " + moveY);


        for ( int i = 0; i < 10; i ++ ) {
            getUsingMatrix().postTranslate( moveX, moveY );
            matrixTurning( getUsingMatrix(), this );
            this.setImageMatrix( getUsingMatrix() );
            setStationsPosition( getUsingMatrix() );
            ttfMapImageViewListener.applyMapScaleChange();

        }
    }


    @Override
    public void init() {
        super.init();
        ttfMapImageViewListener.setMarkerDefault( ALL_MARKERS );
    }

    public List<SemiStation> getSemiStationList() {
        return semiStationList;
    }
}
