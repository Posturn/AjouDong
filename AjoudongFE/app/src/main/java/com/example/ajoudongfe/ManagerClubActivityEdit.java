package com.example.ajoudongfe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class ManagerClubActivityEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        GridView gridView = findViewById(R.id.grid_ac_image);
        ClubActivityImageAdapter adapter = new ClubActivityImageAdapter();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.add_ac_image_btn), ""));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid1), ""));
        gridView.setNestedScrollingEnabled(true);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();

            }

        });

    }
    @Override       //등록 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_clubactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 등록 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn2:
                onBackPressed();
                // playBtn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
