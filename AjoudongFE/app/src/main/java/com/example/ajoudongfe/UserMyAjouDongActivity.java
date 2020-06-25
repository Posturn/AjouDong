package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserMyAjouDongActivity extends AppCompatActivity {

    private static String BASE_URL= Keys.getServerUrl();
    private ArrayAdapter<CharSequence> collegeAdapter;
    private ArrayAdapter<CharSequence> majorAdapter;
    private RetroService retroService;

    final  String TAG = getClass().getSimpleName();

    private int uSchoolID;

    private EditText userphone;
    public MyAjoudongClubGridAdapter adapter;
    private GridView mGridView;

    private Spinner collegeSpinner;
    private Spinner majorSpinner;

    private Toolbar toolbar;

    private String uCollege;
    private String uMajor;
    private int count = 0;

    private List<Integer> clubs = new ArrayList<>();
    private List<ClubObject> clubObjects = new ArrayList<>();

    private void populateGridView(List<ClubObject> clubObjectList, List<Integer> nRecruit, int uScoolID) {        //그리드 생성 함수
        mGridView = findViewById(R.id.myajoudonggrid);
        adapter = new MyAjoudongClubGridAdapter(this, clubObjectList, nRecruit, uScoolID);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_ajou_dong);

        final TextView userID = (TextView) findViewById(R.id.useridcontent);        //컴포넌트들 선언
        final EditText userPW = (EditText) findViewById(R.id.userpwcontent);
        final EditText userNum = (EditText) findViewById(R.id.usernumbercontent);
        final EditText userName = (EditText) findViewById(R.id.usernamecontent);
        final RadioButton genderM = (RadioButton) findViewById(R.id.genderM);
        final RadioButton genderF = (RadioButton) findViewById(R.id.genderF);

        userphone = (EditText) findViewById(R.id.edit_phone);
        userphone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        collegeSpinner = (Spinner)findViewById(R.id.collegeSpinner);
        majorSpinner = (Spinner)findViewById(R.id.majorSpinner);

        toolbar = (Toolbar) findViewById(R.id.ajoudongtoolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        final int uSchoolID = getIntent().getIntExtra("uSchoolID", 0);    //학번 받아오기 및 유저 아이디 세팅
        setuSchoolID(uSchoolID);

        initMyAPI(BASE_URL);

        //Spinner 기본설정
        collegeAdapter = ArrayAdapter.createFromResource(this, R.array.단과대학교, android.R.layout.simple_spinner_item);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpinner.setAdapter(collegeAdapter);

        Log.d(TAG,"GET");       //처음 사용자 정보 불러오기
        Call<UserObject> getCall = retroService.getUserInformation(uSchoolID);
        getCall.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                if( response.isSuccessful()){
                    UserObject item  = response.body();
                    userID.setText(item.getuID());
                    userPW.setText(item.getuPW());
                    userName.setText(item.getuName());
                    userNum.setText(item.getuSchoolID().toString());
                    userphone.setText("010"+item.getuPhoneNumber().toString());
                    uCollege = item.getuCollege();
                    uMajor = item.getuMajor();
                    int collegePosition = collegeAdapter.getPosition(uCollege);     //spinner 단과대학 설정
                    collegeSpinner.setSelection(collegePosition);
                    if(item.isuJender() == false){
                        genderM.setChecked(true);
                    }
                    else{
                        genderF.setChecked(true);
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

        userNum.setClickable(false);        //학번 수정 불가
        userNum.setFocusable(false);

        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!(collegeSpinner.getItemAtPosition(i).equals("--단과대학교선택--")))
                {
                    int resid = getResources().getIdentifier(collegeSpinner.getItemAtPosition(i).toString(), "array", getPackageName());
                    uCollege = collegeSpinner.getItemAtPosition(i).toString();
                    majorAdapter = ArrayAdapter.createFromResource(getApplicationContext(), resid, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                    int majorPosition = majorAdapter.getPosition(uMajor);       //spinner 학과 설정
                    majorSpinner.setSelection(majorPosition);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 하지않음
            }
        });

        majorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                uMajor = majorSpinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 하지않음
            }
        });

        Call<List<ClubMemberGridObject>> call = retroService.getClubMember(uSchoolID);       //grid 생성
        call.enqueue(new Callback<List<ClubMemberGridObject>>() {
            @Override
            public void onResponse(Call<List<ClubMemberGridObject>> call, Response<List<ClubMemberGridObject>> response) {
                List<ClubMemberGridObject> item;
                item = response.body();
                for(int i = 0; i < item.size(); i++){       //속한 클럽 아이디들 리스트에 받아오기
                    clubs.add(item.get(i).getClubID());
                }
                for(int j = 0; j < clubs.size(); j++){
                    Call<ClubObject> call2 = retroService.getClubGrid(clubs.get(j));       //grid 생성
                    call2.enqueue(new Callback<ClubObject>() {
                        @Override
                        public void onResponse(Call<ClubObject> call2, Response<ClubObject> response2) {
                            clubObjects.add(response2.body());      //리스트에 들어간 클럽들 객체로 불러오기
                            if(clubs.size() == clubObjects.size()){
                                if(clubObjects.get(clubs.size()-1) != null){
                                    populateGridView(clubObjects, null, getuSchoolID());
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<ClubObject> call2, Throwable t) {
                            Log.d(TAG,"Fail msg : " + t.getMessage());
                        }
                    });
                }

            }
            @Override
            public void onFailure(Call<List<ClubMemberGridObject>> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });

    }

    protected void onResume(){
        super.onResume();

    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    private void patchProfile(int uSchoolID) {      //글 업데이트 기능 구현
        final EditText userPW = (EditText) findViewById(R.id.userpwcontent);
        //final EditText userNum = (EditText) findViewById(R.id.usernumbercontent);
        final EditText userName = (EditText) findViewById(R.id.usernamecontent);
        final RadioButton genderM = (RadioButton) findViewById(R.id.genderM);
        String phoneNum = String.valueOf(userphone.getText());

        phoneNum = phoneNum.replace("010", "");     //전화번호 디비형식으로 변경해주기
        phoneNum = phoneNum.replace("-","");
        Log.d(TAG, "PATCH");
        UserObject item2 = new UserObject();
        item2.setuPW(String.valueOf(userPW.getText()));
        item2.setuName(String.valueOf(userName.getText()));
        //item2.setuSchoolID(Integer.parseInt(String.valueOf(userNum.getText())));
        item2.setuPhoneNumber(Integer.parseInt(phoneNum));
        item2.setuCollege(uCollege);
        item2.setuMajor(uMajor);
        if(genderM.isChecked() == true){
            item2.setuJender(false);
        }
        else{
            item2.setuJender(true);
        }
        Call<UserObject> patchCall = retroService.patchUserInformation(uSchoolID, item2);
        patchCall.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "patch 성공");
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
                Log.d(TAG, "Fail msg : " + t.getMessage());
            }
        });
    }

    @Override       //저장 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_club_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 저장 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn1:      //저장하기 버튼
                patchProfile(uSchoolID);
                onBackPressed();
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            adapter.onActivityResult(requestCode, data);
            finish();
            startActivity(getIntent());
            /*int id = data.getIntExtra("clubID",0);
            Log.d(TAG,"DELETE");
            Log.d(TAG, "scholl: "+getuSchoolID());
            Call<ResponseObject> deleteCall = retroService.deleteClubMember(id, getuSchoolID());
            deleteCall.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "삭제 완료");
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Status Code : " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Log.d(TAG, "Fail msg : " + t.getMessage());
                }

            });*/
            adapter.notifyDataSetChanged();
        }
    }

}
