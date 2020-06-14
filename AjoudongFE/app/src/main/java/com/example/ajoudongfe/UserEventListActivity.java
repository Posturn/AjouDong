package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserEventListActivity extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;
    private RecyclerView eventListRecyclerView;
    private UserEventAdapter userEventAdapter;
    private List<EventObject> listData = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_event_list);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        toolbar = (Toolbar)findViewById(R.id.eventlisttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.add_FAB).setVisibility(View.GONE);

        final LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        Call<EventListObject> call = getEventListAll();
        Log.d("Call", "Start");

        call.enqueue(new Callback<EventListObject>() {
            @Override
            public void onResponse(Call<EventListObject> call, Response<EventListObject> response) {
                EventListObject data = response.body();
                Log.d("response", ""+data);
                listData = data.getContent();

                eventListRecyclerView = (RecyclerView)findViewById(R.id.eventListRecyclerView);
                userEventAdapter = new UserEventAdapter(listData);

                eventListRecyclerView.setLayoutManager(linearLayoutManager3);
                eventListRecyclerView.setAdapter(userEventAdapter);
            }

            @Override
            public void onFailure(Call<EventListObject> call, Throwable t) {
                t.printStackTrace();
                Log.e("연결실패", "실패");
            }
        });

    }

    private Call<EventListObject> getEventListAll() {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getEventListAll();
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
