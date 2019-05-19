package com.example.mysubwayproject;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private String openAPIKey = "444b484e466b6d723131324854716c4b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, test1.class);
        intent.putExtra("OpenAPIKey", this.openAPIKey);
        startActivity(intent);
    }
}
