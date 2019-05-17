package com.estsoft.r_subway_android.listener;

import com.estsoft.r_subway_android.Repository.StationRepository.Station;

/**
 * Created by estsoft on 2016-08-16.
 */
public interface ServerConnectionListener {

    void getStationStatus(Station station);

}
