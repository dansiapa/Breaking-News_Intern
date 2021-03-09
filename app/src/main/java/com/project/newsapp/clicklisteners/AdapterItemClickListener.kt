package com.project.newsapp.clicklisteners

import com.project.newsapp.model.News

interface AdapterItemClickListener {
    fun onNewsItemClick(news: News?)
}