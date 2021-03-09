package com.dansiapa.mvvmkotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dansiapa.mvvmkotlin.R
import com.dansiapa.mvvmkotlin.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_email.text.toString()
        et_Password.text.toString()
        tv_registrasi.setOnClickListener{
            val click = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(click)
        }
        btn_login.setOnClickListener{
            val a = Intent(this@MainActivity,HomeActivity::class.java)
            startActivity(a)
            loginViewModel.login(et_email.text.toString(), et_Password.text.toString())
        }

        setObserver()
    }

    private fun setObserver() {

        loginViewModel.getLoginResponseModel().observe(this, Observer {
            if(it != null){
                Toast.makeText(this, "Selamat Datang"+it.userModel.userName, Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.getErrorListener().observe(this, Observer {
            if(it){
                Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun login(email: String, password: String = "default"): String{
        return password
    }
}