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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserMajorClubListActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerlayout;
    private Context context = this;

    public static String BASE_URL= "http://10.0.2.2:8000";
    Retrofit retrofit;

    public MajorImageAdapter adapter;
    private GridView mGridView;

    private void populateGridView(List<ClubModel> clubModelList) {
        mGridView = findViewById(R.id.gridView01);
        adapter = new MajorImageAdapter(this,clubModelList);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    String ajoublue ="#91C0EB";
    String gray ="#707070";
    String selectedCategory;

    private Button[] majorButton=new Button[41];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_major_club_list);

        int collegeNum=getIntent().getIntExtra("college", 1);

        //----------전체----------
        majorButton[0]= (Button) findViewById(R.id.cate1);

        //----------공과대학----------
        majorButton[1]= (Button) findViewById(R.id.cate2);
        majorButton[2]= (Button) findViewById(R.id.cate3);
        majorButton[3]= (Button) findViewById(R.id.cate4);
        majorButton[4]= (Button) findViewById(R.id.cate5);
        majorButton[5]= (Button) findViewById(R.id.cate6);
        majorButton[6]= (Button) findViewById(R.id.cate7);
        majorButton[7]= (Button) findViewById(R.id.cate8);
        majorButton[8]= (Button) findViewById(R.id.cate9);
        majorButton[9]= (Button) findViewById(R.id.cate10);

        //----------정보통신대학----------
        majorButton[10]=(Button) findViewById(R.id.cate11);
        majorButton[11]=(Button) findViewById(R.id.cate12);
        majorButton[12]= (Button) findViewById(R.id.cate13);
        majorButton[13]= (Button) findViewById(R.id.cate14);
        majorButton[14]= (Button) findViewById(R.id.cate15);

        //----------자연과학대학----------
        majorButton[15]= (Button) findViewById(R.id.cate16);
        majorButton[16]= (Button) findViewById(R.id.cate17);
        majorButton[17]= (Button) findViewById(R.id.cate18);
        majorButton[18]= (Button) findViewById(R.id.cate19);

        //----------경영대학----------
        majorButton[19]= (Button) findViewById(R.id.cate20);
        majorButton[20]= (Button) findViewById(R.id.cate21);
        majorButton[21]= (Button) findViewById(R.id.cate22);
        majorButton[22]= (Button) findViewById(R.id.cate23);

        //----------인문대학----------
        majorButton[23]= (Button) findViewById(R.id.cate24);
        majorButton[24]= (Button) findViewById(R.id.cate25);
        majorButton[25]= (Button) findViewById(R.id.cate26);
        majorButton[26]= (Button) findViewById(R.id.cate27);
        majorButton[27]= (Button) findViewById(R.id.cate28);

        //----------사회과학대학----------
        majorButton[28]= (Button) findViewById(R.id.cate29);
        majorButton[29]= (Button) findViewById(R.id.cate30);
        majorButton[30]= (Button) findViewById(R.id.cate31);
        majorButton[31]= (Button) findViewById(R.id.cate32);
        majorButton[32]= (Button) findViewById(R.id.cate33);
        majorButton[33]= (Button) findViewById(R.id.cate34);

        //----------의학대학----------
        majorButton[34]= (Button) findViewById(R.id.cate35);

        //----------간호대학----------
        majorButton[35]= (Button) findViewById(R.id.cate36);

        //----------약학대학----------
        majorButton[36]= (Button) findViewById(R.id.cate37);

        //----------국제학부---------
        majorButton[37]= (Button) findViewById(R.id.cate38);
        majorButton[38]= (Button) findViewById(R.id.cate39);
        majorButton[39]= (Button) findViewById(R.id.cate40);
        majorButton[40]= (Button) findViewById(R.id.cate41);

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
                Toast.makeText(UserMajorClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.majorclubtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_major_club_list);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_major_club_list);
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

        if(collegeNum==1){
            makeButtonInvisible();
            makeButtonVisible(0, 0);
        }
        else if(collegeNum==2){
            makeButtonInvisible();
            makeButtonVisible(1, 9);
        }
        else if(collegeNum==3){
            makeButtonInvisible();
            makeButtonVisible(10, 14);
        }
        else if(collegeNum==4){
            makeButtonInvisible();
            makeButtonVisible(15, 18);
        }
        else if(collegeNum==5){
            makeButtonInvisible();
            makeButtonVisible(19, 22);
        }
        else if(collegeNum==6){
            makeButtonInvisible();
            makeButtonVisible(23, 27);
        }
        else if(collegeNum==7){
            makeButtonInvisible();
            makeButtonVisible(28, 33);
        }
        else if(collegeNum==8){
            makeButtonInvisible();
            makeButtonVisible(34, 34);
        }
        else if(collegeNum==9){
            makeButtonInvisible();
            makeButtonVisible(35, 35);
        }
        else if(collegeNum==10){
            makeButtonInvisible();
            makeButtonVisible(36, 36);
        }
        else if(collegeNum==11){
            makeButtonInvisible();
        }
        else if(collegeNum==12){
            makeButtonInvisible();
            makeButtonVisible(37, 40);
        }

        for(int i = 0 ; i < 40 ; i++) {
            majorButton[i].setOnClickListener(this);

        }
        majorButton[0].performClick();
    };

    @Override
    public void onClick(View v)
    {
        Button newButton = (Button) v;

        for(Button tempButton : majorButton)
        {
            if(tempButton == newButton)
            {
                categoryUnclicked();
                tempButton.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));
                tempButton.setTextColor(Color.parseColor(ajoublue));
                tempButton.setBackgroundResource(R.drawable.grid_major_category_click_shape);
                selectedCategory= (String) tempButton.getText();
                Toast.makeText(this, tempButton.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void categoryUnclicked() {

        for (int i = 0; i < 41; i++) {
            majorButton[i].setTextColor(Color.parseColor(gray));
            majorButton[i].setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
            majorButton[i].setBackgroundResource(R.drawable.grid_category_unclick_shape);
        }
    }

    public void makeButtonInvisible(){
        for (int i = 1; i < 41; i++) {
            majorButton[i].setVisibility(View.GONE);
        }
    }

    public void makeButtonVisible(int s, int f){
        for (int i = s; i <= f; i++) {
            majorButton[i].setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(getApplicationContext(), UserMajorClubFilterActivity.class);
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
