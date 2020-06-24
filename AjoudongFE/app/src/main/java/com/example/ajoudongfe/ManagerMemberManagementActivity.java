package com.example.ajoudongfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerMemberManagementActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdpater adapter;
    private Toolbar toolbar;
    private Intent intent;
    private Retrofit retrofit;
    public static String BASE_URL= "http://10.0.2.2:8000";
    private RetroService retroService;
    private String userDeviceToken;
    private String checkmode;
    private int clubID;
    private int mode;//알림으로 불러왔을시 백프레스를 메인으로 연결
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        mode = 0;
        toolbar = (Toolbar)findViewById(R.id.memberManagementToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        initAPI();
        checkmode = intent.getStringExtra("clubID");
        if(checkmode.length() > 0) {
            Log.d("log", "Mode Activated");
            mode = 1;
        }
        else
        {
            Log.d("log", "Mode Doesn't Activated");
            mode = 1;
        }

        Log.d("clubID", Integer.toString(clubID));

        Bundle bundle = new Bundle();
        bundle.putString("your_key", "your_value");

    }

    private void initTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mode > 0)
        {
            Intent modeIntent = new Intent(getApplicationContext(), ManagerMainActivity.class);
            modeIntent.putExtra("clubID", clubID);
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
    }

    @Override
    public void onBackPressed() {
        if(mode > 0)
        {
            Log.d("mode", "back to main");
            Intent modeIntent = new Intent(getApplicationContext(), ManagerMainActivity.class);
            modeIntent.putExtra("clubID", clubID);
            modeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(modeIntent);
        }
        else
        {
            finish();
        }

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
                    clubID = data.getResponse();
                    Toast.makeText(getApplicationContext(), Integer.toString(clubID), Toast.LENGTH_SHORT).show();
                    tabLayout = (TabLayout)findViewById(R.id.tabLayout);
                    adapter = new PagerAdpater(getSupportFragmentManager(), tabLayout.getTabCount(), clubID);
                    viewPager = findViewById(R.id.viewPager);
                    viewPager.setAdapter(adapter);
                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    initTabLayout();

                }
                else
                {
                    clubID = -1;
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
}
