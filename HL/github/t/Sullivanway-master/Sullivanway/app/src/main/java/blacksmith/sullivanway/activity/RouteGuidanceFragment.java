package blacksmith.sullivanway.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.database.Elevator;
import blacksmith.sullivanway.database.MyDBOpenHelper;
import blacksmith.sullivanway.database.Station;
import blacksmith.sullivanway.database.TransferMap;
import blacksmith.sullivanway.routeguidance.Route;
import blacksmith.sullivanway.routeguidance.RouteWrapper;
import blacksmith.sullivanway.utils.SubwayLine;

public class RouteGuidanceFragment extends Fragment {
    private SQLiteDatabase db;
    private int time;
    private int transCnt;
    private int cost;
    private ArrayList<RouteWrapper> items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            items = bundle.getParcelableArrayList("items");
            Route route = bundle.getParcelable("route");
            if (route != null) {
                time = route.getTime();
                transCnt = route.getTransCnt();
                cost = route.getCost();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_guidance, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new MyDBOpenHelper(getContext()).getReadableDatabase();

        /* View 설정 */
        TextView totalTimeView;
        TextView transCntView;
        TextView costView;
        ListView pathList;
        if (getView() != null) {
            totalTimeView = getView().findViewById(R.id.total_time);
            transCntView = getView().findViewById(R.id.trans_cnt);
            costView = getView().findViewById(R.id.cost);
            pathList = getView().findViewById(R.id.path_list);

            // 소요시간, 환승시간 표시
            if (time < 60)
                totalTimeView.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d분 소요", time));
            else
                totalTimeView.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d시간 %d분 소요", time / 60, time % 60));
            transCntView.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d회 환승", transCnt));
            costView.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d 원", cost));

            // 경로 리스트뷰 표시
            RouteGuidanceAdapter adapter = new RouteGuidanceAdapter(getActivity(), items);
            pathList.setAdapter(adapter);
        }
    }

    private class RouteGuidanceAdapter extends BaseAdapter {
        private static final int NUM_OF_ITEMS = 2;
        private static final int TYPE1 = 0;
        //private static final int TYPE2 = 1;

        private Holder holder;

        private Context context;
        private int[] id = { R.layout.item_route_subway_section, R.layout.item_route_walking_section};
        private ArrayList<RouteWrapper> items;

        private RouteGuidanceAdapter(Context context, ArrayList<RouteWrapper> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).type;
        }

        @Override
        public int getViewTypeCount() {
            return NUM_OF_ITEMS;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int itemType = getItemViewType(position);

            // Holder 생성 or ConvertView로부터 받아오기
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder = new RouteGuidanceAdapter.Holder();
                if (itemType == TYPE1) {
                    convertView = inflater.inflate(id[0], parent, false);
                    holder.startCircle = convertView.findViewById(R.id.startCircle);
                    holder.viaLine = convertView.findViewById(R.id.viaLine);
                    holder.endCircle = convertView.findViewById(R.id.endCircle);

                    holder.startLine = convertView.findViewById(R.id.startLine);
                    holder.startStnNm = convertView.findViewById(R.id.startStnNm);
                    holder.direction = convertView.findViewById(R.id.direction);
                    holder.remain = convertView.findViewById(R.id.remain);
                    holder.endLine = convertView.findViewById(R.id.endLine);
                    holder.endStnNm = convertView.findViewById(R.id.endStnNm);
                    holder.door = convertView.findViewById(R.id.door);
                } else {
                    convertView = inflater.inflate(id[1], parent, false);
                    // 엘리베이터 정보 리스트뷰
                    holder.startNearEvLocationView = convertView.findViewById(R.id.startNearEvLocationView);
                    holder.endNearEvLocationView = convertView.findViewById(R.id.endNearEvLocationView);
                    holder.walkImage = convertView.findViewById(R.id.walkImage);
                    holder.remainWalkTime = convertView.findViewById(R.id.remainWalkTime);
                    holder.transMap = convertView.findViewById(R.id.transMap);
                }
                convertView.setTag(holder);
            } else {
                holder = (RouteGuidanceAdapter.Holder) convertView.getTag();
            }

