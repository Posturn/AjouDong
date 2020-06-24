package com.example.ajoudongfe;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QAActivity extends AppCompatActivity implements View.OnClickListener{

    public static String BASE_URL= Keys.getServerUrl();
    private Retrofit retrofit;

    private RetroService retroService;
    private String UserDeviceToken = null;
    private UserObject userobject;
    private QnAObject qnaobject;
    private List<QnAObject> qnaobjectlist = new ArrayList<>();
    private List<CommentObject> commentlist = new ArrayList<>();
    private boolean anony;

    private ImageView profile;
    private TextView username;
    private EditText question;
    private CheckBox anonymous;
    private Button upload;

    private int qnanum;
    private int maxqnanum;

    private RecyclerView qnaRecyclerView;
    private QnAExpandableAdapter qnaExpandableAdapter;

    private int parameterclubID;
    InputMethodManager imm;

    public void populateRecyclerView(List<QnAObject> qnaobject){
        qnaRecyclerView=findViewById(R.id.QnARecylerView);
        qnaExpandableAdapter = new QnAExpandableAdapter(this, qnaobject, parameterclubID);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        qnaRecyclerView.setLayoutManager(manager);
        qnaRecyclerView.setAdapter(qnaExpandableAdapter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        Toolbar toolbar = (Toolbar) findViewById(R.id.userbookmarktoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayShowTitleEnabled(false);

        parameterclubID = getIntent().getIntExtra("clubID", 0);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        qnaRecyclerView=findViewById(R.id.QnARecylerView);
        profile=findViewById(R.id.imageView);
        username=findViewById(R.id.textView21);
        question=findViewById(R.id.editText);
        anonymous=findViewById(R.id.checkBoxQnaMain);
        upload=findViewById(R.id.button3);
        upload.setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        UserDeviceToken = task.getResult().getToken();
                        Log.d("TAG", UserDeviceToken);
                        getUserFromDevice(UserDeviceToken);

                    }
                });
        getFaq(parameterclubID);
        //Log.v("QnA객체", String.valueOf(qnaobject));
        qnaExpandableAdapter = new QnAExpandableAdapter(this, qnaobjectlist, parameterclubID);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        qnaRecyclerView.setLayoutManager(manager);
        qnaRecyclerView.setAdapter(qnaExpandableAdapter);

        maxqnanum=1;





    }

    protected void getUserFromDevice(String UserDeviceToken){
        Call<UserObject> call = retroService.getUserFromDevice(UserDeviceToken);
        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                userobject=response.body();
                Log.v("디바이스객체", String.valueOf(response.body()));
                Picasso.get().load(userobject.getuIMG()).into(profile);
                username.setText(userobject.getuName());

            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
                //Log.v("디바이스객체", UserDeviceToken);
                Log.v("실패", "실패");
            }
        });
    }

    protected void getFaq(int clubID){
        Call<List<QnAObject>> call =retroService.getqna(clubID);
        call.enqueue(new Callback<List<QnAObject>>() {
            @Override
            public void onResponse(Call<List<QnAObject>> call, Response<List<QnAObject>> response) {
                qnaobjectlist=null;
                qnaobjectlist=response.body();
                Log.v("QnA객체", String.valueOf(response.body()));

                        populateRecyclerView(qnaobjectlist);

            }

            @Override
            public void onFailure(Call<List<QnAObject>> call, Throwable t) {
                Log.v("실패", "실패");
            }
        });
    }

    private void postFAQ(int userID, int FAQID, String FAQDate, boolean isAnonymous, String FAQContent, int clubID) {
        //Log.v("clubID", String.valueOf(parameterclubID));
        final QnAObject qnaobject = new QnAObject(userID, FAQID, FAQDate, isAnonymous, FAQContent, clubID);

        //qnaobject(userobject.getuName(), maxqnanum+1, datestr, anony, question.getText().toString(), parameterclubID);

        Call<QnAObject> call = retroService.postfaq(qnaobject);
        call.enqueue(new Callback<QnAObject>() {
            @Override
            public void onResponse(Call<QnAObject> call, Response<QnAObject> response) {


            }

            @Override
            public void onFailure(Call<QnAObject> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button3 :


                if(anonymous.isChecked()) {    //체크 박스가 체크 된 경우
                    anony=true;
                }
                else{   //체크 박스가 해제 된 경우
                    anony=false;
                }

                postFAQ(userobject.getuSchoolID(), 0, "날짜", anony, question.getText().toString(), parameterclubID);
                getFaq(parameterclubID);

                imm.hideSoftInputFromWindow(question.getWindowToken(), 0);
                populateRecyclerView(qnaobjectlist);
                qnaExpandableAdapter.notifyDataSetChanged();

                break;

        }
    }


    // [END retrieve_current_token]

}
