package kr.go.seoul.trafficsubway.Common;

import android.app.Dialog;
import android.content.Context;
import kr.go.seoul.trafficsubway.R.layout;



public class CustomProgressDialog
  extends Dialog
{
  public CustomProgressDialog(Context context)
  {
    super(context, 16973839);
    requestWindowFeature(1);
    setContentView(R.layout.custom_progress_dialog);
  }
}