            // Holder의 View에 값 설정
            if (itemType == TYPE1) {
                int color = Color.parseColor(SubwayLine.getLineColor(items.get(position).lineSymbol));
                String lineSymbol = items.get(position).lineSymbol;
                String lineNm = SubwayLine.getLineNm(lineSymbol);
                String startStnNm = items.get(position).startStnNm;
                String endStnNm = items.get(position).endStnNm;
                int time = items.get(position).time;

                holder.startCircle.setColorFilter(color);
                holder.viaLine.setColorFilter(color);
                holder.endCircle.setColorFilter(color);

                holder.startLine.setText(lineSymbol);
                holder.startStnNm.setText(startStnNm);
                holder.direction.setText(String.format("다음역: %s역", items.get(position).nextStnNm));
                if (time < 60)
                    holder.remain.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d개 역 (%d분)", items.get(position).numOfStn, time));
                else
                    holder.remain.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d개 역 (%d시간 %d분)", items.get(position).numOfStn, time / 60, time % 60));
                holder.endLine.setText(lineSymbol);
                holder.endStnNm.setText(endStnNm);
                holder.door.setText(String.format("내리는 문: %s", items.get(position).door));

                holder.startStnNm.setOnClickListener(new OnStnNmClick(lineNm, startStnNm));
                holder.endStnNm.setOnClickListener(new OnStnNmClick(lineNm, endStnNm));
            } else { //TYPE2
                String transStnNm = items.get(position).transStnNm;
                String transStartLineNm = items.get(position).transStartLineNm;
                String transStartNextStnNm = items.get(position).transStartNextStnNm;
                String transEndLineNm = items.get(position).transEndLineNm;
                String transEndNextStnNm = items.get(position).transEndNextStnNm;

                // 엘리베이터 정보
                String startNearEvLocations = Elevator.getNearEvLocations(transStartLineNm, transStnNm, transStartNextStnNm);
                if (startNearEvLocations == null)
                    startNearEvLocations = getString(R.string.route_guidance_no_ev_info);
                String endNearEvLocations = Elevator.getNearEvLocations(transEndLineNm, transStnNm, transEndNextStnNm);
                if (endNearEvLocations == null)
                    endNearEvLocations = getString(R.string.route_guidance_no_ev_info);
                holder.startNearEvLocationView.setText(startNearEvLocations);
                holder.endNearEvLocationView.setText(endNearEvLocations);

                // 환승 소요시간
                holder.walkImage.setImageResource(R.drawable.ic_man);
                holder.remainWalkTime.setText(String.format(MainActivity.DEFAULT_LOCALE, "%d 분", items.get(position).walkTime));

                // 역명, 환승호선, 방향으로 DB에서 환승지도를 찾는다 (버튼 활성화/비활성화를 위해 데이터가 있는지만 확인)
                String sql = String.format("SELECT stnNm FROM %s WHERE stnNm='%s' AND startLineNm='%s' AND startNextStnNm='%s'" +
                                " AND endLineNm='%s' AND endNextStnNm='%s'",
                        TransferMap.TB_NAME, transStnNm, transStartLineNm, transStartNextStnNm, transEndLineNm, transEndNextStnNm);
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.moveToNext()) {
                    holder.transMap.setOnClickListener(new OnTransMapBtnClick(transStnNm, transStartLineNm, transStartNextStnNm, transEndLineNm, transEndNextStnNm));
                    holder.transMap.setImageResource(R.drawable.ic_map_black_48dp);
                }
                cursor.close();
            }
            return convertView;
        }

        private class Holder {
            // item_route_subway_section
            ImageView startCircle, viaLine, endCircle;
            TextView startLine, startStnNm, direction,
                    remain,
                    endLine, endStnNm, door;

            // item_route_walking_section
            TextView startNearEvLocationView;
            TextView endNearEvLocationView;
            ImageView walkImage, transMap;
            TextView remainWalkTime;
        }
    }

    /* Listener */
    private class OnStnNmClick implements View.OnClickListener {
        private String lineNm;
        private String stnNm;

        private OnStnNmClick(String lineNm, String stnNm) {
            this.lineNm = lineNm;
            this.stnNm = stnNm;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RouteGuidanceFragment.this.getActivity(), StnInfoPagerActivity.class);
            intent.putExtra("lineNm", lineNm);
            intent.putExtra("stnNm", stnNm);
            ArrayList<String> lines = Station.getLines(lineNm, stnNm);
            intent.putExtra("lines", lines);
            startActivity(intent);
        }
    }

    private class OnTransMapBtnClick implements View.OnClickListener {
        private String transStnNm;
        private String transStartLineNm;
        private String transStartNextStnNm;
        private String transEndLineNm;
        private String transEndNextStnNm;

        private OnTransMapBtnClick(String transStnNm, String transStartLineNm, String transStartNextStnNm, String transEndLineNm, String transEndNextStnNm) {
            this.transStnNm = transStnNm;
            this.transStartLineNm = transStartLineNm;
            this.transStartNextStnNm = transStartNextStnNm;
            this.transEndLineNm = transEndLineNm;
            this.transEndNextStnNm = transEndNextStnNm;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RouteGuidanceFragment.this.getActivity(), TransferMapPagerActivity.class);
            intent.putExtra("stnNm", transStnNm);
            intent.putExtra("startLineNm", transStartLineNm);
            intent.putExtra("startNextStnNm", transStartNextStnNm);
            intent.putExtra("endLineNm", transEndLineNm);
            intent.putExtra("endNextStnNm", transEndNextStnNm);
            startActivity(intent);
        }

    }

}
