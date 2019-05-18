package com.example.dahye.metrobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class location extends Activity implements View.OnClickListener {
   private Button locate_attraction, locate_traffic,locate_hotplace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        locate_attraction = (Button)findViewById(R.id.attraction);
        locate_traffic = (Button)findViewById(R.id.traffic);
        locate_hotplace = (Button)findViewById(R.id.hotplace);
        locate_attraction.setOnClickListener((View.OnClickListener)this);
        locate_traffic.setOnClickListener((View.OnClickListener)this);
        locate_hotplace.setOnClickListener((View.OnClickListener)this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.attraction){
            Intent intent = new Intent(this, metrolist_att.class);
            startActivity(intent);
        }else if(view.getId() == R.id.traffic){
            Intent intent = new Intent(this, metrolist_tra.class);
            startActivity(intent);
        }else if(view.getId() == R.id.hotplace){
            Intent intent = new Intent(this, metrolist_hot.class);
            startActivity(intent);
        }
        else {
            finish();
        }
    }
}
