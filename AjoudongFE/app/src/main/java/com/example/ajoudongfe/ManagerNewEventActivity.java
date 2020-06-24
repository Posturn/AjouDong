package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerNewEventActivity extends AppCompatActivity {

    final String TAG = getClass().getSimpleName();
    private final int GET_GALLERY_IMAGE = 200;

    final String BASE_URL = Keys.getServerUrl();
    final String OBJECT_URL = "https://ajoudong.s3.ap-northeast-2.amazonaws.com/events/";
    private RetroService retroService;


    final String accessKey = Keys.getAccessKey();
    final String secretKey = Keys.getSecretKey();
    final String bucketName = "ajoudong";
    final String folderName = "events/";

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

    private int eventID;
    private int clubID;
    static private String imgPath2, imgName2, nowImage2 = "";


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.eventtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        s3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        initMyAPI(BASE_URL);

        final int eventID = getIntent().getIntExtra("eventID", 0);
        final int clubID = getIntent().getIntExtra("clubID", 0);
        setEventID(eventID);
        setClubID(clubID);

        final ImageView addEventIMG = (ImageView)findViewById(R.id.addEventIMG);
        final ImageView eventProfile = (ImageView)findViewById(R.id.eventProfile);
        final EditText eventTitle = (EditText)findViewById(R.id.eventTitle);
        final EditText eventDay = (EditText)findViewById(R.id.eventDay);
        final ImageButton calendar3 = (ImageButton)findViewById(R.id.calendar3);
        final EditText eventInfo = (EditText)findViewById(R.id.eventInfo);

        eventInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }

        });

        if(eventID == 0){       //새롭게 이벤트 등록시
            eventProfile.setImageResource(R.drawable.ajoudong_icon);
        }
        else{
            Log.d(TAG, "GET");       //처음 동아리 활동내역 정보 불러오기
            Call<EventObject> getCall = retroService.getEventObject(eventID);
            getCall.enqueue(new Callback<EventObject>() {
                @Override
                public void onResponse(Call<EventObject> call, Response<EventObject> response) {
                    if (response.isSuccessful()) {
                        EventObject item = response.body();
                        eventTitle.setText(item.getEventName());
                        eventDay.setText(item.getEventDate());
                        eventInfo.setText(item.getEventInfo());
                        if(item.getEventIMG() != null){
                            nowImage2 = item.getEventIMG().substring(item.getEventIMG().lastIndexOf("/") + 1);   //현재 이미지 파일 이름 가져오기
                            Picasso.get().load(item.getEventIMG()).into(eventProfile);
                        }
                        else{
                            eventProfile.setImageResource(R.drawable.icon);
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

        addEventIMG.setClickable(true);
        addEventIMG.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        calendar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManagerNewEventActivity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018-11-28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        EditText eventDay = (EditText)findViewById(R.id.eventDay);
        eventDay.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //갤러리 사용
        ImageView eventProfile = (ImageView)findViewById(R.id.eventProfile);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String profilePath = getImageNameToUri(data.getData());
            imgName2 = profilePath.substring(profilePath.lastIndexOf("/")+1);
            Uri selectedImageUri = data.getData();
            eventProfile.setImageURI(selectedImageUri);
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

    private void transferIMG() {     //이미지 S3에 업데이트
        if(imgPath2 != null){
            new DeleteTask().execute();
            TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(this).build();
            TransferObserver transferObserver = transferUtility.upload(bucketName, folderName+imgName2, new File(imgPath2), CannedAccessControlList.PublicRead);
            transferObserver.setTransferListener(new TransferListener() {       //새 이미지 버킷에 전송
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: " + id + ", " + state.toString());
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                    Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            });
        }
    }

    private void postNewEvent() {
        final EditText eventTitle = (EditText)findViewById(R.id.eventTitle);
        final EditText eventDay = (EditText)findViewById(R.id.eventDay);
        final EditText eventInfo = (EditText)findViewById(R.id.eventInfo);
        Log.d(TAG, "POST");
        EventObject item3 = new EventObject();
        item3.setClubID(getClubID());
        item3.setEventID(0);
        item3.setEventName(String.valueOf(eventTitle.getText()));
        item3.setEventInfo(String.valueOf(eventInfo.getText()));
        item3.setEventDate(eventDay.getText().toString());
        item3.setEventIMG(OBJECT_URL + imgName2); //여기에 바뀐 포스터 이미지 링크 삽입
        Call<EventObject> postCall = retroService.postEventObject(item3);
        postCall.enqueue(new Callback<EventObject>() {
            @Override
            public void onResponse(Call<EventObject> call, Response<EventObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "등록 완료");
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                    Log.d(TAG, call.request().body().toString());
                }
            }
            @Override
            public void onFailure(Call<EventObject> call, Throwable t) {
                Log.d(TAG, "Fail msg : " + t.getMessage());
            }
        });

        Call<ResponseObject> newEventAlarm = retroService.newClubEventAlarm();
        newEventAlarm.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                Log.d(TAG, "신규 행사 알림 전송 성공");
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.d(TAG, "신규 행사 알림 전송 실패");
            }
        });
        Intent intent = new Intent();
        intent.putExtra("clubID", getClubID());
        setResult(11, intent);
    }

    private void plusUnreadEvent(){
        Call<ResponseObject> alarmCall = retroService.addUnreadEvent();
        alarmCall.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void patchEvent() {      //글 업데이트 기능 구현
        final EditText eventTitle = (EditText)findViewById(R.id.eventTitle);
        final EditText eventDay = (EditText)findViewById(R.id.eventDay);
        final EditText eventInfo = (EditText)findViewById(R.id.eventInfo);
        Log.d(TAG, "PATCH");
        EventObject item2 = new EventObject();
        item2.setClubID(getClubID());
        item2.setEventID(getEventID());
        item2.setEventName(String.valueOf(eventTitle.getText()));
        item2.setEventInfo(String.valueOf(eventInfo.getText()));
        item2.setEventDate(eventDay.getText().toString());
        if(imgPath2 != null){
            item2.setEventIMG(OBJECT_URL + imgName2); //여기에 바뀐 포스터 이미지 링크 삽입
        }
        Call<EventObject> patchCall = retroService.patchEventObject(eventID, item2);
        patchCall.enqueue(new Callback<EventObject>() {
            @Override
            public void onResponse(Call<EventObject> call, Response<EventObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "patch 성공");
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EventObject> call, Throwable t) {
                Log.d(TAG, "Fail msg : " + t.getMessage());
            }
        });
        Intent intent = new Intent();
        intent.putExtra("clubID",getClubID());
        setResult(111, intent);
    }

    @Override       //등록 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_club_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 등록 버튼 기능 구현
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("clubID",getClubID());
                setResult(1, intent);
                onBackPressed();
                finish();
            case R.id.action_btn2:
                if (getEventID()== 0) {
                    postNewEvent();
                    plusUnreadEvent();
                } else {
                    patchEvent();
                }
                transferIMG();
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMyAPI(String baseUrl) {     //레트로 핏 설정
        Log.d(TAG, "initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    private void deleteIMG(){       //원래 이미지 버킷에서 삭제
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderName+nowImage2));
            // Log.d(TAG,nowImage +" is deleted!");
        } catch (AmazonServiceException ase) {
            Log.e(TAG, ase.getErrorMessage());
        }
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
