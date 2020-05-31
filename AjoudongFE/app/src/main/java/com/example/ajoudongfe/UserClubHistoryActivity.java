package com.example.ajoudongfe;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserClubHistoryActivity extends AppCompatActivity {

    final  String TAG = getClass().getSimpleName();
    private int parameterclubID = 1;
    final String BASE_URL = "http://10.0.2.2:8000";
    private RetroService retroService;

    public UserClubHistoryAdapter adapter;
    private GridView mGridView;

    private void populateGridView(List<ClubActivityGridObject> clubActivityObjectList) {
        mGridView = findViewById(R.id.user_grid_history);
        adapter = new UserClubHistoryAdapter(this, clubActivityObjectList);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_club_history_activity);

       // parameterclubID = getIntent().getIntExtra("clubID", 0);
        //Toast.makeText(this, parameterclubID, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_history_toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title1);
        toolbarTitle.setText("활동 내역");

        initMyAPI(BASE_URL);

        Log.d(TAG, "GET");
        Call<List<ClubActivityGridObject>> call = retroService.get_activitiesGrid(parameterclubID);       //grid 생성
        call.enqueue(new Callback<List<ClubActivityGridObject>>() {
            @Override
            public void onResponse(Call<List<ClubActivityGridObject>> call, Response<List<ClubActivityGridObject>> response) {
                populateGridView(response.body());
                //mGridView.setNestedScrollingEnabled(true);
            }
            @Override
            public void onFailure(Call<List<ClubActivityGridObject>> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 저장 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
