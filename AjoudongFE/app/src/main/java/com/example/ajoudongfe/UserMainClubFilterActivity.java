package com.example.ajoudongfe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class UserMainClubFilterActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView[] filterViews = new TextView[41];
    private ArrayList<String> tags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_club_filter);

        filterViews[0] = (TextView) findViewById(R.id.recruit_y);
        filterViews[1] = (TextView) findViewById(R.id.recruit_n);
        filterViews[2] = (TextView) findViewById(R.id.recruit_interview_n);
        filterViews[3] = (TextView) findViewById(R.id.recruit_interview_y);
        filterViews[4] = (TextView) findViewById(R.id.recruit_limit_n);

        filterViews[5] = (TextView) findViewById(R.id.field_leisure);
        filterViews[6] = (TextView) findViewById(R.id.field_religion);
        filterViews[7] = (TextView) findViewById(R.id.field_social);
        filterViews[8] = (TextView) findViewById(R.id.field_create);
        filterViews[9] = (TextView) findViewById(R.id.field_academic);
        filterViews[10] = (TextView) findViewById(R.id.field_science);
        filterViews[11] = (TextView) findViewById(R.id.field_athletic);
        filterViews[12] = (TextView) findViewById(R.id.field_art);
        filterViews[13] = (TextView) findViewById(R.id.field_semi);

        filterViews[14] = (TextView) findViewById(R.id.atmosphere_show);
        filterViews[15] = (TextView) findViewById(R.id.atmosphere_competition);
        filterViews[16] = (TextView) findViewById(R.id.atmosphere_regular);
        filterViews[17] = (TextView) findViewById(R.id.atmosphere_contest);
        filterViews[18] = (TextView) findViewById(R.id.atmosphere_obyb);
        filterViews[19] = (TextView) findViewById(R.id.atmosphere_afterparty);
        filterViews[20] = (TextView) findViewById(R.id.atmosphere_study);
        filterViews[21] = (TextView) findViewById(R.id.atmosphere_friendship);

        filterViews[22] = (TextView) findViewById(R.id.moeny_51);
        filterViews[23] = (TextView) findViewById(R.id.moeny_12);
        filterViews[24] = (TextView) findViewById(R.id.moeny_23);
        filterViews[25] = (TextView) findViewById(R.id.moeny_3);
        filterViews[26] = (TextView) findViewById(R.id.moeny_1time);
        filterViews[27] = (TextView) findViewById(R.id.moeny_no);

        filterViews[28] = (TextView) findViewById(R.id.day_mon);
        filterViews[29] = (TextView) findViewById(R.id.day_tue);
        filterViews[30] = (TextView) findViewById(R.id.day_wed);
        filterViews[31] = (TextView) findViewById(R.id.day_thu);
        filterViews[32] = (TextView) findViewById(R.id.day_fri);
        filterViews[33] = (TextView) findViewById(R.id.day_sat);
        filterViews[34] = (TextView) findViewById(R.id.day_sun);
        filterViews[35] = (TextView) findViewById(R.id.day_every);

        filterViews[36] = (TextView) findViewById(R.id.time_am);
        filterViews[37] = (TextView) findViewById(R.id.time_pm);
        filterViews[38] = (TextView) findViewById(R.id.time_black);
        filterViews[39] = (TextView) findViewById(R.id.time_night);
        filterViews[40] = (TextView) findViewById(R.id.time_all);

        tags = getIntent().getStringArrayListExtra("TAGLIST");

        for(int i = 0 ; i < 41 ; i++) {
            filterViews[i].setOnClickListener(this);
            if(tags.contains(filterViews[i].getText())){
                filterViews[i].setSelected(true);
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Button filterButton = (Button) findViewById(R.id.mainfilterbtn);
        filterButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("TAGLIST",tags);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
            case R.id.toolbarFilter:
                for(int i = 0 ; i < 41 ; i++) {
                    tags.clear();
                    filterViews[i].setSelected(false);
                }
                return true;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    @Override
    public void onClick(View v) {
        TextView textView = findViewById(v.getId());
        String now_tag = (String)textView.getText();
        if (v.isSelected()) {
            tags.remove(now_tag);
            v.setSelected(false);
        } else {
            tags.add(now_tag);
            v.setSelected(true);
        }
    }
}
