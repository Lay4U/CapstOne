package com.estsoft.r_subway_android.UI.RouteInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.estsoft.r_subway_android.Repository.StationRepository.Route;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-04.
 */
public class RoutePagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "RoutePagerAdapter";
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"최단경로", "최소환승", "Custom",};
    private RouteNew[] route;
//    private List<RouteInfoFragment> routeInfoFragments = new ArrayList<>();
    private RouteInfoFragment[] routeInfoFragments = new RouteInfoFragment[PAGE_COUNT];
    public RoutePagerAdapter(FragmentManager fm, RouteNew[] route) {
        super(fm);
        this.route = route;

    }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        RouteInfoFragment rf = RouteInfoFragment.newInstance(position, route);
//        Log.d(TAG, "getItem: position - " + position );
        routeInfoFragments[position] = rf;
        return rf;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void reinflateRouteCongestion( int position ) {
//        Log.d(TAG, "reinflateRouteCongestion: " + position);
//        routeInfoFragments.get(position).reInflateRouteConInfo();
        routeInfoFragments[position].reInflateRouteConInfo();
    }
}
