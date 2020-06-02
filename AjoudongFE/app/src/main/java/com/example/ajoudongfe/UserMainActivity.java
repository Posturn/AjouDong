package com.example.ajoudongfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserMainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Context context = this;

    private final int GET_GALLERY_IMAGE = 200;
    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = "http://10.0.2.2:8000";
    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/user_profile/";
    private RetroService retroService;

    final String accessKey = Keys.getAccessKey();
    final String secretKey = Keys.getSecretKey();
    final String bucketName = "ajoudong";
    final String folderName = "user_profile/";
    static String imgPath3, imgName3, nowImage3 = "";

    private int user_ID = 201421234; //테스트용 사용자 아이디

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ConstraintLayout majorclub = (ConstraintLayout)findViewById(R.id.majorclubLayout);
        ConstraintLayout mainclub = (ConstraintLayout)findViewById(R.id.mainclubLayout);
        ConstraintLayout newclub = (ConstraintLayout)findViewById(R.id.newclubLayout);

        ImageButton eventButton = (ImageButton)findViewById(R.id.usereventlist);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_main);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_main);
        final int user_ID = getIntent().getIntExtra("uSchoolID", 0);    //학번 받아오기 및 유저 아이디 세팅
        setUser_ID(user_ID);

        Log.d(TAG,"GET");       //처음 사용자 정보 불러오기
        Call<UserAccountObject> getCall = retroService.get_useraccount_pk(user_ID);
        getCall.enqueue(new Callback<UserAccountObject>() {
            @Override
            public void onResponse(Call<UserAccountObject> call, Response<UserAccountObject> response) {
                if( response.isSuccessful()){
                    UserAccountObject item  = response.body();
                    Log.d(TAG, String.valueOf(user_profile.getId()));
                    user_name.setText(item.getuName());
                    if(item.getuIMG() != null){
                        Picasso.get().load(item.getuIMG()).into(user_profile);
                    }
                    else{
                        user_profile.setImageResource(R.drawable.ajoudong_icon);
                    }
                    nowImage3 = item.getuIMG().substring(item.getuIMG().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
                    Log.d(TAG, nowImage3);
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserAccountObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

        // 이미지 편집 버튼 기능 구현
        final ImageButton profile_btn = (ImageButton)header.findViewById(R.id.user_profile_edit);
        profile_btn.setClickable(true);
        profile_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                Toast.makeText(getApplicationContext(), "프로필 수정", Toast.LENGTH_LONG).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerlayout.closeDrawers();
                int id = menuItem.getItemId();

                String title = menuItem.getTitle().toString();
                if(id == R.id.user_info_edit){
                    Intent intent = new Intent(getApplicationContext(), UserMyAjouDongActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_apply_result){
                    Intent intent = new Intent(getApplicationContext(), UserApplicationResultActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_bookmarked_list){
                    Intent intent = new Intent(getApplicationContext(), UserBookmarkClubActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_new_club_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.user_apply_state_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.user_new_event_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.user_logout){
                    Toast.makeText(context, "로그아웃중", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                else if(id == R.id.user_profile_edit){
                    profile_btn.callOnClick();
                }

                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }

                if(id == R.id.user_profile_edit){
                    profile_btn.callOnClick();
                }

                return true;
            }
        });

        majorclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorSelectActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mainclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMainClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        newclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserNewClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), UserEventListActivity.class);
                startActivity(intent);
            }
        });
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}