package com.example.mysubwayproject;

import android.os.Bundle;
import android.widget.TextView;


public class StartStation extends MapMaker {
    private InterfaceStst mWebViewInterface;
    private TextView textPageNM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
    }
    private void initView() {
        this.textPageNM = (TextView)findViewById(R.id.my_textView);
        textPageNM.setText("시작역을 선택하시오");
        //System.out.println(textPageNM.getText());
        //this.textPageNM.setText("시작역");
        this.mWebViewInterface = new InterfaceStst(this);
        super.lineMapWebview.addJavascriptInterface(this.mWebViewInterface, "Android");
    }
}
