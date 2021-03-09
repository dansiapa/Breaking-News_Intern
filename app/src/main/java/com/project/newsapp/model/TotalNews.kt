package com.project.newsapp.model

import com.google.gson.annotations.SerializedName

class TotalNews {
    var status: String? = null

    @SerializedName("totalResults")
    var totalNewsCount:Int = 0

    @SerializedName("articles")
    var newsList: List<News>? = null

}