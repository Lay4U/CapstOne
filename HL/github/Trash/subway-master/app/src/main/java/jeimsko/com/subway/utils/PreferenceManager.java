package jeimsko.com.subway.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import jeimsko.com.subway.JeimsKoApplication;

/**
 * Created by jeimsko on 2016. 9. 9..
 */

public class PreferenceManager {


    public static SharedPreferences GetSharedPreference() {
        final Context context = JeimsKoApplication.getInstance().getAppContext();
        SharedPreferences pref = context.getSharedPreferences("subway", Context.MODE_PRIVATE);
        return pref;
    }


    private static SharedPreferences.Editor GetSharedPreferenceEditor() {
        SharedPreferences pref = GetSharedPreference();
        SharedPreferences.Editor edit = pref.edit();
        return edit;
    }

    public static void put(String key, String value) {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.putString(key, value);
        edit.commit();
    }
    public static String getString(String key, String defaultVal) {
        SharedPreferences pref = GetSharedPreference();
        return pref.getString(key, defaultVal);
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key, boolean value) {
        SharedPreferences pref = GetSharedPreference();
        return pref.getBoolean(key, value);
    }

    public static void put(String key, int value) {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(String key, int value) {
        SharedPreferences pref = GetSharedPreference();
        return pref.getInt(key, value);
    }

    public static void put(String key, long value) {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(String key, long value) {
        SharedPreferences pref = GetSharedPreference();
        return pref.getLong(key, value);
    }

    public static void put(String key, float value) {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static float getFloat(String key, float value) {
        SharedPreferences pref = GetSharedPreference();
        return pref.getFloat(key, value);
    }

    public static void clearData() {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.clear();
        edit.commit();
    }

    public static void remove(String key) {
        SharedPreferences.Editor edit = GetSharedPreferenceEditor();
        edit.remove(key);
        edit.commit();
    }
    public static boolean isEmpty() {
        SharedPreferences pref = GetSharedPreference();
        Map<String, ?> map = pref.getAll();
        if(map != null && map.size() > 0){
            return false;
        } else {
            return true;
        }
    }


}
