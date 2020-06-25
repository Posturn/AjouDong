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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.models.PieModel;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserMainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Context context = this;

    private NavigationView navigationView;
    private View header;
    private ImageView user_profile;
    private TextView user_name;
    private ImageButton eventButton;
    private ConstraintLayout majorclub;
    private ConstraintLayout mainclub;
    private ConstraintLayout newclub;
    private ImageButton profile_btn;

    private long backKeyPressedTime;
    private AlarmStateObject userAlarm;
    private Switch stateAlarmSwitch;
    private Switch eventAlarmSwitch;
    private Switch newclubAlarmSwitch;
    private Menu navMenu;
    private boolean loadingAlarm = false;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private final int GET_GALLERY_IMAGE = 200;
    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = Keys.getServerUrl();
    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/user_profile/";
    private RetroService retroService;

    final String accessKey = Keys.getAccessKey();
    final String secretKey = Keys.getSecretKey();
    final String bucketName = "ajoudong";
    final String folderName = "user_profile/";
    static private String imgPath3, imgName3, nowImage3 = "";

    private int uSchoolID = 201720988; //테스트용 사용자 아이디
    private int unread;
    private int viewCount;

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
    private String userDeviceToken;

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

        pref = getSharedPreferences("autologin", MODE_PRIVATE);
        editor = pref.edit();

        majorclub = (ConstraintLayout)findViewById(R.id.majorclubLayout);
        mainclub = (ConstraintLayout)findViewById(R.id.mainclubLayout);
        newclub = (ConstraintLayout)findViewById(R.id.newclubLayout);

        navigationView = (NavigationView) findViewById(R.id.nav_view_user_main);
        header = navigationView.getHeaderView(0);
        eventButton = (ImageButton)findViewById(R.id.usereventlist);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_main);

        // 이미지 편집 버튼 기능 구현
        profile_btn = (ImageButton)header.findViewById(R.id.user_profile_edit);
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

        userAlarm = new AlarmStateObject();
        getUserAlarmState(uSchoolID);

        navMenu = navigationView.getMenu();
        stateAlarmSwitch = (Switch) navMenu.findItem(R.id.user_apply_state_alarm).getActionView().findViewById(R.id.switch_alarm);
        eventAlarmSwitch = (Switch) navMenu.findItem(R.id.user_new_event_alarm).getActionView().findViewById(R.id.switch_alarm);
        newclubAlarmSwitch = (Switch) navMenu.findItem(R.id.user_new_club_alarm).getActionView().findViewById(R.id.switch_alarm);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                String title = menuItem.getTitle().toString();
                if(id == R.id.user_info_edit){
                    Intent intent = new Intent(getApplicationContext(), UserMyAjouDongActivity.class);
                    intent.putExtra("uSchoolID", uSchoolID);
                    startActivity(intent);
                }
                else if(id == R.id.user_apply_result){
                    Intent intent = new Intent(getApplicationContext(), UserApplicationResultActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_bookmarked_list){
                    Intent intent = new Intent(getApplicationContext(), UserBookmarkClubActivity.class);
                    intent.putExtra("uSchoolID", uSchoolID);
                    startActivity(intent);
                }
                else if(id == R.id.user_logout){
                    editor.clear();
                    editor.commit();
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

                return true;
            }
        });

        stateAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userAlarm.setStateAlarm(isChecked);
                changeAlarmState(1);
            }
        });

        eventAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userAlarm.setEventAlarm(isChecked);
                changeAlarmState(2);
            }
        });

        newclubAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userAlarm.setNewclubAlarm(isChecked);
                changeAlarmState(4);
            }
        });


        majorclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorSelectActivity.class);
                intent.putExtra("uSchoolID", uSchoolID);
                startActivity(intent);
                return false;
            }
        });

        mainclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMainClubListActivity.class);
                intent.putExtra("uSchoolID", uSchoolID);
                startActivity(intent);
                return false;
            }
        });

        newclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserNewClubListActivity.class);
                intent.putExtra("uSchoolID", uSchoolID);
                startActivity(intent);
                return false;
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), UserEventListActivity.class);
                startActivity(intent);
                changeAlarmState(3);
            }
        });
    }

    public void changeAlarmState(int type){
        if(loadingAlarm == false) return;
        Call<ResponseObject> alarmCall = retroService.updateUserAlarm(uSchoolID, type);
        alarmCall.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
            }

        });
    }

    protected void onResume() {     //재시작시에 사용자 정보 새로고침
        super.onResume();
        navigationView = (NavigationView) findViewById(R.id.nav_view_user_main);
        header = navigationView.getHeaderView(0);
        user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        user_name = (TextView)header.findViewById(R.id.user_name);
        final ImageView ads1 = (ImageView) findViewById(R.id.ads1);
        getUserprofile();

        Log.d(TAG,"GET");
        Call<AdsObject> getCall2 = retroService.getAdsObject(3);
        getCall2.enqueue(new Callback<AdsObject>() {
            @Override
            public void onResponse(Call<AdsObject> call, Response<AdsObject> response) {
                if( response.isSuccessful()){
                    AdsObject item2  = response.body();
                    int count = item2.getAdsView()-1;
                    setViewCount(count);
                    if(item2.getAdsIMG() != null && item2.getAdsView() != 0){
                        Picasso.get().load(item2.getAdsIMG()).into(ads1);
                    }
                    else{
                        ads1.setImageResource(R.drawable.noads);
                    }
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
                if(getViewCount() >= 0){
                    Log.d(TAG, "PATCH");
                    AdsObject item3 = new AdsObject();
                    item3.setAdsSpace(1);
                    item3.setAdsView(getViewCount());
                    Call<AdsObject> patchCall = retroService.patchAdsObject(3, item3);
                    patchCall.enqueue(new Callback<AdsObject>() {
                        @Override
                        public void onResponse(Call<AdsObject> call, Response<AdsObject> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "patch 성공");
                            } else {
                                Log.d(TAG, "Status Code : " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<AdsObject> call, Throwable t) {
                            Log.d(TAG, "Fail msg : " + t.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<AdsObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
        getUserAlarmState(uSchoolID);
    }

    private void getUserprofile() {
        Log.d(TAG,"GET");       //처음 사용자 정보 불러오기
        Call<UserObject> getCall = retroService.getUserInformation(uSchoolID);
        getCall.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                if( response.isSuccessful()){
                    UserObject item  = response.body();
                    Log.d(TAG, String.valueOf(user_profile.getId()));
                    user_name.setText(item.getuName());
                    if(item.getuIMG() != null){
                        Picasso.get().load(item.getuIMG()).into(user_profile);
                        nowImage3 = item.getuIMG().substring(item.getuIMG().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
                    }
                    else{
                        user_profile.setImageResource(R.drawable.ajoudong_icon);
                    }
//                    nowImage3 = item.getuIMG().substring(item.getuIMG().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
                    Log.d(TAG, nowImage3);
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });


    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
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

        userDeviceToken = FirebaseInstanceId.getInstance().getToken();
        getIDfromToken(userDeviceToken);

    }

    private void getIDfromToken(String userDeviceToken) {
        Call<ResponseObject> call = retroService.getIDbyToken(userDeviceToken);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
                Log.d("Response", Integer.toString(data.getResponse()));
                if(data.getResponse() > 0)
                {
                    uSchoolID = data.getResponse();
                    getUserprofile();

                }
                else
                {
                    uSchoolID = -1;
                    Toast.makeText(getApplicationContext(), "등록되지않은 디바이스", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteIMG(){       //원래 이미지 버킷에서 삭제
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderName+nowImage3));
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
        UserObject item2 = new UserObject();
        item2.setuSchoolID(uSchoolID);
        item2.setuIMG(OBJECT_URL + imgName3); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<UserObject> patchCall = retroService.patchUserInformation(uSchoolID, item2);
        patchCall.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"patch 성공");
                }else{
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
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
        ActivityCompat.finishAffinity(this);
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

    public void getUserAlarmState(int uSchoolID){
        Call<AlarmStateObject> alarmcall = retroService.getUserAlarmState(uSchoolID);     //매니저의 동아리 아이디 받아오기 및 세팅
        alarmcall.enqueue(new Callback<AlarmStateObject>() {
            @Override
            public void onResponse(Call<AlarmStateObject> call, Response<AlarmStateObject> response) {
                if(response.isSuccessful()){
                    AlarmStateObject alarm = response.body();
                    userAlarm.setEventAlarm(alarm.isEventAlarm());
                    userAlarm.setNewclubAlarm(alarm.isNewclubAlarm());
                    userAlarm.setStateAlarm(alarm.isStateAlarm());
                    userAlarm.setUnreadEvent(alarm.getUnreadEvent());

                    stateAlarmSwitch.setChecked(userAlarm.isStateAlarm());
                    eventAlarmSwitch.setChecked(userAlarm.isEventAlarm());
                    newclubAlarmSwitch.setChecked(userAlarm.isNewclubAlarm());
                    loadingAlarm = true;
                }else {
                    Log.d("실패","실패 Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<AlarmStateObject> call, Throwable t) {
                Log.d("실패","실패 msg : " + t.getMessage());
            }
        });
    }

}