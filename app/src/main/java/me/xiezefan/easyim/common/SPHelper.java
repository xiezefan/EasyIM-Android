package me.xiezefan.easyim.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.xiezefan.easyim.Application;

/**
 * Shared Preferences Helper
 * Created by XieZeFan on 2015/4/12 0012.
 */
public class SPHelper {
    /*----KEY----*/
    public static final String USER_ID = "USER_ID";
    public static final String USERNAME = "USERNAME";
    public static final String NICKNAME = "NICKNAME";
    public static final String USER_AVATAR = "USER_AVATAR";
    public static final String USER_DESCRIPTION = "USER_DESCRIPTION";
    public static final String USER_LOCATION = "USER_LOCATION";
    public static final String USER_SEX = "USER_SEX";

    public static final String AUTH_CODE = "AUTH_CODE";
    public static final String REGISTER_ID = "REGISTER_ID";

    public static final String FILE_UPLOAD_TOKEN = "FILE_UPLOAD_TOKEN";

    // System Profile
    public static final String NOTIFICATION_VIBRATION = "NOTIFICATION_VIBRATION";
    public static final String NOTIFICATION_VOICE = "NOTIFICATION_VOICE";

    public static void clearAll() {
        getPreferences().edit().clear().commit();
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    public static boolean setString(String key, String value) {
        return getPreferences().edit().putString(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    public static boolean setBoolean(String key, boolean value) {
        return getPreferences().edit().putBoolean(key, value).commit();
    }

    public static int getInt(String key) {
        return getInt(key, -1);
    }

    public static int getInt(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    public static boolean setInt(String key, int value) {
        return getPreferences().edit().putInt(key, value).commit();
    }

    private static SharedPreferences preferences;
    private static SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(Application.getInstance());
        }
        return preferences;
    }
}
