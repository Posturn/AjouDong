package com.example.ajoudongfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class ManagerMainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Context context = this;

    private long backKeyPressedTime;

    private final int GET_GALLERY_IMAGE = 200;
    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = "http://10.0.2.2:8000";
    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/manager_profile/";
    private RetroService retroService;

    final String accessKey = Keys.getAccessKey();
    final String secretKey = Keys.getSecretKey();
    final String bucketName = "ajoudong";
    final String folderName = "manager_profile/";
    static String imgPath4, imgName4, nowImage4 = "";

    private String mID = "manager"; //테스트용 간부 아이디
    private int clubID;
    private int clubMajor;

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        initMyAPI(BASE_URL);

        pref = getSharedPreferences("autologin", MODE_PRIVATE);
        editor = pref.edit();

        ConstraintLayout editclubinfo = (ConstraintLayout)findViewById(R.id.editclubinfoLayout);
        ConstraintLayout membermanagement = (ConstraintLayout)findViewById(R.id.managementclubmemberLayout);
        ConstraintLayout newevent = (ConstraintLayout)findViewById(R.id.neweventLayout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_manager_main);
        final View header = navigationView.getHeaderView(0);
        final ImageView manager_profile = (ImageView)header.findViewById(R.id.manager_default_icon);
        final TextView manager_name = (TextView)header.findViewById(R.id.manager_name);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_manager_main);

        final String mID = getIntent().getStringExtra("mID");       //매니저 아이디 받아오기 및 세팅
        setmID(mID);

        final int clubID = getIntent().getIntExtra("clubID", 0);    //매니저의 동아리 받아오기 및 세팅팅
       setClubID(clubID);

        Log.d(TAG,"GET");       //처음 간부 이름 정보 불러오기
        Call<ManagerAccountObject> getCall = retroService.get_manageraccount_pk(mID);
        getCall.enqueue(new Callback<ManagerAccountObject>() {
            @Override
            public void onResponse(Call<ManagerAccountObject> call, Response<ManagerAccountObject> response) {
                if( response.isSuccessful()){
                    ManagerAccountObject item  = response.body();
                    manager_name.setText(item.getClubName());
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ManagerAccountObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

        Log.d(TAG,"GET");       //처음 간부(클럽) 프로필 정보 불러오기
        Call<ClubObject> getCall2 = retroService.getClubGrid(clubID);
        getCall2.enqueue(new Callback<ClubObject>() {
            @Override
            public void onResponse(Call<ClubObject> call, Response<ClubObject> response) {
                if( response.isSuccessful()){
                    ClubObject item3  = response.body();
                    Log.d(TAG, String.valueOf(manager_profile.getId()));
                    setClubMajor(item3.getClubMajor());
                    Picasso.get().load(item3.getIMG()).into(manager_profile);
                    nowImage4 = item3.getIMG().substring(item3.getIMG().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
                     Log.d(TAG, nowImage4);
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ClubObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

        // 이미지 편집 버튼 기능 구현
        final ImageButton profile_btn = (ImageButton)header.findViewById(R.id.manager_profile_edit);
        profile_btn.setClickable(true);
        profile_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerlayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.club_apply_setting){
                    Intent intent = new Intent(getApplicationContext(), ManagerClubApplySettingActivity.class);
                    intent.putExtra("clubID", getClubID());
                    startActivity(intent);
                }
                else if(id == R.id.club_filter_setting){
                    Intent intent = new Intent(getApplicationContext(), ManagerClubFilterSettingActivity.class);
                    intent.putExtra("clubMajor", getClubMajor());
                    intent.putExtra("clubID", getClubID());
                    startActivity(intent);
                }
                else if(id == R.id.club_apply_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.club_logout){
                    editor.clear();
                    editor.commit();
                    Toast.makeText(context, "로그아웃 완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                else if(id == R.id.manager_profile_edit){
                    profile_btn.callOnClick();
                }

                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }

                return true;
            }
        });

        editclubinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerClubInfoEditActivity.class);
                intent.putExtra("clubID", getClubID());
                startActivity(intent);
                return false;
            }
        });

        membermanagement.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerMemberManagementActivity.class);
                intent.putExtra("clubID", getClubID());
                startActivity(intent);
                return false;
            }
        });

        newevent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerEventListActivity.class);
                intent.putExtra("clubID", getClubID());
                startActivity(intent);
                return false;
            }
        });
    }

    public int getClubMajor() {
        return clubMajor;
    }

    public void setClubMajor(int clubMajor) {
        this.clubMajor = clubMajor;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }


    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
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
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_manager_main);
        final View header = navigationView.getHeaderView(0);
        ImageView manager_profile = (ImageView)header.findViewById(R.id.manager_default_icon);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String profilePath = getImageNameToUri(data.getData());
            imgName4 = profilePath.substring(profilePath.lastIndexOf("/")+1);
            Uri selectedImageUri = data.getData();
            manager_profile.setImageURI(selectedImageUri);
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
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderName+nowImage4));
            // Log.d(TAG,nowImage +" is deleted!");
        } catch (AmazonServiceException ase) {
            Log.e(TAG, ase.getErrorMessage());
        }
    }

    private void transferIMG(){     //이미지 S3에 업데이트
        if(imgPath4 != null){
            new DeleteTask().execute();
            TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(this).build();
            TransferObserver transferObserver = transferUtility.upload(bucketName, folderName+imgName4, new File(imgPath4), CannedAccessControlList.PublicRead);
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
        ClubDetailObject item2 = new ClubDetailObject();
        item2.setRealClubID(1);
        item2.setClubIMG(OBJECT_URL + imgName4); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<ClubDetailObject> patchCall = retroService.patchClubDetailObject(clubID, item2);
        patchCall.enqueue(new Callback<ClubDetailObject>() {
            @Override
            public void onResponse(Call<ClubDetailObject> call, Response<ClubDetailObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"patch 성공");
                }else{
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ClubDetailObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

    public String getImageNameToUri(Uri data) {     //이미지 경로 구하기
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imgPath4 = cursor.getString(column_index);
        return imgPath4;
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

    @Override
    public void onBackPressed() {
        //1번째 백버튼 클릭
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else{
            AppFinish();
        }
    }

    public void AppFinish(){
//        finish();
//        System.exit(1);
        ActivityCompat.finishAffinity(this);
    }


}
