package com.example.ajoudongfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserNewClubListActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout drawerlayout;
    private Context context = this;
    private int schoolID;

    public static String BASE_URL= Keys.getServerUrl();
    private Retrofit retrofit;

    private final int GET_GALLERY_IMAGE = 200;
    final  String TAG = getClass().getSimpleName();

    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/user_profile/";
    private RetroService retroService;

    final String accessKey = Keys.getAccessKey();
    final String secretKey = Keys.getSecretKey();
    final String bucketName = "ajoudong";
    final String folderName = "user_profile/";
    static private String imgPath3, imgName3, nowImage3 = "";

    private int uSchoolID = 201720988; //테스트용 사용자 아이디

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

    public List<ClubObject> clubObjects;
    public ClubGridAdapter adapter;
    private GridView mGridView;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SearchView searchView;
    private String search_text = null;
    private boolean search_now = false;
    private int now_spin = 0;
    private int club_num = 1;
    private String selectedCategory = "전체";
    private boolean tag_now = false;

    private ArrayList<String> tags = new ArrayList<String>();
    private List<Integer> nRecruitClub = new ArrayList<>();
    private long backKeyPressedTime;

    private AlarmStateObject userAlarm;
    private Switch stateAlarmSwitch;
    private Switch eventAlarmSwitch;
    private Switch newclubAlarmSwitch;
    private Menu navMenu;
    private boolean loadingAlarm = false;

    private NavigationView navigationView;

    private void populateGridView(List<ClubObject> clubObjectList, List<Integer> nRecruit) {
        mGridView = findViewById(R.id.gridView01);
        adapter = new ClubGridAdapter(this, clubObjectList, nRecruit, schoolID);
        mGridView.setAdapter(adapter);
    }

    final String ajouorange="#F5A21E";
    final String gray ="#707070";

    private Button[] newfoundButton=new Button[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new_club_list);
        uSchoolID = getIntent().getIntExtra("uSchoolID", 0);    //학번 받아오기 및 유저 아이디 세팅

        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        initMyAPI(BASE_URL);

        newfoundButton[0] = (Button) findViewById(R.id.cateNewAll);
        newfoundButton[1] = (Button) findViewById(R.id.cateNewNew);
        newfoundButton[2] = (Button) findViewById(R.id.cateNewStartup);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        Call<List<Integer>> nrecruitcall = retroService.getnRecruitClub();
        nrecruitcall.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                nRecruitClub = response.body();
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable throwable) {
               // Toast.makeText(UserNewClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Call<List<ClubObject>> call = retroService.getClubGridAll(club_num, selectedCategory, now_spin);
        CallEnqueueClubObject(call);

        pref = getSharedPreferences("autologin", MODE_PRIVATE);
        editor = pref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.newclubtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view_user_new_club_list);
        final View header = navigationView.getHeaderView(0);
        final ImageView user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        final TextView user_name = (TextView)header.findViewById(R.id.user_name);

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

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_new_club_list);
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
        for(int i = 0 ; i < 3 ; i++) {
            newfoundButton[i].setOnClickListener(this);
        }
        newfoundButton[0].performClick();

        Spinner spinner = (Spinner)findViewById(R.id.newClubSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                now_spin = position;
                if(search_text==null || search_text.equals("")){
                    search_now=false;
                }
                //0. 정렬(랜덤) 1. 가나다순(오름차순) 2. 가나다순(내림차순)
                if(search_now == false && tag_now == false){
                    ClubSort();
                }else if(search_now == true && tag_now == false){
                    ClubSearch();
                }else if(search_now == false && tag_now == true){
                    newfoundButton[0].performClick();
                    ClubFilter();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ClubSort();
            }
        });
    }

    protected void onResume(){
        super.onResume();
        navigationView = (NavigationView) findViewById(R.id.nav_view_user_new_club_list);
        final View header = navigationView.getHeaderView(0);
        final ImageView user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        final TextView user_name = (TextView)header.findViewById(R.id.user_name);

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
        getUserAlarmState(uSchoolID);
    }



    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
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
    public void onClick(View v) {
        Button newButton = (Button) v;

        for(Button tempButton : newfoundButton)
        {
            if(tempButton == newButton)
            {
                categoryUnclicked();
                tempButton.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));
                tempButton.setTextColor(Color.parseColor(ajouorange));
                tempButton.setBackgroundResource(R.drawable.grid_new_category_click_shape);
                selectedCategory= (String) tempButton.getText();
                ClubSort();
            }
        }
    }

    public void categoryUnclicked(){
        for (int i = 0; i < 3; i++) {
            newfoundButton[i].setTextColor(Color.parseColor(gray));
            newfoundButton[i].setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
            newfoundButton[i].setBackgroundResource(R.drawable.grid_category_unclick_shape);
        }
    }



    //툴바 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.toolbarSearch:{
                return true;
            }
            case R.id.toolbarFilter:{
                Intent intent = new Intent(getApplicationContext(), UserNewClubFilterActivity.class);
                intent.putStringArrayListExtra("TAGLIST",tags);
                startActivityForResult(intent, 1);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_search_menu, menu);
        searchView = (SearchView)menu.findItem(R.id.toolbarSearch).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("동아리명을 입력하세요.");
        searchView.setBackgroundColor(255);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//검색 완료시
                search_text = s;
                search_now = true;
                if(tag_now == false) ClubSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //검색어 입력시
                search_text = null;
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK && requestCode == 1){
            tags = data.getStringArrayListExtra("TAGLIST");
            if(tags.size() == 0 || tags.isEmpty()) {
                tag_now = false;
                ClubSort();
            }
            else{
                tag_now = true;
                newfoundButton[0].performClick();
                ClubFilter();
            }
        }else{
            tag_now = false;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_new_club_list);       //갤러리에서 이미지 고르기
        final View header = navigationView.getHeaderView(0);
        ImageView user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String profilePath = getImageNameToUri(data.getData());
            imgName3 = profilePath.substring(profilePath.lastIndexOf("/") + 1);
            Uri selectedImageUri = data.getData();
            user_profile.setImageURI(selectedImageUri);
            transferIMG();
            patchProfile();
        }
    }

    protected void ClubSort(){
        Call<List<ClubObject>> call = retroService.getClubGridAll(club_num, selectedCategory, now_spin);
        CallEnqueueClubObject(call);
    }

    protected void ClubSearch(){
        Call<List<ClubObject>> call = retroService.getClubGridSearch(club_num, selectedCategory, now_spin, search_text);
        CallEnqueueClubObject(call);
    }

    protected void ClubFilter(){
        final ClubFilterObject clubFilterObject = new ClubFilterObject(club_num, now_spin, tags);
        Call<List<ClubObject>> call = retroService.getClubGridFilter(clubFilterObject);
        CallEnqueueClubObject(call);
    }

    protected void CallEnqueueClubObject(Call<List<ClubObject>> call){
        call.enqueue(new Callback<List<ClubObject>>() {
            @Override
            public void onResponse(Call<List<ClubObject>> call, Response<List<ClubObject>> response) {
                populateGridView(response.body(), nRecruitClub);
            }

            @Override
            public void onFailure(Call<List<ClubObject>> call, Throwable throwable) {
               // Toast.makeText(UserNewClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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


    public void changeAlarmState(int type){
        if(loadingAlarm == false) return;
        Call<ResponseObject> alarmCall = retroService.updateUserAlarm(uSchoolID, type);
        alarmCall.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
                if(data.getResponse() == 1) Toast.makeText(getApplicationContext(), "알림 변경", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();
            }

        });
    }


}


