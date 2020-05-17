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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserNewClubListActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout drawerlayout;
    private Context context = this;

    public static String BASE_URL= "http://10.0.2.2:8000";
    Retrofit retrofit;

    public List<ClubModel> clubModels;
    public MajorImageAdapter adapter;
    private GridView mGridView;

    private void populateGridView(List<ClubModel> clubModelList) {
        mGridView = findViewById(R.id.gridView01);
        adapter = new MajorImageAdapter(this,clubModelList);
        mGridView.setAdapter(adapter);
    }

    final String ajouorange="#F5A21E";
    final String gray ="#707070";
    String selectedCategory;

    private Button[] newfoundButton=new Button[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new_club_list);

        newfoundButton[0] = (Button) findViewById(R.id.cateNewAll);
        newfoundButton[1] = (Button) findViewById(R.id.cateNewNew);
        newfoundButton[2] = (Button) findViewById(R.id.cateNewStartup);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroService retroService = retrofit.create(RetroService.class);

        Call<List<ClubModel>> call = retroService.getClubGridAll();
        call.enqueue(new Callback<List<ClubModel>>() {

            @Override
            public void onResponse(Call<List<ClubModel>> call, Response<List<ClubModel>> response) {
                populateGridView(response.body());
            }

            @Override
            public void onFailure(Call<List<ClubModel>> call, Throwable throwable) {
                Toast.makeText(UserNewClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.newclubtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
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

        for(int i = 0 ; i < 3 ; i++) {
            newfoundButton[i].setOnClickListener(this);
        }
        newfoundButton[0].performClick();
    };

    @Override
    public void onClick(View v) {
        Button newButton = (Button) v;

        for(Button tempButton : newfoundButton)
        {
            if(tempButton == newButton)
            {
                categoryUnclicked();
                tempButton.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));
                tempButton.setTextColor(Color.parseColor(ajouorange));
                tempButton.setBackgroundResource(R.drawable.grid_new_category_click_shape);
                selectedCategory= (String) tempButton.getText();
                Toast.makeText(this, tempButton.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void categoryUnclicked(){
        for (int i = 0; i < 3; i++) {
            newfoundButton[i].setTextColor(Color.parseColor(gray));
            newfoundButton[i].setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
            newfoundButton[i].setBackgroundResource(R.drawable.grid_category_unclick_shape);
        }
    }



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
                Intent intent = new Intent(getApplicationContext(), UserNewClubFilterActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_mainmenu, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.toolbarSearch).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("동아리명을 입력하세요.");
        searchView.setBackgroundColor(255);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//검색 완료시
                Toast.makeText(getApplicationContext(),"검색완료",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //검색어 입력시
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}


