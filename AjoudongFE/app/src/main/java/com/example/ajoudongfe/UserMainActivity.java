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

import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        initMyAPI(BASE_URL);

        ConstraintLayout majorclub = (ConstraintLayout)findViewById(R.id.majorclubLayout);
        ConstraintLayout mainclub = (ConstraintLayout)findViewById(R.id.mainclubLayout);
        ConstraintLayout newclub = (ConstraintLayout)findViewById(R.id.newclubLayout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_main);
        final View header = navigationView.getHeaderView(0);
        ImageButton eventButton = (ImageButton)findViewById(R.id.usereventlist);
        final ImageView user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        final TextView user_name = (TextView)header.findViewById(R.id.user_name);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_main);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //갤러리 사용
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_main);
        final View header = navigationView.getHeaderView(0);
        ImageView user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String profilePath = getImageNameToUri(data.getData());
            imgName3 = profilePath.substring(profilePath.lastIndexOf("/")+1);
            Uri selectedImageUri = data.getData();
            user_profile.setImageURI(selectedImageUri);
        }
        transferIMG();
        patchProfile();
    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    private void deleteIMG(){       //원래 이미지 버킷에서 삭제
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderName+nowImage3));
            // Log.d(TAG,nowImage +" is deleted!");
        } catch (AmazonServiceException ase) {
            Log.e(TAG, ase.getErrorMessage());
        }
    }

    private void transferIMG(){     //이미지 S3에 업데이트
        if(imgPath3 != null){
            new DeleteTask().execute();
            TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(this).build();
            TransferObserver transferObserver = transferUtility.upload(bucketName, folderName+imgName3, new File(imgPath3), CannedAccessControlList.PublicRead);
            transferObserver.setTransferListener(new TransferListener() {       //새 이미지 버킷에 전송
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: " + id + ", " + state.toString());
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int)percentDonef;
                    Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
                }
                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            });
        }
    }

    private void patchProfile(){        // 동아리 이미지 업데이트
        Log.d(TAG,"PATCH");
        UserAccountObject item2 = new UserAccountObject();
        item2.setuSchoolID(user_ID);
        item2.setuIMG(OBJECT_URL + imgName3); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<UserAccountObject> patchCall = retroService.patch_useraccount_pk(user_ID, item2);
        patchCall.enqueue(new Callback<UserAccountObject>() {
            @Override
            public void onResponse(Call<UserAccountObject> call, Response<UserAccountObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"patch 성공");
                }else{
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserAccountObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

    public String getImageNameToUri(Uri data) {     //이미지 경로 구하기
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imgPath3 = cursor.getString(column_index);
        return imgPath3;
    }

    private class DeleteTask extends AsyncTask< Void, Void, String > {
        @Override
        protected String doInBackground(Void... voids) {
            deleteIMG();
            return null;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //result 값을 파싱하여 원하는 작업을 한다
        }
    }

}
