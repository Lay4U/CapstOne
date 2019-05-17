package blacksmith.sullivanway.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.Congestion;
import blacksmith.sullivanway.database.Elevator;
import blacksmith.sullivanway.database.Station;
import blacksmith.sullivanway.utils.SubwayLine;
import blacksmith.sullivanway.database.MyDBOpenHelper;

public class StnInfoPagerActivity extends AppCompatActivity {
    private MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this);
    private Vector<Station> stations = new Vector<>(); //stnNm역에 대한 정보가 호선별로 저장된다

    /* View */
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stn_info_pager);

        TextView stnNmTextView = findViewById(R.id.stnNm);
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        SQLiteDatabase db = myDBOpenHelper.getReadableDatabase();
        String stnNm = getIntent().getStringExtra("stnNm");
        @SuppressWarnings("unchecked")
        ArrayList<String> lines = (ArrayList<String>) getIntent().getSerializableExtra("lines");
        //lines에는 stnNm역에서 탑승가능한 모든 호선이 저장되어 있다.
        //SELECT문에서 lines.get(0), lines.get(1) 등 으로 각 호선의 stnNm역을 검색할 수 있다

        for (String lineNm : lines) {
            String sql = String.format("SELECT id, km, wgsx, wgsy, door, contact, toilet, elevator, escalator, wheelLift, pointx, pointy FROM %s WHERE lineNm='%s' AND stnNm='%s'", Station.TB_NAME, lineNm, stnNm);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                double km = cursor.getDouble(1);
                double wgsx = cursor.getDouble(2);
                double wgsy = cursor.getDouble(3);
                String door = cursor.getString(4);
                String contact = cursor.getString(5);
                String toilet = cursor.getString(6);
                String elevator = cursor.getString(7);
                String escalator = cursor.getString(8);
                String wheelLift = cursor.getString(9);
                int pointx = cursor.getInt(10);
                int pointy = cursor.getInt(11);
                stations.add(new Station(lineNm, id, stnNm, km, wgsx, wgsy, door, contact, toilet, elevator, escalator, wheelLift, pointx, pointy));
            }
            cursor.close();
        }

        /* View 설정 */
        // 역이름
        stnNmTextView.setText(stnNm);

        // Tab으로 사용할 Fragment 생성하여 Adapter에 추가
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        for (Station mStn : stations) {
            // 혼잡도 정보
            Congestion congestion = Congestion.createCongestionInfo(mStn.getLineNm(), mStn.getStnNm());

            // 엘리베이터 정보
            ArrayList<Elevator> elevators = new ArrayList<>();
            String sql = String.format("SELECT num, floor, location FROM %s WHERE lineNm ='%s' AND stnNm='%s'", Elevator.TB_NAME, mStn.getLineNm(), mStn.getStnNm());
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String num = cursor.getString(0);
                String floor = cursor.getString(1);
                String location = cursor.getString(2);
                elevators.add(new Elevator(num, floor, location));
            }
            cursor.close();

            // Bundle에 역정보 추가
            StnInfoFragment fragment = new StnInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("Station", mStn);
            bundle.putParcelable("Congestion", congestion);
            bundle.putParcelableArrayList("EvInfos", elevators);
            fragment.setArguments(bundle);

            // Adapter에 Fragment, Tab Icon(호선그림) 추가
            adapter.add(fragment);
        }

        // ViewPager
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(stations.size() - 1);
        viewPager.addOnPageChangeListener(new OnStnInfoPageChange());

        // ViewPager, TabLayout 연결
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < stations.size(); i++) { //Tab icon 설정
            int resId = SubwayLine.getResId(stations.get(i).getLineNm());
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setIcon(resId);
        }

    }


    // ViewPager를 위한 Adapter 클래스
    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<StnInfoFragment> fragments = new ArrayList<>();

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void add(StnInfoFragment fragment) {
            fragments.add(fragment);
        }
    }

    private class OnStnInfoPageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            MyPagerAdapter adapter = (MyPagerAdapter) viewPager.getAdapter();
            if (adapter != null) {
                StnInfoFragment fragment = (StnInfoFragment) adapter.getItem(position);
                NaverMapFragment mapFragment = fragment.getMapFragment();
                mapFragment.setMapCenter();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
