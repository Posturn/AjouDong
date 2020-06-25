package com.example.ajoudongfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    public static String BASE_URL= Keys.getServerUrl();
    private Retrofit retrofit;
    private RetroService retroService;

    private CommentObject commentObject;
    private ImageView profile;
    private TextView username;
    private TextView question;
    private CheckBox anonymous;
    private ImageView upload;
    private EditText commentedit;
    private String UserDeviceToken = null;
    private UserObject userobject;
    private int commenter;

    private List<CommentObject> commentObjectList =new ArrayList<>();

    private String faqusername;
    private String faquserimg;
    private String faqquestion;
    private int parameterclubID;
    private int FAQID;
    private boolean anony;

    private RecyclerView commentRecyclerView;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;
    InputMethodManager imm;

    public void populateRecyclerView(List<CommentObject> commentobject){
        commentRecyclerView=findViewById(R.id.commentRecylerView);
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(this, commentobject, FAQID);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(manager);
        commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.commenttoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("문의하기");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroService = retrofit.create(RetroService.class);

        profile=findViewById(R.id.commentimageView);
        username=findViewById(R.id.commenttextView);
        question=findViewById(R.id.commenteditText);
        anonymous=findViewById(R.id.commentCheckbox);
        upload=findViewById(R.id.commentButton);
        commentedit=findViewById(R.id.commentwriteText);
        commentRecyclerView=findViewById(R.id.commentRecylerView);


        FAQID=getIntent().getIntExtra("FAQID", 0);
        faquserimg=getIntent().getStringExtra("userIMG");
        faqusername=getIntent().getStringExtra("userName");
        faqquestion=getIntent().getStringExtra("faqquestion");
        parameterclubID=getIntent().getIntExtra("clubID", 0);
        upload.setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //profile.setImageResource(R.drawable.icon);

        username.setText(faqusername);
        if(faquserimg=="default"){
            //profile.setImageResource(R.drawable.icon);
        }
        else{
            Picasso.get().load(faquserimg).into(profile);
        }
        question.setText(faqquestion);

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

        getComment(FAQID);
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(this, commentObjectList, FAQID);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(manager);
        commentRecyclerView.setAdapter(commentRecyclerViewAdapter);

    }

    protected void getComment(int FAQID){
        Call<List<CommentObject>> call =retroService.getcomment(FAQID);
        call.enqueue(new Callback<List<CommentObject>>() {
            @Override
            public void onResponse(Call<List<CommentObject>> call, Response<List<CommentObject>> response) {
                commentObjectList=null;
                commentObjectList=response.body();
                Log.v("QnA객체", String.valueOf(response.body()));

                populateRecyclerView(commentObjectList);

            }

            @Override
            public void onFailure(Call<List<CommentObject>> call, Throwable t) {
                Log.v("실패", "실패");
            }
        });
    }

    protected void getUserFromDevice(String UserDeviceToken){
        Call<UserObject> call = retroService.getUserFromDevice(UserDeviceToken);
        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                userobject=response.body();
                Log.v("디바이스객체", String.valueOf(response.body()));


            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
                //Log.v("디바이스객체", UserDeviceToken);
                Log.v("실패", "실패");
            }
        });
    }

    protected void postComment(int userID, int FAQCommentID, String FAQCommentDate, boolean isAnonymous, String FAQCommentContent, int FAQID, int clubID){
        final CommentObject commentobject = new CommentObject(userID, FAQCommentID, FAQCommentDate, isAnonymous, FAQCommentContent, FAQID, clubID);
        Call<CommentObject> call=retroService.postcomment(commentobject);
        call.enqueue(new Callback<CommentObject>() {
            @Override
            public void onResponse(Call<CommentObject> call, Response<CommentObject> response) {

            }

            @Override
            public void onFailure(Call<CommentObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.commentButton :


                if(anonymous.isChecked()) {    //체크 박스가 체크 된 경우
                    anony=true;
                }
                else{   //체크 박스가 해제 된 경우
                    anony=false;
                }

                postComment(userobject.getuSchoolID(), 0, "날짜", anony, commentedit.getText().toString(), FAQID ,parameterclubID);
                getComment(FAQID);

                imm.hideSoftInputFromWindow(commentedit.getWindowToken(), 0);
                
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                break;

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{//뒤로가기
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
