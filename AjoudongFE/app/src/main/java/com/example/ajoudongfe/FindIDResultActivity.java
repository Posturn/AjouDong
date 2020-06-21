package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class FindIDResultActivity extends AppCompatActivity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;
    private RetroService retroService;

    private TextView maskedIDText;
    private Button goLoginButton;
    private Button goFindPWButton;
    private TextView entireIDText;

    private int uSchoolID;
    private String uName;
    private String maskedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_i_d_result);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        maskedIDText = (TextView)findViewById(R.id.maskedIDText);

        uName = getIntent().getStringExtra("uName");
        uSchoolID = getIntent().getIntExtra("uSchoolID", 0);
        maskedID = getIntent().getStringExtra("maskedID");

        maskedIDText.setText(maskedID);

        goLoginButton = (Button)findViewById(R.id.goLoginButton);
        goFindPWButton = (Button)findViewById(R.id.goFindPWButton);
        entireIDText = (TextView)findViewById(R.id.entireIDText);

        goLoginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        goFindPWButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Intent intent = new Intent(getApplicationContext(), FindPWActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
            }
        });

        entireIDText.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseObject> call = getEntireIDRequest();

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        if(data.getResponse() == 1)
                            Toast.makeText(getApplicationContext(), "이메일이 발송되었습니다!", Toast.LENGTH_LONG).show();
                        else if(data.getResponse() == -2)
                            Toast.makeText(getApplicationContext(), "서버 오류가 발생하였습니다. 잠시후 시도해 주십시오", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "서버 오류가 발생하였습니다. 잠시후 시도해 주십시오", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private Call<ResponseObject> getEntireIDRequest()
    {
        FindIDResultObject findIDResultObject = new FindIDResultObject();

        String secretKey = "AxVnaaKAL8tFnTlI5EUKCBH8wRSR2CRVZEMt3zcD";
        String accessKey = "f8jYvSP0idEEd97qTu5l";
        String encrptedKey = new String();
        int j=1;

        Date date = new Date();
        long timeMilli = date.getTime();
        String timeStamp = new String();
        timeStamp = Long.toString(timeMilli);

        encrptedKey = makeSignature("POST", timeStamp, accessKey, secretKey, "/api/v1/mails");

        Log.d(encrptedKey, "암호화된 비밀키");
        Log.d(timeStamp.toString(), "타임스탬프");
        Log.d(accessKey, "인증키");

        findIDResultObject.setTemplateSid(1606);
        findIDResultObject.setAccessKey(accessKey);
        findIDResultObject.setEncryptedKey(encrptedKey);
        findIDResultObject.setTimeStamp(timeStamp);
        findIDResultObject.setType("r");
        findIDResultObject.setuName(uName);
        findIDResultObject.setuSchoolID(uSchoolID);

        retroService = retrofit.create(RetroService.class);
        return retroService.getEntireID(findIDResultObject);
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