package com.example.dahye.metrobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class popUp extends Activity implements View.OnClickListener{
    private  TextView station_name1;
    String name;
    private ArrayAdapter<String> adapter, adapter1;
    ListView listview_pop, list;
    static final String[] LIST = {"대형보관함", "중형 보관함", "소형 보관함", "위치", "호선","전체 사물함"};
    List<Object> Array = new ArrayList<Object>();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        listview_pop = (ListView) findViewById(R.id.listview_popup);
        list = (ListView)findViewById(R.id.list);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, LIST);
        list.setAdapter(adapter1);
        Intent intent = getIntent();
        name = intent.getStringExtra("station_name");
        station_name1 = (TextView)findViewById(R.id.station_name);
        station_name1.setText(name);
        initDatabase(name);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listview_pop.setAdapter(adapter);
        mReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg2 = messageData.getValue().toString();
                    Array.add(msg2);
                    adapter.add(msg2);
                }
                adapter.notifyDataSetChanged();
                listview_pop.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View view) {
    }
    private void initDatabase(String n) {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("station_info").child(n);

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}
