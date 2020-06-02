package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerClubFilterSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private int clubMajor;
    private int numOfTags = 41;
    private int clubID;
    ArrayList<String> tags = new ArrayList<>();
    TextView[] filterViews = new TextView[numOfTags];
    final  String TAG = getClass().getSimpleName();
    private RetroService retroService;

    final String BASE_URL = "http://10.0.2.2:8000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMyAPI(BASE_URL);

        final int clubMajor = getIntent().getIntExtra("clubMajor", 0);
        setClubMajor(clubMajor);

        final int clubID = getIntent().getIntExtra("clubID", 0);
        setClubID(clubID);

        if(clubMajor == 0){     //동아리 종류에 맞게 클럽 필터를 보여줌
            setContentView(R.layout.activity_user_main_club_filter);
            setNumOfTags(41);

            filterViews[0] = (TextView) findViewById(R.id.recruit_y);
            filterViews[1] = (TextView) findViewById(R.id.recruit_n);
            filterViews[2] = (TextView) findViewById(R.id.recruit_interview_n);
            filterViews[3] = (TextView) findViewById(R.id.recruit_interview_y);
            filterViews[4] = (TextView) findViewById(R.id.recruit_limit_n);

            filterViews[5] = (TextView) findViewById(R.id.field_leisure);
            filterViews[6] = (TextView) findViewById(R.id.field_religion);
            filterViews[7] = (TextView) findViewById(R.id.field_social);
            filterViews[8] = (TextView) findViewById(R.id.field_create);
            filterViews[9] = (TextView) findViewById(R.id.field_academic);
            filterViews[10] = (TextView) findViewById(R.id.field_science);
            filterViews[11] = (TextView) findViewById(R.id.field_athletic);
            filterViews[12] = (TextView) findViewById(R.id.field_art);
            filterViews[13] = (TextView) findViewById(R.id.field_semi);

            filterViews[14] = (TextView) findViewById(R.id.atmosphere_show);
            filterViews[15] = (TextView) findViewById(R.id.atmosphere_competition);
            filterViews[16] = (TextView) findViewById(R.id.atmosphere_regular);
            filterViews[17] = (TextView) findViewById(R.id.atmosphere_contest);
            filterViews[18] = (TextView) findViewById(R.id.atmosphere_obyb);
            filterViews[19] = (TextView) findViewById(R.id.atmosphere_afterparty);
            filterViews[20] = (TextView) findViewById(R.id.atmosphere_study);
            filterViews[21] = (TextView) findViewById(R.id.atmosphere_friendship);

            filterViews[22] = (TextView) findViewById(R.id.moeny_51);
            filterViews[23] = (TextView) findViewById(R.id.moeny_12);
            filterViews[24] = (TextView) findViewById(R.id.moeny_23);
            filterViews[25] = (TextView) findViewById(R.id.moeny_3);
            filterViews[26] = (TextView) findViewById(R.id.moeny_1time);
            filterViews[27] = (TextView) findViewById(R.id.moeny_no);

            filterViews[28] = (TextView) findViewById(R.id.day_mon);
            filterViews[29] = (TextView) findViewById(R.id.day_tue);
            filterViews[30] = (TextView) findViewById(R.id.day_wed);
            filterViews[31] = (TextView) findViewById(R.id.day_thu);
            filterViews[32] = (TextView) findViewById(R.id.day_fri);
            filterViews[33] = (TextView) findViewById(R.id.day_sat);
            filterViews[34] = (TextView) findViewById(R.id.day_sun);
            filterViews[35] = (TextView) findViewById(R.id.day_every);

            filterViews[36] = (TextView) findViewById(R.id.time_am);
            filterViews[37] = (TextView) findViewById(R.id.time_pm);
            filterViews[38] = (TextView) findViewById(R.id.time_black);
            filterViews[39] = (TextView) findViewById(R.id.time_night);
            filterViews[40] = (TextView) findViewById(R.id.time_all);

        }
        else if(clubMajor == 1){
            setContentView(R.layout.activity_user_new_club_filter);
            setNumOfTags(32);

            filterViews[0] = (TextView) findViewById(R.id.recruit_y);
            filterViews[1] = (TextView) findViewById(R.id.recruit_n);
            filterViews[2] = (TextView) findViewById(R.id.recruit_interview_n);
            filterViews[3] = (TextView) findViewById(R.id.recruit_interview_y);
            filterViews[4] = (TextView) findViewById(R.id.recruit_limit_n);

            filterViews[5] = (TextView) findViewById(R.id.atmosphere_show);
            filterViews[6] = (TextView) findViewById(R.id.atmosphere_competition);
            filterViews[7] = (TextView) findViewById(R.id.atmosphere_regular);
            filterViews[8] = (TextView) findViewById(R.id.atmosphere_contest);
            filterViews[9] = (TextView) findViewById(R.id.atmosphere_obyb);
            filterViews[10] = (TextView) findViewById(R.id.atmosphere_afterparty);
            filterViews[11] = (TextView) findViewById(R.id.atmosphere_study);
            filterViews[12] = (TextView) findViewById(R.id.atmosphere_friendship);

            filterViews[13] = (TextView) findViewById(R.id.moeny_51);
            filterViews[14] = (TextView) findViewById(R.id.moeny_12);
            filterViews[15] = (TextView) findViewById(R.id.moeny_23);
            filterViews[16] = (TextView) findViewById(R.id.moeny_3);
            filterViews[17] = (TextView) findViewById(R.id.moeny_1time);
            filterViews[18] = (TextView) findViewById(R.id.moeny_no);

            filterViews[19] = (TextView) findViewById(R.id.day_mon);
            filterViews[20] = (TextView) findViewById(R.id.day_tue);
            filterViews[21] = (TextView) findViewById(R.id.day_wed);
            filterViews[22] = (TextView) findViewById(R.id.day_thu);
            filterViews[23] = (TextView) findViewById(R.id.day_fri);
            filterViews[24] = (TextView) findViewById(R.id.day_sat);
            filterViews[25] = (TextView) findViewById(R.id.day_sun);
            filterViews[26] = (TextView) findViewById(R.id.day_every);

            filterViews[27] = (TextView) findViewById(R.id.time_am);
            filterViews[28] = (TextView) findViewById(R.id.time_pm);
            filterViews[29] = (TextView) findViewById(R.id.time_black);
            filterViews[30] = (TextView) findViewById(R.id.time_night);
            filterViews[31] = (TextView) findViewById(R.id.time_all);

        }
        else{
            setContentView(R.layout.activity_user_major_club_filter);
            setNumOfTags(37);

            filterViews[0] = (TextView) findViewById(R.id.recruit_y);
            filterViews[1] = (TextView) findViewById(R.id.recruit_n);
            filterViews[2] = (TextView) findViewById(R.id.recruit_interview_n);
            filterViews[3] = (TextView) findViewById(R.id.recruit_interview_y);
            filterViews[4] = (TextView) findViewById(R.id.recruit_limit_n);

            filterViews[5] = (TextView) findViewById(R.id.field_sport);
            filterViews[6] = (TextView) findViewById(R.id.field_music);
            filterViews[7] = (TextView) findViewById(R.id.field_art);
            filterViews[8] = (TextView) findViewById(R.id.field_academic);
            filterViews[9] = (TextView) findViewById(R.id.field_etc);

            filterViews[10] = (TextView) findViewById(R.id.atmosphere_show);
            filterViews[11] = (TextView) findViewById(R.id.atmosphere_competition);
            filterViews[12] = (TextView) findViewById(R.id.atmosphere_regular);
            filterViews[13] = (TextView) findViewById(R.id.atmosphere_contest);
            filterViews[14] = (TextView) findViewById(R.id.atmosphere_obyb);
            filterViews[15] = (TextView) findViewById(R.id.atmosphere_afterparty);
            filterViews[16] = (TextView) findViewById(R.id.atmosphere_study);
            filterViews[17] = (TextView) findViewById(R.id.atmosphere_friendship);

            filterViews[18] = (TextView) findViewById(R.id.moeny_51);
            filterViews[19] = (TextView) findViewById(R.id.moeny_12);
            filterViews[20] = (TextView) findViewById(R.id.moeny_23);
            filterViews[21] = (TextView) findViewById(R.id.moeny_3);
            filterViews[22] = (TextView) findViewById(R.id.moeny_1time);
            filterViews[23] = (TextView) findViewById(R.id.moeny_no);

            filterViews[24] = (TextView) findViewById(R.id.day_mon);
            filterViews[25] = (TextView) findViewById(R.id.day_tue);
            filterViews[26] = (TextView) findViewById(R.id.day_wed);
            filterViews[27] = (TextView) findViewById(R.id.day_thu);
            filterViews[28] = (TextView) findViewById(R.id.day_fri);
            filterViews[29] = (TextView) findViewById(R.id.day_sat);
            filterViews[30] = (TextView) findViewById(R.id.day_sun);
            filterViews[31] = (TextView) findViewById(R.id.day_every);

            filterViews[32] = (TextView) findViewById(R.id.time_am);
            filterViews[33] = (TextView) findViewById(R.id.time_pm);
            filterViews[34] = (TextView) findViewById(R.id.time_black);
            filterViews[35] = (TextView) findViewById(R.id.time_night);
            filterViews[36] = (TextView) findViewById(R.id.time_all);
        }

        for(int i = 0 ; i < numOfTags ; i++) {      //텍스트 뷰에 클릭리스너 추가
            filterViews[i].setOnClickListener(this);
        }

        Log.d(TAG, "GET");
        Call<List<ClubTagObject>> call = retroService.getClubTagObject(clubID);       //tags에 설정된 태그들 넣어줌
        call.enqueue(new Callback<List<ClubTagObject>>() {
            @Override
            public void onResponse(Call<List<ClubTagObject>> call, Response<List<ClubTagObject>> response) {
                List<ClubTagObject> item =  response.body();
                for(int i = 0 ; i < item.size(); i++){
                    tags.add(item.get(i).getClubTag());     //태그목록에 동아리에 맞는 태그들 추가
                }
                for(int i = 0 ; i < numOfTags ; i++) {          //가져온 동아리 태그에 해당하는 부분 활성화
                    if(tags.contains(filterViews[i].getText())){
                        filterViews[i].setSelected(true);
                    }
                    if(i>=5 && i<=13 && clubMajor == 0){        //중앙 동아리 분야 수정 불가
                        filterViews[i].setClickable(false);
                        filterViews[i].setFocusable(false);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ClubTagObject>> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar) ;        //툴바 관련 생성
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Button filterButton = (Button) findViewById(R.id.mainfilterbtn);    //저장(필터링) 버튼 기능 구현
        filterButton.setText("저장");
        filterButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                deleteTags();
                postNewTags();
                onBackPressed();
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void deleteTags(){      //동아리 태그 목록 모두 삭제
        Log.d(TAG, "DELETE");
        Call<ClubTagObject> deleteCall = retroService.deleteClubTagObject(clubID);
        deleteCall.enqueue(new Callback<ClubTagObject>() {
            @Override
            public void onResponse(Call<ClubTagObject> call, Response<ClubTagObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "삭제 완료");
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ClubTagObject> call, Throwable t) {
                Log.d(TAG, "Fail msg : " + t.getMessage());
            }
        });
    }

    private void postNewTags() {        //동아리 태그 목록 모두 등록
        Log.d(TAG, "POST");
        ClubTagObject item2 = new ClubTagObject();
        item2.setClubID(clubID);
        item2.setTags(tags);
        Call<ClubTagObject> postCall = retroService.postClubTagObject(item2);
        postCall.enqueue(new Callback<ClubTagObject>() {
            @Override
            public void onResponse(Call<ClubTagObject> call, Response<ClubTagObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "등록 완료");
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                    Log.d(TAG, response.errorBody().toString());
                    Log.d(TAG, call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<ClubTagObject> call, Throwable t) {
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

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public void setClubMajor(int clubMajor) {
        this.clubMajor = clubMajor;
    }

    public void setNumOfTags(int numOfTags) {
        this.numOfTags = numOfTags;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //필터 메뉴 생성
        getMenuInflater().inflate(R.menu.user_filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //상단바 버튼 기능 구현
        switch (item.getItemId()) {
            case android.R.id.home :
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
            case R.id.toolbarFilter:
                for(int i = 0 ; i < numOfTags ; i++) {
                    tags.clear();
                    filterViews[i].setSelected(false);
                }
                return true;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    public void onClick(View v) {       //태그 클릭시 활성화 및 비활성화 그리고 태그 목록에 추가 및 삭제
        TextView textView = findViewById(v.getId());
        String now_tag = (String)textView.getText();
        if (v.isSelected()) {
            tags.remove(now_tag);
            v.setSelected(false);
        } else {
            tags.add(now_tag);
            v.setSelected(true);
        }
    }
}
