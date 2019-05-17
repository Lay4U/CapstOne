package blacksmith.sullivanway.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.TextView;

import blacksmith.sullivanway.R;

public class CloudMessageDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_activity_cloud_message);
        getWindow().getAttributes().width = (int)(getResources().getDisplayMetrics().widthPixels * 0.8);
        getWindow().getAttributes().height = (int)(getResources().getDisplayMetrics().heightPixels * 0.4);

        String msg = getIntent().getStringExtra("message");

        // TextView
        TextView fcmMsgTextView = findViewById(R.id.textView);
        fcmMsgTextView.setMovementMethod(new ScrollingMovementMethod());
        fcmMsgTextView.setText(msg);

        // Button
        TextView button = findViewById(R.id.confirmButton);
        button.setOnClickListener(view -> finish());
    }

}
