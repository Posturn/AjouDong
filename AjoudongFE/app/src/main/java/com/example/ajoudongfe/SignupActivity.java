package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    private static String VERIFY_URL = "https://mail.apigw.ntruss.com/api/v1/";
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
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button signupButton;
    private int IDChecker = -1;
    private int gender = -1;
    private String tempID;
    private String uMajor;
    private String uCollege;
    private String verify_code;

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
        maleRadioButton = (RadioButton)findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton)findViewById(R.id.femaleRadioButton);
        checkSameID = (TextView)findViewById(R.id.checkSameID);
        signupButton = (Button)findViewById(R.id.signupButton);

        phoneNumberInputText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RadioGroup.OnCheckedChangeListener genderRadioCheck = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getId() == R.id.genderRadioGroup)
                {
                    if(i == R.id.maleRadioButton)
                    {
                        gender = 1;
                    }
                    else
                    {
                        gender = 0;
                    }
                }
            }
        };

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
                Call<ResponseObject> call = sendSameIDRequest(idInputText.getText().toString() + "@ajou.ac.kr");

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        try {
                            if (data.getResponse() == 1) {
                                Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다!!", Toast.LENGTH_LONG).show();//좀더 개선 필요, 실사용 앱처럼 아이디 변경시 다시 체크하도록 만들것
                                IDChecker = 1;
                                tempID = idInputText.getText().toString() + "@ajou.ac.kr";
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
                Call<ResponseObject> call = emailVerifyRequest(idInputText.getText().toString() + "@ajou.ac.kr", nameInputText.getText().toString());

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<com.example.ajoudongfe.ResponseObject> call, Response<com.example.ajoudongfe.ResponseObject> response) {

                        String phoneNum = String.valueOf(phoneNumberInputText.getText());

                        phoneNum = phoneNum.replace("010", "");     //전화번호 디비형식으로 변경해주기
                        phoneNum = phoneNum.replace("-","");

                        Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
                        intent.putExtra("uSchoolID", Integer.parseInt(schoolIDInputText.getText().toString()));
                        intent.putExtra("uName", nameInputText.getText().toString());
                        intent.putExtra("uID", idInputText.getText().toString() + "@ajou.ac.kr");
                        intent.putExtra("uPhoneNumber", Integer.parseInt(phoneNum));
                        intent.putExtra("uMajor", uMajor);
                        intent.putExtra("uCollege", uCollege);
                        intent.putExtra("uJender", gender);//TODO jender바꿔야댐
                        intent.putExtra("uPW", pwInputText.getText().toString());
                        intent.putExtra("verify_code", verify_code);
                        startActivity(intent);
                        return;
                    }

                    @Override
                    public void onFailure(Call<com.example.ajoudongfe.ResponseObject> call, Throwable t) {
                        t.printStackTrace();
                        Log.e("메일 요청 결과", "통신 실패");
                    }
                });
            }
        });

    }

    private String checkParameter()
    {
        if(idInputText.getText() == null)
            return "이메일을 입력해주십시오.";
        else if(IDChecker < 0)
            return "이메일 중복확인은 필수입니다.";
        else if(gender < 0)
            return "성별을 선택해 주십시오";
        else if(pwInputText.getText().toString() != pwCheckInputText.getText().toString())
            return "비밀번호와 확인이 맞지 않습니다.";
        else
            return null;
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
//        VerifyObject verifyObject = new VerifyObject();
        VerifyInfoObject verifyInfoObject = new VerifyInfoObject();
        List<RecipientForRequest> listdata = new ArrayList<>();
        RecipientForRequest recipientForRequest = new RecipientForRequest();
        Parameters parameters = new Parameters();
        verify_code = new String();
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
            Log.d(verify_code, "인증코드");
//            j = j*10;
        }

        Log.d(name, "이름");
        recipientForRequest.setAddress(toString);
        recipientForRequest.setName(name);
        recipientForRequest.setType("R");

        parameters.setWho_signup(name);
        parameters.setVerify_code(verify_code);

        recipientForRequest.setParameters(parameters);

        listdata.add(recipientForRequest);

        encrptedKey = makeSignature("POST", timeStamp, accessKey, secretKey, "/api/v1/mails");

        VerifyObject verifyObject = new VerifyObject(1419, listdata);

        Log.d(encrptedKey, "암호화된 비밀키");
        Log.d(timeStamp.toString(), "타임스탬프");
        Log.d(accessKey, "인증키");

        verifyInfoObject.setName(name);
        verifyInfoObject.setAddress(toString);
        verifyInfoObject.setTemplateSid(1419);
        verifyInfoObject.setType("r");
        verifyInfoObject.setVerify_code(verify_code);
        verifyInfoObject.setWho_signup(name);
        verifyInfoObject.setAccessKey(accessKey);
        verifyInfoObject.setEncryptedKey(encrptedKey);
        verifyInfoObject.setTimeStamp(timeStamp);

//        RetroService retroService = verifyRetrofit.create(RetroService.class);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.emailVerify(verifyInfoObject);
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
