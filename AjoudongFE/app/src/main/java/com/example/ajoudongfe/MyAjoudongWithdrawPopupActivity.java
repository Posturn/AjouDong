package com.example.ajoudongfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAjoudongWithdrawPopupActivity extends Activity {

    private static String BASE_URL= "http://10.0.2.2:8000";
    final  String TAG = getClass().getSimpleName();
    private RetroService retroService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myajoudong_withdraw_popup);
        final int clubID = getIntent().getIntExtra("clubID",0);
        final int uSchoolID = getIntent().getIntExtra("uSchoolID", 0);
        initMyAPI(BASE_URL);

        Button yes_btn1 = (Button)findViewById(R.id.yes_btn1);
        yes_btn1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", 0);
                intent.putExtra("clubID",clubID);
                Log.d(TAG,"DELETE");
                Call<ResponseObject> deleteCall = retroService.deleteClubMember(clubID, uSchoolID);
                deleteCall.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "삭제 완료");
                        } else {
                            Log.d(TAG, "Status Code : " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        Log.d(TAG, "Fail msg : " + t.getMessage());
                    }
                });
                setResult(RESULT_OK, intent);
                finish();       //팝업 닫기
            }
        });
        Button no_btn1  = (Button)findViewById(R.id.no_btn1);
        no_btn1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                Intent intent = new Intent();
                intent.putExtra("result", 1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}