package org.androidtown.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;

/**
 * Created by 민섭 on 2016-08-14.
 */

public class DialogActivity extends Activity {
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_press_bell);
        AlertDialog.Builder alarm = new AlertDialog.Builder(this);

        SharedPreferences prefdefault = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useBell = prefdefault.getBoolean("useAlarm",false);
        String Bell = prefdefault.getString("autoUpdate_ringtone","DEFAULT_SOUND");
        Uri defaultSoundUri = Uri.parse(Bell);
        final MediaPlayer player = MediaPlayer.create(this, defaultSoundUri);
        player.setLooping(true);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 1000};
        ///////////소리 설정되있으면 울림

        if(useBell){
            player.start();
        }
        v.vibrate(pattern, 0);

        alarm.setMessage(" 출발하셔야 합니다! \n 역까지의 경로를 확인 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    //취소버튼시 하는 동작
                    public void onClick(DialogInterface dialog, int whichButton) {
                        v.cancel();
                        player.stop();
                        try {
                            if (MainService.service_flag == 1) {
                                stopServiceMethod();
                                MainActivity.FLAG_service = 2;
                            }
                            MainActivity.bellBtn.setBackgroundResource(R.drawable.offbutton);
                            MainActivity.timeAlways.setBackgroundResource(R.drawable.timeoff);
                            MainActivity.editor.remove("checking");
                            MainActivity.editor.remove("checkButton");
                            MainActivity.editor.commit();
                        } catch (Exception e) {

                        }
                        finish();

                    }
                })
                        //확인 버튼시 하는 동작
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                v.cancel();
                                player.stop();
                                try {
                                    if (MainService.service_flag == 1) {
                                        stopServiceMethod();
                                        Intent intent = new Intent(DialogActivity.this, TmapOpen2.class);
                                        startActivity(intent);
                                    }
                                    if (MainActivity.FLAG_service !=2){
                                        Intent intent = new Intent(DialogActivity.this , MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(DialogActivity.this, TmapOpen.class);
                                        startActivity(intent);

                                    }
                                    MainActivity.bellBtn.setBackgroundResource(R.drawable.offbutton);
                                    MainActivity.timeAlways.setBackgroundResource(R.drawable.timeoff);
                                    MainActivity.editor.remove("checking");
                                    MainActivity.editor.remove("checkButton");
                                    MainActivity.editor.commit();

                                } catch (
                                        Exception e
                                        )

                                {

                                }

                                finish();
                            }
                        }

                );

        AlertDialog dialog = alarm.create();
        dialog.show();
    }

    public void stopServiceMethod() {
        Intent Service = new Intent(this, MainService.class);
        stopService(Service);
    }


}
