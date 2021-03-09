package com.project.newsapp.restapi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("login")
    Call<LoginResponseModel> login(@Query("email") String personEmail, @Query("password") String personPassword);

    @FormUrlEncoded
    @POST("register")
    Call<LoginResponseModel> register(@Field("email") String personEmail,
                                      @Field("name") String personName,
                                      @Field("dob") String personDob,
                                      @Field("password") String personPassword);

}
