package com.example.ajoudongfe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity
{
    public static String BASE_URL= "http://10.0.2.2:8000";

    Button loginButton;
    TextInputLayout idLayout;
    TextInputLayout pwLayout;
    TextInputEditText idText;
    TextInputEditText pwText;
    RadioButton autoLogin;
    TextView findID;
    TextView findPW;
    TextView signup;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        idLayout = (TextInputLayout) findViewById(R.id.idLayout);
        pwLayout = (TextInputLayout) findViewById(R.id.pwLayout);
        idText = (TextInputEditText) findViewById(R.id.idInputText);
        pwText = (TextInputEditText) findViewById(R.id.pwInputText);
        autoLogin = (RadioButton) findViewById(R.id.autoLogin);
        findID = (TextView)findViewById(R.id.findID);
        findPW = (TextView)findViewById(R.id.findPW);
        signup = (TextView)findViewById(R.id.signup);



        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pref = getSharedPreferences("aldata", MODE_PRIVATE);
        editor = pref.edit();

        loginButton.setClickable(true);
        findID.setClickable(true);
        findPW.setClickable(true);
        signup.setClickable(true);

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                LoginObject loginObject = new LoginObject(idText.getText().toString(), pwText.getText().toString());

                RetroService retroService = retrofit.create(RetroService.class);
                Call<ResponseModel> call = retroService.login(loginObject);

                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        ResponseModel data = response.body();
                        if(data.getMessage() == 1)//로그인 성공
                        {
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        findID.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "아이디 찾기", Toast.LENGTH_LONG).show();
            }
        });

        findPW.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "비밀번호 찾기", Toast.LENGTH_LONG).show();
            }
        });

        signup.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_LONG).show();
            }
        });
    }
}

