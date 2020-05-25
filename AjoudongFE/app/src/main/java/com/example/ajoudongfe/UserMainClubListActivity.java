package com.example.ajoudongfe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserMainClubListActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerlayout;
    private Context context = this;

    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;

    private RetroService retroService;

    public ClubGridAdapter adapter;
    private GridView mGridView;

    private SearchView searchView;
    private String search_text = null;
    private boolean search_now = false;
    private int now_spin = 0;
    private int club_num = 0;

    private void populateGridView(List<ClubObject> clubObjectList) {
        mGridView = findViewById(R.id.gridView01);
        adapter = new ClubGridAdapter(this, clubObjectList);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    final String ajoublue ="#005BAC";
    final String gray ="#707070";
    String selectedCategory;

    private Button[] mainButton=new Button[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_club_list);

        mainButton[0] = (Button) findViewById(R.id.cateAll);
        mainButton[1] = (Button) findViewById(R.id.cateSports);
        mainButton[2] = (Button) findViewById(R.id.cateReligion);
        mainButton[3] = (Button) findViewById(R.id.cateSocial);
        mainButton[4] = (Button) findViewById(R.id.cateCreate);
        mainButton[5] = (Button) findViewById(R.id.cateStudy);
        mainButton[6] = (Button) findViewById(R.id.catePE);
        mainButton[7] = (Button) findViewById(R.id.cateArt);
        mainButton[8] = (Button) findViewById(R.id.cateSub);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        Call<List<ClubObject>> call = retroService.getClubGridAll(club_num, now_spin);
        call.enqueue(new Callback<List<ClubObject>>() {

             @Override
             public void onResponse(Call<List<ClubObject>> call, Response<List<ClubObject>> response) {
                 for(ClubObject v : response.body())
                 {
                     Log.v("value", String.valueOf(v.getClubID()));
                 }
                 populateGridView(response.body());
             }

             @Override
             public void onFailure(Call<List<ClubObject>> call, Throwable throwable) {
                 Toast.makeText(UserMainClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
             }
         });

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainclubtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_main_club_list);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_main_club_list);
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

        for(int i = 0 ; i < 9 ; i++) {
            mainButton[i].setOnClickListener(this);
        }
        mainButton[0].performClick();

        Spinner spinner = (Spinner)findViewById(R.id.mainClubSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                now_spin = position;
                if(search_text==null || search_text.equals("")){
                    search_now=false;
                }
                //0. 정렬(랜덤) 1. 가나다순(오름차순) 2. 가나다순(내림차순)
                if(search_now == false){
                    ClubSort(now_spin);
                }else{
                    ClubSearch(search_text, now_spin);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ClubSort(0);
            }
        });


    };

    @Override
    public void onClick(View v) {
        Button newButton = (Button) v;

        for(Button tempButton : mainButton)
        {
            if(tempButton == newButton)
            {
                categoryUnclicked();
                tempButton.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));
                tempButton.setTextColor(Color.parseColor(ajoublue));
                tempButton.setBackgroundResource(R.drawable.grid_category_click_shape);
                selectedCategory= (String) tempButton.getText();
                Toast.makeText(this, tempButton.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void categoryUnclicked(){
        for (int i = 0; i < 9; i++) {
            mainButton[i].setTextColor(Color.parseColor(gray));
            mainButton[i].setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
            mainButton[i].setBackgroundResource(R.drawable.grid_category_unclick_shape);
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
                Intent intent = new Intent(getApplicationContext(), UserMainClubFilterActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_search_menu, menu);
        searchView = (SearchView)menu.findItem(R.id.toolbarSearch).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("동아리명을 입력하세요.");
        searchView.setBackgroundColor(255);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//검색 완료시
                search_text = s;
                search_now = true;
                ClubSearch(search_text, now_spin);
                Toast.makeText(getApplicationContext(),"검색중",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //검색어 입력시
                search_text = null;
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    protected void ClubSort(int spinner_num){
        Log.d("ClubSort","spinner_num: "+spinner_num);
        Call<List<ClubObject>> call = retroService.getClubGridAll(0, spinner_num);
        call.enqueue(new Callback<List<ClubObject>>() {
            @Override
            public void onResponse(Call<List<ClubObject>> call, Response<List<ClubObject>> response) {
                populateGridView(response.body());
            }

            @Override
            public void onFailure(Call<List<ClubObject>> call, Throwable throwable) {
                Toast.makeText(UserMainClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void ClubSearch(String keyword, int spinner_num){
        Log.d("ClubSearch","spinner_num: "+spinner_num+", keyword: "+keyword);
        Call<List<ClubObject>> call = retroService.getClubGridSearch(club_num,spinner_num, keyword);
        call.enqueue(new Callback<List<ClubObject>>() {
            @Override
            public void onResponse(Call<List<ClubObject>> call, Response<List<ClubObject>> response) {
                populateGridView(response.body());
            }

            @Override
            public void onFailure(Call<List<ClubObject>> call, Throwable throwable) {
                Toast.makeText(UserMainClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
