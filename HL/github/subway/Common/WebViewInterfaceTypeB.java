package kr.go.seoul.trafficsubway.Common;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import kr.go.seoul.trafficsubway.TrafficSubwayInfoTypeB;










public class WebViewInterfaceTypeB
{
  private WebView mAppView;
  private Activity mContext;
  private String openAPIKey;
  private String subwayLocationAPIKey;
  
  public WebViewInterfaceTypeB(Activity activity, WebView view, String openAPIKey, String subwayLocationAPIKey)
  {
    mAppView = view;
    mContext = activity;
    this.openAPIKey = openAPIKey;
    this.subwayLocationAPIKey = subwayLocationAPIKey;
  }
  
  @JavascriptInterface
  public void showSubwayInfo(String station) {
    Intent intent = new Intent(mContext, TrafficSubwayInfoTypeB.class);
    intent.putExtra("OpenAPIKey", openAPIKey);
    intent.putExtra("SubwayLocationAPIKey", subwayLocationAPIKey);
    intent.putExtra("StationNM", station);
    mContext.startActivity(intent);
  }
}
