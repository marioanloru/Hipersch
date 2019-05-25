package com.tfg.hipersch;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenSaver {
    public final static String SHARED_PREF_NAME = "hipersch.SHARED_PREF";
    public final static String TOKEN_KEY = "hipersch.TOKEN_KEY";

    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }
}
