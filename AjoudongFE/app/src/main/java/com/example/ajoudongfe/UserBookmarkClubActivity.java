package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserBookmarkClubActivity<list> extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;

    private RetroService retroService;

    public ClubGridAdapter adapter;
    private GridView mGridView;
    private int schoolID=201421234;
    private List<BookmarkObject> bookmarkmodels;
    private List<ClubObject> clubmodels = new ArrayList<>();


    private void populateGridView(List<ClubObject> clubObjectList) {
        mGridView = findViewById(R.id.bookmarkgrid);
        adapter = new ClubGridAdapter(this, clubObjectList, null);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetBookmarkGrid(schoolID);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookmark_club);


        Toolbar toolbar = (Toolbar) findViewById(R.id.userbookmarktoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayShowTitleEnabled(false);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

    }


    protected void GetBookmarkGrid(int schoolID){
        Call<List<BookmarkObject>> call = retroService.getBookmark(schoolID);
        call.enqueue(new Callback<List<BookmarkObject>>() {

            @Override
            public void onResponse(Call<List<BookmarkObject>> call, Response<List<BookmarkObject>> response) {
                Log.v("북마크객체", String.valueOf(response.body()));
                bookmarkmodels=response.body();
                for(BookmarkObject value : response.body()){
                    GridSearch(value);
                    clubmodels=new ArrayList<>();
                }
                Log.v("그리드 객체", String.valueOf(clubmodels));
                //populateGridView(clubmodels);
            }

            @Override
            public void onFailure(Call<List<BookmarkObject>> call, Throwable throwable) {
                Toast.makeText(UserBookmarkClubActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

protected void GridSearch(BookmarkObject value){
    Call<ClubObject> callclub = retroService.getClubGrid(value.getClubID());
    callclub.enqueue(new Callback<ClubObject>() {

        @Override
        public void onResponse(Call<ClubObject> call, Response<ClubObject> response) {

            clubmodels.add(response.body());
            Log.v("클럽객체", String.valueOf(response.body()));
            Log.v("add 후 객체", String.valueOf(clubmodels));
            populateGridView(clubmodels);
        }

        @Override
        public void onFailure(Call<ClubObject> call, Throwable t) {
            Toast.makeText(UserBookmarkClubActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }

    });
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{//뒤로가기
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}