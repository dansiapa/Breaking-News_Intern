package com.project.newsapp.utils

import android.R
import android.app.Activity
import android.graphics.PorterDuff
import android.os.Build
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

object Util {
    const val APP_NAME = "NEWS_APP"
    const val API_BASE_URL = "http://newsapi.org/"
    const val COUNTRY_PREF = "countryPref"
    fun setSystemBarColor(act: Activity, @ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = act.resources.getColor(color)
        }
    }

    fun setSystemBarLight(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val view = act.findViewById<View>(R.id.content)
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
        }
    }

    fun changeMenuIconColor(menu: Menu, @ColorInt color: Int) {
        for (i in 0 until menu.size()) {
            val drawable = menu.getItem(i).icon ?: continue
            drawable.mutate()
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}