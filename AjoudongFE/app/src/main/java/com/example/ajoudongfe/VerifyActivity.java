package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class VerifyActivity extends AppCompatActivity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;

    private int uSchoolID;
    private String uName;
    private String uID;
    private int uPhoneNumber;
    private String uMajor;
    private String uCollege;
    private int uJender;
    private String uPW;
    private String verify_code;

    private Toolbar toolbar;
    private Button finishButton;
    private TextInputEditText verifyInputText;
    private Button verifyButton;
    private Button reverifyButton;
    private TextView checkText;

    private int myColor;

    private int verified = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        verifyButton = (Button)findViewById(R.id.verifyButton);
        reverifyButton = (Button)findViewById(R.id.reverifyButton);
        finishButton = (Button)findViewById(R.id.finishButton);
        toolbar = (Toolbar)findViewById(R.id.verifyToolbar);
        verifyInputText = (TextInputEditText)findViewById(R.id.verifyInputText);
        checkText = (TextView)findViewById(R.id.checkText);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uSchoolID = getIntent().getIntExtra("uSchoolID", 0);
        verify_code = getIntent().getStringExtra("verify_code");
        uName = getIntent().getStringExtra("uName");
        uJender = getIntent().getIntExtra("uJender", 1);
        uID = getIntent().getStringExtra("uID");
        uPhoneNumber = getIntent().getIntExtra("uPhoneNumber", 1);
        uMajor = getIntent().getStringExtra("uMajor");
        uCollege = getIntent().getStringExtra("uCollege");
        uPW = getIntent().getStringExtra("uPW");



        verifyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check;

                check = Integer.parseInt(verifyInputText.getText().toString());

                Log.d("인증코드", verify_code);

                if(check == Integer.parseInt(verify_code))
                {
                    myColor = ContextCompat.getColor(getApplicationContext(), R.color.black);
                    verified = 1;
                    checkText.setText("인증되었습니다!!");
                    checkText.setTextColor(myColor);
                }
                else
                {
                    myColor = ContextCompat.getColor(getApplicationContext(), R.color.res_red);
                    verified = -1;
                    checkText.setText("인증 실패하였습니다.");
                    checkText.setTextColor(myColor);
                }
            }
        });

        reverifyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseObject> call = emailVerifyRequest(uID, uName);

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        Toast.makeText(getApplicationContext(), "인증 메일이 재발송되었습니다.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "인증 메일 발송이 실패하였습니다.\n잠시후 다시 시도해주십시오", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        finishButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verified < 0)
                {
                    Toast.makeText(getApplicationContext(), "인증 먼저 실시해 주십시오.", Toast.LENGTH_LONG).show();
                }
                else {
                    SignupObject signupObject = new SignupObject(
                            uID, uPW, uName, uJender,
                            uSchoolID, uMajor, uCollege, uPhoneNumber
                    );
                    Call<ResponseObject> call = signupRequest(signupObject);

                    call.enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            ResponseObject data = response.body();
                            if (data.getResponse() == 1) {
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다!!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "통신중 오류가 발생하였습니다.\n잠시 후 다시 시도해주십시오.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "통신중 오류가 발생하였습니다.\n잠시 후 다시 시도해주십시오.", Toast.LENGTH_LONG).show();
                        }

                    });
                }
            }
        });



    }

    private Call<ResponseObject> signupRequest(SignupObject signupObject) {

        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.signup(signupObject);
    }

    private Call<ResponseObject> emailVerifyRequest(String toString, String name)
    {
        VerifyInfoObject verifyInfoObject = new VerifyInfoObject();
        List<RecipientForRequest> listdata = new ArrayList<>();
        RecipientForRequest recipientForRequest = new RecipientForRequest();
        Parameters parameters = new Parameters();
        verify_code = "";
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

}
