package com.estsoft.r_subway_android.UI.Tutorial;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dd.processbutton.iml.ActionProcessButton;

import com.estsoft.r_subway_android.MainActivity;
import com.estsoft.r_subway_android.R;


public class TutorialFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TutorialFragment";
    private static final float imageMag = 0.5f;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private int windowWidth = 0;
    private int windowHeight= 0;

    public static TutorialFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TutorialFragment fragment = new TutorialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //PAGE받기
        mPage = getArguments().getInt(ARG_PAGE);

        switch (mPage) {

            case 1:
                LinearLayout linearLayout1 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page1, container, false);

//                Drawable d = getActivity().getResources().getDrawable(R.drawable.splash_tutorial_1);
//                int imageWidth = d.getIntrinsicWidth();
//                int imageHeight = d.getIntrinsicHeight();
//                Log.d(TAG, "onCreateView: " + imageWidth +  " / " + imageHeight);
//                Log.d(TAG, "onCreateView: " + windowWidth +  " /  " + windowHeight);
//                ImageView imageView = (ImageView)getActivity().findViewById(R.id.splash_tutorial_image_1);
//                imageView.getLayoutParams().height = windowHeight;

                LinearLayout background1 = (LinearLayout) linearLayout1.findViewById(R.id.background);
                background1.setBackgroundColor(new ColorDrawable().getAlpha());
                return linearLayout1;
            case 2:
                LinearLayout linearLayout2 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page2, container, false);
                RelativeLayout background2 = (RelativeLayout) linearLayout2.findViewById(R.id.background);
                background2.setBackgroundColor(new ColorDrawable().getAlpha());
                return linearLayout2;
            case 3:
                LinearLayout linearLayout3 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page3, container, false);
                RelativeLayout background3 = (RelativeLayout) linearLayout3.findViewById(R.id.background);
                background3.setBackgroundColor(new ColorDrawable().getAlpha());

                return linearLayout3;
            case 4 :
                LinearLayout linearLayout4 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page4, container, false);
                RelativeLayout background4 = (RelativeLayout) linearLayout4.findViewById(R.id.background);
                background4.setBackgroundColor(new ColorDrawable().getAlpha());

                return linearLayout4;
            case 5 :
                LinearLayout linearLayout5 = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page5, container, false);
                RelativeLayout background5 = (RelativeLayout) linearLayout5.findViewById(R.id.background);
                //TextView page_num4 = (TextView) linearLayout4.findViewById(R.id.page_num);
                final ActionProcessButton btnStart = (ActionProcessButton) background5.findViewById(R.id.btnSignIn);
                //page_num4.setText(String.valueOf(4));
                background5.setBackgroundColor(new ColorDrawable().getAlpha());
                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
//                        Log.e("onClick","finish");
                        getActivity().finish();
                    }
                });


                return linearLayout5;
            default:
                LinearLayout linearLayoutD = (LinearLayout) inflater.inflate(R.layout.fragment_tutorial_page5, container, false);
                LinearLayout backgroundD = (LinearLayout) linearLayoutD.findViewById(R.id.background);
                //TextView page_numD = (TextView) linearLayoutD.findViewById(R.id.page_num);
//                page_numD.setText("Error");
//                Button btnD = (Button) linearLayoutD.findViewById(R.id.TutorialF);
//                linearLayoutD.setBackgroundColor(new ColorDrawable().getAlpha());
//                btnD.setOnClickListener(this);
                return linearLayoutD;
        }
    }


    public void onClick(View v)
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }



}