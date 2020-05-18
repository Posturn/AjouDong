package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    public static String BASE_URL= "http://10.0.2.2:8000";

    private Button loginButton;
    private Button userLogin;
    private Button managerLogin;
    private TextInputLayout idLayout;
    private TextInputLayout pwLayout;
    private TextInputEditText idText;
    private TextInputEditText pwText;
    private CheckBox autoLogin;
    private TextView findID;
    private TextView findPW;
    private TextView signup;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Retrofit retrofit;
    private RetroRequest retroRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        loginButton = (Button) findViewById(R.id.loginButton);
        userLogin = (Button) findViewById(R.id.userLogin);
        managerLogin = (Button) findViewById(R.id.managerLogin);
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
        userLogin.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                Toast.makeText(getApplicationContext(), "사용자 화면 이동", Toast.LENGTH_LONG).show();//테스트용 파일
                startActivity(intent);
            }
        });

        managerLogin.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                Toast.makeText(getApplicationContext(), "동아리 간부 화면 이동", Toast.LENGTH_LONG).show();//테스트용 파일
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Call<ResponseModel> call = sendRequest(idText.getText().toString(), pwText.getText().toString());

                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        ResponseModel data = response.body();
                        if(getResponse(data) > 0)
                        {
                            checkAutoLogin();
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

    private void checkAutoLogin() {
        if (autoLogin.isChecked()) {
            editor.putBoolean("Auto_Login_Enabled", true);
            editor.putString("ID", idText.getText().toString());
            editor.putString("PW", pwText.getText().toString());
            editor.commit();
        }
    }

    private Call<ResponseModel> sendRequest(String ID, String PW) {
        LoginObject loginObject = new LoginObject(ID, PW);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.login(loginObject);
    }

    private String makeSHA256(String originPW)
    {
        String EncryptedPW = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(originPW.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < byteData.length; i++)
            {
                stringBuffer.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            EncryptedPW = stringBuffer.toString();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            EncryptedPW = null;
        }
        return EncryptedPW;
    }
}
