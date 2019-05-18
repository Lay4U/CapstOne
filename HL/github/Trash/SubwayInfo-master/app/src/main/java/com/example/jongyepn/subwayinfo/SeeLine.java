package com.example.jongyepn.subwayinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SeeLine extends AppCompatActivity {
    static StaionAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findsubwaystaion);
        ArrayList<MyItem> data = new ArrayList<MyItem>();
        data.add(new MyItem(R.drawable.line1, "1", "호선"));
        data.add(new MyItem(R.drawable.line2, "2", "호선"));
        data.add(new MyItem(R.drawable.line3, "3", "호선"));
        data.add(new MyItem(R.drawable.line4, "4", "호선"));
        data.add(new MyItem(R.drawable.line5, "5","호선"));
        data.add(new MyItem(R.drawable.line6, "6", "호선"));
        data.add(new MyItem(R.drawable.line7, "7", "호선"));
        data.add(new MyItem(R.drawable.line8, "8", "호선"));
        data.add(new MyItem(R.drawable.shinbundangicon, "신분당선", ""));

        //어댑터 생성
        adapter = new StaionAdapter(this, R.layout.findsubwaystaionitem, data);

        //어댑터 연결
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                //   String name = (String) ((TextView)vClicked.findViewById(R.id.textItem1)).getText();
                String name = ((MyItem) adapter.getItem(position)).nStaionname;
                Toast.makeText(SeeLine.this, name + " selected",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),
                        DetailLineinfo.class);
                startActivity(intent);
            }
        });
    }
}
