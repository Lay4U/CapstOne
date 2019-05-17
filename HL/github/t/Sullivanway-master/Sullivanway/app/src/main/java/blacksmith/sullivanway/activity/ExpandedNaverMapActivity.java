package blacksmith.sullivanway.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.util.ArrayList;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.Elevator;

public class ExpandedNaverMapActivity extends NMapActivity {
    private static final String CLIENT_ID = "st0CXRNX5tTLT0YepCPH";// 애플리케이션 클라이언트 아이디 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_naver_map);

        // Get Intent
        Intent intent = getIntent();
        String lineNm = intent.getStringExtra("lineNm");
        String stnNm = intent.getStringExtra("stnNm");
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        ArrayList<Elevator> elevators = intent.getParcelableArrayListExtra("EvInfos");
        int bgResId = intent.getIntExtra("bgResId", -1);

        // AppBarLayout Title 설정
        TextView appBarTitleView = findViewById(R.id.appBarTitle);
        appBarTitleView.setText(String.format("%s %s", lineNm, stnNm));

        // NMapView 생성
        NMapView mapView = findViewById(R.id.mapView);
        mapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        setupMapView(mapView);

        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        // 확대, 축소
        mapView.setScalingFactor(1.5f, true);
        NMapController nMapController = mapView.getMapController();
        nMapController.setZoomEnabled(true);
        nMapController.setMapViewPanoramaMode(false);
        nMapController.setMapCenter(new NGeoPoint(latitude, longitude), 11);

        /*
        NMapProjection nMapProjection = mapView.getMapProjection();
        nMapProjection.fromPixels(37.588458, 127.006221);
        */

        // 지도 높이 설정
        ViewGroup.LayoutParams mapContainerParam = mapView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        mapContainerParam.height = (int) (height * 0.7);
        mapView.setLayoutParams(mapContainerParam);

        // 테두리
        if (bgResId != -1) {
            LinearLayout mapContent = findViewById(R.id.mapContent);
            mapContent.setBackground(getResources().getDrawable(bgResId, null));
            LinearLayout evContent = findViewById(R.id.evContent);
            evContent.setBackground(getResources().getDrawable(bgResId, null));
        }

        // 엘리베이터 정보 리스트뷰
        if (elevators != null && elevators.size() > 0) {
            ListView evListView = findViewById(R.id.evListView);
            evListView.setAdapter(new EvAdapter(this, R.layout.item_elevator, elevators));
            setListViewHeight(evListView);
        } else {
            TextView tmp = findViewById(R.id.tmp);
            tmp.setText(R.string.no_ev_item);
            tmp.setTextSize(18);
            tmp.setPadding(0, 15, 0, 0);
            tmp.setGravity(Gravity.CENTER);
        }
    }


    /* EvListView */
    void setListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        int height = 0;
        for (int position = 0; position < adapter.getCount(); position++) {
            View itemView = adapter.getView(position, null, listView);
            itemView.measure(0, 0);
            height += itemView.getMeasuredHeight();
        }

        int dividersHeight = listView.getDividerHeight() * adapter.getCount();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height + dividersHeight;
        listView.setLayoutParams(params);
    }

    private class EvAdapter extends BaseAdapter {
        private Context context;
        private int resId;
        private ArrayList<Elevator> elevators;

        EvAdapter(Context context, int resId, ArrayList<Elevator> elevators) {
            this.context = context;
            this.resId = resId;
            this.elevators = elevators;
        }

        @Override
        public int getCount() {
            return elevators.size();
        }

        @Override
        public Object getItem(int position) {
            return elevators.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resId, parent, false);
                holder = new Holder();
                holder.num = convertView.findViewById(R.id.num);
                holder.floor = convertView.findViewById(R.id.floor);
                holder.location = convertView.findViewById(R.id.location);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.num.setText(elevators.get(position).getNum());
            holder.floor.setText(elevators.get(position).getFloor());
            holder.location.setText(elevators.get(position).getLocation());

            return convertView;
        }

        private class Holder {
            TextView num, floor, location;
        }

    }

}
