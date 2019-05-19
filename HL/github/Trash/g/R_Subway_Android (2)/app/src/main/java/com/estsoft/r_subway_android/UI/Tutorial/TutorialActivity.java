package com.estsoft.r_subway_android.UI.Tutorial;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

import com.estsoft.r_subway_android.R;



import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewInit();

    }

    private void viewInit()
    {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerT);
        final TutorialFragmentPagerAdapter tutorialFragmentPagerAdapter = new TutorialFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tutorialFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

        final VideoView videoView = (VideoView) findViewById(R.id.videoViews);
        final Uri videoFile =  Uri.parse("android.resource://"+getPackageName()+"/raw/splash_30s");

        videoView.requestFocus();
        videoView.setVideoURI(videoFile);
        //videoView.setSoundEffectsEnabled(false);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                videoView.seekTo(0);
                videoView.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                videoView.seekTo(0);
                videoView.start();
            }
        });

//        final SubmitButton button = (SubmitButton) findViewById(R.id.startBtn);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });


        final CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        tutorialFragmentPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());


        viewPager.setOffscreenPageLimit(4);

    }
}