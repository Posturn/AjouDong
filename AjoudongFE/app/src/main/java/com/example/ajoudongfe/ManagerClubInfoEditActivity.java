package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ManagerClubInfoEditActivity extends AppCompatActivity {

    final  String TAG = getClass().getSimpleName();
    private final int GET_GALLERY_IMAGE = 200;


    final String BASE_URL = "http://10.0.2.2:8000/promotions/";
    final String GRID_URL = "http://10.0.2.2:8000/activities/";
    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/";
    private RetroService retroService;

    //final String endPoint = "ajoudong.s3.ap-northeast-2.amazonaws.com";
    final String accessKey = "AKIAYDYHJ54M6HIXDUX2";
    final String secretKey = "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";
    final String bucketName = "ajoudong";
    static String imgPath, imgName;


    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_info_edit);

        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        //s3Client.setEndpoint(endPoint);

        final EditText ET_clubInfo = (EditText)findViewById(R.id.editText);
        final EditText ET_clubApply = (EditText)findViewById(R.id.editText2);
        final EditText ET_clubFAQ = (EditText)findViewById(R.id.editText3);
        final EditText ET_clubContact = (EditText)findViewById(R.id.editText7);
        final ImageView IV_clubPoster = (ImageView)findViewById(R.id.clubProfile);

        GridView gridView = findViewById(R.id.activity_grid);
        ClubHistoryAdapter adapter = new ClubHistoryAdapter();

        findViewById(R.id.camera_btn).bringToFront();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        initMyAPI(BASE_URL);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.ic_add), ""));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid1), "활동1"));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid2), "활동2"));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid3), "활동3"));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid4), "활동4"));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid5), "활동5"));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid6), "활동6"));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid7), "활동7"));
        gridView.setNestedScrollingEnabled(true);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {        //그리드 선택 버튼 기능 구현
                        Intent intent = new Intent(getApplicationContext(), ManagerClubHistoryEditActivity.class);
                        Toast.makeText(getApplicationContext(), "활동 내용 추가", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
        });

        Log.d(TAG,"GET");       //처음 동아리 정보 불러오기
        Call<PromotionObject> getCall = retroService.get_promotions_pk(1);
        getCall.enqueue(new Callback<PromotionObject>() {
            @Override
            public void onResponse(Call<PromotionObject> call, Response<PromotionObject> response) {
                if( response.isSuccessful()){
                    PromotionObject item  = response.body();
                        ET_clubInfo.setText(item.getClubInfo());
                        ET_clubApply.setText(item.getClubApply());
                        ET_clubFAQ.setText(item.getClubFAQ());
                        ET_clubContact.setText(item.getClubContact());
                        Picasso.get().load(item.getPosterIMG()).into(IV_clubPoster);
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<PromotionObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

        ImageButton profile_btn = (ImageButton)findViewById(R.id.camera_btn);       // 이미지 편집 버튼 기능 구현
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

}

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    private void transferIMG(){     //이미지 S3에 업데이트
        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(this).build();
        TransferObserver transferObserver = transferUtility.upload(bucketName, imgName, new File(imgPath), CannedAccessControlList.PublicRead);
        transferObserver.setTransferListener(new TransferListener() {
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

    private void patchProfile(){        // 동아리 이미지 업데이트
        final ImageView IV_clubPoster = (ImageView)findViewById(R.id.clubProfile);
        Log.d(TAG,"PATCH");
        PromotionObject item2 = new PromotionObject();
        item2.setPosterIMG(OBJECT_URL + imgName); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<PromotionObject> patchCall = retroService.patch_promotions_pk(1,item2);
        patchCall.enqueue(new Callback<PromotionObject>() {
            @Override
            public void onResponse(Call<PromotionObject> call, Response<PromotionObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"patch 성공");
                }else{
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<PromotionObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

    private void patchPromo(){      //글 업데이트 기능 구현
        final EditText ET_clubInfo = (EditText)findViewById(R.id.editText);
        final EditText ET_clubApply = (EditText)findViewById(R.id.editText2);
        final EditText ET_clubFAQ = (EditText)findViewById(R.id.editText3);
        final EditText ET_clubContact = (EditText)findViewById(R.id.editText7);
        Log.d(TAG,"PATCH");
        PromotionObject item2 = new PromotionObject();
        item2.setClubInfo(String.valueOf(ET_clubInfo.getText()));
        item2.setClubApply(String.valueOf(ET_clubApply.getText()));
        item2.setClubFAQ(String.valueOf(ET_clubFAQ.getText()));
        item2.setClubContact(String.valueOf(ET_clubContact.getText()));
        Call<PromotionObject> patchCall = retroService.patch_promotions_pk(1,item2);
        patchCall.enqueue(new Callback<PromotionObject>() {
            @Override
            public void onResponse(Call<PromotionObject> call, Response<PromotionObject> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"patch 성공");
                }else{
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PromotionObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

    @Override       //저장 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_club_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 저장 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn1:      //저장하기 버튼
                patchPromo();       //텍스트 저장
                transferIMG();      //이미지 전송
                patchProfile();     //이미지 업데이트
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //갤러리 사용
        ImageView IV_clubPoster = (ImageView)findViewById(R.id.clubProfile);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String profilePath = getImageNameToUri(data.getData());
            imgName = profilePath.substring(profilePath.lastIndexOf("/")+1);
            Uri selectedImageUri = data.getData();
            IV_clubPoster.setImageURI(selectedImageUri);
        }

    }
    public String getImageNameToUri(Uri data) {     //이미지 경로 구하기
        String[] proj = { MediaStore.Images.Media.DATA };
        //CursorLoader loader = new CursorLoader(mContext, data, proj,null,null)
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imgPath = cursor.getString(column_index);
        return imgPath;
    }



}
