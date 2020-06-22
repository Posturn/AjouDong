package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class FindPWActivity extends AppCompatActivity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;
    private RetroService retroService;

    private TextInputEditText findPWNameInputText;
    private TextInputEditText findPWuSchoolIDInputText;
    private TextInputEditText findPWPhoneNumberInputText;
    private TextInputEditText findPWEmailInputText;

    private Button findPWButton;
    private TextView findIDText;
    private TextView goLoginText;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_p_w);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetroService retroService = retrofit.create(RetroService.class);

        toolbar = (Toolbar)findViewById(R.id.findPWtoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findPWNameInputText = (TextInputEditText)findViewById(R.id.findPWNameInputText);
        findPWuSchoolIDInputText = (TextInputEditText)findViewById(R.id.findPWuSchoolIDInputText);
        findPWPhoneNumberInputText = (TextInputEditText)findViewById(R.id.findPWPhoneNumberInputText);
        findPWEmailInputText = (TextInputEditText)findViewById(R.id.findPWEmailInputText);
        findPWButton = (Button)findViewById(R.id.findPWButton);
        findIDText = (TextView)findViewById(R.id.findIDText);
        goLoginText = (TextView)findViewById(R.id.goLoginText2);

        findPWButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseObject> call= getTempPWRequest();

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        if(data.getResponse() == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), FindPWResultActivity.class);
                            startActivity(intent);
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
        findIDText.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindIDActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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

    private Call<ResponseObject> getTempPWRequest() {
        FindPWObject findPWObject = new FindPWObject();
        String secretKey = "AxVnaaKAL8tFnTlI5EUKCBH8wRSR2CRVZEMt3zcD";
        String accessKey = "f8jYvSP0idEEd97qTu5l";
        String encrptedKey = new String();

        Date date = new Date();
        long timeMilli = date.getTime();
        String timeStamp = new String();
        timeStamp = Long.toString(timeMilli);

        encrptedKey = makeSignature("POST", timeStamp, accessKey, secretKey, "/api/v1/mails");

        Log.d(encrptedKey, "암호화된 비밀키");
        Log.d(timeStamp.toString(), "타임스탬프");
        Log.d(accessKey, "인증키");

        findPWObject.setAccessKey(accessKey);
        findPWObject.setEncryptedKey(encrptedKey);
        findPWObject.setTemplateSid(1607);
        findPWObject.setTimeStamp(timeStamp);
        findPWObject.setType("r");
        findPWObject.setuID(findPWEmailInputText.getText().toString());
        findPWObject.setuSchoolID(Integer.parseInt(findPWuSchoolIDInputText.getText().toString()));
        findPWObject.setuName(findPWNameInputText.getText().toString());

        retroService = retrofit.create(RetroService.class);
        return retroService.getTempPW(findPWObject);

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