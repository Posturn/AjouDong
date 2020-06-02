package com.example.ajoudongfe;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewMemberRecyclerAdapter extends RecyclerView.Adapter<NewMemberRecyclerAdapter.ItemViewHolder> {
    private static String BASE_URL = "http://10.0.2.2:8000";
    private Context context;
    private List<MemberInfoObject> listData = new ArrayList<>();
    private Retrofit retrofit;
    private int clubID;
    private SparseBooleanArray selectedItems= new SparseBooleanArray();
    private int prePosition = -1;

    public NewMemberRecyclerAdapter(Context context, List<MemberInfoObject> listData, int clubID) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newember_recycler_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position), position);
        final int uSchoolID = listData.get(position).getuSchoolID();
        final int item_position = position;

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseObject> call = newAppliedUser(uSchoolID, clubID);

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

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseObject> call = deleteAppliedUser(uSchoolID, clubID);

                call.enqueue(new Callback<ResponseObject>() {
                    @Override
                    public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                        ResponseObject data = response.body();
                        if(data.getResponse() != 1)
                        {
                            Log.e("Error", "User was not rejected");
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

        holder.userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.get(item_position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(item_position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(item_position, true);
                }
                if (prePosition != -1)
                    notifyItemChanged(prePosition);
                notifyItemChanged(item_position);
                prePosition = item_position;
            }
        });
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.get(item_position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(item_position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(item_position, true);
                }
                if (prePosition != -1)
                    notifyItemChanged(prePosition);
                notifyItemChanged(item_position);
                prePosition = item_position;
            }
        });
    }

    private Call<ResponseObject> newAppliedUser(int uSchoolID, int clubID) {
        MemberObject memberObject =  new MemberObject(clubID, uSchoolID);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.newAppliedUser(clubID, uSchoolID);
    }

    private Call<ResponseObject> deleteAppliedUser(int uSchoolID, int clubID) {
        MemberObject memberObject =  new MemberObject(clubID, uSchoolID);
        RetroService retroService = retrofit.create(RetroService.class);
        return retroService.deleteAppliedUser(clubID, uSchoolID);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView uSchoolIDText;
        private TextView uMajorText;
        private TextView uNameText;
        private ImageView uIMG;
        private ImageButton acceptButton;
        private ImageButton rejectButton;

        private TextView childuSchoolIDText;
        private TextView childuMajorText;
        private TextView childuNameText;
        private TextView childuContent;
        private ImageButton closeButton;
        private ImageView childuIMG;

        private ConstraintLayout newMemberItemLayout;
        private ConstraintLayout newMemberChildLayout;
        private LinearLayout userInfoLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            uSchoolIDText = itemView.findViewById(R.id.uSchoolIDText);
            uMajorText = itemView.findViewById(R.id.uMajorText);
            uNameText = itemView.findViewById(R.id.uNameText);
            uIMG = itemView.findViewById(R.id.uIMG);
            acceptButton = itemView.findViewById(R.id.acceptbutton);
            rejectButton = itemView.findViewById(R.id.rejectbutton);

            childuSchoolIDText = itemView.findViewById(R.id.childuSchoolID);
            childuMajorText = itemView.findViewById(R.id.childuMajor);
            childuNameText = itemView.findViewById(R.id.childuName);
            childuContent = itemView.findViewById(R.id.childuContent);
            closeButton = itemView.findViewById(R.id.closeButton);
            childuIMG = itemView.findViewById(R.id.uChildIMG);

            newMemberItemLayout = itemView.findViewById(R.id.newmemberItemLayout);
            newMemberChildLayout = itemView.findViewById(R.id.newmemberChildLayout);
            userInfoLayout = itemView.findViewById(R.id.userInfoLayout);
        }

        void onBind(MemberInfoObject memberInfoObject, int position)
        {
            uSchoolIDText.setText("학번 : " + Integer.toString(memberInfoObject.getuSchoolID()));
            uMajorText.setText("학과 : " + memberInfoObject.getuMajor());
            uNameText.setText("이름 : " + memberInfoObject.getuName());

            childuSchoolIDText.setText("학번 : " + Integer.toString(memberInfoObject.getuSchoolID()));
            childuMajorText.setText("학과 : " + memberInfoObject.getuMajor());
            childuNameText.setText("이름 : " + memberInfoObject.getuName());
            childuContent.setText("세부 사항 : " + memberInfoObject.getAdditionalApplyContent());
            if(memberInfoObject.getuIMG() != null && memberInfoObject.getuName().length() > 0) {
                Picasso.get().load(memberInfoObject.getuIMG()).into(uIMG);
                Picasso.get().load(memberInfoObject.getuIMG()).into(childuIMG);
                uIMG.setBackground(new ShapeDrawable(new OvalShape()));
                uIMG.setClipToOutline(true);

            }
            else
            {
                uIMG.setImageResource(R.drawable.icon);
                childuIMG.setImageResource(R.drawable.icon);
                uIMG.setBackground(new ShapeDrawable(new OvalShape()));
                uIMG.setClipToOutline(true);
            }

            changeVisibility(selectedItems.get(position));



        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(newMemberItemLayout.getHeight(), newMemberChildLayout.getHeight()) : ValueAnimator.ofInt(newMemberChildLayout.getHeight(), newMemberItemLayout.getHeight());
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
//                    int value = (int) animation.getAnimatedValue();
//                    imageView2.getLayoutParams().height = value;
//                    imageView2.requestLayout();
//                    imageView2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                    newMemberChildLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    newMemberItemLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
                }
            });
            // Animation start
            va.start();
        }
    }


}