package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    private static String VERIFY_URL = "https://mail.apigw.ntruss.com/api/v1/mails";
    private ArrayAdapter<CharSequence> majorAdapter;
    private Retrofit retrofit;
    private Retrofit verifyRetrofit;
    private TextView checkSameID;
    private TextInputEditText idInputText;
    private TextInputEditText pwInputText;
    private TextInputEditText pwCheckInputText;
    private TextInputEditText nameInputText;
    private TextInputEditText schoolIDInputText;
    private Spinner collegeSpinner;
    private Spinner majorSpinner;
    private TextInputEditText phoneNumberInputText;
    private Toolbar toolbar;
    private RadioGroup genderRadioGroup;
    private Button signupButton;
    private int IDChecker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar)findViewById(R.id.signupToolbar);
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

        verifyRetrofit = new Retrofit.Builder()
                .baseUrl(VERIFY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!(collegeSpinner.getItemAtPosition(i).equals("--단과대학교선택--")))
                {
                    int resid = getResources().getIdentifier(collegeSpinner.getItemAtPosition(i).toString(), "array", getPackageName());
                    majorAdapter = ArrayAdapter.createFromResource(getApplicationContext(), resid, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 하지않음
            }
        });

        checkSameID.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "아이디 중복 체크 : " + idInputText.getText().toString(), Toast.LENGTH_LONG).show();
                Call<ResponseObject> call = sendSameIDRequest(idInputText.getText().toString());

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();

                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Call<ResponseObject> sendSameIDRequest(String toString) {
        CheckID checkID = new CheckID(toString);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.checkSameID(checkID);
    }

}
