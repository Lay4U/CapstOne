package com.estsoft.r_subway_android.listener;

import android.graphics.Matrix;
import android.graphics.PointF;

import com.estsoft.r_subway_android.Repository.StationRepository.SemiStation;


/**
 * Created by estsoft on 2016-06-28.
 */
public interface TtfMapImageViewListener {
    void setMarkerDefault(int markerMode);
    void setActiveStation(SemiStation semiStation);
    void applyMapScaleChange(  );
}

