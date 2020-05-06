package com.example.ajoudongfe;

import retrofit2.Retrofit;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity
{
    public static String BASE_URL= "http://127.0.0.1:8000/";

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

    RetroService retroService;

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
                String ID = idText.getText().toString();
                String PW = pwText.getText().toString();


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

