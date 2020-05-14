package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import static java.sql.Types.NULL;


public class ManagerClubInfoEdit extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_info_edit);

        GridView gridView = findViewById(R.id.activity_grid);
        ClubActivityAdapter adapter = new ClubActivityAdapter();

        findViewById(R.id.camera_btn).bringToFront();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.ac_add_btn), ""));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid1), "활동1"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid2), "활동2"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid3), "활동3"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid4), "활동4"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid5), "활동5"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid6), "활동6"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.grid7), "활동7"));
        gridView.setNestedScrollingEnabled(true);
        gridView.setAdapter(adapter);

       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), ManagerClubActivityEdit.class);
                        Toast.makeText(getApplicationContext(), "활동 내용 추가", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }

        });

}

    @Override       //저장 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_clubinfomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 저장 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn1:
                onBackPressed();
               // playBtn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
