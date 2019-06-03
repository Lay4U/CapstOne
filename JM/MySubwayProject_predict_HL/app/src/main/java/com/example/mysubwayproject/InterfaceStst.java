package com.example.mysubwayproject;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

/*
 * 시작역선택 후 정보를 전달하기 위한 소스코드 입니다!
 * */

public class InterfaceStst {
    private Activity mContext;

    public InterfaceStst(Activity activity){
        this.mContext = activity;
    }

    @JavascriptInterface
    public void showSubwayInfo(String station){
        Intent intent = new Intent(this.mContext, TimeSetter.class);
        intent.putExtra("StartStationNM", station);
        this.mContext.finish();
        this.mContext.startActivity(intent);
    }
}
