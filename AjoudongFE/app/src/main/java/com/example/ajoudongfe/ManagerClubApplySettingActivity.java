package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerClubApplySettingActivity extends AppCompatActivity {

    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = "http://10.0.2.2:8000";
    private RetroService retroService;

    private int manager_ClubID = 1;

    public int getSelectCalendar() {
        return selectCalendar;
    }

    public void setSelectCalendar(int selectCalendar) {
        this.selectCalendar = selectCalendar;
    }

    private int selectCalendar;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(getSelectCalendar() == 1){
                updateLabel(1);
            }
            else{
                updateLabel(2);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_apply_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        initMyAPI(BASE_URL);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        final EditText ET_additionQ = (EditText)findViewById(R.id.plusQuestion);
        final EditText ET_startDate = (EditText) findViewById(R.id.recruitDate1);
        final EditText ET_EndDate = (EditText) findViewById(R.id.recruitDate2);

        final int manager_ClubID = getIntent().getIntExtra("clubID", 0);
        setManager_ClubID(manager_ClubID);

        Log.d(TAG,"GET");       //처음 동아리 정보 불러오기
        Call<PromotionObject> getCall = retroService.get_promotions_pk(manager_ClubID);
        getCall.enqueue(new Callback<PromotionObject>() {
            @Override
            public void onResponse(Call<PromotionObject> call, Response<PromotionObject> response) {
                if( response.isSuccessful()){
                    PromotionObject item  = response.body();
                    ET_additionQ.setText(item.getAdditionalApply());
                    ET_startDate.setText(item.getRecruitStartDate());
                    ET_EndDate.setText(item.getRecruitEndDate());
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<PromotionObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

        ImageButton IB_Date1 = (ImageButton) findViewById(R.id.calendar1);
        IB_Date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectCalendar(1);
                new DatePickerDialog(ManagerClubApplySettingActivity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ImageButton IB_Date2 = (ImageButton) findViewById(R.id.calendar2);
        IB_Date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectCalendar(2);
                new DatePickerDialog(ManagerClubApplySettingActivity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public int getManager_ClubID() {
        return manager_ClubID;
    }

    public void setManager_ClubID(int manager_ClubID) {
        this.manager_ClubID = manager_ClubID;
    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    private void patchPromo() throws ParseException {      //글 업데이트 기능 구현
        final EditText ET_additionQ = (EditText)findViewById(R.id.plusQuestion);
        final EditText ET_startDate = (EditText) findViewById(R.id.recruitDate1);
        final EditText ET_EndDate = (EditText) findViewById(R.id.recruitDate2);
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018-11-28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        Calendar cal = Calendar.getInstance();
        if(sdf.format(cal.getTime()).compareTo(String.valueOf(ET_startDate.getText())) >= 0 && sdf.format(cal.getTime()).compareTo(String.valueOf(ET_EndDate.getText())) <= 0){
            Log.d(TAG, "PATCH");
            Call<ResponseObject> patchCall = retroService.patchrecruitTag(manager_ClubID, 1);
            patchCall.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "패치 완료");
                    } else {
                        Log.d(TAG, "Status Code : " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Log.d(TAG, "Fail msg : " + t.getMessage());
                }
            });
        }
        else{
            Log.d(TAG, "PATCH");
            Call<ResponseObject> patchCall = retroService.patchrecruitTag(manager_ClubID, 0);
            patchCall.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "패치 완료");
                    } else {
                        Log.d(TAG, "Status Code : " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Log.d(TAG, "Fail msg : " + t.getMessage());
                }
            });
        }
        Log.d(TAG,"PATCH");
        PromotionObject item2 = new PromotionObject();
        item2.setAdditionalApply(String.valueOf(ET_additionQ.getText()));
        item2.setRecruitStartDate(ET_startDate.getText().toString());
        item2.setRecruitEndDate(ET_EndDate.getText().toString());
        Log.d(TAG, ET_startDate.getText().toString());
        Call<PromotionObject> patchCall = retroService.patch_promotions_pk(manager_ClubID,item2);
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
                try {
                    patchPromo();       //텍스트 저장
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage());
                }
                onBackPressed();
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel(int selection) {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018-11-28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText ET_startDate = (EditText) findViewById(R.id.recruitDate1);
        EditText ET_EndDate = (EditText) findViewById(R.id.recruitDate2);
        if(selection == 1){
            if(sdf.format(myCalendar.getTime()).compareTo(String.valueOf(ET_EndDate.getText())) > 0){
                Toast.makeText(getApplicationContext(), "종료날짜를 변경해주세요!", Toast.LENGTH_LONG).show();
            }
            else{
                ET_startDate.setText(sdf.format(myCalendar.getTime()));
            }
        }
        else{
            if(sdf.format(myCalendar.getTime()).compareTo(String.valueOf(ET_startDate.getText())) < 0){
                Toast.makeText(getApplicationContext(), "시작날짜를 변경해주세요!", Toast.LENGTH_LONG).show();
            }
            else{
                ET_EndDate.setText(sdf.format(myCalendar.getTime()));
            }
        }
    }

}
