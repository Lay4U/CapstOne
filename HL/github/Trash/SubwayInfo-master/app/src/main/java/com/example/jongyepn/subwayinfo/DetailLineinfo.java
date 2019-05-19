package com.example.jongyepn.subwayinfo;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DetailLineinfo extends AppCompatActivity {

    Variable variable;

    TextView PreiviousStation;
    TextView NextStation;
    TextView Down1;
    TextView Down2;
    TextView Up1;
    TextView Up2;

    TextView Information; // 상행선 제일 가까운거 정보
    TextView Information2; // 하행선 제일 가까운거 정보

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stationinfo);
            //usl 1번만 가능
            // information 1~4
            Information = findViewById(R.id.Information);
            Information.setText("온도: " + variable.getField1() + "%\n습도: " + variable.getField2() + "%\n미세먼지: " + variable.getField3() + "%\n혼잡도: " + variable.getField4() + "명");

            //information 5~8
            Information2 = findViewById(R.id.Information2);
            Information2.setText("온도: " + variable.getField5() + "%\n습도: " + variable.getField6() + "%\n미세먼지: " + variable.getField7() + "%\n혼잡도: " + variable.getField8() + "명");

            //Intent intent = getIntent();
            String infostation = variable.getSubwayInfo().get(1).getStatnNm();  //
            PreiviousStation = findViewById(R.id.Previous_Station);
            NextStation = findViewById(R.id.Next_Station);
            Down1 = findViewById(R.id.Down1);
            Down2 = findViewById(R.id.Down2);
            Up1 = findViewById(R.id.Up1);
            Up2 = findViewById(R.id.Up2);
            TextView staioninfotext = (TextView) findViewById(R.id.centertext);   // 현재역 넣은거
            staioninfotext.setText(infostation);

            Log.e("지하철6", variable.getUPSubwayInfo().get(1).getStatnNm());   //이게 혜화잖아
            Log.e("지하철6", infostation);   //이게 혜화잖아

            Log.e("지하철1", variable.getDNSubwayInfo().get(0).getArvlMsg3() + "  " + variable.getDNSubwayInfo().get(0).getArvlMsg2());
            Log.e("지하철2", variable.getDNSubwayInfo().get(1).getArvlMsg3() + "  " + variable.getDNSubwayInfo().get(1).getArvlMsg2());
            Log.e("지하철3", variable.getUPSubwayInfo().get(0).getArvlMsg3() + "  " + variable.getUPSubwayInfo().get(0).getArvlMsg2());
            Log.e("지하철4", variable.getUPSubwayInfo().get(1).getArvlMsg3() + "  " + variable.getUPSubwayInfo().get(1).getArvlMsg2());


            Log.e("지하철5", variable.getLine4().get(0));
            Log.e("지하철5", variable.getLine4().get(1));
            Log.e("지하철5", variable.getLine4().get(2));
            Log.e("지하철5", variable.getLine4().get(3));


            for (int i = 0; i < variable.getLine4().size(); i++) {  // 0은 제외로함 지금 왜냐면 첫역은 따로처리해야하니까 걍 넣었음 길음역 추가해서
                Log.e("지하철", String.valueOf(variable.getLine4().size()));
                Log.e("지하철", String.valueOf(infostation.equals(variable.getLine4().get(i))));
                if (infostation.equals(variable.getLine4().get(i))) {  // 한성대 였을때
                    PreiviousStation.setText(variable.getLine4().get(i - 1));  // 성신여대
                    Log.e("지하철", PreiviousStation.getText().toString());
                    NextStation.setText(variable.getLine4().get(i + 1));  // 혜화
                }
            }


            Down1.setText(variable.getDNSubwayInfo().get(0).getArvlMsg2() + variable.getDNSubwayInfo().get(0).getBarvlDt() + "초 후 역도착");
            Down2.setText(variable.getDNSubwayInfo().get(1).getArvlMsg2() + variable.getDNSubwayInfo().get(1).getBarvlDt() + "초 후 역도착");

            Up1.setText(variable.getUPSubwayInfo().get(0).getArvlMsg2() + variable.getUPSubwayInfo().get(0).getBarvlDt() + "초 후 역도착");
            Up2.setText(variable.getUPSubwayInfo().get(1).getArvlMsg2() + variable.getUPSubwayInfo().get(1).getBarvlDt() + "초 후 역도착");
//
//            Down1.setText(variable.getDNSubwayInfo().get(0).getArvlMsg3() + "  " + variable.getDNSubwayInfo().get(0).getArvlMsg2());
//            Down2.setText(variable.getDNSubwayInfo().get(1).getArvlMsg3() + "  " + variable.getDNSubwayInfo().get(1).getArvlMsg2());
//            Up1.setText(variable.getUPSubwayInfo().get(0).getArvlMsg3() + "  " + variable.getUPSubwayInfo().get(0).getArvlMsg2());
//            Up2.setText(variable.getUPSubwayInfo().get(1).getArvlMsg3() + "  " + variable.getUPSubwayInfo().get(1).getArvlMsg2());


        } catch (Exception e) {
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(variable.getAllSubwayInfo() == null){
//
//        }else{
//            variable.getAllSubwayInfo().clear();  // 모든데이터 지우기
//            variable.getUPSubwayInfo().clear();  // 상행 지우기
//            variable.getDNSubwayInfo().clear();  // 하행 지우기

    }

}
