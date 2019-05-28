package com.tfg.hipersch;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    //  Ejemplo sin variables
    @GET("status")
    Call<ApiResponse> getApiStatus();

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
}
