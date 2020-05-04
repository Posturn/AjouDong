package com.example.ajoudongfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserMainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ConstraintLayout majorclub = (ConstraintLayout)findViewById(R.id.majorclubLayout);
        ConstraintLayout mainclub = (ConstraintLayout)findViewById(R.id.mainclubLayout);
        ConstraintLayout newclub = (ConstraintLayout)findViewById(R.id.newclubLayout);

        ImageButton eventButton = (ImageButton)findViewById(R.id.usereventlist);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_main);
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

        majorclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMajorClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mainclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserMainClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        newclub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), UserNewClubListActivity.class);
                startActivity(intent);
                return false;
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), UserNewClubListActivity.class);
                startActivity(intent);
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
