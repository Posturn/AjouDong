package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindIDActivity extends AppCompatActivity {

    private static String BASE_URL= Keys.getServerUrl();
    private Retrofit retrofit;
    private RetroService retroService;

    private TextInputEditText findIDNameInputText;
    private TextInputEditText findIDuSchoolIDInputText;
    private TextInputEditText findIDPhoneNumberInputText;
    private Button findIDButton;
    private TextView findPWText;
    private TextView goLoginText;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_i_d);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetroService retroService = retrofit.create(RetroService.class);

        toolbar = (Toolbar)findViewById(R.id.findIDtoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findIDNameInputText = (TextInputEditText)findViewById(R.id.findIDNameInputText);
        findIDuSchoolIDInputText = (TextInputEditText)findViewById(R.id.findIDuSchoolIDInputText);
        findIDPhoneNumberInputText = (TextInputEditText)findViewById(R.id.findIDPhoneNumberInputText);
        findIDButton = (Button)findViewById(R.id.findIDButton);
        findPWText = (TextView)findViewById(R.id.findPWText);
        goLoginText = (TextView)findViewById(R.id.goLoginText);

        findIDButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseObject> call= retroService.getMaskedID(new FindIDObject(findIDNameInputText.getText().toString(), Integer.parseInt(findIDuSchoolIDInputText.getText().toString())));

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        if(data.getResponse() == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), FindIDResultActivity.class);
                            intent.putExtra("maskedID", data.getMessage());
                            intent.putExtra("uName", findIDNameInputText.getText().toString());
                            intent.putExtra("uSchoolID", Integer.parseInt(findIDuSchoolIDInputText.getText().toString()));
                            startActivity(intent);
                        }
                        else if(data.getResponse() == 2)
                        {
                            Toast.makeText(getApplicationContext(), data.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        else if(data.getResponse() == -1)
                        {
                            Toast.makeText(getApplicationContext(), "등록되지 않은 회원정보입니다", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "통신 오류", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "현재 인터넷 상태가 불안합니다.\n잠시 후 다시 시도해 주십시오", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        findPWText.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), FindPWActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
            }
        });

        goLoginText.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }
}