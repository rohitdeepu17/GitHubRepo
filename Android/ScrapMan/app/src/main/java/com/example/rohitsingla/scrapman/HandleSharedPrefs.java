package com.example.rohitsingla.scrapman;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rohitsingla on 08/09/15.
 */
public class HandleSharedPrefs {
    public static void saveUsernameSharedPref(Context context, String key, String username){
        SharedPreferences mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEditor = mSharedPref.edit();
        mEditor.putString(key, username);
        mEditor.commit();
    }

    public static String getUsernameSharedPref(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String username = sp.getString("username","");
        return username;

    }
}
