package com.project.newsapp.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class News {
    @SerializedName("source")
    var source: Source? = null

    @SerializedName("title")
    var newsTitle: String? = null

    @SerializedName("description")
    var newsDescription: String? = null

    @SerializedName("url")
    var newsUrl: String? = null

    @SerializedName("urlToImage")
    var newsImage: String? = null

    @SerializedName("publishedAt")
    private var newsPublishedDate: Date? = null
    fun getNewsPublishedDate(): String? {
        return SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(newsPublishedDate)
    }

    fun setNewsPublishedDate(newsPublishedDate: Date?) {
        this.newsPublishedDate = newsPublishedDate
    }

    //Added for Child JSON Object
    inner class Source {
        @SerializedName("name")
        var sourceName: String? = null
    }

    companion object {
        //Image Binding - I didn't write newsviewmodel for just this method
        @BindingAdapter("bind:imgUrl")
        fun setImage(imageView: ImageView, imgUrl: String?) {
            Glide.with(imageView.context).load(imgUrl).into(imageView)
        }
    }
}