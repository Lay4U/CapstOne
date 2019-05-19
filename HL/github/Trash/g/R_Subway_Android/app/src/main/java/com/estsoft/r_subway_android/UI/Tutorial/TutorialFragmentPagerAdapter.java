package com.estsoft.r_subway_android.UI.Tutorial;


import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 2016-06-29.
 */
public class TutorialFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 5;

    public TutorialFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return TutorialFragment.newInstance(position + 1);
    }

}
