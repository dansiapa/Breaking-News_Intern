package com.dansiapa.mvvmkotlin.model

data class UserModel(
    val userId: Int,
    val userName: String,
    val userEmail: String,
    val userPassword:String,
    val userDob: String
)
