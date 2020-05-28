package com.example.ajoudongfe;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.Types.NULL;

public class ManagerClubHistoryEditActivity extends AppCompatActivity {

    final  String TAG = getClass().getSimpleName();
    private final int GET_GALLERY_IMAGE = 200;
    //static final int GET_GALLERY_VIDEO = 100;

    final String BASE_URL = "http://10.0.2.2:8000";
    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/";
    private RetroService retroService;

    final String accessKey = "A";
    final String secretKey = "j";
    final String bucketName = "ajoudong";

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

    private int activityID;
    int manager_clubID = 1;
    static String imgPath2, imgName2, nowImage2;
    String checkImgMp4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_history_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));

        findViewById(R.id.historyImageVideo).bringToFront();
        initMyAPI(BASE_URL);

         final int activityID = getIntent().getIntExtra("activityID", 0);
         setActivityID(activityID);
        // ↑ 그리드 클릭시 넘어오는 활동 내역 pk 번호

        final EditText ET_clubActivityInfo = (EditText)findViewById(R.id.editText4);
        final EditText ET_clubActivityDetail = (EditText)findViewById(R.id.editText5);
        final ImageView IV_historyPoster = (ImageView)findViewById(R.id.historyImageVideo);
        final VideoView VV_historyVideo = (VideoView)findViewById(R.id.videoView);
        VV_historyVideo.setVisibility(View.GONE);

        if(activityID == 0){
            Log.d(TAG, "empty");        //새로운 빈 활동내역 생성
            IV_historyPoster.setImageResource(R.drawable.image_add_btn);
        }
        else{
            Log.d(TAG,"GET");       //처음 동아리 활동내역 정보 불러오기
            Call<ClubActivityObject> getCall = retroService.get_activities_pk(activityID);
            getCall.enqueue(new Callback<ClubActivityObject>() {
                @Override
                public void onResponse(Call<ClubActivityObject> call, Response<ClubActivityObject> response) {
                    if( response.isSuccessful()){
                        ClubActivityObject item  = response.body();
                        ET_clubActivityInfo.setText(item.getClubActivityInfo());
                        ET_clubActivityDetail.setText(item.getClubActivityDetail());
                        nowImage2 = item.getClubActivityFile().substring(item.getClubActivityFile().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
                        checkImgMp4 = nowImage2.substring(nowImage2.lastIndexOf(".")+1);
                            Picasso.get().load(item.getClubActivityFile()).into(IV_historyPoster);
                       // Log.d(TAG, nowImage);
                    }else {
                        Log.d(TAG,"Status Code : " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ClubActivityObject> call, Throwable t) {
                    Log.d(TAG,"Fail msg : " + t.getMessage());
                }
            });
        }

        IV_historyPoster.setClickable(true);
        IV_historyPoster.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                Toast.makeText(getApplicationContext(), "이미지 등록", Toast.LENGTH_LONG).show();
            }
        });

        Button delete_btn = (Button)findViewById(R.id.delete_ac_btn);
        delete_btn.setClickable(true);
        delete_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(activityID == 0){
                    Toast.makeText(getApplicationContext(), "활동 내역이 비어있습니다!", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.d(TAG,"DELETE");
                    // pk 값은 임의로 변경가능
                    Call<ClubActivityObject> deleteCall = retroService.delete_activities_pk(activityID);
                    deleteCall.enqueue(new Callback<ClubActivityObject>() {
                        @Override
                        public void onResponse(Call<ClubActivityObject> call, Response<ClubActivityObject> response) {
                            if(response.isSuccessful()){
                                Log.d(TAG,"삭제 완료");
                            }else {
                                Log.d(TAG,"Status Code : " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<ClubActivityObject> call, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                        }
                    });
                    Toast.makeText(getApplicationContext(), "활동 내역이 삭제되었습니다!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });


    }

    private int getActivityID() {
        return activityID;
    }

    private void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //갤러리 사용
        ImageView IV_historyPoster = (ImageView)findViewById(R.id.historyImageVideo);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String profilePath = getImageNameToUri(data.getData());
            imgName2 = profilePath.substring(profilePath.lastIndexOf("/")+1);
            Uri selectedImageUri = data.getData();
            IV_historyPoster.setImageURI(selectedImageUri);
        }
    }

    public String getImageNameToUri(Uri data) {     //이미지 경로 구하기
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imgPath2 = cursor.getString(column_index);
        return imgPath2;
    }

    private void transferIMG(){     //이미지 S3에 업데이트
        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(this).build();
        TransferObserver transferObserver = transferUtility.upload(bucketName, imgName2, new File(imgPath2), CannedAccessControlList.PublicRead);
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

    private void postNewHistory(int activityID){
        final EditText ET_clubActivityInfo = (EditText)findViewById(R.id.editText4);
        final EditText ET_clubActivityDetail = (EditText)findViewById(R.id.editText5);
        Log.d(TAG,"POST");
        ClubActivityObject item3 = new ClubActivityObject();
        item3.setClubID(manager_clubID);
        item3.setClubActivityID(0);
        item3.setClubActivityInfo(String.valueOf(ET_clubActivityInfo.getText()));
        item3.setClubActivityDetail(String.valueOf(ET_clubActivityDetail.getText()));
        item3.setClubActivityFile(OBJECT_URL + imgName2); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<ClubActivityObject> postCall = retroService.post_activities(item3);
        postCall.enqueue(new Callback<ClubActivityObject>() {
            @Override
            public void onResponse(Call<ClubActivityObject> call, Response<ClubActivityObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"등록 완료");
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                    Log.d(TAG,call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<ClubActivityObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

    private void patchHistory(int activityID){      //글 업데이트 기능 구현
        final EditText ET_clubActivityInfo = (EditText)findViewById(R.id.editText4);
        final EditText ET_clubActivityDetail = (EditText)findViewById(R.id.editText5);
        Log.d(TAG,"PATCH");
        ClubActivityObject item2 = new ClubActivityObject();
        item2.setClubActivityID(activityID);
        item2.setClubActivityInfo(String.valueOf(ET_clubActivityInfo.getText()));
        item2.setClubActivityDetail(String.valueOf(ET_clubActivityDetail.getText()));
        item2.setClubActivityFile(OBJECT_URL + imgName2); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<ClubActivityObject> patchCall = retroService.patch_activities_pk(activityID, item2);
        patchCall.enqueue(new Callback<ClubActivityObject>() {
            @Override
            public void onResponse(Call<ClubActivityObject> call, Response<ClubActivityObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"patch 성공");
                }else{
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ClubActivityObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

    @Override       //등록 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_club_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 등록 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn2:
                int activityID = getActivityID();
                if(activityID == 0){
                    postNewHistory(activityID);
                }
                else{
                    patchHistory(activityID);
                }
                transferIMG();
                Toast.makeText(getApplicationContext(), "활동 내역이 저장되었습니다!", Toast.LENGTH_LONG).show();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

}
