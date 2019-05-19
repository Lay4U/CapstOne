package jeimsko.com.subway.fragment;


import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import jeimsko.com.subway.R;
import jeimsko.com.subway.utils.ComUtils;
import jeimsko.com.subway.utils.PreferenceManager;

/**
 * Created by jeimsko on 2016. 9. 9..
 */

public class FirstPageFragment extends Fragment {

    private String TAG = FirstPageFragment.class.getSimpleName();

    private Timer timer = null;
    TextView Station_txt;
    TextView PreStation_txt;
    TextView NextStation_txt;
    public static FirstPageFragment newInstance() {

        FirstPageFragment ff = new FirstPageFragment();

        return ff;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.firstpage, container, false);

        ComUtils.printLog(TAG, "FirstPageFragment createview");

        Station_txt = (TextView) rootView.findViewById(R.id.stationnm_txt);
        PreStation_txt = (TextView) rootView.findViewById(R.id.pre_stationnm_txt);
        NextStation_txt = (TextView) rootView.findViewById(R.id.next_stationnm_txt);

        if (timer == null) {
            timer = new Timer();
            TimerTask tt = new MyTimerTask();
            timer.schedule(tt, 0, 6000);

        }

        return rootView;
    }


    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            ComUtils.printLog(TAG, "handle call");
            String mStationName_txt = "";
            mStationName_txt = PreferenceManager.getString("save_stationname", "");
            Station_txt.setText(mStationName_txt);
        }


    };

    @Override
    public void onPause() {
        super.onPause();
        ComUtils.printLog(TAG, "pause ");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ComUtils.printLog(TAG, "onResume ");
    }

    @Override
    public void onStop() {
        super.onStop();

        ComUtils.printLog(TAG, " first fragment stop");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            try {

                ComUtils.printLog(TAG, "timer call");
                Message msg = handler.obtainMessage();
                handler.sendEmptyMessage(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
