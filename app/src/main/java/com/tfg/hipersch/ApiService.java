package com.tfg.hipersch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    //  Ejemplo sin variables
    @GET("status")
    Call<ApiResponse> getApiStatus();

    @FormUrlEncoded
    @POST("user/login")
    Call<ApiResponse> userLogin(@Field("username") String username,
                                @Field("password") String password);
    //  Ejemplo con variables
    /*@GET("status/{uid}")
    Call<List<Response>> getApiStatusParam(@Path("uid") String uid);*/

    //  Ejemplo de peticion POST
    /*@POST("user/login")
    Call<User> userLogin(@Body User user);*/
}
