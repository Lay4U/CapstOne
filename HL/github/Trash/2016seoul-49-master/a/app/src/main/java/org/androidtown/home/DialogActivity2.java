package org.androidtown.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;

/**
 * Created by 민섭 on 2016-08-14.
 */
public class DialogActivity2 extends Activity {
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_press_bell);
        AlertDialog.Builder alarm = new AlertDialog.Builder(this);

        //우희가 추가한 코드
        SharedPreferences prefdefault = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useBell = prefdefault.getBoolean("useAlarm",false);
        String Bell = prefdefault.getString("autoUpdate_ringtone","DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(Bell);
        final MediaPlayer player = MediaPlayer.create(this, defaultSoundUri);
        player.setLooping(true);
        //////////////////////////////////

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 1000};
        v.vibrate(pattern, 0);
        ///////////소리 설정되있으면 울림
        if(useBell){
            player.start();
        }

        alarm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                v.cancel();
                player.stop();
                selectStartEnd.btn2.setBackgroundResource(R.drawable.stendalarmbt);
                selectStartEnd.editor.remove("check");
                selectStartEnd.editor.commit();
                finish();

            }
        });
        alarm.setMessage("\t\t\t 집갈시간이야! \t\t\t");


        AlertDialog dialog = alarm.create();
        dialog.show();
    }


}
