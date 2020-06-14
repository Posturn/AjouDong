package com.example.ajoudongfe;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserEventDetailActivity extends AppCompatActivity {
    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = "http://10.0.2.2:8000";
    private RetroService retroService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.eventinfotoolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        final int eventID = getIntent().getIntExtra("eventID", 0);
        initMyAPI(BASE_URL);

        final TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title3);
        final ImageView userEventIMG  = (ImageView) findViewById(R.id.userEventProfile);
        final TextView eventDate = (TextView) findViewById(R.id.userEventDay);
        final TextView eventInfo = (TextView) findViewById(R.id.userEventInfo);
        final TextView eventContact = (TextView) findViewById(R.id.userEventContact);


        Log.d(TAG, "GET");       //처음 동아리 활동내역 정보 불러오기
        Call<EventObject> getCall = retroService.getEventObject(eventID);
        getCall.enqueue(new Callback<EventObject>() {
            @Override
            public void onResponse(Call<EventObject> call, Response<EventObject> response) {
                if (response.isSuccessful()) {
                    EventObject item = response.body();
                    toolbarTitle.setText(item.getEventName());
                    eventDate.setText(item.getEventDate());
                    eventInfo.setText(item.getEventInfo());
                    eventContact.setText(item.getEventFAQ());
                    if(item.getEventIMG() != null){
                        Picasso.get().load(item.getEventIMG()).into(userEventIMG);
                    }
                    else{
                        userEventIMG.setImageResource(R.drawable.icon);
                    }
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EventObject> call, Throwable t) {
                Log.d(TAG, "Fail msg : " + t.getMessage());
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
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 저장 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

