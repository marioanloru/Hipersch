package com.tfg.hipersch;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @GET("status")
    Call<ApiResponse> getApiStatus();

    //  User routes
    @GET("user/data")
    Call<ApiResponse> getUserData(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("user/login")
    Call<ApiResponse> userLogin(@Field("email") String email,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<ApiResponse> userRegister(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("firstName") String name,
                                   @Field("lastName") String lastName,
                                   @Field("bodyWeight") String bodyWeight,
                                   @Field("height") String height,
                                   @Field("gender") String gender,
                                   @Field("role") String role);


    //  Running routes
    @GET("running/test")
    Call<List<ApiResponse>> getRunningTests(@Header("Authorization") String token);


    @FormUrlEncoded
    @POST("running/test")
    Call<ApiResponse> sendRunningTest(@Header("Authorization") String token,
                                      @Field("distance") String distance);

    //  Cycling routes
    @GET("cycling/test")
    Call<List<ApiResponse>> getCyclingTests(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("cycling/test/sixsec")
    Call<ApiResponse> sendCyclingSixSecTest(@Header("Authorization") String token,
                                            @Field("peakPower") String peakPower);

    @FormUrlEncoded
    @POST("cycling/test/onemin")
    Call<ApiResponse> sendCyclingOneMinTest(@Header("Authorization") String token,
                                            @Field("peakPower") String peakPower);

    @FormUrlEncoded
    @POST("cycling/test/sixmin")
    Call<ApiResponse> sendCyclingSixMinTest(@Header("Authorization") String token,
                                            @Field("peakPower") String peakPower);

    @FormUrlEncoded
    @POST("cycling/test/twentymin")
    Call<ApiResponse> sendCyclingTwentyMinTest(@Header("Authorization") String token,
                                               @Field("peakPower") String peakPower);

    //  Swimming routes
    @GET("swimming/test")
    Call<List<ApiResponse>> getSwimmingTests(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("swimming/test")
    Call<ApiResponse> sendSwimmingTest(@Header("Authorization") String token,
                                       @Field("timeFourHundred") String timeFourHundred,
                                       @Field("timeTwoHundred") String timeTwoHundred);
}
