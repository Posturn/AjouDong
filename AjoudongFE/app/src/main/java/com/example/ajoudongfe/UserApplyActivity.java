package com.example.ajoudongfe;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApplyActivity extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;
    private RetroService retroService;

    private int schoolID;
    private int parameterclubID;
    private int clubCategory;
    private Button apply_btn;

    private UserObject user_info;
    private QuestionObject question;

    private TextView username;
    private TextView usernumber;
    private TextView usermajor;
    private TextView userphone;
    private TextView userclub;
    private RadioButton usergenderM;
    private RadioButton usergenderF;
    private EditText userapplycontent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_apply);

        clubCategory = getIntent().getIntExtra("clubCategory", 0);
        parameterclubID = getIntent().getIntExtra("clubID", 0);
        schoolID = getIntent().getIntExtra("userSchoolID", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.applytoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        apply_btn = (Button) findViewById(R.id.club_apply_btn);
        apply_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyObject userApplication = new ApplyObject();
                userApplication.setuSchoolID_id(user_info.getuSchoolID());
                userApplication.setClubID_id(parameterclubID);
                userApplication.setAdditionalApplyContent(String.valueOf(userapplycontent.getText()));

                Call<ResponseObject> call = retroService.clubApply(userApplication);
                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        if(data.getResponse() == 1) Toast.makeText(getApplicationContext(), "지원 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onFailure(Call<ResponseObject> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "지원 오류", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }) ;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        username = (TextView) findViewById(R.id.usernamecontent);
        usernumber = (TextView) findViewById(R.id.usernumbercontent);
        usermajor = (TextView) findViewById(R.id.usermajorcontent);
        userphone = (TextView) findViewById(R.id.userphonecontent);
        userclub = (TextView) findViewById(R.id.userclub);

        usergenderM = (RadioButton) findViewById(R.id.genderM);
        usergenderF = (RadioButton) findViewById(R.id.genderF);

        userapplycontent = (EditText) findViewById(R.id.userclubanswer);

        Call<UserObject> call = retroService.getUserInformation(schoolID);
        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {

                user_info = response.body();
                username.setText(user_info.getuName());
                usernumber.setText(String.valueOf(user_info.getuSchoolID()));
                usermajor.setText(user_info.getuMajor());
                userphone.setText(String.valueOf("010-"+String.valueOf(user_info.getuPhoneNumber()/10000)+"-"+String.valueOf(user_info.getuPhoneNumber()%10000)));
                if(user_info.isuJender()){ // M
                    usergenderM.setChecked(true);
                } else{
                    usergenderF.setChecked(true);
                }
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable throwable) {
                Toast.makeText(UserApplyActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // 동아리 요청 질문
        Call<QuestionObject> clubcall = retroService.getClubQuestion(parameterclubID);
        clubcall.enqueue(new Callback<QuestionObject>() {
            @Override
            public void onResponse(Call<QuestionObject> call, Response<QuestionObject> response) {
                question = response.body();
                if(question.getadditionalApply() == null){
                    userclub.setVisibility(View.GONE);
                    userapplycontent.setVisibility(View.GONE);
                    return ;
                }
                userclub.setText(question.getadditionalApply());
            }

            @Override
            public void onFailure(Call<QuestionObject> call, Throwable t) {
                Toast.makeText(UserApplyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

       apply_btn.setBackgroundResource(R.drawable.bottom_button_round);
        if(clubCategory == 1){
            toolbar.setBackgroundColor(getColor(R.color.ajouLogoOrange));
            apply_btn.setBackgroundResource(R.drawable.bottom_button_round_orange);
        }
        else if(clubCategory >= 2){
            toolbar.setBackgroundColor(getColor(R.color.ajouLogoSky));
            apply_btn.setBackgroundResource(R.drawable.bottom_button_round_sky);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
