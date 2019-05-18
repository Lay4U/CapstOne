

package com.example.test4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MainActivity extends AppCompatActivity {
    private static final String MODEL_FILE = "file:///android_asset/model.pb";      //학습모델
    private static final String INPUT_NODE = "Input";       //input 텐서 이름
    private static final String OUTPUT_NODES = "Output";    //output 텐서 이름
    private static final String[] OUTPUT_NODE = {"output:0"};   //저장되는거같음
    private static final int INPUT_SIZE = 1;        //값하나받아서
    private static final int OUTPUT_SIZE = 1;       //값하나출력
    TensorFlowInferenceInterface inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);    //tensorflow interface



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        float[] result = new float[OUTPUT_SIZE];

        final EditText editNum1 = (EditText) findViewById(R.id.editText);   //사용자 입력값 받아서
        float num1 = Float.parseFloat(editNum1.getText().toString());       //정수형으로 만들고


        inferenceInterface.feed(INPUT_NODE, new float[]{num1}, 1, INPUT_SIZE);  //feed -> 값을 줘서
        inferenceInterface.run(OUTPUT_NODE);        //실행시키고
        inferenceInterface.fetch(OUTPUT_NODES, result); //결과 값은 result에 저장

    }
}

//
//package com.example.test4;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.EditText;
//
//import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
//
//public class MainActivity extends AppCompatActivity {
//    private static final String INPUT_NODE = "input:0"; // input tensor name
//    private static final String OUTPUT_NODE = "output:0"; // output tensor name
//    private static final String[] OUTPUT_NODES = {"output:0"};
//    private static final String MODEL_FILE = "file:///android_asset/model.pb";
//    private static final int OUTPUT_SIZE = 10; // number of classes
//    private static final int INPUT_SIZE = 784; // size of the input
//    float[] result = new float[OUTPUT_SIZE];
//    TensorFlowInferenceInterface inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);
//
//    inferenceInterface.feed(INPUT_NODE, INPUT_IMAGE, 1, INPUT_SIZE); //1-D input (1,INPUT_SIZE)
//    inferenceInterface.run(OUTPUT_NODES);
//    inferenceInterface.fetch(OUTPUT_NODE, result);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        final EditText editNum1 = (EditText) findViewById(R.id.editNum1);
//
//        float num1 = Float.parseFloat(editNum1.getText().toString());
//
//        float inputFloats = num1;
//
//        inferenceInterface.
//                inferenceInterface.fillNodeFloat(INPUT_NODE, INPUT_SIZE, inputFloats);
//
//        inferenceInterface.runInference(new String[] {OUTPUT_NODE});
//    }
//}
