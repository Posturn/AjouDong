package com.example.ajoudongfe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthActivity extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    Retrofit retrofit;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        pref = getSharedPreferences("autologin", MODE_PRIVATE);
        editor = pref.edit();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(pref.getBoolean("Auto_Login_Enabled", false))//자동로그인
        {
            Toast.makeText(getApplicationContext(), pref.getString("ID", "") + "&"+ pref.getString("PW", ""), Toast.LENGTH_LONG).show();//테스트용 파일
            Call<ResponseModel> call = sendRequest(pref.getString("ID", ""), pref.getString("PW", ""));

            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    ResponseModel data = response.body();
                    getResponse(data);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();//테스트용 파일
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    private int getResponse(ResponseModel data) {
        if(data.getResponse() == 1)//사용자
        {
            Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
            startActivity(intent);
            return 1;
        }
        else if(data.getResponse() == 2)//간부
        {
            Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
            startActivity(intent);
            return 1;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    private Call<ResponseModel> sendRequest(String ID, String PW) {
        LoginObject loginObject = new LoginObject(ID, PW);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.login(loginObject);
    }
}
