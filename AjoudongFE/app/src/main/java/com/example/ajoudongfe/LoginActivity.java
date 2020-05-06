package com.example.ajoudongfe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextInputLayout idLayout = (TextInputLayout) findViewById(R.id.idLayout);
        TextInputLayout pwLayout = (TextInputLayout) findViewById(R.id.pwLayout);
        TextInputEditText idText = (TextInputEditText) findViewById(R.id.idInputText);
        TextInputEditText pwText = (TextInputEditText) findViewById(R.id.pwInputText);
        Chip autoLogin = (Chip) findViewById(R.id.autoLogin);
        TextView findID = (TextView)findViewById(R.id.findID);
        TextView findPW = (TextView)findViewById(R.id.findPW);
        TextView signup = (TextView)findViewById(R.id.signup);

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {

            }
        });

    }
}

