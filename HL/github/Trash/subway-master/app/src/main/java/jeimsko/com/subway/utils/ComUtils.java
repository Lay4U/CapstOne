package jeimsko.com.subway.utils;

import android.util.Log;

/**
 * Created by jeimsko on 2016. 9. 9..
 */

public class ComUtils {

    public static void printLog(String tag, String strLog) {

//        //+ 운영계의 경우 로그 출력 x
//        if (Conf.Log_Use) {
//            return;
//        }

        Log.v(tag, strLog);
    }
}
