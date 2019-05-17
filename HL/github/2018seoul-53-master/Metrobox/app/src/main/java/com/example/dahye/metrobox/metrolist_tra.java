package com.example.dahye.metrobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class metrolist_tra extends Activity implements View.OnClickListener {
    private ListView listview;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metrolist_tra);
//        databaseReference.child("station").child("traffic").push().setValue("강변");
//        databaseReference.child("station").child("traffic").push().setValue("고속터미널A");
//        databaseReference.child("station").child("traffic").push().setValue("고속터미널B");
//        databaseReference.child("station").child("traffic").push().setValue("남부터미널");
//        databaseReference.child("station").child("traffic").push().setValue("상봉");
//        databaseReference.child("station").child("traffic").push().setValue("서울");
//        databaseReference.child("station").child("traffic").push().setValue("수서");
//        databaseReference.child("station").child("traffic").push().setValue("영등포");
//        databaseReference.child("station").child("traffic").push().setValue("옥수");
//        databaseReference.child("station").child("traffic").push().setValue("용산");
//        databaseReference.child("station").child("traffic").push().setValue("왕십리");
//        databaseReference.child("station").child("traffic").push().setValue("청량리");
        listview = (ListView) findViewById(R.id.listviewing_tra);
        initDatabase();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), popUp.class);
                intent.putExtra("station_name",adapterView.getAdapter().getItem(i).toString());
                startActivity(intent);
            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listview.setAdapter(adapter);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg2 = messageData.getValue().toString();
                    Array.add(msg2);
                    adapter.add(msg2);
                }
                adapter.notifyDataSetChanged();
                listview.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }


    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("station").child("traffic");
        //mReference.setValue("check");

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
