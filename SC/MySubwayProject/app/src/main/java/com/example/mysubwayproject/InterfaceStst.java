package com.example.mysubwayproject;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class InterfaceStst {
    private Activity mContext;

    public InterfaceStst(Activity activity){
        this.mContext = activity;
    }

    @JavascriptInterface
    public void showSubwayInfo(String station){
        Intent intent = new Intent(this.mContext, TimeSetter.class);
        intent.putExtra("StartStationNM", station);
        this.mContext.startActivity(intent);
    }
}
