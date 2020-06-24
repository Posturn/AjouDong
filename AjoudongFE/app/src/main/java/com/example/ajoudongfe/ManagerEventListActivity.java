package com.example.ajoudongfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerEventListActivity extends AppCompatActivity {

    public static String BASE_URL= Keys.getServerUrl();
    private Retrofit retrofit;
    private RecyclerView eventListRecyclerView;
    private EventAdapter eventAdapter;
    private int clubID;
    private List<EventObject> listData = new ArrayList<>();
    private Toolbar toolbar;
    private int count = 0;

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

        final int clubID = getIntent().getIntExtra("clubID", 0);

        FloatingActionButton add_FAB = (FloatingActionButton) findViewById(R.id.add_FAB);
        add_FAB.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagerNewEventActivity.class);
                intent.putExtra("eventID", 0);
                intent.putExtra("clubID", clubID);
                startActivityForResult(intent, 11);


            }
        });

        final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        //final int clubID = getIntent().getIntExtra("clubID", 0);
        Log.d("clubID", ""+clubID);
        Call<EventListObject> call = getEventList(clubID);
        Log.d("Call", "Start");

        call.enqueue(new Callback<EventListObject>() {
            @Override
            public void onResponse(Call<EventListObject> call, Response<EventListObject> response) {
                EventListObject data = response.body();
                Log.d("response", ""+data);
                listData = data.getContent();

                eventListRecyclerView = (RecyclerView)findViewById(R.id.eventListRecyclerView);
                eventAdapter = new EventAdapter(listData, clubID);

                eventListRecyclerView.setLayoutManager(linearLayoutManager2);
                eventListRecyclerView.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<EventListObject> call, Throwable t) {
                t.printStackTrace();
                Log.e("연결실패", "실패");
            }
        });

    }
    
    private Call<EventListObject> getEventList(int clubID) {
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.getEventList(clubID);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            eventAdapter.onActivityResult(requestCode, data);
        }
        else if(requestCode == 11 || requestCode == 111){
            clubID = data.getIntExtra("clubID", 0);
            final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
            //final int clubID = getIntent().getIntExtra("clubID", 0);
            Log.d("clubID", ""+clubID);
            Call<EventListObject> call = getEventList(clubID);
            Log.d("Call", "Start");

            call.enqueue(new Callback<EventListObject>() {
                @Override
                public void onResponse(Call<EventListObject> call, Response<EventListObject> response) {
                    EventListObject data = response.body();
                    Log.d("response", ""+data);
                    listData = data.getContent();

                    eventListRecyclerView = (RecyclerView)findViewById(R.id.eventListRecyclerView);
                    eventAdapter = new EventAdapter(listData, clubID);

                    eventListRecyclerView.setLayoutManager(linearLayoutManager2);
                    eventListRecyclerView.setAdapter(eventAdapter);
                    eventAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<EventListObject> call, Throwable t) {
                    t.printStackTrace();
                    Log.e("연결실패", "실패");
                }
            });
        }
    }
}
