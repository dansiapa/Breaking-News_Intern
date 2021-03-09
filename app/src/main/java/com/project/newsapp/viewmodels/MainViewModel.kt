package com.project.newsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.newsapp.model.News
import com.project.newsapp.model.TotalNews
import com.project.newsapp.restapi.ApiClient
import com.project.newsapp.restapi.RestInterface
import com.project.newsapp.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel : ViewModel() {
    val newsLiveData: MutableLiveData<List<News>?>
    private val newsList: MutableList<News>
    private var countryCode: String? = null
    private var apiKey: String? = null
    fun setApiKey(apiKey: String?) {
        this.apiKey = apiKey
    }

    fun setCountryCode(countryCode: String?) {
        this.countryCode = countryCode
        getNews(countryCode, "")
    }

    private val restInterface: RestInterface?
        private get() {
            val restInterface = arrayOfNulls<RestInterface>(1)
            restInterface[0] = ApiClient.getClient(Util.API_BASE_URL).create<RestInterface>(RestInterface::class.java)
            return restInterface[0]
        }

    private fun getNews(langCode: String?, category: String) {
        val restInterface = restInterface
        val call: Call<TotalNews?>?
        newsList.clear()
        newsLiveData.value = null
        call = if (category != "") {
            restInterface!!.getTotalNews(langCode, category, apiKey)
        } else {
            restInterface!!.getTotalNews(langCode, apiKey)
        }
        call!!.enqueue(object : Callback<TotalNews?> {
            override fun onResponse(call: Call<TotalNews?>, response: Response<TotalNews?>) {
                if (response.body() != null) {
                    val totalNews = response.body()
                    fillNewsList(totalNews)
                }
            }

            override fun onFailure(call: Call<TotalNews?>, t: Throwable) {
                newsLiveData.value = null
            }
        })
    }

    private fun getSearchedNews(keyword: String) {
        val restInterface = restInterface
        val call: Call<TotalNews?>?
        newsList.clear()
        newsLiveData.setValue(null)
        call = restInterface!!.getSearchedTotalNews(keyword, apiKey)
        call!!.enqueue(object : Callback<TotalNews?> {
            override fun onResponse(call: Call<TotalNews?>, response: Response<TotalNews?>) {
                if (response.body() != null) {
                    val totalNews = response.body()
                    fillNewsList(totalNews)
                }
            }

            override fun onFailure(call: Call<TotalNews?>, t: Throwable) {
                newsLiveData.setValue(null)
            }
        })
    }

    private fun fillNewsList(totalNews: TotalNews?) {
        newsList.addAll(totalNews!!.newsList!!)
        newsLiveData.value = newsList
    }

    fun newsCategoryClick(category: Any) {
        getNews(countryCode, category.toString())
    }

    fun searchNews(keyword: String) {
        getSearchedNews(keyword)
    }

    init {
        newsLiveData = MutableLiveData()
        newsList = ArrayList()
    }
}