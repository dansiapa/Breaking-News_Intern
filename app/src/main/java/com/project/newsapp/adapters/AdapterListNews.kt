package com.project.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.newsapp.R
import com.project.newsapp.adapters.AdapterListNews.NewsViewHolder
import com.project.newsapp.clicklisteners.AdapterItemClickListener
import com.project.newsapp.databinding.NewsBinding
import com.project.newsapp.model.News

class AdapterListNews(private val items: List<News>, private val adapterItemClickListener: AdapterItemClickListener) : RecyclerView.Adapter<NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsBinding: NewsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_news_dashboard, parent, false)
        return NewsViewHolder(newsBinding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), adapterItemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): News {
        return items[position]
    }

    inner class NewsViewHolder(private val newsBinding: NewsBinding) : RecyclerView.ViewHolder(newsBinding.root) {
        fun bind(news: News?, adapterItemClickListener: AdapterItemClickListener?) {
            newsBinding.news = news
            newsBinding.clickListener = adapterItemClickListener
        }
    }
}