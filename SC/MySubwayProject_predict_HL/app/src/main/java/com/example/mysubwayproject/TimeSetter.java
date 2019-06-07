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
import android.app.DatePickerDialog;



import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * 시간을 선택하는 화면을 구성하는 소스코드 입니다!
 * 뒤로가기 키를 구현해야 합니다.
 * */

public class TimeSetter extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener, DatePicker.OnDateChangedListener {

    private String StartStationNM;
    private DatePickerDialog dialog;
    private TimePicker tp;

    private int cyear, cmonth, cday;

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

        TextView textView = (TextView) findViewById(R.id.timeSetterText) ;
        textView.setText("출발역 _ " + StartStationNM);
        tp = (TimePicker) findViewById(R.id.tp);

        c = Calendar.getInstance();

        cyear = c.get(c.YEAR);
        cmonth = c.get(c.MONTH);
        cday = c.get(c.DAY_OF_MONTH);


        hour = c.get(c.HOUR_OF_DAY);
        minute = c.get(c.MINUTE);
        tp.setOnTimeChangedListener(this);


        dialog = new DatePickerDialog(this,R.style.DialogTheme,listener, cyear, cmonth, cday);
        dialog.show();


        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, EndStation.class);
        intent.putExtra("StartStationNM", StartStationNM);
        intent.putExtra("Year", cyear);
        intent.putExtra("Month", cmonth);
        intent.putExtra("Day", cday);
        intent.putExtra("Hour", hour);
        intent.putExtra("Minute", minute);
        this.finish();
        startActivity(intent);

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.cyear= year;
        this.cmonth = monthOfYear;
        this.cday=dayOfMonth;
    }

/*
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    String strDate = String.valueOf(year) + "년 ";
                    strDate += String.valueOf(monthOfYear+1) + "월 ";
                    strDate += String.valueOf(dayOfMonth) + "일";


                    Toast.makeText(getApplicationContext(), strDate, Toast.LENGTH_SHORT).show();
                }
            };

*/

 DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

    @Override

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        cyear=year;
        cmonth=monthOfYear+1;
        cday=dayOfMonth;

        Toast.makeText(getApplicationContext(), cyear + "년" + cmonth + "월" + cday +"일", Toast.LENGTH_SHORT).show();

    }
};

}
