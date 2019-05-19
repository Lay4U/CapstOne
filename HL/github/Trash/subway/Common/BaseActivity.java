package kr.go.seoul.trafficsubway.Common;

import android.app.Activity;
import android.view.Window;

public class BaseActivity extends Activity
{
  public BaseActivity() {}
  
  public void setContentView(int layoutResID)
  {
    super.setContentView(layoutResID);
    
    FontUtils.getInstance(this).setGlobalFont(getWindow().getDecorView());
  }
}
