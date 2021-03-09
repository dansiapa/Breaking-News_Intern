package com.project.newsapp.model

import com.google.gson.annotations.SerializedName

class UserModel {
    var personId:Int = 0
    var personName: String? = null
    var personEmail: String? = null
    var personPassword: String? = null

    @SerializedName("personDob")
    var personDob: String? = null
}