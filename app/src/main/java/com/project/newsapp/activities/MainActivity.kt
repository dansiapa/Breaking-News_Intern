package com.project.newsapp.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.project.newsapp.R
import com.project.newsapp.adapters.AdapterListNews
import com.project.newsapp.clicklisteners.AdapterItemClickListener
import com.project.newsapp.clicklisteners.NewsDialogClickListeners
import com.project.newsapp.databinding.NewsDialogBinding
import com.project.newsapp.model.News
import com.project.newsapp.utils.LocaleHelper
import com.project.newsapp.utils.Util
import com.project.newsapp.viewmodels.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity(), LifecycleOwner, AdapterItemClickListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerView)
    var recyclerView: RecyclerView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivToolbarCountry)
    var ivToolbarCountry: ImageView? = null
    var context: MainActivity? = null
    var viewModel: MainViewModel? = null
    var adapterListNews: AdapterListNews? = null
    var newsList: MutableList<News>? = null
    private val firstControl = "firstControl"
    private val countryPositionPref = "countryPositionPref"
    var pref: SharedPreferences? = null
    private var countrys: Array<String>? = null
    private var countrysIcons: TypedArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = applicationContext.getSharedPreferences(Util.APP_NAME, MODE_PRIVATE)
        languageControl()
        setContentView(R.layout.activity_main)
        context = this
        ButterKnife.bind(this)
        countrys = resources.getStringArray(R.array.countrys)
        countrysIcons = resources.obtainTypedArray(R.array.countrysIcons)
        initToolbar()
        newsList = ArrayList()
        adapterListNews = AdapterListNews(newsList as ArrayList<News>, this)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = adapterListNews
        if (pref!!.contains(countryPositionPref)) ivToolbarCountry!!.setImageResource(countrysIcons!!.getResourceId(pref!!.getInt(countryPositionPref, 0), 0))
        viewModel = ViewModelProviders.of(context!!).get(MainViewModel::class.java)
        viewModel!!.newsLiveData.observe(context!!, newsListUpdateObserver)
        viewModel!!.setApiKey(getString(R.string.news_api_key))
        viewModel!!.setCountryCode(pref!!.getString(Util.COUNTRY_PREF, "tr"))
    }

    private fun languageControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !pref!!.getBoolean(firstControl, false)) {
            val primaryLocale = resources.configuration.locales[0]
            LocaleHelper.setLocale(this@MainActivity, primaryLocale.language)
            val position = getLanguagePosition(primaryLocale.language)
            pref!!.edit().putInt(countryPositionPref, position).apply()
            pref!!.edit().putBoolean(firstControl, true).apply()
            recreate()
        }
    }

    private fun getLanguagePosition(displayLanguage: String): Int {
        val codes = resources.getStringArray(R.array.countrysCodes)
        for (i in codes.indices) {
            if (codes[i] == displayLanguage) return i
        }
        return 0
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //getSupportActionBar().setTitle(null);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Util.setSystemBarColor(this, android.R.color.white)
        Util.setSystemBarLight(this)
    }

    private fun showLanguageDialog() {
        AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Choose Country")
                .setSingleChoiceItems(countrys, pref!!.getInt(countryPositionPref, 0), null)
                .setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
                .setPositiveButton(R.string.ok) { dialog, whichButton ->
                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    pref!!.edit().putInt(countryPositionPref, selectedPosition).apply()
                    pref!!.edit().putString(Util.COUNTRY_PREF, resources.getStringArray(R.array.countrysCodes)[selectedPosition]).apply()
                    LocaleHelper.setLocale(this@MainActivity, resources.getStringArray(R.array.countrysCodes)[selectedPosition])
                    recreate()
                    dialog.dismiss()
                }
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        Util.changeMenuIconColor(menu, Color.BLACK)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = this@MainActivity.getSystemService(SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))
        searchView!!.queryHint = getString(R.string.search_in_everything)
        if (searchView != null) searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (viewModel != null) viewModel!!.searchNews(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    fun categoryClicked(view: View) {
        viewModel!!.newsCategoryClick(view.tag.toString())
    }

    fun countryClick(view: View?) {
        showLanguageDialog()
    }

    var newsListUpdateObserver: Observer<List<News>> = Observer { news ->
        newsList!!.clear()
        if (news != null) {
            newsList!!.addAll(news)
        }
        adapterListNews!!.notifyDataSetChanged()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onNewsItemClick(news: News?) {
        news?.let { showDialogPolygon(it) }
    }

    private fun showDialogPolygon(news: News?) {
        val dialog = Dialog(this)
        val binding: NewsDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(applicationContext), R.layout.dialog_header_polygon, null, false)
        binding.news = news
        binding.listener = object : NewsDialogClickListeners {
            override fun onGotoWebSiteClick(url: String?) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            override fun onDismissClick() {
                dialog.dismiss()
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()
    }
}