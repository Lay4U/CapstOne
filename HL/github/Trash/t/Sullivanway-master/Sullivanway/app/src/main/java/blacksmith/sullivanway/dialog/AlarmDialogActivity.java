package blacksmith.sullivanway.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import blacksmith.sullivanway.R;

public class AlarmDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_activity_alarm);
        getWindow().getAttributes().width = (int)(getResources().getDisplayMetrics().widthPixels * 0.7);
        getWindow().getAttributes().height = (int)(getResources().getDisplayMetrics().heightPixels * 0.2);

        String msg = getIntent().getStringExtra("message");

        // TextView
        TextView textView = findViewById(R.id.textView);
        textView.setText(msg);//도착 지하철정보 설정

        // Button
        TextView button = findViewById(R.id.confirmButton);
        button.setOnClickListener(view -> finish());
    }

}
