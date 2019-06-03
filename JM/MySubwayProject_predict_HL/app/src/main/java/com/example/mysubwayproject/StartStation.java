package com.example.mysubwayproject;

import android.os.Bundle;
import android.widget.TextView;

/*
 * 시작역을 선택하는 화면을 구성하는 소스코드 입니다!
 * */

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
        this.mWebViewInterface = new InterfaceStst(this);// 역 선택시 정보전달
        super.lineMapWebview.addJavascriptInterface(this.mWebViewInterface, "Android");
    }
}
