package blacksmith.sullivanway.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import blacksmith.sullivanway.database.Station;

public class SubwayMapTouchPoint {
    @SuppressWarnings("FieldCanBeLocal")
    private final float INTERVAL = 8.5f; //역 터치했을 때 범위
    private Context context;

    public Station startStn = null; //팝업메뉴로 선택된 출발역 StnPoint
    public Station endStn = null; //팝업메뉴로 선택된 도착역 StnPoint

    public SubwayMapTouchPoint(Context context) {
        this.context = context;
    }

    public Station getStation(ArrayList<Station> stnIdx, float left, float top, float scale, float x, float y) {
        /* 계산방법: 화면상 터치한 위치 + view의 Rect값으로 '이미지상 터치한 위치'(resX, resY) 계산한다.
         * 이유:     화면을 오른쪽, 아래쪽으로 이동할 수록 getDisplayRect().left와 top이 감소되기 때문에
         *           '화면상 터치한 위치'(x, y)에서 '전체 이미지상 화면의 위치'(left, top) 값을 뺀다.  */
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float resX = (x - left) / scale / metrics.density;
        float resY = (y - top) / scale / metrics.density;
        CircleF range = new CircleF(INTERVAL, resX, resY);

        // 이미지상 터치한 위치로 StnPoints 상수목록에서 역명 찾기
        for (Station stn : stnIdx)
            if (range.contains(stn.getPointx(), stn.getPointy())) //터치한 좌표에 역이 있는 경우....
                return stn; //역 반환
        //Toast.makeText(context, resX + " / " + resY, Toast.LENGTH_SHORT).show();
        return null;
    }

    public Station getStation(ArrayList<Station> stnIdx, String mLineNm, String mStnNm) {
        for (Station stn : stnIdx) {
            if (stn.getLineNm().equals(mLineNm))
                if (stn.getStnNm().equals(mStnNm))
                    return stn;
        }
        return null;
    }

    public ArrayList<String> getLineNms(ArrayList<Station> stnIdx, Station mStn) {
        ArrayList<String> lineNms = new ArrayList<>();
        for (Station stn : stnIdx) {
            if (stn.getPointx() == mStn.getPointx() && stn.getPointy() == mStn.getPointy())
                lineNms.add(stn.getLineNm());
        }
        return lineNms;
    }

    public ArrayList<Integer> getIndexes(ArrayList<Station> stnIdx, Station mStn) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0, n = stnIdx.size(); i < n; i++) {
            Station stn = stnIdx.get(i);
            if (stn.getPointx() == mStn.getPointx() && stn.getPointy() == mStn.getPointy())
                indexes.add(i);
        }
        return indexes;
    }


    private class CircleF {
        float r, x, y;
        CircleF(float r, float x, float y) {
            this.r = r;
            this.x = x;
            this.y = y;
        }

        boolean contains(int x1, int y1) {
            float dx = x - x1, dy = y - y1;
            float distance = dx * dx + dy * dy;

            return distance < r * r;
        }
    }

}
