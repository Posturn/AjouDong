package com.example.ajoudongfe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class UserMajorSelectActivity extends AppCompatActivity {
    private DrawerLayout drawerlayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_major_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.majorselecttoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_major_select_xml);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_major_select);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerlayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.user_info_edit){
                    Intent intent = new Intent(getApplicationContext(), UserMyAjouDongActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_apply_result){
                    Intent intent = new Intent(getApplicationContext(), UserApplicationResultActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_bookmarked_list){
                    Intent intent = new Intent(getApplicationContext(), UserBookmarkClubActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.user_new_club_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.user_apply_state_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.user_new_event_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.user_logout){
                    Toast.makeText(context, "로그아웃중", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
        LinearLayout all = (LinearLayout) findViewById(R.id.majorAll);
        LinearLayout engin = (LinearLayout) findViewById(R.id.majorEngin);
        LinearLayout com = (LinearLayout)findViewById(R.id.majorCom);

        LinearLayout science = (LinearLayout)findViewById(R.id.majorScience);
        LinearLayout bus = (LinearLayout)findViewById(R.id.majorBus);
        LinearLayout human = (LinearLayout)findViewById(R.id.majorHuman);

        LinearLayout social = (LinearLayout)findViewById(R.id.majorSocial);
        LinearLayout medic = (LinearLayout)findViewById(R.id.majorMedic);
        LinearLayout nurse = (LinearLayout)findViewById(R.id.majorNurse);

        LinearLayout phar = (LinearLayout)findViewById(R.id.majorPhar);
        LinearLayout dasan = (LinearLayout)findViewById(R.id.majorDasan);
        LinearLayout national = (LinearLayout)findViewById(R.id.majorNational);

        all.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 1);
                startActivity(intent);
                return false;
            }
        });

        engin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 2);
                startActivity(intent);
                return false;
            }
        });

        com.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 3);
                startActivity(intent);
                return false;
            }
        });

        science.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 4);
                startActivity(intent);
                return false;
            }
        });

        bus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 5);
                startActivity(intent);
                return false;
            }
        });

        human.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 6);
                startActivity(intent);
                return false;
            }
        });

        social.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 7);
                startActivity(intent);
                return false;
            }
        });

        medic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 8);
                startActivity(intent);
                return false;
            }
        });

        nurse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 9);
                startActivity(intent);
                return false;
            }
        });

        phar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 10);
                startActivity(intent);
                return false;
            }
        });

        dasan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 11);
                startActivity(intent);
                return false;
            }
        });

        national.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                intent.putExtra("college", 12);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
