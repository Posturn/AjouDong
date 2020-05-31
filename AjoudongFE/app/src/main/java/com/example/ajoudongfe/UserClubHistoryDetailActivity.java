package com.example.ajoudongfe;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
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

public class UserClubHistoryDetailActivity extends AppCompatActivity {
    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = "http://10.0.2.2:8000";
    private RetroService retroService;

    static String nowImage5 = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_club_history_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_history_detail_toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        final int activityID = getIntent().getIntExtra("activityID", 0);
        initMyAPI(BASE_URL);

        final TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title2);
        final TextView TV_userHistroyDetail = (TextView) findViewById(R.id.user_history_text);
        final ImageView IV_userHistoryImage = (ImageView) findViewById(R.id.user_history_image);
        final VideoView VV_userHistoryVideo = (VideoView) findViewById(R.id.user_history_video);
        VV_userHistoryVideo.setVisibility(View.GONE);

        final MediaController controller = new MediaController(UserClubHistoryDetailActivity.this) {
            @Override
            public void setMediaPlayer(MediaPlayerControl player) {
                super.setMediaPlayer(player);
                super.hide();
            }
            int count = 1;
            @Override
            public void show(int timeout){
                if(count == 1){
                    super.hide();
                    count = 2;
                }
                else
                    super.show(0);
            }
        };
        //미디어컨트롤러 추가하는 부분
        controller.setAnchorView(VV_userHistoryVideo);
        VV_userHistoryVideo.setMediaController(controller);
        VV_userHistoryVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // 동영상 재생준비가 완료된후 호출되는 메서드
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(),
                        "동영상 재생가능", Toast.LENGTH_LONG).show();
            }
        });
        VV_userHistoryVideo.setOnTouchListener(new View.OnTouchListener() {
            boolean flag = true;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (flag) {
                            controller.show(0);
                        }
                        else {
                            //controller.setVisibility(View.VISIBLE);
                            controller.hide();
                        }
                        flag = !flag;
                        return true;
                }
                return false;
            }
        });

        Log.d(TAG, "GET");       //처음 동아리 활동내역 정보 불러오기
        Call<ClubActivityObject> getCall = retroService.get_activities_pk(activityID);
        getCall.enqueue(new Callback<ClubActivityObject>() {
            @Override
            public void onResponse(Call<ClubActivityObject> call, Response<ClubActivityObject> response) {
                if (response.isSuccessful()) {
                    ClubActivityObject item = response.body();
                    toolbarTitle.setText(item.getClubActivityInfo());
                    TV_userHistroyDetail.setText(item.getClubActivityDetail());
                    nowImage5 = item.getClubActivityFile().substring(item.getClubActivityFile().lastIndexOf("/") + 1);   //현재 이미지 파일 이름 가져오기
                    if (nowImage5.contains("mp4")) {
                        VV_userHistoryVideo.setVisibility(View.VISIBLE);
                        IV_userHistoryImage.setVisibility(View.GONE);
                        VV_userHistoryVideo.setVideoURI(Uri.parse(item.getClubActivityFile()));
                        VV_userHistoryVideo.seekTo(1);
                    } else {
                        Picasso.get().load(item.getClubActivityFile()).into(IV_userHistoryImage);
                    }
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ClubActivityObject> call, Throwable t) {
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
