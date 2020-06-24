package com.example.ajoudongfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerMemberManagementActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PagerAdpater adapter;
    private Toolbar toolbar;
    private Intent intent;
    private Retrofit retrofit;
    public static String BASE_URL= "http://10.0.2.2:8000";
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

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        checkmode = intent.getStringExtra("clubID");
        if(checkmode.indexOf('_') < 0) {
            Log.d("log", "Mode Doesn't Activated");
            clubID = Integer.parseInt(checkmode);
            mode = 0;
        }
        else
        {
            Log.d("log", "Mode Activated");
            clubID = Integer.parseInt(checkmode.substring(1, checkmode.length()));
            mode = 1;
        }
        Log.d("clubID", Integer.toString(clubID));
        Log.d("checkmode", checkmode);

//        clubID = Integer.parseInt(intent.getStringExtra("clubID"));
//        Log.d("clubID", Integer.toString(clubID));



        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        adapter = new PagerAdpater(getSupportFragmentManager(), tabLayout.getTabCount(), clubID);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Bundle bundle = new Bundle();
        bundle.putString("your_key", "your_value");

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
}
