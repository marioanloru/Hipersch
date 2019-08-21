package com.tfg.hipersch;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenManager {
    public final static String SHARED_PREF_NAME = "hipersch.SHARED_PREF";
    public final static String TOKEN_KEY = "hipersch.TOKEN_KEY";
    public final static String TOKEN_ATHLETE_KEY = "hipersch.TOKEN_ATHLETE_KEY";

    private String password;
    private String email;

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

    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }

    public void updateToken() {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        Call<ApiResponse> call = apiService.userLogin(this.email, this.password);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    TokenManager.setToken(this, apiResponse.getToken());
                    apiResponse.getToken();
                } else {
                    System.out.println("Something failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Error:", t.getMessage());
            }
        });
    }


}
