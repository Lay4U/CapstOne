package org.androidtown.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class select_LastBtn extends Activity {

    String railnM;
    String transnM;
    String sTime;
    String mTime;
    String nearS;
    String arriveS;
    ImageButton dir;
    String resultransnM;
    TextView start,end,mtime,trans,stime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select__last_btn);
        start = (TextView)findViewById(R.id.startS);
        end = (TextView)findViewById(R.id.endS);
        mtime = (TextView)findViewById(R.id.mTime);
        trans = (TextView)findViewById(R.id.transT);
        stime = (TextView)findViewById(R.id.stimeT);
        dir = (ImageButton)findViewById(R.id.dir);

        Intent intent = getIntent();
        railnM = intent.getExtras().getString("rail");
        transnM = intent.getExtras().getString("trans");
        sTime =intent.getExtras().getString("stime");
        mTime =intent.getExtras().getString("mtime");
        nearS = intent.getExtras().getString("nearS");
        arriveS = intent.getExtras().getString("arriveS");

        start.setText(nearS);
        end.setText(arriveS);

        resultransnM="";
        String ant[] = transnM.split(",");
        ArrayList<String> onio = new ArrayList<>();
        for(int i=0;i<ant.length;i++){
            onio.add(ant[i]);
        }


        for(int i=0;i<onio.size();i++){
            if(nearS.equals(onio.get(i)) || arriveS.equals(onio.get(i))){
                onio.remove(i);
            }
        }

        for(int i=0; i<onio.size();i++){
            resultransnM = resultransnM+onio.get(i)+",";
        }

        mtime.setText("막차 시간 - "+mTime);
        trans.setText("환승역 - " + resultransnM.substring(0,resultransnM.length()-1));
        stime.setText("소요시간 - " + sTime +"분");


        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(select_LastBtn.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                String railArray[] = railnM.substring(1, railnM.length()).split("-");
                ArrayList<String> onr = new ArrayList<String>();
                for (int i = 0; i < railArray.length; i++) {
                    onr.add(railArray[i]);
                }
                String message = "";
                for (int i = 0; i < onr.size(); i++) {
                    if (resultransnM.contains(onr.get(i))) {
                        message = message + onr.get(i) + "------------------((환승))" + "\n" + " *" + "\n";
                    } else {
                        message = message + onr.get(i) + "\n" + " *" + "\n";
                    }
                }
                alert.setMessage(message.substring(0, message.length() - 2));
                alert.show();
            }
        });


    }
}
