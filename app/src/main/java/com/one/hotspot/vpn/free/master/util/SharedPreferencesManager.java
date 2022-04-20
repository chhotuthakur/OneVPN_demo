package com.one.hotspot.vpn.free.master.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {


    private static SharedPreferences mSharedPref;


    synchronized public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static String getString(String key, String extra) {

        return mSharedPref.getString(key, extra);
    }



}
