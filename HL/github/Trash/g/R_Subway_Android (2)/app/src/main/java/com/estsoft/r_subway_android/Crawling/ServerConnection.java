package com.estsoft.r_subway_android.Crawling;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.estsoft.r_subway_android.MainActivity;
import com.estsoft.r_subway_android.Repository.StationRepository.Station;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by estsoft on 2016-08-16.
 */
public class ServerConnection {

    private static final String TAG = "ServerConnection";

    private Context mContext = null ;
    private ConnectivityManager cm = null;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    private static int ACCIDENT_TRUE = 10;
    private static int ACCIDENT_FALSE = 11;
    private static int SERVER_CONNECTION_FAILED = 12;
    private static int INTERNET_DISCONNECTED = 13;

    public ServerConnection(Context mContext) {
        this.mContext = mContext;
        this.cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public int getAccidentInfo( Station station ) {
        AccidentTask at = new AccidentTask(station);
        if ( InternetManager.getInstance().checkNetwork() ) {
            at.execute();
        } else {
//            Toast.makeText(mContext, "INTERNET DISABLED", Toast.LENGTH_SHORT).show();
//            ((MainActivity)mContext).setStationStatus( INTERNET_DISCONNECTED );
        }

        //Canceling Task...
//        at.cancel(false);
//        Log.d(TAG, "getAccidentInfo: AccidentTask Canceled? " +  at.isCancelled());

        return -1;
    }

    private boolean checkYunYear( int year ) {
        boolean result = ( year % 4 == 0 ) && ( year % 100 != 0 || ( year % 400 == 0 ) ) ;
        return result;
    }


    private class AccidentTask extends AsyncTask<Void, Void,Integer> {

        private static final String TAG = "AccidentTask";
        private String serverAddr = "222.239.250.207";
        private int port = 11011;
        private String stationName;
        private String accidentInfo;

        private Station station = null;
        public AccidentTask(Station station) {
            this.station = station;
            this.stationName = station.getStationName();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Socket socket = null;
            try {
                socket = new Socket(serverAddr, port);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF(stationName);
                outputStream.flush();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                accidentInfo = input.readLine();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex ) {
                ex.printStackTrace();
            } finally {
                accidentInfo = "Server Connection Fail";
            }
            if ( accidentInfo.equals("T") ) return ACCIDENT_TRUE;
            else if (accidentInfo.equals("F") ) return ACCIDENT_FALSE;
            return SERVER_CONNECTION_FAILED;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d(TAG, "onPostExecute: " + integer);
//            SystemClock.sleep(2000);
//            ((MainActivity)mContext).setStationStatus( integer );
        }

    }


}
