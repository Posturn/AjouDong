package com.example.ajoudongfe;

import com.example.ajoudongfe.ExpandableListAdapter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;



import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClubInfomation extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info_list);

        String clubName=getIntent().getStringExtra("clubName");
        // ↑ 그리드 클릭시 넘어오는 동아리 이름

        Toolbar toolbar = (Toolbar) findViewById(R.id.majorselecttoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayShowTitleEnabled(false);


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
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.club_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
