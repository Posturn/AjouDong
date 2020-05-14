package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupActivity extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    Retrofit retrofit;
    Retrofit verifyRetrofit;
    TextView checkSameID;
    TextInputEditText idText;
    TextInputEditText pwText;
    TextInputEditText nameText;
    TextInputEditText schoolIDText;
    Spinner collegeSpinner;
    Spinner majorSpinner;
    TextInputEditText phoneNumberText;
    int IDChecker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        checkSameID = (TextView)findViewById(R.id.checkSameID);
        idText = (TextInputEditText)findViewById(R.id.idInputText);
        pwText = (TextInputEditText)findViewById(R.id.pwInputText);
        nameText = (TextInputEditText)findViewById(R.id.nameInputText);
        schoolIDText = (TextInputEditText)findViewById(R.id.schoolIDInputText);
        collegeSpinner = (Spinner)findViewById(R.id.collegeSpinner);
        majorSpinner = (Spinner)findViewById(R.id.majorSpinner);
        phoneNumberText = (TextInputEditText)findViewById(R.id.phoneNumberInputText);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        checkSameID.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Call<ResponseModel> call = sendSameIDRequest(idText.getText().toString());

                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        ResponseModel data = response.body();
                        if(data.getResponse() == 0)
                        {
                            IDChecker = 1;
                            Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다.", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "사용불가능한 아이디입니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "통신 오류, 잠시후 다시 시도해 주십시요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private Call<ResponseModel> sendSameIDRequest(String toString) {
        CheckID checkID = new CheckID(toString);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.checkSameID(checkID);
    }

}
