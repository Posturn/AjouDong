package com.example.ajoudongfe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

public class UserMajorClubListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_major_club_list);

        final GridView gridView = findViewById(R.id.gridView01);
        ;
        final MajorImageAdapter adapter = new MajorImageAdapter();

        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_0), "동아리1"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_1), "동아리2"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_2), "동아리3"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_3), "동아리4"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_4), "동아리5"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_5), "동아리6"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_6), "동아리7"));
        adapter.addItem(new ClubGridListTest(ContextCompat.getDrawable(this, R.drawable.sample_7), "동아리8"));







        final String ajoublue ="#005BAC";
        final String gray ="#707070";

        final Button ButtonAll = (Button) findViewById(R.id.cateAll);
        final Button ButtonSports = (Button) findViewById(R.id.cateSports);
        final Button ButtonReligion = (Button) findViewById(R.id.cateReligion);
        final Button ButtonSocial = (Button) findViewById(R.id.cateSocial);
        final Button ButtonCreate = (Button) findViewById(R.id.cateCreate);
        final Button ButtonStudy = (Button) findViewById(R.id.cateStudy);
        final Button ButtonPE = (Button) findViewById(R.id.catePE);
        final Button ButtonArt = (Button) findViewById(R.id.cateArt);
        final Button ButtonSub = (Button) findViewById(R.id.cateSub);

        ButtonAll.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View view) {
                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(ajoublue));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));


                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonSports.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(ajoublue));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonReligion.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(ajoublue));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonSocial.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(ajoublue));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonCreate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(ajoublue));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonStudy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(ajoublue));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonPE.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(ajoublue));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonArt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(ajoublue));
                ButtonSub.setTextColor(Color.parseColor(gray));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_click_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_unclick_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        ButtonSub.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSub.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothicbold.ttf"));

                ButtonAll.setTextColor(Color.parseColor(gray));
                ButtonSports.setTextColor(Color.parseColor(gray));
                ButtonReligion.setTextColor(Color.parseColor(gray));
                ButtonSocial.setTextColor(Color.parseColor(gray));
                ButtonCreate.setTextColor(Color.parseColor(gray));
                ButtonStudy.setTextColor(Color.parseColor(gray));
                ButtonPE.setTextColor(Color.parseColor(gray));
                ButtonArt.setTextColor(Color.parseColor(gray));
                ButtonSub.setTextColor(Color.parseColor(ajoublue));

                ButtonAll.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSports.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonReligion.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonSocial.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonCreate.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonStudy.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonPE.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));
                ButtonArt.setTypeface(Typeface.createFromAsset(getAssets(), "nanumbarungothic.ttf"));

                ButtonAll.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSports.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonReligion.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSocial.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonCreate.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonStudy.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonPE.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonArt.setBackgroundResource(R.drawable.grid_category_unclick_shape);
                ButtonSub.setBackgroundResource(R.drawable.grid_category_click_shape);

                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    };
}


