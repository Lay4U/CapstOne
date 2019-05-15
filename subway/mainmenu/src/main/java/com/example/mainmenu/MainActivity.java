package com.example.mainmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.PopupWindow;
//타임픽커 //팝업창
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    Button testBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);


        //testBtn = (Button)findViewById(R.id.test_button);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Intent intent = new Intent(this, resultMenu.class);
        Intent intent = new Intent(this, VectorTest.class);
        //intent.putExtra("",startstation);
        //intent.putExtra("",endstation);
        //intent.putExtra("",time);
        startActivity(intent);
    }
}

//package com.example.mainmenu;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.content.Intent;
//import android.widget.PopupWindow;
//import android.widget.Toast;
//
////타임픽커 //팝업창
//public class MainActivity extends AppCompatActivity { // 이거는왜 ?implements View.OnClickListener
//
//    Button testBtn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //Button handling
//        Button button = findViewById(R.id.button);
//
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            pulblic void onClick(View view){
//                Toast.makeText(getApplicationContext(), "Hello world!", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        //testBtn = (Button)findViewById(R.id.test_button);
//
////        testBtn.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        //Intent intent = new Intent(this, resultMenu.class);
//        Intent intent = new Intent(this, VectorTest.class);
//        //intent.putExtra("",startstation);
//        //intent.putExtra("",endstation);
//        //intent.putExtra("",time);
//        startActivity(intent);
//    }
//}
