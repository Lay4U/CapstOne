package org.androidtown.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by 민섭 on 2016-09-26.
 */
public class onPressBell extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_press_bell);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.bellBtn.setBackgroundResource(R.drawable.offbutton);
                MainActivity.editor.remove("checkButton");
                MainActivity.editor.commit();
                dialog.dismiss();
                finish();
            }
        });
        alert.setMessage("우리집에 갈 수있는 오늘의 운행이 종료되었습니다.");
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}
