package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    private static String VERIFY_URL = "https://mail.apigw.ntruss.com/api/v1/mails/";
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
    private String tempID;
    private String uMajor;
    private String uCollege;

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
        signupButton = (Button)findViewById(R.id.signupButton);

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
                    uCollege = collegeSpinner.getItemAtPosition(i).toString();
                    majorAdapter = ArrayAdapter.createFromResource(getApplicationContext(), resid, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 하지않음
            }
        });

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                uMajor = majorSpinner.getItemAtPosition(i).toString();
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
                Call<ResponseObject> call = sendSameIDRequest(idInputText.getText().toString());

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        try {
                            if (data.getResponse() == 1) {
                                Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다!!", Toast.LENGTH_LONG).show();//좀더 개선 필요, 실사용 앱처럼 아이디 변경시 다시 체크하도록 만들것
                                IDChecker = 1;
                                tempID = idInputText.getText().toString();
                            } else {
                                Toast.makeText(getApplicationContext(), "이미 사용중인 아이디 입니다!!", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        signupButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Call<ResponseObject> call = signupRequest(new SignupObject(
                        idInputText.getText().toString(),
                        pwInputText.getText().toString(),
                        nameInputText.getText().toString(),
                        1,
                        Integer.parseInt(schoolIDInputText.getText().toString()),
                        uMajor,
                        uCollege,
                        Integer.parseInt(phoneNumberInputText.getText().toString())));

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        try {
                            Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_LONG).show();
                            finish();
                            Log.d("Response", response.body().toString());
                        }
                        catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Log.e("메일 요청 결과", "통신 실패");
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
        CheckIDObject checkIDObject = new CheckIDObject(toString);

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.checkSameID(checkIDObject);
    }

    private Call<ResponseObject> emailVerifyRequest(String toString, String name)
    {
        VerifyObject verifyObject = new VerifyObject();
        RecipientForRequest recipientForRequest = new RecipientForRequest();
        Parameters parameters = new Parameters();
        String verify_code = new String();
        String secretKey = "AxVnaaKAL8tFnTlI5EUKCBH8wRSR2CRVZEMt3zcD";
        String accessKey = "f8jYvSP0idEEd97qTu5l";
        String encrptedKey = new String();
        int j=1;

        Date date = new Date();
        long timeMilli = date.getTime();
        String timeStamp = new String();
        timeStamp = Long.toString(timeMilli);

        for(int i =0; i < 4; i++)
        {
            int n = (int)(Math.random() * 10);
            verify_code = verify_code + Integer.toString(n*j);
            j = j*10;
        }
        recipientForRequest.setAddress(toString);
        recipientForRequest.setName(null);
        recipientForRequest.setType("R");

        parameters.setWho_signup(name);
        parameters.setVerify_code(verify_code);

        recipientForRequest.setParameters(parameters);

        encrptedKey = makeSignature("POST", timeStamp, accessKey, secretKey, "/api/v1/mails");
        Log.d(encrptedKey, "암호화된 비밀키");
        Log.d(timeStamp.toString(), "타임스탬프");

        RetroService retroService = verifyRetrofit.create(RetroService.class);
        return retroService.emailVerify(timeStamp.toString(), accessKey, encrptedKey, verifyObject);
    }

    private Call<ResponseObject> signupRequest(SignupObject signupObject)
    {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.signup(signupObject);
    }

    public String makeSignature(String method, String timestamp, String accessKey, String secretKey, String url)
    {
        String space = " ";  // 공백
        String newLine = "\n";  // 줄바꿈
        String signature = new String();
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            Base64.Encoder encoder = Base64.getEncoder();
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            signature = encoder.encodeToString(rawHmac);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return signature;
    }


}
