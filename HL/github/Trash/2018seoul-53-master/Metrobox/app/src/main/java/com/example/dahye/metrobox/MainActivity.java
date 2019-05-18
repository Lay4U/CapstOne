package com.example.dahye.metrobox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button locatebox, usingbox, mybox, info;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locatebox = (Button) findViewById(R.id.locatebox);
        usingbox = (Button) findViewById(R.id.usingbox);
        info = (Button) findViewById(R.id.info);
        mybox = (Button) findViewById(R.id.mybox);
        locatebox.setOnClickListener((View.OnClickListener) this);
        usingbox.setOnClickListener((View.OnClickListener) this);
        mybox.setOnClickListener((View.OnClickListener) this);
        info.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.locatebox) {
            Intent intent = new Intent(this, location.class);
            startActivity(intent);
        } else if(view.getId() == R.id.usingbox){
            Intent intent = new Intent(this, using.class);
            startActivity(intent);
        } else if(view.getId() == R.id.mybox){
            Intent intent = new Intent(this, mybox.class);
            startActivity(intent);
        } else if(view.getId() == R.id.info){
            Intent intent = new Intent(this, infomation.class);
            startActivity(intent);
        } else{
            finish();
        }
    }
    private long time2 = 0;
    @Override
    public void onBackPressed(){
         if(System.currentTimeMillis()-time2>=2000){
             time2 = System.currentTimeMillis();
             Toast.makeText(MainActivity.this,"\'뒤로\' 버튼을 한번 더 누르면 종료합니다!",Toast.LENGTH_SHORT).show();
         }else if(System.currentTimeMillis()-time2<2000){
             finish();
         }
    }
 }
