package com.example.ajoudongfe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    CheckBox autoLogin;
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
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);
        findID = (TextView)findViewById(R.id.findID);
        findPW = (TextView)findViewById(R.id.findPW);
        signup = (TextView)findViewById(R.id.signup);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pref = getSharedPreferences("autologin", MODE_PRIVATE);
        editor = pref.edit();

        loginButton.setClickable(true);
        findID.setClickable(true);
        findPW.setClickable(true);
        signup.setClickable(true);

        if(pref.getBoolean("Auto_Login_Enabled", false))//자동로그인
        {
            Toast.makeText(getApplicationContext(), pref.getString("ID", "") + "&"+ pref.getString("PW", ""), Toast.LENGTH_LONG).show();//테스트용 파일
        }

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
                        System.out.println(data.getResponse());
                        if(data.getResponse() == 1)//사용자
                        {
                            Toast.makeText(getApplicationContext(), "사용자 로그인 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                            if(autoLogin.isChecked()) {
                                startActivity(intent);
                                editor.putBoolean("Auto_Login_Enabled", true);
                                editor.putString("ID", idText.getText().toString());
                                editor.putString("PW", pwText.getText().toString());
                                editor.commit();
                            }
                            startActivity(intent);
                        }
                        else if(data.getResponse() == 2)//간부
                        {
                            Toast.makeText(getApplicationContext(), "동아리 간부 로그인 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                            if(autoLogin.isChecked()) {
                                startActivity(intent);
                                editor.putBoolean("Auto_Login_Enabled", true);
                                editor.putString("ID", idText.getText().toString());
                                editor.putString("PW", pwText.getText().toString());
                                editor.commit();
                            }
                            startActivity(intent);
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

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!(autoLogin.isChecked())){
                    editor.clear();
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "자동 로그인 해제", Toast.LENGTH_LONG).show();
                }
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

