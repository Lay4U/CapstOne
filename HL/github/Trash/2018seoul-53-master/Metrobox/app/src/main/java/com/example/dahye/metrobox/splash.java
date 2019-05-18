package com.example.dahye.metrobox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        try{
            Thread.sleep(1000);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
