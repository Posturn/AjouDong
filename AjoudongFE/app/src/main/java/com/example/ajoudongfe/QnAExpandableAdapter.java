package com.example.ajoudongfe;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QnAExpandableAdapter extends RecyclerView.Adapter<QnAExpandableAdapter.ItemViewHolder> {
    private static String BASE_URL = Keys.getServerUrl();
    private Context context;
    private List<QnAObject> listData = new ArrayList<>();
    private UserObject userData= new UserObject();
    private Retrofit retrofit;
    private RetroService retroService;
    private int clubID;
    private int userID;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private int faqID;
    private String username;
    private String userimg;
    private String faqquestion;
    private String faqdate;


    public QnAExpandableAdapter(Context context, List<QnAObject> listData, int clubID) {
        this.context = context;
        this.listData = listData;
        this.clubID = clubID;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_header_shape, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 데이터 리스트로부터 아이템 데이터 참조.
                Log.v("FAQID1", String.valueOf(faqID));
                Intent intent = new Intent(context.getApplicationContext(), CommentActivity.class);
                intent.putExtra("FAQID", faqID);
                intent.putExtra("userName", username);
                intent.putExtra("userIMG", userimg);
                intent.putExtra("faqquestion", faqquestion);
                intent.putExtra("faqdate", faqdate);
                intent.putExtra("clubID", clubID);
                context.startActivity(intent);

            }
        });
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QnAExpandableAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    void addItem(QnAObject data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        //Header
        private ImageView userProfile;
        private TextView userName;
        private TextView date;
        private TextView question;
        private CheckBox anonymous;
        private Button upload;
        private QnAObject qnaObject;
        private ImageView spreadbutton;
        private TextView spreadtext;
        private int position;
        //Child
        private ImageView childuserProfile;
        private TextView childuserName;
        private TextView childdate;
        private TextView childquestion;

        private ConstraintLayout qnaHeaderLayout;

        ItemViewHolder(View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.headimageView);
            userName = itemView.findViewById(R.id.headtextView);
            date = itemView.findViewById(R.id.headdateView);
            question = itemView.findViewById(R.id.headeditText);
            anonymous = itemView.findViewById(R.id.checkBoxQnaMain);

            qnaHeaderLayout = (ConstraintLayout)itemView.findViewById(R.id.qnaheader);







        }

        public void onBind(QnAObject qnaObject, int position) {
            this.qnaObject = qnaObject;
            this.position = position;

            faqID=qnaObject.getFAQID();


            Log.v("날짜", String.valueOf(qnaObject.getFAQDate()));
            Log.v("질문", String.valueOf(qnaObject.getFAQContent()));
            getUserInfo(qnaObject.getUserID());

            date.setText(qnaObject.getFAQDate().substring(0, 19));
            question.setText(qnaObject.getFAQContent());
            faqquestion=qnaObject.getFAQContent();
            faqdate=qnaObject.getFAQDate();
            //childdate.setText(qnaObject.getFAQDate());
            //childquestion.setText(qnaObject.getFAQContent());

            getUserInfo(qnaObject.getUserID());





            //changeVisibility(selectedItems.get(position));

        }



        public void getUserInfo(int userID) {
            RetroService retroService = retrofit.create(RetroService.class);

            Log.d("userID", String.valueOf(userID));

            Call<UserObject> call = retroService.getUserInformation(userID);

            call.enqueue(new Callback<UserObject>() {
                @Override
                public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                    userData = response.body();
                    username=userData.getuName();


                    if(userData.getuIMG()=="default"||userData.getuIMG()==null){
                        userProfile.setImageResource(R.drawable.icon);
                        userimg="default";
                    }
                    else{
                        Picasso.get().load(userData.getuIMG()).into(userProfile);
                        userimg=userData.getuIMG();

                    }
                    userName.setText(userData.getuName());

                    if(qnaObject.getIsAnonymous()==true){
                        userProfile.setImageResource(R.drawable.icon);
                        userName.setText("익명");
                    }

                }

                @Override
                public void onFailure(Call<UserObject> call, Throwable t) {
                    Log.e("Connection Error", "Bad Connection");
                }
            });
        }

    }



}