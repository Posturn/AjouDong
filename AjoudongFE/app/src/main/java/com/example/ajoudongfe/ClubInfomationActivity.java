package com.example.ajoudongfe;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubInfomationActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private RecyclerView recyclerview;

    private int schoolID=201421234;
    private int parameterclubID;
    private String parameterclubName;

    private int bkmark=0;


    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;

    private RetroService retroService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info_list);

        String clubName=getIntent().getStringExtra("clubName");
        // ↑ 그리드 클릭시 넘어오는 동아리 이름
        int clubID=getIntent().getIntExtra("clubID", 0);
        // ↑ 그리드 클릭시 넘어오는 동아리 ID
        parameterclubID=clubID;
        parameterclubName=clubName;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        isBookmarked(schoolID);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        ExpandableListAdapter.Item intro = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "동아리 소개");
        intro.invisibleChildren = new ArrayList<>();
        intro.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "사진 동영상 등"));
        intro.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "소개 텍스트"));

        ExpandableListAdapter.Item apply= new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "지원 요강");
        apply.invisibleChildren = new ArrayList<>();
        apply.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "지원 자격"));
        apply.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "온라인으로 지원하세요"));

        ExpandableListAdapter.Item record= new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "활동 내역");
        record.invisibleChildren = new ArrayList<>();
        record.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "활동내역 화면으로 이동"));

        ExpandableListAdapter.Item ratio= new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "회원 비율");
        ratio.invisibleChildren = new ArrayList<>();
        ratio.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "그래프"));

        ExpandableListAdapter.Item contact = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "회장단 연락처");
        contact.invisibleChildren = new ArrayList<>();
        contact.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Instargram : ajoudong"));
        contact.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "회장 : 010-1234-5678"));
        contact.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "부회장 : 010-1234-5678"));

        data.add(intro);
        data.add(apply);
        data.add(record);
        data.add(ratio);
        data.add(contact);


        recyclerview.setAdapter(new ExpandableListAdapter(data));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
            case R.id.toolbarBookmark:{
                if(bkmark == 1){
                deleteBookmark(parameterclubID, schoolID);
                item.setIcon(R.drawable.ic_bookmark);//changing the icon
                bkmark = 0;
            }else{
                postBookmark(parameterclubID, schoolID);
                item.setIcon(R.drawable.ic_bookmarked);//changing the icon
                bkmark = 1;
            }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(bkmark == 0){
            getMenuInflater().inflate(R.menu.club_bookmark_menu, menu);
        }else{
            getMenuInflater().inflate(R.menu.club_bookmarked_menu, menu);
        }
        return true;
    }

    public void postBookmark(int clubID, int schoolID){
        BookmarkObject bookmark = new BookmarkObject(clubID, schoolID);

        Call<BookmarkObject>call = retroService.postBookmark(bookmark);
        call.enqueue(new Callback<BookmarkObject>() {

            @Override
            public void onResponse(Call<BookmarkObject> call, Response<BookmarkObject> response) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName+" 동아리를 담아두었습니다." , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BookmarkObject> call, Throwable throwable) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName+"동아리를 담아두었습니다." , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteBookmark(int clubID, int schoolID){
        BookmarkObject bookmark = new BookmarkObject(clubID, schoolID);

        Call<BookmarkObject>call = retroService.deleteBookmark(clubID, schoolID);
        call.enqueue(new Callback<BookmarkObject>() {
            @Override
            public void onResponse(Call<BookmarkObject> call, Response<BookmarkObject> response) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName+" 동아리 담아두기를 해제하였습니다." , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BookmarkObject> call, Throwable t) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName+" 동아리 담아두기를 해제하였습니다." , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void isBookmarked(int schoolID){

        Call<List<BookmarkObject>> call = retroService.getBookmark(schoolID);
        call.enqueue(new Callback<List<BookmarkObject>>() {

            @Override
            public void onResponse(Call<List<BookmarkObject>> call, Response<List<BookmarkObject>> response) {
                for(BookmarkObject b : response.body()){
                    if(b.getClubID()==parameterclubID){
                        Log.v("clubid", String.valueOf(b.getClubID()));
                        Log.v("paraclubid", String.valueOf(parameterclubID));
                        bkmark=1;
                        Log.v("bkmark2", String.valueOf(bkmark));

                        Toolbar toolbar = (Toolbar) findViewById(R.id.majorselecttoolbar);
                        setSupportActionBar(toolbar);
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setDisplayShowCustomEnabled(true);
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
                        actionBar.setDisplayShowTitleEnabled(false);
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.majorselecttoolbar);
                    setSupportActionBar(toolbar);
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setDisplayShowCustomEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
                    actionBar.setDisplayShowTitleEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<List<BookmarkObject>> call, Throwable throwable) {

            }
        });
    }

}
