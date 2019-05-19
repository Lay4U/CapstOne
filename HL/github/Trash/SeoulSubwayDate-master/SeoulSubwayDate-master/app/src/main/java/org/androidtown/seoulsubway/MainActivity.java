package org.androidtown.seoulsubway;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {

    Button searchBtn;
    LinearLayout categoryLaout;
    LinearLayout contentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryLaout = (LinearLayout)findViewById(R.id.categoryLayout);
        contentsLayout = (LinearLayout)findViewById(R.id.contentsLayout);

        // 역이름 자동완성 기능
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.searchEdit);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        textView.setAdapter(adapter);

        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryLaout.setVisibility(View.VISIBLE);
                contentsLayout.setVisibility(View.GONE);
            }
        });


    }

    // contentsLayout 인플레이트
    public void inflateLayout(){
        // 버튼 아래 레이아웃 객체 참조
        LinearLayout contentsLayout = (LinearLayout)findViewById(R.id.contentsLayout);
        // 레이아웃 인플레이터 객체 참조
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Button.xml의 레이아웃에 대해 인플레이션 수행
        inflater.inflate(R.layout.category, contentsLayout, true);
    }

    // 역이름
    static final String[] COUNTRIES = new String[] {
            "서울역", "서정리역", "서동탄역", "서초역", "서울대입구역"
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
