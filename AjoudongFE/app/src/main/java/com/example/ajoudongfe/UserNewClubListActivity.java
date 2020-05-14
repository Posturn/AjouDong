package com.example.ajoudongfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserNewClubListActivity extends AppCompatActivity {

    private DrawerLayout drawerlayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new_club_list);


        final GridView gridView = findViewById(R.id.gridView01);

        final MajorImageAdapter adapter = new MajorImageAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ClubInfomation.class);
                startActivity(intent);
            }
        });

        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_0), "동아리1"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_1), "동아리2"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_2), "동아리3"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_3), "동아리4"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_4), "동아리5"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_5), "동아리6"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_6), "동아리7"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_7), "동아리8"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.newclubtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_new_club_list);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_new_club_list);
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

        final String ajoublue ="#005BAC";
        final String ajouorange="#F5A21E";
        final String gray ="#707070";

        final Button ButtonNewAll = (Button) findViewById(R.id.cateNewAll);
        final Button ButtonNewNew = (Button) findViewById(R.id.cateNewNew);
        final Button ButtonNewStartup = (Button) findViewById(R.id.cateNewStartup);


        ButtonNewAll.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View view) {
                ButtonNewAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonNewAll.setTextColor(Color.parseColor(ajouorange));
                ButtonNewNew.setTextColor(Color.parseColor(gray));
                ButtonNewStartup.setTextColor(Color.parseColor(gray));

                ButtonNewNew.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonNewStartup.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonNewAll.setBackgroundResource(R.drawable.grid_new_category_click_shape);
                ButtonNewNew.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonNewStartup.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonNewNew.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonNewNew.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonNewAll.setTextColor(Color.parseColor(gray));
                ButtonNewNew.setTextColor(Color.parseColor(ajouorange));
                ButtonNewStartup.setTextColor(Color.parseColor(gray));

                ButtonNewAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonNewStartup.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonNewAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonNewNew.setBackgroundResource(R.drawable.grid_new_category_click_shape);
                ButtonNewStartup.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonNewStartup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonNewStartup.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonNewAll.setTextColor(Color.parseColor(gray));
                ButtonNewNew.setTextColor(Color.parseColor(gray));
                ButtonNewStartup.setTextColor(Color.parseColor(ajouorange));


                ButtonNewAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonNewNew.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonNewAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonNewNew.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonNewStartup.setBackgroundResource(R.drawable.grid_new_category_click_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonNewAll.performClick();
    };
    //툴바 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.toolbarSearch:{
                return true;
            }
            case R.id.toolbarFilter:{
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}


