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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private Retrofit retrofit;

    private RetroService retroService;

    public ClubGridAdapter adapter;
    private GridView mGridView;

    private SearchView searchView;
    private String search_text = null;
    private boolean search_now = false;
    private int now_spin = 0;
    private int club_num = 13;
    private String selectedCategory = "전체";
    private boolean tag_now = false;
    private int user_ID = 201720988; //테스트용 사용자 아이디

    final  String TAG = getClass().getSimpleName();

    private ArrayList<String> tags = new ArrayList<String>();
    private List<Integer> nRecruitClub = new ArrayList<>();

    private void populateGridView(List<ClubObject> clubObjectList, List<Integer> nRecruit) {
        mGridView = findViewById(R.id.gridView01);
        adapter = new ClubGridAdapter(this, clubObjectList, nRecruit);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String ajoublue ="#91C0EB";
    private String gray ="#707070";

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

        retroService = retrofit.create(RetroService.class);

        Call<List<Integer>> nrecruitcall = retroService.getnRecruitClub();
        nrecruitcall.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                nRecruitClub = response.body();
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable throwable) {
              //  Toast.makeText(UserMajorClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Call<List<ClubObject>> call = retroService.getClubGridAll(club_num, selectedCategory, now_spin);
        CallEnqueueClubObject(call);

        final int uSchoolID = getIntent().getIntExtra("uSchoolID", 0);    //학번 받아오기 및 유저 아이디 세팅
        setUser_ID(uSchoolID);
        Log.d(TAG, "user: " + uSchoolID);

        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout_user_major_club_list);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user_major_club_list);
        final View header = navigationView.getHeaderView(0);
        ImageButton eventButton = (ImageButton)findViewById(R.id.usereventlist);
        final ImageView user_profile = (ImageView)header.findViewById(R.id.user_default_icon);
        final TextView user_name = (TextView)header.findViewById(R.id.user_name);

        Log.d(TAG,"GET");       //처음 사용자 정보 불러오기
        Call<UserObject> getCall = retroService.getUserInformation(uSchoolID);
        getCall.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                if( response.isSuccessful()){
                    UserObject item  = response.body();
                    Log.d(TAG, String.valueOf(user_profile.getId()));
                    user_name.setText(item.getuName());
                    if(item.getuIMG() != null){
                        Picasso.get().load(item.getuIMG()).into(user_profile);
                    }
                    else{
                        user_profile.setImageResource(R.drawable.ajoudong_icon);
                    }
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.majorclubtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


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
                    intent.putExtra("uSchoolID", uSchoolID);
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
                    Toast.makeText(context, "로그아웃 완료", Toast.LENGTH_SHORT).show();
                }

                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                return true;
            }
        });

        if(collegeNum==1){
            club_num = 13;
            makeButtonInvisible();
            makeButtonVisible(0, 0);
        }
        else if(collegeNum==2){
            club_num = 2;
            makeButtonInvisible();
            makeButtonVisible(1, 9);
        }
        else if(collegeNum==3){
            club_num = 3;
            makeButtonInvisible();
            makeButtonVisible(10, 14);
        }
        else if(collegeNum==4){
            club_num = 4;
            makeButtonInvisible();
            makeButtonVisible(15, 18);
        }
        else if(collegeNum==5){
            club_num = 5;
            makeButtonInvisible();
            makeButtonVisible(19, 22);
        }
        else if(collegeNum==6){
            club_num = 6;
            makeButtonInvisible();
            makeButtonVisible(23, 27);
        }
        else if(collegeNum==7){
            club_num = 7;
            makeButtonInvisible();
            makeButtonVisible(28, 33);
        }
        else if(collegeNum==8){
            club_num = 8;
            makeButtonInvisible();
            makeButtonVisible(34, 34);
        }
        else if(collegeNum==9){
            club_num = 9;
            makeButtonInvisible();
            makeButtonVisible(35, 35);
        }
        else if(collegeNum==10){
            club_num = 10;
            makeButtonInvisible();
            makeButtonVisible(36, 36);
        }
        else if(collegeNum==11){
            club_num = 11;
            makeButtonInvisible();
        }
        else if(collegeNum==12){
            club_num = 12;
            makeButtonInvisible();
            makeButtonVisible(37, 40);
        }

        for(int i = 0 ; i < 40 ; i++) {
            majorButton[i].setOnClickListener(this);

        }
        majorButton[0].performClick();

        Spinner spinner = (Spinner)findViewById(R.id.majorClubSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                now_spin = position;
                if(search_text==null || search_text.equals("")){
                    search_now=false;
                }
                //0. 정렬(랜덤) 1. 가나다순(오름차순) 2. 가나다순(내림차순)
                if(search_now == false && tag_now == false){
                    ClubSort();
                }else if(search_now == true && tag_now == false){
                    ClubSearch();
                }else if(search_now == false && tag_now == true){
                    ClubFilter();
                }else{
                    ClubFilterSearch();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ClubSort();
            }
        });
    };

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

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
                Log.d("sssss", selectedCategory);
                ClubSort();
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
                intent.putStringArrayListExtra("TAGLIST",tags);
                startActivityForResult(intent, 1);
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
                if(tag_now == false) ClubSearch();
                else ClubFilterSearch();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK && requestCode == 1){
            tags = data.getStringArrayListExtra("TAGLIST");
            if(tags.size() == 0 || tags.isEmpty()) {
                tag_now = false;
                ClubSort();
            }
            else{
                tag_now = true;
                ClubFilter();
            }
        }else{
            tag_now = false;
        }
    }

    protected void ClubSort(){
        Call<List<ClubObject>> call = retroService.getClubGridAll(club_num, selectedCategory, now_spin);
        CallEnqueueClubObject(call);
    }

    protected void ClubSearch(){
        Call<List<ClubObject>> call = retroService.getClubGridSearch(club_num, selectedCategory, now_spin, search_text);
        CallEnqueueClubObject(call);
    }

    protected void ClubFilter(){
        final ClubFilterObject clubFilterObject = new ClubFilterObject(club_num, now_spin, tags);
        Call<List<ClubObject>> call = retroService.getClubGridFilter(clubFilterObject);
        CallEnqueueClubObject(call);
    }

    protected void ClubFilterSearch(){
        Log.d("test", "구현 필요");
    }

    protected void CallEnqueueClubObject(Call<List<ClubObject>> call){
        call.enqueue(new Callback<List<ClubObject>>() {
            @Override
            public void onResponse(Call<List<ClubObject>> call, Response<List<ClubObject>> response) {
                populateGridView(response.body(), nRecruitClub);
            }

            @Override
            public void onFailure(Call<List<ClubObject>> call, Throwable throwable) {
                Toast.makeText(UserMajorClubListActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
