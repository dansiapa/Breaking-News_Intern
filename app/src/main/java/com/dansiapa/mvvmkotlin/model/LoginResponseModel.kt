package com.dansiapa.mvvmkotlin.model


data class LoginResponseModel(
    val status: String,
    val userModel: UserModel
)