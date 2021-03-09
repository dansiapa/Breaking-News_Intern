package com.dansiapa.mvvmkotlin.repo

import com.dansiapa.mvvmkotlin.model.LoginResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("user/login")
    fun login(@Query("email") email: String, @Query("password") password: String): Single<LoginResponseModel>
    fun register(
        @Query("email")email: String,
        @Query("password")password: String,
        @Query("dob")dob:String,
        @Query("name")name:String
    )
}