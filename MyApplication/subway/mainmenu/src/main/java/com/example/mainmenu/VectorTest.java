package com.example.mainmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class VectorTest extends AppCompatActivity {
    // Pinch zoom will occurred on this image widget.
    private ImageView imageView = null;

    // Used to detect pinch zoom gesture.
    private ScaleGestureDetector scaleGestureDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_test);

        setTitle("dev2qa.com - Andriod Pinch Zoom Example");

        initControls();
    }

    private void initControls()
    {
        if(imageView == null)
        {
            // Get the image view in the layout xml.
            imageView = (ImageView)findViewById(R.id.pinch_image_view);
        }

        if(scaleGestureDetector == null)
        {
            // Create an instance of OnPinchListner custom class.
            OnPinchListener onPinchListener = new OnPinchListener(getApplicationContext(), imageView);

            // Create the new scale gesture detector object use above pinch zoom gesture listener.
            scaleGestureDetector = new ScaleGestureDetector(getApplicationContext(), onPinchListener);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Dispatch activity on touch event to the scale gesture detector.
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
