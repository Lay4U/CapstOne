package kr.go.seoul.trafficsubway;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import kr.go.seoul.trafficsubway.Common.FontUtils;



public class TrafficSubwayButtonTypeA
  extends LinearLayout
  implements View.OnClickListener
{
  private LayoutInflater layoutInflater;
  private LinearLayout mainButton;
  private ImageView buttonImg;
  private TextView buttonText;
  private String openAPIKey = "";
  
  public TrafficSubwayButtonTypeA(Context context) {
    super(context);
    initView();
  }
  
  public TrafficSubwayButtonTypeA(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }
  
  public TrafficSubwayButtonTypeA(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }
  
  @TargetApi(21)
  public TrafficSubwayButtonTypeA(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initView();
  }
  
  private void initView() {
    layoutInflater = ((LayoutInflater)getContext().getSystemService("layout_inflater"));
    View view = layoutInflater.inflate(R.layout.traffic_subway_button_type_a, this, false);
    FontUtils.getInstance(getContext()).setGlobalFont(view);
    addView(view);
    
    mainButton = ((LinearLayout)view.findViewById(R.id.main_button));
    mainButton.setOnClickListener(this);
    
    buttonImg = ((ImageView)view.findViewById(R.id.button_img));
    buttonText = ((TextView)view.findViewById(R.id.button_text));
  }
  
  public void onClick(View view)
  {
    Intent intent = new Intent(getContext(), TrafficSubwayDetailTypeA.class);
    intent.putExtra("OpenAPIKey", openAPIKey);
    getContext().startActivity(intent);
  }
  
  public void setOpenAPIKey(String key) {
    openAPIKey = key;
  }
  
  public void setButtonImage(int resID) {
    buttonImg.setImageResource(resID);
    if (Build.VERSION.SDK_INT >= 16) {
      mainButton.setBackground(null);
    } else {
      mainButton.setBackgroundDrawable(null);
    }
  }
  
  public void setButtonText(String text) {
    buttonText.setText(text);
    if (text.equals("")) {
      buttonText.setVisibility(8);
    } else {
      if (Build.VERSION.SDK_INT >= 16) {
        mainButton.setBackground(null);
      } else {
        mainButton.setBackgroundDrawable(null);
      }
      buttonText.setVisibility(0);
    }
  }
}
