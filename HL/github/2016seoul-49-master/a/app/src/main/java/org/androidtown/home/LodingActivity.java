package org.androidtown.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by 민섭 on 2016-09-23.
 */
public class LodingActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loding_);
        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(LodingActivity.this, MainActivity.class);

                startActivity(i);

                finish();

            }


        }, 3000);

    }


}


