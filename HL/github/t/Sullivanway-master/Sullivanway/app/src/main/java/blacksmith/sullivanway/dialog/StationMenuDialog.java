package blacksmith.sullivanway.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import blacksmith.sullivanway.R;
import blacksmith.sullivanway.utils.SubwayLine;

/**
 * 노선도에서 역 터치하면 뜨는 대화창
 * 역 정보, 출발역/도착역 선택가능
 */
public class StationMenuDialog extends Dialog {

    public StationMenuDialog(Context context, ArrayList<String> lineNms, String mStnNm) {
        super(context, R.style.NoTitleDialog);
        setContentView(R.layout.dialog_station_menu);
        setCancelable(true);
        TextView stnNm = findViewById(R.id.stnNm);
        stnNm.setText(mStnNm);

        LinearLayout lineSymLayout = findViewById(R.id.lineSymLayout);
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (String lineNm : lineNms) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(SubwayLine.getResId(lineNm));
            lineSymLayout.addView(imageView, params);
        }

        // Dialog 크기 설정
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ViewGroup.LayoutParams param = linearLayout.getLayoutParams();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        param.width = (int) (width * 0.7);
        param.height = (int) (height * 0.2);
        linearLayout.setLayoutParams(param);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        TextView start = findViewById(R.id.start);
        start.setOnClickListener(listener);
        TextView end = findViewById(R.id.end);
        end.setOnClickListener(listener);
        TextView info = findViewById(R.id.info);
        info.setOnClickListener(listener);
    }
}
