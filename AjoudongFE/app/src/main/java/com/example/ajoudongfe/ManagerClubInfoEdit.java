package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;


public class ManagerClubInfoEdit extends AppCompatActivity {

    //EditText et1 = (EditText) findViewById(R.id.editText);
    //EditText et2 = (EditText) findViewById(R.id.editText2);
    //EditText et3 = (EditText) findViewById(R.id.editText3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_club_info_edit);

        findViewById(R.id.camera_btn).bringToFront();

        ActionBar actionBar = getSupportActionBar();    //뒤로가기 버튼 생성
        actionBar.setDisplayHomeAsUpEnabled(true);
       // et1.setMovementMethod(new ScrollingMovementMethod());   //edit text에 스크롤 추가

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //키보드 UI 가림 방지

    }

    @Override       //저장 버튼 생성
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manager_clubinfomenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //뒤로가기, 저장 버튼 기능 구현
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_btn1:
                onBackPressed();
               // playBtn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
