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
import android.widget.EditText;
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

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ItemViewHolder> {
    private static String BASE_URL = "http://10.0.2.2:8000";
    private Context context;
    private List<CommentObject> listData = new ArrayList<>();
    private UserObject userData= new UserObject();
    private ManagerAccountObject managerData= new ManagerAccountObject();
    private Retrofit retrofit;
    private RetroService retroService;

    private int FAQID;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private int faqID;


    public CommentRecyclerViewAdapter(Context context, List<CommentObject> listData, int FAQID) {
        this.context = context;
        this.listData = listData;
        this.FAQID = FAQID;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_shape, parent, false);


        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerViewAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position), position);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    void addItem(CommentObject data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView userProfile;
        private TextView userName;
        private TextView date;
        private TextView question;

        private CommentObject CommentObject;

        private int position;


        private ConstraintLayout commentlayout;

        ItemViewHolder(View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.commentchildimageview);
            userName = itemView.findViewById(R.id.commentchildusername);
            date = itemView.findViewById(R.id.commentchilddate);
            question = itemView.findViewById(R.id.commentchildquestion);


            commentlayout = (ConstraintLayout) itemView.findViewById(R.id.commentshape);
        }

        public void onBind(CommentObject CommentObject, int position) {
            this.CommentObject = CommentObject;
            this.position = position;

            if(CommentObject.getUserID()>9999){
                getUserInfo(CommentObject.getUserID());
            }
            else{
                getManagerInfo(CommentObject.getUserID());
            }
            date.setText(CommentObject.getFAQCommentDate().substring(0, 19));
            question.setText(CommentObject.getFAQCommentContent());


        }

        public void getUserInfo(int userID) {
            RetroService retroService = retrofit.create(RetroService.class);

            Log.d("userID", String.valueOf(userID));

            Call<UserObject> call = retroService.getUserInformation(userID);

            call.enqueue(new Callback<UserObject>() {
                @Override
                public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                    userData = response.body();
                    Log.v("유저객체", String.valueOf(response.body()));
                    Log.v("유저이미지", String.valueOf(userData.getuIMG()));

                    if (userData.getuIMG() == "default" || userData.getuIMG() == null) {
                        userProfile.setImageResource(R.drawable.icon);
                    } else {
                        Picasso.get().load(userData.getuIMG()).into(userProfile);

                    }
                    userName.setText(userData.getuName());

                    if (CommentObject.getIsAnonymous() == true) {
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

        public void getManagerInfo(int managerID){
            RetroService retroService = retrofit.create(RetroService.class);

            Call<ManagerAccountObject> call = retroService.getManagerInformation(managerID);

            call.enqueue(new Callback<ManagerAccountObject>() {
                @Override
                public void onResponse(Call<ManagerAccountObject> call, Response<ManagerAccountObject> response) {
                    managerData = response.body();
                    Log.v("매니저객체", String.valueOf(response.body()));
                    Log.v("매니저이미지", String.valueOf(managerData.getClubIMG()));

                    if (managerData.getClubIMG() == "default" || managerData.getClubIMG() == null) {
                        userProfile.setImageResource(R.drawable.icon);
                    } else {
                        Picasso.get().load(managerData.getClubIMG()).into(userProfile);

                    }
                    userName.setText(managerData.getClubName());

                    if (CommentObject.getIsAnonymous() == true) {
                        userProfile.setImageResource(R.drawable.icon);
                        userName.setText("익명");
                    }

                }

                @Override
                public void onFailure(Call<ManagerAccountObject> call, Throwable t) {
                    Log.e("Connection Error", "Bad Connection");
                }
            });


        }


    }

}