package com.estsoft.r_subway_android.utility;


import android.support.v4.util.Pair;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.util.ArrayList;


/**
 * Created by gangGongUi on 2016. 7. 19..
 */
public class ShortestPath
{
    static
    {
        System.loadLibrary("BstarShortestPath");
    }

    public static native void setLineRange(boolean[] isExcept);

    /**
     * TODO Return the shortest path as an integer array receives input the departure station and arrival station.
     * @param adj Subway station linked list representation.
     * @param start Departure station.
     * @param end Arrival station.
     * @return int[] Shortest route expressed in index.
     * */
    public static native int[] getShortestPathByIntArray(ArrayList<Pair<Station, Integer>> adj[], Station start, Station end);

    /**
     * TODO Return the minimum transfer path as an integer array receives input the departure station and arrival station.
     * @param adj Subway station linked list representation.
     * @param start Departure station.
     * @param end Arrival station.
     * @return int[] minimum transfer route expressed in index.
     * */
    public static native int[] getMinimumTransferPathByIntArray(ArrayList<Pair<Station, Integer>> adj[],  Station start, Station end);

    /**
     * TODO  Return the shortest route, excluding the specific Route input the departure station and arrival station and laneType.
     * @param adj Subway station linked list representation.
     * @param start Departure station.
     * @param end Arrival station.
     * @param isExcept Whether to exclude.
     * @return int[] Specific Route expressed in index. If the path does not exist, the size returns an array of 0
     * */
//    public static native int[] getParticularRoutePathByIntArray(ArrayList<Pair<Station, Integer>> adj[], Station start, Station end, boolean[] isExcept);

}
