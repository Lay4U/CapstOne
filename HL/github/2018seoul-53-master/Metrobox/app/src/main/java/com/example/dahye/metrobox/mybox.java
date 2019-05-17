package com.example.dahye.metrobox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class mybox extends AppCompatActivity implements View.OnClickListener {
    TextView use1;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    private Button addingbtn, finishbtn, confirmbtn, confirmbtn1;
    private TableLayout addingcontent, addingcontent1, setcontent;
    private EditText stationname, boxnum, usingtime;
    private CheckBox checkalarm, easy_saved;
    private NumberPicker stationsection;
    private TextView set_stationname, set_boxnum, set_usingtime, set_timepassby;
    private String name1, section_s, num;
    int time1;
    private String curTime;
    stationlist station_list = new stationlist();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybox);
        use1 = (TextView)findViewById(R.id.use);
        stationname = (EditText)findViewById(R.id.station_name);
        stationsection = (NumberPicker)findViewById(R.id.station_section);

        final String section[] = {"A","B","C","D","E","없음"};
        stationsection.setValue(0);
        stationsection.setMinValue(0);
        stationsection.setMaxValue(section.length -1);
        stationsection.setDisplayedValues(section);
        stationsection.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        stationsection.setWrapSelectorWheel(false);
        section_s = section[0];
        NumberPicker.OnValueChangeListener PickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                     section_s = section[i1];
            }
        };
        stationsection.setOnValueChangedListener(PickerListener);
        boxnum = (EditText)findViewById(R.id.box_num);
        usingtime = (EditText)findViewById(R.id.using_time);
        set_stationname = (TextView)findViewById(R.id.set_station_name);
        set_boxnum = (TextView)findViewById(R.id.set_box_num);
        set_usingtime = (TextView)findViewById(R.id.set_using_time);
        set_timepassby = (TextView)findViewById(R.id.set_time_passby);
        addingbtn = (Button)findViewById(R.id.adding);
        finishbtn = (Button)findViewById(R.id.finish_btn);
        confirmbtn = (Button)findViewById(R.id.confirm_btn);
        confirmbtn1 = (Button)findViewById(R.id.confirm_btn1);
        addingcontent = (TableLayout)findViewById(R.id.adding_content);
        addingcontent1 = (TableLayout)findViewById(R.id.adding_content1);
        setcontent = (TableLayout) findViewById(R.id.set_content);
        checkalarm = (CheckBox)findViewById(R.id.check_alarm);
        easy_saved = (CheckBox)findViewById(R.id.easysaved);

        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        set_stationname.setText(pref.getString("stationname",""));
        set_boxnum.setText(pref.getString("stationnum",""));
        set_usingtime.setText(pref.getString("usingtime",""));
        set_timepassby.setText(pref.getString("starttime",""));
        easy_saved.setChecked(pref.getBoolean("easysave",false));
        use1.setText(pref.getString("use",""));
        if(set_stationname.getText().toString() != ""){
            setcontent.setVisibility(View.VISIBLE);
            addingbtn.setVisibility(View.INVISIBLE);
        }else{
            addingbtn.setVisibility(View.VISIBLE);
        }
        addingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingbtn.setVisibility(View.INVISIBLE);
                addingcontent.setVisibility(View.VISIBLE);
                curTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
            }
        });
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stationname.getText().length() == 0) {
                    Toast.makeText(mybox.this, "역 이름을 입력해주세요", Toast.LENGTH_LONG).show();
                    stationname.requestFocus();
                    return;
                } else {
                    if (section_s.equals("없음")) {
                        section_s = "";
                    }
                    name1 = stationname.getText().toString();
                    if (easy_saved.isChecked()) {
                        addingcontent.setVisibility(View.INVISIBLE);
                        addingcontent1.setVisibility(View.VISIBLE);
                    } else {
                        if (!(station_list.contains(name1 + section_s))) {
                            Toast.makeText(mybox.this, "역 이름을 정확히 적어주세요", Toast.LENGTH_LONG).show();
                            stationname.requestFocus();
                            return;
                        } else {
                            initDatabase(name1 + section_s);
                            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String msg2 = dataSnapshot.child("using").getValue().toString();
                                    use1.setText(msg2);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            addingcontent.setVisibility(View.INVISIBLE);
                            addingcontent1.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        confirmbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (boxnum.getText().length() == 0) {
                    Toast.makeText(mybox.this, "보관함 번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    boxnum.requestFocus();
                    return;
                } else if (usingtime.getText().length() == 0) {
                    Toast.makeText(mybox.this, "사용 시간을 입력해주세요", Toast.LENGTH_LONG).show();
                    usingtime.requestFocus();
                    return;
                } else {
                    num = boxnum.getText().toString();
                    time1 = Integer.parseInt(usingtime.getText().toString());
                    if (checkalarm.isChecked()==true) {
                        new AlarmHatt(getApplicationContext()).Alarm(time1);
                    }
                    set_stationname.setText(name1 + section_s);
                    set_boxnum.setText(num);
                    set_usingtime.setText(time1 + "시간");
                    set_timepassby.setText(curTime);
                    setcontent.setVisibility(View.VISIBLE);
                    addingcontent1.setVisibility(View.INVISIBLE);
                   if(!easy_saved.isChecked()){
                       int using = Integer.parseInt(use1.getText().toString()) + 1;
                       mReference.child("using").setValue(using);
                       use1.setText(String.valueOf(using));

                   }
                }
            }
        });

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!easy_saved.isChecked()) {
                    initDatabase(set_stationname.getText().toString());
                    int using = Integer.parseInt(use1.getText().toString()) - 1;
                    mReference.child("using").setValue(0);
                    use1.setText("");
                }
                stationname.setText("");
                boxnum.setText("");
                usingtime.setText("");
                set_stationname.setText("");
                set_boxnum.setText("");
                set_usingtime.setText("");
                set_timepassby.setText("");
                addingbtn.setVisibility(View.VISIBLE);
                setcontent.setVisibility(View.INVISIBLE);
                new AlarmHatt(getApplicationContext()).cancel();
            }
        });
    }
    @Override
    public void onClick (View view){

    }
    public class AlarmHatt{
        private Context context;
        public AlarmHatt(Context context){
            this.context = context;
        }
        public void Alarm(int time){
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mybox.this, broadcastAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mybox.this,0,intent,0);
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),
                    calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
            calendar.add(Calendar.HOUR_OF_DAY,time -1);
            calendar.add(Calendar.MINUTE,30);
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        public void cancel(){
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mybox.this, broadcastAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mybox.this,0,intent,0);
            alarmManager.cancel(pendingIntent);
        }
    }
    protected void onStop(){
        super.onStop();
        TextView station_name = (TextView)findViewById(R.id.set_station_name);
        TextView station_num = (TextView)findViewById(R.id.set_box_num);
        TextView using = (TextView)findViewById(R.id.set_using_time);
        TextView time = (TextView)findViewById(R.id.set_time_passby);
        TextView usetext = (TextView)findViewById(R.id.use);
        CheckBox easysave = (CheckBox)findViewById(R.id.easysaved);
        String stn_nm = station_name.getText().toString();
        String stn_num = station_num.getText().toString();
        String usetime = using.getText().toString();
        String starttime = time.getText().toString();
        String use = usetext.getText().toString();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("easysave",easysave.isChecked());
        editor.putString("stationname",stn_nm);
        editor.putString("stationnum",stn_num);
        editor.putString("usingtime",usetime);
        editor.putString("starttime",starttime);
        editor.putString("use",use);
        editor.commit();
    }

    private void initDatabase(String station) {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("station_box").child(station);

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


