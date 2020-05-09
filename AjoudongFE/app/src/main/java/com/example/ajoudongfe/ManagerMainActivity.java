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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ManagerMainActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        ConstraintLayout editclubinfo = (ConstraintLayout)findViewById(R.id.editclubinfoLayout);
        ConstraintLayout membermanagement = (ConstraintLayout)findViewById(R.id.managementclubmemberLayout);
        ConstraintLayout newevent = (ConstraintLayout)findViewById(R.id.neweventLayout);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_manager_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_manager_main);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerlayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.club_apply_setting){
                    Intent intent = new Intent(getApplicationContext(), ManagerClubApplySettingActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.club_filter_setting){
                    Intent intent = new Intent(getApplicationContext(), ManagerClubFilterSettingActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.club_apply_alarm){
                    Toast.makeText(context, "구현필요", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.club_logout){
                    Toast.makeText(context, "로그아웃중", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }

                return true;
            }
        });

        editclubinfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerClubInfoEdit.class);
                startActivity(intent);
                return false;
            }
        });

        membermanagement.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerMemberManagementActivity.class);
                startActivity(intent);
                return false;
            }
        });

        newevent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), ManagerNewEventActivity.class);
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
