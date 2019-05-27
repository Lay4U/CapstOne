package com.example.mysubwayproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/*
 * 시간을 선택하는 화면을 구성하는 소스코드 입니다!
 * 뒤로가기 키를 구현해야 합니다.
 * */

public class TimeSetter extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener{

    private String StartStationNM;
    private DatePicker dp;
    private TimePicker tp;
    private int year, month, day;
    private int hour;
    private int minute;
    private Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setter);
        if (getIntent() != null && getIntent().getStringExtra("StartStationNM") != null) {
            this.StartStationNM = getIntent().getStringExtra("StartStationNM");
        }

        TextView textView = (TextView) findViewById(R.id.textView) ;
        textView.setText("출발 : "+StartStationNM);

        tp = (TimePicker) findViewById(R.id.tp);
        c = Calendar.getInstance();
        year = c.get(c.YEAR);
        month = c.get(c.MONTH);
        day = c.get(c.DAY_OF_MONTH);
        hour = c.get(c.HOUR_OF_DAY);
        minute = c.get(c.MINUTE);
        tp.setOnTimeChangedListener(this);

        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);
        dialog.show();

        dp = (DatePicker) findViewById(R.id.dp);


        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, EndStation.class);
        intent.putExtra("StartStationNM", StartStationNM);
        intent.putExtra("Year", dp.getYear());
        intent.putExtra("Month", dp.getMonth()+1);
        intent.putExtra("Day", dp.getDayOfMonth());
        intent.putExtra("Hour", hour);
        intent.putExtra("Minute", minute);
        startActivity(intent);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    // DatePickerDialog


    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();

        }

    };


}
