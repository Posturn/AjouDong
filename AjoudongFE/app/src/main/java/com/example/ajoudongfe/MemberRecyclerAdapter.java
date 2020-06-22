package com.example.ajoudongfe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.ItemViewHolder>{
    private static String BASE_URL = "http://10.0.2.2:8000";
    private Context context;
    private List<MemberInfoObject> listData = new ArrayList<>();
    private Retrofit retrofit;
    private int clubID;
    private int value;

    public MemberRecyclerAdapter(Context context, List<MemberInfoObject> listData, int clubID) {
        this.context = context;
        this.listData = listData;
        this.clubID = clubID;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_recycler_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
        final int uSchoolID = listData.get(position).getuSchoolID();

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show(holder, uSchoolID);

            }
        });
    }

    public void show(final ItemViewHolder holder, final int uSchoolID)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("정말 삭제하시겠습니까?");
        builder.setMessage("취소하시려면 아니오를 누르십시오");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ResponseObject> call = deleteMember(clubID, uSchoolID);

                        call.enqueue(new Callback<ResponseObject>() {
                            @Override
                            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                                ResponseObject data = response.body();
                                if(data.getResponse() != 1)
                                {
                                    Log.e("Error", "User was not accepted");
                                }
                                //TODO 새로고침 혹은 리사이클러 뷰 변환
                                listData.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), listData.size());
                            }

                            @Override
                            public void onFailure(Call<ResponseObject> call, Throwable t) {
                                Log.e("Connection Error", "Bad Connection");
                            }
                        });
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    private Call<ResponseObject> deleteMember(int clubID, int uSchoolID)
    {
        MemberObject memberObject = new MemberObject(clubID, uSchoolID);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.deleteMember(clubID, uSchoolID);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView uSchoolIDText;
        private TextView uMajorText;
        private TextView uNameText;
        private ImageView uIMG;
        private ImageButton deleteButton;

        ItemViewHolder(View itemView) {
            super(itemView);
            uSchoolIDText = itemView.findViewById(R.id.uSchoolIDText);
            uMajorText = itemView.findViewById(R.id.uMajorText);
            uNameText = itemView.findViewById(R.id.uNameText);
            uIMG = itemView.findViewById(R.id.uIMG);
            deleteButton = itemView.findViewById(R.id.deletebutton);
        }

        void onBind(MemberInfoObject memberInfoObject)
        {
            uSchoolIDText.setText("학번 : " + Integer.toString(memberInfoObject.getuSchoolID()));
            Log.d("USI", Integer.toString(memberInfoObject.getuSchoolID()));
            uMajorText.setText("학과 : " + memberInfoObject.getuMajor());
            uNameText.setText("이름 : " + memberInfoObject.getuName());
            if(memberInfoObject.getuIMG() != null && memberInfoObject.getuIMG().equals("default") == true){
                uIMG.setImageResource(R.drawable.icon);
            }
            else if(memberInfoObject.getuIMG() != null && memberInfoObject.getuName().length() > 0) {
                Picasso.get().load(memberInfoObject.getuIMG()).into(uIMG);
                //uIMG.setBackground(new ShapeDrawable(new OvalShape()));
                //uIMG.setClipToOutline(true);
            }
            else
            {
                uIMG.setImageResource(R.drawable.icon);
                //uIMG.setBackground(new ShapeDrawable(new OvalShape()));
                //uIMG.setClipToOutline(true);
            }

        }
    }



}

