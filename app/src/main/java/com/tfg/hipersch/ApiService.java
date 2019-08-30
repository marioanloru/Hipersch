package com.tfg.hipersch;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("status")
    Call<ApiResponse> getApiStatus();

    //  User routes
    @GET("user/data")
    Call<ApiResponse> getUserData(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("user/data")
    Call<ApiResponse> updateUserData(@Header("Authorization") String token,
                                @Field("height") String height,
                                @Field("bodyWeight") String bodyWeight);

    @FormUrlEncoded
    @POST("user/login")
    Call<ApiResponse> userLogin(@Field("email") String email,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("user/login")
    Call<ApiResponse> userLoginTrainer(@Field("email") String email,
                                @Field("password") String password,
                                @Field("athlete") String athlete);

    @FormUrlEncoded
    @POST("user/register")
    Call<ApiResponse> userRegister(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("firstName") String name,
                                   @Field("lastName") String lastName,
                                   @Field("bodyWeight") String bodyWeight,
                                   @Field("height") String height,
                                   @Field("gender") String gender,
                                   @Field("role") String role,
                                   @Field("swimmingCategory") String swimmingCategory);

    @GET("user/athletes")
    Call<List<ApiResponse>> getUserAthletes(@Header("Authorization") String token);


    //  Running routes
    @GET("running/test/{limit}/{offset}")
    Call<List<ApiResponse>> getRunningTests(@Header("Authorization") String token,
                                            @Path("limit") String limit,
                                            @Path("offset") String offset);
    @GET("running/progress")
    Call<List<ApiResponse>> getRunningProgress(@Header("Authorization") String token);


    @GET("running/trainingZone")
    Call<ApiResponse> getRunningTrainingZone(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("running/test")
    Call<ApiResponse> sendRunningTest(@Header("Authorization") String token,
                                      @Field("distance") String distance);

    @DELETE("running/test/{testId}")
    Call<ApiResponse> deleteRunningTest(@Header("Authorization") String token,
                                        @Path("testId") String testId);
    //  Cycling routes
    @GET("cycling/test/{limit}/{offset}")
    Call<List<ApiResponse>> getCyclingTests(@Header("Authorization") String token,
                                            @Path("limit") String limit,
                                            @Path("offset") String offset);

    @GET("cycling/progress")
    Call<List<ApiResponse>> getCyclingProgress(@Header("Authorization") String token);



    @GET("cycling/test/sixsec")
    Call<List<ApiResponse>> getCyclingTestsSixSec(@Header("Authorization") String token);

    @GET("cycling/test/onemin")
    Call<List<ApiResponse>> getCyclingTestsOneMin(@Header("Authorization") String token);

    @GET("cycling/test/sixmin")
    Call<List<ApiResponse>> getCyclingTestsSixMin(@Header("Authorization") String token);

    @GET("cycling/test/twentymin")
    Call<List<ApiResponse>> getCyclingTestsTwentyMin(@Header("Authorization") String token);

    @GET("cycling/trainingZone")
    Call<ApiResponse> getCyclingTrainingZone(@Header("Authorization") String token);

    @DELETE("cycling/test/{testId}")
    Call<ApiResponse> deleteCyclingTest(@Header("Authorization") String token,
                                        @Path("testId") String testId);


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
    @GET("swimming/test/{limit}/{offset}")
    Call<List<ApiResponse>> getSwimmingTests(@Header("Authorization") String token,
                                             @Path("limit") String limit,
                                             @Path("offset") String offset);

    @GET("swimming/progress")
    Call<List<ApiResponse>> getSwimmingProgress(@Header("Authorization") String token);


    @GET("swimming/trainingZone")
    Call<ApiResponse> getSwimmingTrainingZone(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("swimming/test")
    Call<ApiResponse> sendSwimmingTest(@Header("Authorization") String token,
                                       @Field("timeFourHundred") String timeFourHundred,
                                       @Field("timeTwoHundred") String timeTwoHundred);

    @DELETE("swimming/test/{testId}")
    Call<ApiResponse> deleteSwimmingTest(@Header("Authorization") String token,
                                        @Path("testId") String testId);
}
