package blacksmith.sullivanway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.Congestion;
import blacksmith.sullivanway.database.Elevator;
import blacksmith.sullivanway.database.Station;
import blacksmith.sullivanway.utils.SubwayLine;

public class StnInfoFragment extends Fragment {
    private Station stn;
    private Congestion congestion;
    private ArrayList<Elevator> elevators;
    private int bgResId;

    /* View */
    private NaverMapFragment mapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            stn = bundle.getParcelable("Station"); //역 정보
            congestion = bundle.getParcelable("Congestion"); //혼잡도
            elevators = bundle.getParcelableArrayList("EvInfos"); //엘리베이터
            bgResId = SubwayLine.getBgResId(stn.getLineNm());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stn_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* View */
        View view = getView();
        if (view != null) {
            // 테두리 설정
            TableLayout basicContent = view.findViewById(R.id.basicContent);
            basicContent.setBackground(getResources().getDrawable(bgResId, null));
            LinearLayout mapContent = view.findViewById(R.id.mapContent);
            mapContent.setBackground(getResources().getDrawable(bgResId, null));
            RelativeLayout etcContent = view.findViewById(R.id.etcContent);
            etcContent.setBackground(getResources().getDrawable(bgResId, null));

            // 기본정보
            TextView toilet = view.findViewById(R.id.toilet);
            TextView door = view.findViewById(R.id.door);
            TextView contact = view.findViewById(R.id.contact);
            TextView elevator = view.findViewById(R.id.elevator);
            TextView escalator = view.findViewById(R.id.escalator);
            TextView wheelChairLift = view.findViewById(R.id.wheelChairLift);

            toilet.setText(String.format("%s", stn.getToilet()));
            door.setText(String.format("%s", stn.getDoor()));
            contact.setText(String.format("%s", stn.getContact()));
            elevator.setText(String.format("%s", stn.getElevator()));
            escalator.setText(String.format("%s", stn.getEscalator()));
            wheelChairLift.setText(String.format("%s", stn.getWheelLift()));

            // 주변지도
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            mapFragment = new NaverMapFragment();
            Bundle mapBundle = new Bundle();
            mapBundle.putDouble("latitude", stn.getWgsy());
            mapBundle.putDouble("longitude", stn.getWgsx());
            mapBundle.putParcelableArrayList("EvInfos", elevators);
            mapFragment.setArguments(mapBundle);
            ft.replace(R.id.mapContainer, mapFragment);
            ft.commit();

            FrameLayout mapContainer = view.findViewById(R.id.mapContainer);
            mapContainer.setClickable(true);
            mapContainer.setOnClickListener(new OnMapClick());
            mapContainer.setLongClickable(true);

            // 혼잡도
            TextView congstnBtn = view.findViewById(R.id.cngstnBtn);
            congstnBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CongestionGraphActivity.class);
                intent.putExtra("lineNm", stn.getLineNm());
                intent.putExtra("stnNm", stn.getStnNm());
                intent.putExtra("bgResId", bgResId);
                intent.putExtra("Congestion", congestion);
                if (getActivity() != null)
                    getActivity().startActivity(intent);
            });

            // 시간표
            TextView timeTableBtn = view.findViewById(R.id.timetableBtn);
            timeTableBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), TimeTablePagerActivity.class);
                intent.putExtra("lineNm", stn.getLineNm());
                intent.putExtra("stnNm", stn.getStnNm());
                intent.putExtra("bgResId", bgResId);
                if (getActivity() != null)
                    getActivity().startActivity(intent);
            });
        }

    }

    public NaverMapFragment getMapFragment() {
        return mapFragment;
    }

    private class OnMapClick implements ViewGroup.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ExpandedNaverMapActivity.class);
            intent.putExtra("lineNm", stn.getLineNm());
            intent.putExtra("stnNm", stn.getStnNm());
            intent.putExtra("latitude", stn.getWgsy());
            intent.putExtra("longitude", stn.getWgsx());
            intent.putParcelableArrayListExtra("EvInfos", elevators);
            intent.putExtra("bgResId", bgResId);
            startActivity(intent);
        }
    }

}
