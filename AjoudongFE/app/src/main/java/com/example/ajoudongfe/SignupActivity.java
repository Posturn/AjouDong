package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
    TextInputEditText pwCheckText;
    TextInputEditText nameText;
    TextInputEditText schoolIDText;
    Spinner collegeSpinner;
    Spinner majorSpinner;
    TextInputEditText phoneNumberText;
    Toolbar toolbar;
    RadioGroup genderRadioGroup;
    int IDChecker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//
    }

    private Call<ResponseModel> sendSameIDRequest(String toString) {
        CheckID checkID = new CheckID(toString);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.checkSameID(checkID);
    }

}
