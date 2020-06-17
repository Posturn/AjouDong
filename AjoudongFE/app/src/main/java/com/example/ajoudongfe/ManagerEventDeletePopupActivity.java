package com.example.ajoudongfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerEventDeletePopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manager_event_delete_popup);
        final int position = getIntent().getIntExtra("position", 1000);

        Button yes_btn = (Button)findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", 0);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();       //팝업 닫기
            }
        });
        Button no_btn  = (Button)findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
               Intent intent = new Intent();
               intent.putExtra("result", 1);
               setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
