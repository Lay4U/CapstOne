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
XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
삭제예정
 * */

public class DateSetter extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener {

    private String StartStationNM;
    private DatePickerDialog dpd;

    private int year, month, day;

    private Calendar c;

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ;
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
        }
    };



    DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            c.set(year, monthOfYear, dayOfMonth);
        }
    };

   // DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        if (getIntent() != null && getIntent().getStringExtra("StartStationNM") != null) {
            this.StartStationNM = getIntent().getStringExtra("StartStationNM");
        }
/*
        TextView textView = (TextView) findViewById(R.id.datePicker) ;
        textView.setText("출발역 " + StartStationNM);
*/
        c = Calendar.getInstance();

        year = c.get(c.YEAR);
        month = c.get(c.MONTH);
        day = c.get(c.DAY_OF_MONTH);

        dialog.show();

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, EndStation.class);
        intent.putExtra("StartStationNM", StartStationNM);
        intent.putExtra("Year", year);
        intent.putExtra("Month", month);
        intent.putExtra("Day", day);
        this.finish();
        startActivity(intent);

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year= year;
        this.month = monthOfYear;
        this.day=dayOfMonth;
    }



}