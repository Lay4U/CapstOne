package kr.go.seoul.trafficsubway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import kr.go.seoul.trafficsubway.Common.BaseActivity;
import kr.go.seoul.trafficsubway.Common.WebViewInterfaceTypeB;

public class TrafficSubwayDetailTypeB extends BaseActivity
{
  private String openAPIKey = "";
  private String subwayLocationAPIKey = "";
  private ImageView btnBackSubway;
  
  public TrafficSubwayDetailTypeB() {}
  
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.traffic_subway_detail);
    if ((getIntent() != null) && (getIntent().getStringExtra("OpenAPIKey") != null)) {
      openAPIKey = getIntent().getStringExtra("OpenAPIKey");
    }
    if ((getIntent() != null) && (getIntent().getStringExtra("SubwayLocationAPIKey") != null)) {
      subwayLocationAPIKey = getIntent().getStringExtra("SubwayLocationAPIKey");
    }
    initView(); }
  
  private WebView lineMapWebview;
  private WebViewInterfaceTypeB mWebViewInterface;
  private void initView() { btnBackSubway = ((ImageView)findViewById(R.id.btn_back_subway));
    btnBackSubway.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View view) {
        finish();
      }
    });
    lineMapWebview = ((WebView)findViewById(R.id.line_map_webview));
    lineMapWebview.setWebViewClient(new WebViewClient());
    lineMapWebview.getSettings().setJavaScriptEnabled(true);
    lineMapWebview.getSettings().setBuiltInZoomControls(true);
    lineMapWebview.getSettings().setSupportZoom(true);
    lineMapWebview.getSettings().setDisplayZoomControls(false);
    lineMapWebview.getSettings().setDefaultTextEncodingName("UTF-8");
    mWebViewInterface = new WebViewInterfaceTypeB(this, lineMapWebview, openAPIKey, subwayLocationAPIKey);
    lineMapWebview.addJavascriptInterface(mWebViewInterface, "Android");
    lineMapWebview.loadUrl("file:///android_asset/mSeoul_Subway.html");
  }
}
