package com.project.newsapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.project.newsapp.R


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            //finish this activity
            finish()
        },4000)
    }
}