package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
    ArrayAdapter<CharSequence> majorAdapter;
    Retrofit retrofit;
    Retrofit verifyRetrofit;
    TextView checkSameID;
    TextInputEditText idInputText;
    TextInputEditText pwInputText;
    TextInputEditText pwCheckInputText;
    TextInputEditText nameInputText;
    TextInputEditText schoolIDInputText;
    Spinner collegeSpinner;
    Spinner majorSpinner;
    TextInputEditText phoneNumberInputText;
    Toolbar toolbar;
    RadioGroup genderRadioGroup;
    int IDChecker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //toolbar = (Toolbar)findViewById(R.id.signupToolbar);
        idInputText = (TextInputEditText)findViewById(R.id.idInputText);
        pwInputText = (TextInputEditText)findViewById(R.id.pwInputText);
        pwCheckInputText = (TextInputEditText)findViewById(R.id.pwCheckInputText);
        nameInputText = (TextInputEditText)findViewById(R.id.nameInputText);
        schoolIDInputText = (TextInputEditText)findViewById(R.id.schoolIDInputText);
        phoneNumberInputText = (TextInputEditText)findViewById(R.id.phoneNumberInputText);
        collegeSpinner = (Spinner)findViewById(R.id.collegeSpinner);
        majorSpinner = (Spinner)findViewById(R.id.majorSpinner);
        genderRadioGroup = (RadioGroup)findViewById(R.id.genderRadioGroup);
        checkSameID = (TextView)findViewById(R.id.checkSameID);

        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!(collegeSpinner.getItemAtPosition(i).equals("--단과대학교선택--")))
                {
                    majorAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.간호대학, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private Call<ResponseModel> sendSameIDRequest(String toString) {
        CheckID checkID = new CheckID(toString);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.checkSameID(checkID);
    }

}
