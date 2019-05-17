package org.androidtown.home;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 민섭 on 2016-08-14.
 */
public class AlarmReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent popupintent = new Intent(context,DialogActivity2.class);

        selectStartEnd.pi = PendingIntent.getActivity(context,selectStartEnd.mCrouteCode,popupintent,PendingIntent.FLAG_ONE_SHOT);
        try{
            selectStartEnd.pi.send();
        }catch (Exception e){

        }

    }



}
