package blacksmith.sullivanway.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.util.ArrayList;
import java.util.Objects;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.Elevator;

/**
 * Fragment LifeCycle
 * onAttach -> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume
 * running
 * onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
 *
 * onCreateView <- onDestroyView
 * onStart <- onStop
 */
public class NaverMapFragment extends Fragment {
    private static final String CLIENT_ID = "st0CXRNX5tTLT0YepCPH";// 애플리케이션 클라이언트 아이디 값

    private double latitude, longitude;
    private ArrayList<Elevator> elevators;

    /* View */
    private NMapContext mMapContext;
    private NMapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 지도 실행 컨텍스트
        mMapContext = new NMapContext(Objects.requireNonNull(getActivity()));
        mMapContext.onCreate();

        Bundle bundle = getArguments();
        if (bundle != null) {
            // 위도, 경도
            latitude = bundle.getDouble("latitude");
            longitude = bundle.getDouble("longitude");

            // 엘리베이터
            elevators = bundle.getParcelableArrayList("EvInfos");
        } else {
            elevators = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_naver_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if (view != null) {
            mapView = view.findViewById(R.id.mapView);
            mapView.setClientId(CLIENT_ID);
            mMapContext.setupMapView(mapView);
            mapView.setScalingFactor(1.5f, true);
            setMapCenter();

            // 엘리베이터 정보 리스트뷰
            if (elevators != null && elevators.size() > 0) {
                ListView evListView = view.findViewById(R.id.evListView);
                evListView.setAdapter(new EvAdapter(getActivity(), R.layout.item_elevator, elevators));
                setListViewHeight(evListView);
            } else {
                TextView tmp = view.findViewById(R.id.tmp);
                tmp.setText(R.string.no_ev_item);
                tmp.setTextSize(18);
                tmp.setPadding(0, 15, 0, 0);
                tmp.setGravity(Gravity.CENTER);
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }

    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }

    public void setMapCenter() {
        if (mapView != null) {
            // 확대, 축소
            NMapController mMapController = mapView.getMapController();
            mMapController.setZoomEnabled(true);
            mMapController.setMapViewPanoramaMode(false);
            mMapController.setMapCenter(new NGeoPoint(latitude, longitude), 11);

            /*
            NMapProjection nMapProjection = mapView.getMapProjection();
            nMapProjection.fromPixels(37.588458, 127.006221);
            */
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
