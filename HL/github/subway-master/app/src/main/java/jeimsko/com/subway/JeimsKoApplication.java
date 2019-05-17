package jeimsko.com.subway;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.HashMap;

/**
 * Created by jeimsko on 2016. 9. 9..
 */

public class JeimsKoApplication extends Application {

    private static Context mContext;
    private Handler mMainHandler = null;

    protected static JeimsKoApplication sSingleton;

    private static HashMap<String, Object> mMemoyPrefMap; // 메모리에 임시저장 하여 사용할것

    public static JeimsKoApplication getInstance() {
        return sSingleton;
    }
    private ActivityManager mActivityManager;

    @Override
    public void onCreate(){
        super.onCreate();
        sSingleton = this;
        JeimsKoApplication.mContext = getApplicationContext();
        mMainHandler = new Handler();

        initializeCookie(this);                   //쿠키 초기화
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    public static void initializeCookie(Context context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();

    }

    public static HashMap<String, Object> getmMemoyPrefMap(){

        return mMemoyPrefMap;
    }

    public static Context getAppContext() {
        return JeimsKoApplication.mContext;
    }

    public Handler getMainHandler() {
        if(this.mMainHandler == null) {
            this.mMainHandler = new Handler();
        }

        return this.mMainHandler;
    }

    public ActivityManager getActivityManager() {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

}
