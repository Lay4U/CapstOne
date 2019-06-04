package com.example.mysubwayproject;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

class test{
    private WebView mAppView;
    private Activity mContext;
    private String openAPIKey;

    public test(Activity activity, WebView view, String openAPIKey){
        this.mAppView = view;
        this.mContext = activity;
        this.openAPIKey = openAPIKey;
    }

    @JavascriptInterface
    public void showSubwayInfo(String station){
        Intent intent = new Intent(this.mContext, test2.class);
        intent.putExtra("OpenAPIKey", this.openAPIKey);
        intent.putExtra("StationNM", station);
        this.mContext.startActivity(intent);
    }
}

