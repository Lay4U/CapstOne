package org.androidtown.home;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 민섭 on 2016-08-14.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent popupintent = new Intent(context,DialogActivity.class);

        MainActivity.pi = PendingIntent.getActivity(context,MainActivity.mCrouteCode,popupintent,PendingIntent.FLAG_ONE_SHOT);
        try{
            MainActivity.pi.send();
        }catch (Exception e){

        }

    }



}
