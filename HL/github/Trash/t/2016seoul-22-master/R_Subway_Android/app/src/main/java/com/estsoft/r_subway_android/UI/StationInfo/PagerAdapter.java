package com.estsoft.r_subway_android.UI.StationInfo;

import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-04.
 */
public class PagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    int PAGE_COUNT;
    private int[] imageResId = {R.drawable.lane1,R.drawable.lane2,R.drawable.lane3,R.drawable.lane4,R.drawable.lane5,R.drawable.lane6,R.drawable.lane7,R.drawable.lane8,R.drawable.lane9,R.drawable.lane21,R.drawable.lane100,R.drawable.lane101,R.drawable.lane104,R.drawable.lane107, R.drawable.lane108,R.drawable.lane109,R.drawable.lane110,R.drawable.lane111};
    private static List<Station> exStations = null;

    public PagerAdapter(FragmentManager fm, List<Station> exStations) {
        super(fm);
        this.exStations = exStations;

    }

    @Override
    public int getCount() {

        //page 개수 넘기기_ 환승역 line수+1(자기자신)+(다음정류장수-1) 다음정류장이 2개인곳, 성수
        PAGE_COUNT = exStations.size();

        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return StationInfoFragment.newInstance(position, exStations);
    }

    @Override
    public int getPageIconResId(int position) {
        switch (exStations.get(position).getLaneType()){

            case 1: return imageResId[0];
            case 2: return imageResId[1];
            case 3: return imageResId[2];
            case 4: return imageResId[3];
            case 5: return imageResId[4];
            case 6: return imageResId[5];
            case 7: return imageResId[6];
            case 8: return imageResId[7];
            case 9: return imageResId[8];
            case 21: return imageResId[9];
            case 100: return imageResId[10];
            case 101: return imageResId[11];
            case 104: return imageResId[12];
            case 107: return imageResId[13];
            case 108: return imageResId[14];
            case 109: return imageResId[15];
            case 110: return imageResId[16];
            case 111: return imageResId[17];
            default: return imageResId[0];

        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}


