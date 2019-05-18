package kr.go.seoul.trafficsubway.Common;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import kr.go.seoul.trafficsubway.TrafficSubwayInfoTypeA;









public class WebViewInterfaceTypeA
{
  private WebView mAppView;
  private Activity mContext;
  private String openAPIKey;
  
  public WebViewInterfaceTypeA(Activity activity, WebView view, String openAPIKey)
  {
    mAppView = view;
    mContext = activity;
    this.openAPIKey = openAPIKey;
  }
  
  @JavascriptInterface
  public void showSubwayInfo(String station) {
    Intent intent = new Intent(mContext, TrafficSubwayInfoTypeA.class);
    intent.putExtra("OpenAPIKey", openAPIKey);
    intent.putExtra("StationNM", station);
    mContext.startActivity(intent);
  }
}
