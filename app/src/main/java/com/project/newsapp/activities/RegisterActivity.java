package com.project.newsapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.newsapp.R;
import com.project.newsapp.restapi.RetrofitInstance;
import com.project.newsapp.restapi.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_username;
    private EditText et_dob;
    private EditText et_password;
    private Button btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        et_dob = findViewById(R.id.et_dob);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_Password);
        et_username = findViewById(R.id.et_name);
        btn_signup = findViewById(R.id.btn_signup);

        UserService userService = RetrofitInstance.getRetrofitInstance().create(UserService.class);

        btn_signup.setOnClickListener(v->{
            String email = et_email.getText().toString();
            String userName = et_username.getText().toString();
            String dob = et_dob.getText().toString();
            String password = et_password.getText().toString();

            Call<LoginResponseModel> call = userService.register(email, userName, dob, password);
            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    LoginResponseModel loginResponseModel = response.body();
                    if(loginResponseModel.getStatus().equals("success")){
                        Toast.makeText(RegisterActivity.this, "Success Register "+loginResponseModel.getUserModel().getPersonName(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Failed Register "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}