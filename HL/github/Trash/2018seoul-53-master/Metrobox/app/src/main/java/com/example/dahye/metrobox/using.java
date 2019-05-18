package com.example.dahye.metrobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class using extends Activity implements View.OnClickListener {
    Button back, find_btn;
    EditText find_station;
    private ListView listview;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.using);
        listview = (ListView)findViewById(R.id.listviewing_find);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listview.setAdapter(adapter);
        listview.setTextFilterEnabled(true);
        initDatabase();
         mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    int using = Integer.parseInt(messageData.child("using").getValue().toString());
                    int total = Integer.parseInt(messageData.child("total_real").getValue().toString());
                    int total_real = total - using;
                    String msg2 = messageData.getKey().toString()+" 에는 "+total_real+"개 남아있습니다.";
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

        find_station = (EditText)findViewById(R.id.find_station_editText);
        find_station.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                listview.setVisibility(View.VISIBLE);
                String filterText = edit.toString();
                if(filterText.length() == 0){
                    listview.clearTextFilter();
                    listview.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listview.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listview.setVisibility(View.VISIBLE);
                listview.setFilterText(find_station.getText().toString());
            }
        });
    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("station_box");

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
    @Override
    public void onClick(View view) {
    }
}
