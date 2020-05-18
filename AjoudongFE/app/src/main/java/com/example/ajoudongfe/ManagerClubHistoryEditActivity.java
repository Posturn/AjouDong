package com.example.ajoudongfe;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerClubHistoryEditActivity extends AppCompatActivity {

    final  String TAG = getClass().getSimpleName();

    final String BASE_URL = "http://10.0.2.2:8000/activities/";
    private RetroService retroService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_history_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Button delete_btn = (Button)findViewById(R.id.action_btn2);
        delete_btn.setClickable(true);
        delete_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"DELETE");
                // pk 값은 임의로 변경가능
                Call<ClubActivityObject> deleteCall = retroService.delete_activities_pk(1);
                deleteCall.enqueue(new Callback<ClubActivityObject>() {
                    @Override
                    public void onResponse(Call<ClubActivityObject> call, Response<ClubActivityObject> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG,"삭제 완료");
                        }else {
                            Log.d(TAG,"Status Code : " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ClubActivityObject> call, Throwable t) {
                        Log.d(TAG,"Fail msg : " + t.getMessage());
                    }
                });
                Toast.makeText(getApplicationContext(), "활동 내역이 삭제되었습니다!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });


        GridView gridView = findViewById(R.id.grid_ac_image);
        ClubHistoryImageAdapter adapter = new ClubHistoryImageAdapter();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.ic_add), ""));
        adapter.addItem(new GridListObject(ContextCompat.getDrawable(this, R.drawable.grid1), ""));
        gridView.setNestedScrollingEnabled(true);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();

            }

        });

    }
    @Override       //등록 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_club_history_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 등록 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn2:
                onBackPressed();
                // playBtn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
