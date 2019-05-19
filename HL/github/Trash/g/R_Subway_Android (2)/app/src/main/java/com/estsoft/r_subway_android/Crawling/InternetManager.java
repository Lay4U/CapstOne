package com.estsoft.r_subway_android.Crawling;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;

/**
 * Created by estsoft on 2016-08-16.
 */
public class InternetManager {

    private static InternetManager ourInstance = null;

    public static InternetManager getInstance() {
        return ourInstance;
    }

    public static void init(Context context) {
        if (mContext == null || cm == null) {
            mContext = context;
            cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        ourInstance = new InternetManager();

    }

    private static Context mContext = null;
    private static ConnectivityManager cm = null;

    private InternetManager() {
    }

    public static boolean checkNetwork() {
        NetworkInfo network = cm.getActiveNetworkInfo();
        if ( network != null ) {
            if( network.getType() == ConnectivityManager.TYPE_WIFI
                    || network.getType() == ConnectivityManager.TYPE_MOBILE ) return true;
        }
        return false;
    }

}
