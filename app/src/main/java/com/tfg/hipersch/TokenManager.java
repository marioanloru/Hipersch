package com.tfg.hipersch;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    public final static String SHARED_PREF_NAME = "hipersch.SHARED_PREF";
    public final static String TOKEN_KEY = "hipersch.TOKEN_KEY";
    public final static Strin TOKEN_ATHLETE_KEY = "hipersch.TOKEN_ATHLETE_KEY";

    public static String getToken(Context c) {
        System.out.println("El contextooo!!! --> " + c.toString());
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static String getAthleteToken(Context c) {
        System.out.println("El contextooo!!! --> " + c.toString());
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_ATHLETE_KEY, "");
    }

    public static void setToken(Context c, String token) {
        System.out.println("Seteo token ---> " + token);
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static void setAthleteToken(Context c, String token) {
        System.out.println("Seteo token ---> " + token);
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_ATHLETE_KEY, token);
        editor.apply();
    }
}
