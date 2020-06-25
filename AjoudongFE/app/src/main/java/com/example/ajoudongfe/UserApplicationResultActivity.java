package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApplicationResultActivity extends AppCompatActivity {
    public static String BASE_URL= Keys.getServerUrl();
    private Retrofit retrofit;
    private RetroService retroService;
    private RecyclerView applicationResultRecyclerView;
    private ApplicationResultAdapter applicationResultAdapter;
    private int uSchoolID;
    private Intent intent;
    private int mode;
    private LinearLayoutManager linearLayoutManager;
    private List<ApplicationObject> listData = new ArrayList<>();
    private Toolbar toolbar;
    private String userDeviceToken;
    private String checkmode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_application_result);
        linearLayoutManager = new LinearLayoutManager(this);

        intent = getIntent();

        toolbar = (Toolbar)findViewById(R.id.applicationtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initAPI();
        checkmode = intent.getStringExtra("pushed");
        if(checkmode != null) {
            Log.d("log", "Mode Activated");
            mode = 1;
        }
        else
        {
            Log.d("log", "Mode Doesn't Activated");
            mode = 0;
        }

    }

    private void initAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
                    getList();

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

    private void getList() {
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
        if(mode > 0)
        {
            Intent modeIntent = new Intent(getApplicationContext(), UserMainActivity.class);
            modeIntent.putExtra("uSchoolID", uSchoolID);
            modeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(modeIntent);
            return true;
        }
        else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mode > 0)
        {
            Log.d("mode", "back to main");
            Intent modeIntent = new Intent(getApplicationContext(), UserMainActivity.class);
            modeIntent.putExtra("uSchoolID", uSchoolID);
            modeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(modeIntent);
        }
        else
        {
            finish();
        }

    }
}
