package com.example.mysubwayproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/*
 * 지도을 작성하는 소스코드 입니다! 절대 건드리지 마시오.
 * */
public class MapMaker extends AppCompatActivity {

    public WebView lineMapWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_traffic_subway_detail);
        this.initView();
    }

    private void initView() {
        this.lineMapWebview = (WebView)findViewById(R.id.my_line_map_webview);
        this.lineMapWebview.setWebViewClient(new WebViewClient());
        this.lineMapWebview.getSettings().setJavaScriptEnabled(true);
        this.lineMapWebview.getSettings().setBuiltInZoomControls(true);
        this.lineMapWebview.getSettings().setSupportZoom(true);
        this.lineMapWebview.getSettings().setDisplayZoomControls(false);
        this.lineMapWebview.getSettings().setDefaultTextEncodingName("UTF-8");
        this.lineMapWebview.loadUrl("file:///android_asset/mSeoul_Subway.html");
    }
}
