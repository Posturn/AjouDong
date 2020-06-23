package com.example.ajoudongfe;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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

public class QAActivity extends AppCompatActivity {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private Retrofit retrofit;

    private RetroService retroService;
    private String UserDeviceToken = null;
    private UserObject userobject;
    private DeviceObject deviceobject;

    private ImageView profile;
    private TextView username;
    private EditText question;
    private CheckBox anonymous;
    private Button upload;

    private RecyclerView qnaRecyclerView;
    private QnAExpandableAdapter qnaExpandableAdapter;
    private List<QnAObject> listData = new ArrayList<>();

    private int parameterclubID;

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

        profile=findViewById(R.id.imageView);
        username=findViewById(R.id.textView21);
        question=findViewById(R.id.editText);
        anonymous=findViewById(R.id.checkBoxQnaMain);
        upload=findViewById(R.id.button3);

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

        qnaExpandableAdapter = new QnAExpandableAdapter(this, listData, parameterclubID);
        //qnaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        qnaRecyclerView.setAdapter(qnaExpandableAdapter);

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

    protected void getClubQnA(int clubID){
        Call<QnAObject> call =retroService.getQnA(clubID);
        call.enqueue(new Callback<QnAObject>() {
            @Override
            public void onResponse(Call<QnAObject> call, Response<QnAObject> response) {
                
            }

            @Override
            public void onFailure(Call<QnAObject> call, Throwable t) {

            }
        });
    }




    // [END retrieve_current_token]

}
