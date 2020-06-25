package com.example.ajoudongfe;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClubInfomationActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerlayout;
    private RecyclerView recyclerview;

    private int schoolID;
    private int parameterclubID;
    private String parameterclubName;

    private int clubCategory;
    private int bkmark = 0;

    private boolean applyActive = false;
    private boolean applyRecord = false;

    public static String BASE_URL = Keys.getServerUrl();
    private Retrofit retrofit;

    private RetroService retroService;

    private FloatingActionButton applybutton;

    private PromotionObject clubinfo;
    private ClubObject clubobject;

    private ImageView infoback;

    List<ExpandableListAdapter.Item> data = new ArrayList<>();

    ExpandableListAdapter.Item intro;
    ExpandableListAdapter.Item apply;
    ExpandableListAdapter.Item record;
    ExpandableListAdapter.Item ratio;
    ExpandableListAdapter.Item contact;
    ExpandableListAdapter.Item faq;
    ExpandableListAdapter.Item qna;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info_list);
        schoolID = getIntent().getIntExtra("uSchoolID", 0);    //학번 받아오기 및 유저 아이디 세팅

        String clubName = getIntent().getStringExtra("clubName");
        // ↑ 그리드 클릭시 넘어오는 동아리 이름
        int clubID = getIntent().getIntExtra("clubID", 0);
        // ↑ 그리드 클릭시 넘어오는 동아리 ID

        parameterclubID = clubID;
        parameterclubName = clubName;
        clubCategory = getIntent().getIntExtra("clubCategory", 0);

        infoback = (ImageView) findViewById((R.id.clubinfoimage));

        applybutton = (FloatingActionButton) findViewById(R.id.applybutton);
        applybutton.setOnClickListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        intro = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, 0, "동아리 소개");
        apply = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, 0, "지원 요강");
        record = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, parameterclubID,"활동 내역                   ");
        ratio = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, parameterclubID,"회원 비율                   ");
        contact = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, 0,"회장단 연락처");
        faq = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, 0, "자주 하는 질문");
        qna = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, null, parameterclubID,"문의하기                    ");


        isBookmarked(schoolID);
        getClubObject(parameterclubID);
        getClubInfo(parameterclubID);

        checkApply();

        data.add(intro);
        data.add(apply);
        data.add(record);
        data.add(ratio);
        data.add(contact);
        data.add(faq);
        data.add(qna);


        recyclerview.setAdapter(new ExpandableListAdapter(data));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.toolbarBookmark: {
                if (bkmark == 1) {
                    deleteBookmark(parameterclubID, schoolID);
                    item.setIcon(R.drawable.ic_bookmark);//changing the icon
                    bkmark = 0;
                } else {
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

        if (bkmark == 0) {
            getMenuInflater().inflate(R.menu.club_bookmark_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.club_bookmarked_menu, menu);
        }
        return true;
    }

    public void postBookmark(int clubID, int schoolID) {
        BookmarkObject bookmark = new BookmarkObject(clubID, schoolID);

        Call<BookmarkObject> call = retroService.postBookmark(bookmark);
        call.enqueue(new Callback<BookmarkObject>() {

            @Override
            public void onResponse(Call<BookmarkObject> call, Response<BookmarkObject> response) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName + " 동아리를 담아두었습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BookmarkObject> call, Throwable throwable) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName + "동아리를 담아두었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteBookmark(int clubID, int schoolID) {
        BookmarkObject bookmark = new BookmarkObject(clubID, schoolID);

        Call<BookmarkObject> call = retroService.deleteBookmark(clubID, schoolID);
        call.enqueue(new Callback<BookmarkObject>() {
            @Override
            public void onResponse(Call<BookmarkObject> call, Response<BookmarkObject> response) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName + " 동아리 담아두기를 해제하였습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BookmarkObject> call, Throwable t) {
                Toast.makeText(ClubInfomationActivity.this, parameterclubName + " 동아리 담아두기를 해제하였습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void isBookmarked(int schoolID) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.majorselecttoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayShowTitleEnabled(false);

        Call<List<BookmarkObject>> call = retroService.getBookmark(schoolID);
        call.enqueue(new Callback<List<BookmarkObject>>() {

            @Override
            public void onResponse(Call<List<BookmarkObject>> call, Response<List<BookmarkObject>> response) {
                for (BookmarkObject b : response.body()) {
                    if (b.getClubID() == parameterclubID) {
                        Log.v("clubid", String.valueOf(b.getClubID()));
                        Log.v("paraclubid", String.valueOf(parameterclubID));
                        bkmark = 1;
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

    public void getClubInfo(final int paraclubID){
        Call<PromotionObject> call = retroService.get_promotions_pk(paraclubID);
        call.enqueue(new Callback<PromotionObject>(){

            @Override
            public void onResponse(Call<PromotionObject> call, Response<PromotionObject> response) {
                clubinfo=response.body();
                intro.invisibleChildren = new ArrayList<>();
                intro.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, clubinfo.getPosterIMG(), 0, clubinfo.getClubInfo()));

                apply.invisibleChildren = new ArrayList<>();
                apply.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, null, 0,clubinfo.getClubApply()));

                record.invisibleChildren = new ArrayList<>();
                record.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, null, 0,"활동내역 화면으로 이동"));

                ratio.invisibleChildren = new ArrayList<>();
                ratio.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, null, 0,"그래프"));

                contact.invisibleChildren = new ArrayList<>();
                contact.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,null, 0,clubinfo.getClubContact()));

                faq.invisibleChildren = new ArrayList<>();
                faq.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD,null, 0, clubinfo.getClubFAQ()));
            }

            @Override
            public void onFailure(Call<PromotionObject> call, Throwable t) {

            }
        });

    }


    protected void getClubObject(int clubID){
        Call<ClubObject> call = retroService.getClubGrid(clubID);

        call.enqueue(new Callback<ClubObject>() {
            @Override
            public void onResponse(Call<ClubObject> call, Response<ClubObject> response) {
                clubobject=response.body();

                Picasso.get().load(clubobject.getIMG()).into(infoback);
            }

            @Override
            public void onFailure(Call<ClubObject> call, Throwable throwable) {
                Toast.makeText(ClubInfomationActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.applybutton:
                if(applyActive == false) {
                    Toast.makeText(ClubInfomationActivity.this, "동아리 모집기간이 아닙니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                checkApplication();
                if(applyRecord == true){
                    Toast.makeText(ClubInfomationActivity.this, "중복지원할 수 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), UserApplyActivity.class);
                intent.putExtra("clubID", parameterclubID);
                intent.putExtra("userSchoolID", schoolID);
                intent.putExtra("clubCategory", clubCategory);
                startActivity(intent);
        }

    }

    private void checkApplication(){
        Call<ResponseObject> call = retroService.getApplicationRecord(parameterclubID, schoolID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
                if(data.getResponse() == 1)
                {
                    applyRecord = true;
                }else{
                    applyRecord = false;
                }
                Log.d("APPPLY", ""+data.getResponse());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable throwable) {
                Toast.makeText(ClubInfomationActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkApply(){
        Call<ResponseObject> call = retroService.getApplyActive(parameterclubID);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                ResponseObject data = response.body();
                if(data.getResponse() == 1)
                {
                    applyActive = true;
                }else{
                    applyActive = false;
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable throwable) {
                Toast.makeText(ClubInfomationActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
