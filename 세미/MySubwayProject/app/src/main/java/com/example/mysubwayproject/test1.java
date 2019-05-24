package com.example.mysubwayproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import kr.go.seoul.trafficsubway.Common.WebViewInterfaceTypeA;

public class test1 extends AppCompatActivity {

    private String openAPIKey = "";
    private ImageView btnBackSubway;
    private WebView lineMapWebview;
    private test mWebViewInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_subway_detail);
        if (getIntent() != null && getIntent().getStringExtra("OpenAPIKey") != null) {
            this.openAPIKey = getIntent().getStringExtra("OpenAPIKey");
        }
        this.initView();
    }

    private void initView() {
        this.btnBackSubway = (ImageView)findViewById(R.id.btn_back_subway);
        this.btnBackSubway.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                test1.this.finish();
            }
        });
        this.lineMapWebview = (WebView)findViewById(R.id.line_map_webview);
        this.lineMapWebview.setWebViewClient(new WebViewClient());
        this.lineMapWebview.getSettings().setJavaScriptEnabled(true);
        this.lineMapWebview.getSettings().setBuiltInZoomControls(true);
        this.lineMapWebview.getSettings().setSupportZoom(true);
        this.lineMapWebview.getSettings().setDisplayZoomControls(false);
        this.lineMapWebview.getSettings().setDefaultTextEncodingName("UTF-8");
        this.mWebViewInterface = new test(this, this.lineMapWebview, this.openAPIKey);
        this.lineMapWebview.addJavascriptInterface(this.mWebViewInterface, "Android");
        this.lineMapWebview.loadUrl("file:///android_asset/mSeoul_Subway.html");
    }
}
