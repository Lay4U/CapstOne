package com.devaom.limjg.gproj;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText editText_Nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telManager.getLine1Number();

        TextView tv_PhoneNumber = (TextView)findViewById(R.id.tv_PhoneNumber);
        tv_PhoneNumber.setText(phoneNumber);

        editText_Nickname = (EditText)findViewById(R.id.editText_Nickname);
        Button button_setName = (Button)findViewById(R.id.button_setName);
        button_setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nickname",editText_Nickname.getText().toString());
                editor.commit();

                Log.v("prefTest",editText_Nickname.getText().toString());
            }
        });
    }
}