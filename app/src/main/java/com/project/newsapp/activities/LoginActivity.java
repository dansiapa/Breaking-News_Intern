package com.project.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.newsapp.R;
import com.project.newsapp.restapi.RetrofitInstance;
import com.project.newsapp.restapi.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private Button btn_login;
    private TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserService userService = RetrofitInstance.getRetrofitInstance().create(UserService.class);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_Password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_registrasi);

        tv_register.setOnClickListener(v->{
            Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(register);
        });

        btn_login.setOnClickListener(v -> {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();



            Call<LoginResponseModel> call = userService.login(email, password);
            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    LoginResponseModel loginResponseModel = response.body();
                    if(loginResponseModel.getStatus().equals("success")){

                        Toast.makeText(LoginActivity.this, "Success Login "+loginResponseModel.getUserModel().getPersonName(), Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(a);
                    }else {
                        Toast.makeText(LoginActivity.this, "Failed Login ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error Login "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
