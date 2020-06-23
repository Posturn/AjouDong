package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApplicationResultActivity extends AppCompatActivity {
    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;
    private RecyclerView applicationResultRecyclerView;
    private ApplicationResultAdapter applicationResultAdapter;
    private int uSchoolID;
    private List<ApplicationObject> listData = new ArrayList<>();
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_application_result);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        toolbar = (Toolbar)findViewById(R.id.applicationtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uSchoolID = 201720988;
        Call<ApplicationListObject> call = getApplicationResultList(uSchoolID);
        Log.d("Call", "Start");

        call.enqueue(new Callback<ApplicationListObject>() {
            @Override
            public void onResponse(Call<ApplicationListObject> call, Response<ApplicationListObject> response) {
                ApplicationListObject data = response.body();
                listData = data.getContent();

                applicationResultRecyclerView = (RecyclerView)findViewById(R.id.applicationResultRecyclerView);
                applicationResultAdapter = new ApplicationResultAdapter(UserApplicationResultActivity.this,listData, uSchoolID);

                applicationResultRecyclerView.setLayoutManager(linearLayoutManager);
                applicationResultRecyclerView.setAdapter(applicationResultAdapter);

            }

            @Override
            public void onFailure(Call<ApplicationListObject> call, Throwable t) {
                t.printStackTrace();
                Log.e("연결실패", "실패");
            }
        });


    }

    private Call<ApplicationListObject> getApplicationResultList(int uSchoolID) {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getApplicationResult(uSchoolID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
