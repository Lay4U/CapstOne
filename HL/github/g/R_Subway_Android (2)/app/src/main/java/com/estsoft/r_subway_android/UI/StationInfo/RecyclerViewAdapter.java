package com.estsoft.r_subway_android.UI.StationInfo;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.Crawling.InternetManager;
import com.estsoft.r_subway_android.Crawling.ServerConnection;
import com.estsoft.r_subway_android.Crawling.ServerConnectionSingle;
import com.estsoft.r_subway_android.Parser.JSONTimetableParser;
import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.RouteNew;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;
import com.estsoft.r_subway_android.listener.ServerConnectionListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016-07-04.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private int expandedPosition = -1;
    private final FragmentActivity mActivity;
    private List<String> StationInfo;
    private static List<Station> stations = null;
    OnItemClickListener mItemClickListener;
    View.OnClickListener mClickListener;
    private int page;    //pager page ; page에 맞게 수정할 예정

    //인규 - SERVER CONNECTION
    private ViewHolder congestionHolder = null;
    // congestionStatus : 예상인원
    public void setStationStatus(int internetStatus, int accidentStatus, int congestionStatus, int congestionColor,Station station) {
        String accidentMsg = "";
        String congestionMsg = "";
        String congestionPercent = "혼잡도 정보를 받아오고 있습니다.";
        String congestionNum = "승하차 인원: 약 ";
        if ( internetStatus == ServerConnectionSingle.INTERNET_GOOD ) {

            if ( accidentStatus == ServerConnectionSingle.SERVER_CONNECTION_FAILED ) {
                accidentMsg = "사고정보 서버와 통신이 원활하지 않습니다.";
            } else if ( accidentStatus == ServerConnectionSingle.ACCIDENT_TRUE ) {
                accidentMsg = "현재 역에 사고 발생함. 지연 가능성 있습니다";
            } else {
                accidentMsg = "현재 역에 특별한 상황이 없습니다.";
            }

            if ( congestionStatus == ServerConnectionSingle.NONE_EXIST_STATION ) {
                congestionMsg = "지원하지 않는 역입니다.";
                congestionPercent = "";
                congestionHolder.congestionPercent.setVisibility(View.GONE);
                congestionHolder.congestionNum.setVisibility(View.GONE);
            } else {
//                int movePerson = congestion/(mStation.getTrainsPerHour()*10*4);
                // red : 12
//                Log.d(TAG,"congestion:"+congestionStatus+"stationgetTrain"+station.getTrainsPerHour());
                congestionPercent = (int)(congestionStatus/((double)station.getTrainsPerHour()*10*4*12)*100)+"%";
                congestionMsg = getCongestionColor(congestionColor);
                congestionNum += congestionStatus+" 명 (1시간 기준)";
                congestionHolder.congestionNum.setText(congestionNum);
                congestionHolder.congestionPercent.setVisibility(View.VISIBLE);
                congestionHolder.congestionNum.setVisibility(View.VISIBLE);

            }

        } else {
            accidentMsg = "네트워크가 활성화 되지 않았습니다.";
        }

//        if (congestionHolder == null) Log.d(TAG, "setStationStatus: NULL!!!!!!!!!!!!!!!!!!!");

        congestionHolder.crawling.setText(accidentMsg);
        congestionHolder.crawling.setTextColor(Color.BLACK);


        congestionHolder.congestion.setText("혼잡도: "+congestionMsg);
        congestionHolder.congestionPercent.setTextColor(Color.BLACK);
        congestionHolder.congestionNum.setTextColor(Color.BLACK);


        congestionHolder.congestionPercent.setText(congestionPercent);
        if(congestionMsg.equals("혼잡")) {congestionHolder.congestionPercent.setTextColor(Color.RED);}
        else if(congestionMsg.equals("보통")) {congestionHolder.congestionPercent.setTextColor(Color.rgb(253,219,86));}
        else if(congestionMsg.equals("원활")) {congestionHolder.congestionPercent.setTextColor(Color.GREEN);}
        else {congestionHolder.congestionPercent.setTextColor(Color.BLACK);}


    }
    private String getCongestionColor( int congestionColor ) {
        if (congestionColor == ServerConnectionSingle.CON_RED) return "혼잡";
        if (congestionColor == ServerConnectionSingle.CON_YELLOW) return "보통";
        if (congestionColor == ServerConnectionSingle.CON_GREEN) return "원활";
        return "NONE";
    }


    public RecyclerViewAdapter(FragmentActivity mActivity, List<Station> stations1, int page) {
        this.mActivity = mActivity;
        stations = stations1;
        this.page = page;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.statioin_info_item, parent, false);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                //이전역
                if(stations.get(page).getPrevStations().size() > 0) holder.preStation.setText("" + stations.get(page).getPrevStations().get(0).getStationName());
                else holder.preStation.setText("");
                    holder.preTime1.setText(stations.get(page).getTimeStringList().get(0));
                    holder.preTime2.setText(stations.get(page).getTimeStringList().get(1));
                // 현재역
                holder.curStation.setText("" + stations.get(page).getStationName());

                // 다음역
                if(stations.get(page).getNextStations().size() > 0)  holder.nextStation.setText("" + stations.get(page).getNextStations().get(0).getStationName());
                else holder.nextStation.setText("");
                    //prev : 0 , 1 next : 2, 3
                    holder.nextTime1.setText(stations.get(page).getTimeStringList().get(2));
                    holder.nextTime2.setText(stations.get(page).getTimeStringList().get(3));

                holder.curInfo.setVisibility(View.VISIBLE);
                holder.infoName.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.useInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);
                break;


            case 1:

                holder.infoName.setText("시간표");
                holder.goToTimetable.setVisibility(View.VISIBLE);

                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);
                break;

            case 2:

                holder.infoName.setText("현재 역 상황");
                holder.crawling.setText("정보를 받아오는 중입니다.");
                // Automatically call setStationStatus

                holder.stationCongestion.setVisibility(View.VISIBLE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);

                //인규 - AsyncTask 용
                congestionHolder = holder;

                new ServerConnectionSingle().setStationInfo(stations.get(page), this);

                // getting Server AccidentInfo

                break;


            case 3:

                holder.infoName.setText("역 기본 정보");

                switch (stations.get(page).getPlatform()) {
                    case 1:
                        holder.Platform.setText("플랫폼 위치: 중앙");
                        break;
                    case 2:
                        holder.Platform.setText("플랫폼 위치: 양쪽");
                        break;
                    case 3:
                        holder.Platform.setText("플랫폼 위치: 복선(국철)");
                        break;
                    case 4:
                        holder.Platform.setText("플랫폼 위치: 일방향");
                        break;
                    default:
                        holder.Platform.setText("플랫폼 위치: 기타");
                        break;
                }
                switch (stations.get(page).getOffDoor()) {
                    case 1:
                        holder.OffDoor.setText("내리는 문: 오른쪽");
                        break;
                    case 2:
                        holder.OffDoor.setText("내리는 문: 양쪽");
                        break;
                    default:
                        holder.OffDoor.setText("내리는 문: 왼쪽");
                        break;
                }


                if (stations.get(page).getCrossOver() == 0) {
                    holder.CrossOver.setText("반대편 횡단: 불가능");

                } else {
                    holder.CrossOver.setText("반대편 횡단: 가능");

                }

                holder.stationDefaultInfo.setVisibility(View.VISIBLE);
                holder.useInfo.setVisibility(View.GONE);
                holder.curInfo.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                break;

            case 4:

                holder.infoName.setText("역내 시설정보");

                switch (stations.get(page).getRestroom()) {
                    case 1:
                        holder.Restroom.setText("화장실: 안쪽");
                        break;
                    case 2:
                        holder.Restroom.setText("화장실: 바깥쪽");
                        break;
                    case 3:
                        holder.Restroom.setText("화장실: 환승역 연결");
                        break;
                    case 4:
                        holder.Restroom.setText("화장실: 안쪽, 바깥쪽");
                        break;
                    default:
                        holder.Restroom.setText("화장실: 없음");
                        break;
                }

                if (stations.get(page).getHandicapCount() == 0) {
                    holder.HandicapCount.setText("장애인 편의시설: 없음");
                } else {
                    holder.HandicapCount.setText("장애인 편의시설: 있음");
                }

                if (stations.get(page).getMeetingPlace() == 0) {
                    holder.MeetingPlace.setText("만남의 장소: 없음 ");
                } else {
                    holder.MeetingPlace.setText("만남의 장소: 있음");
                }
                if (stations.get(page).getCivilCount() == 0) {
                    holder.CivilCount.setText("민원 안내: 없음");
                } else {
                    holder.CivilCount.setText("민원 안내: 있음");
                }

                if (stations.get(page).getBicycleCount() == 0) {
                    holder.BicycleCount.setText("자전거 보관소: 없음");
                } else {
                    holder.BicycleCount.setText("자전거 보관소: 있음");
                }
                if (stations.get(page).getParkingCount() == 0) {
                    holder.ParkingCount.setText("환승 주차장: 없음");

                } else {
                    holder.ParkingCount.setText("환승 주차장: 있음");

                }

                if (stations.get(page).getPublicPlace() == 0) {
                    holder.PublicPlace.setText("현장사무소: 없음");
                } else {
                    holder.PublicPlace.setText("현장사무소: 있음");
                }
                holder.Tel.setText(stations.get(page).getTel());
                holder.Address.setText(stations.get(page).getAddress());

                holder.curInfo.setVisibility(View.GONE);
                holder.goToTimetable.setVisibility(View.GONE);
                holder.stationInfo.setVisibility(View.GONE);
                holder.stationDefaultInfo.setVisibility(View.GONE);
                holder.useInfo.setVisibility(View.VISIBLE);
                break;


        }


    }


    @Override
    public int getItemCount() {
        if (stations.get(page) != null) return 5;
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //현재 역 bottomsheet 처음 뜨는 부분 정보
        LinearLayout curInfo;
        //현재 역 상세정보
        TextView preStation, curStation, nextStation, infoName, stationInfo;
        TextView preTime1, preTime2, nextTime1, nextTime2;
        ImageView goToTimetable;
        GridLayout useInfo, stationDefaultInfo, stationCongestion;
        TextView Platform, MeetingPlace, Restroom, OffDoor, CrossOver, HandicapCount, ParkingCount, BicycleCount, CivilCount, Tel, Address, PublicPlace;

        TextView congestion, congestionPercent, crawling, congestionNum;
        public ViewHolder(View view) {
            super(view);


//            Log.d("station", "station" + stations.get(page).toString());
            curInfo = (LinearLayout) view.findViewById(R.id.cur_info);
            preStation = (TextView) view.findViewById(R.id.pre_station);
            curStation = (TextView) view.findViewById(R.id.cur_station);
            nextStation = (TextView) view.findViewById(R.id.next_station);
            infoName = (TextView) view.findViewById(R.id.info_name);
            stationInfo = (TextView) view.findViewById(R.id.info_stationinfo);

            preTime1 = (TextView) view.findViewById(R.id.pre_time1);
            preTime2 = (TextView) view.findViewById(R.id.pre_time2);
            nextTime1 = (TextView) view.findViewById(R.id.next_time1);
            nextTime2 = (TextView) view.findViewById(R.id.next_time2);


            goToTimetable = (ImageView) view.findViewById(R.id.timetable_next);

            stationDefaultInfo = (GridLayout) view.findViewById(R.id.station_default_info);
            Platform = (TextView) view.findViewById(R.id.platform);
            OffDoor = (TextView) view.findViewById(R.id.offdoor);
            CrossOver = (TextView) view.findViewById(R.id.crossover);

            useInfo = (GridLayout) view.findViewById(R.id.station_useinfo);
            Restroom = (TextView) view.findViewById(R.id.restroom);
            HandicapCount = (TextView) view.findViewById(R.id.handicap_count);
            MeetingPlace = (TextView) view.findViewById(R.id.meeting_place);
            CivilCount = (TextView) view.findViewById(R.id.civil_count);
            ParkingCount = (TextView) view.findViewById(R.id.parking_count);
            BicycleCount = (TextView) view.findViewById(R.id.bicycle_count);
            Tel = (TextView) view.findViewById(R.id.tel);
            PublicPlace = (TextView) view.findViewById(R.id.public_place);
            Address = (TextView) view.findViewById(R.id.address);

            stationCongestion = (GridLayout) view.findViewById(R.id.station_cur_info);
            congestion = (TextView) view.findViewById(R.id.congestion);
            congestionPercent = (TextView) view.findViewById(R.id.congestion_percent);
            crawling = (TextView) view.findViewById(R.id.crawling);
            congestionNum = (TextView) view.findViewById(R.id.congestion_num);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }



}