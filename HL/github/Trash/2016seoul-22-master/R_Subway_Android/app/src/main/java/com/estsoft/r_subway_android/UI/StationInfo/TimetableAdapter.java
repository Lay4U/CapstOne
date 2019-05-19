package com.estsoft.r_subway_android.UI.StationInfo;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;
import com.estsoft.r_subway_android.Repository.StationRepository.StationTimetable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-18.
 */
public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

    private static final String TAG = "TimeTableAdapter";

    private Context context;
    private List<String> stationTimetable;

    OnItemClickListener mItemClickListener;
    View.OnClickListener mClickListener;

    // default : ord, 1 : sat, 2 : sun
    public TimetableAdapter(Context context, List<String> stationTimetable) {
        this.context = context;
        this.stationTimetable = stationTimetable;

    }

    @Override
    public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.station_info_time_table, parent, false);

        return new TimetableViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(TimetableViewHolder holder, int position) {

        holder.time1.setText(stationTimetable.get(position));

}


    @Override
    public int getItemCount() {
        return stationTimetable.size();
    }

    public class TimetableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView time1;

        public TimetableViewHolder(View view) {
            super(view);

            time1 = (TextView) view.findViewById(R.id.time1);

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