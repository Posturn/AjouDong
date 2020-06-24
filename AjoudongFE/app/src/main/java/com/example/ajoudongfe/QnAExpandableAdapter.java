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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QnAExpandableAdapter extends RecyclerView.Adapter<QnAExpandableAdapter.ItemViewHolder> {
    private static String BASE_URL = "http://10.0.2.2:8000";
    private Context context;
    private List<QnAObject> listData = new ArrayList<>();
    private UserObject userData= new UserObject();
    private Retrofit retrofit;
    private RetroService retroService;
    private int clubID;
    private int userID;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;

    public QnAExpandableAdapter(Context context, List<QnAObject> listData, int clubID) {
        this.context = context;
        this.listData = listData;
        this.clubID = clubID;
    }


    @NonNull
    @Override
    public QnAExpandableAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_header_shape, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QnAExpandableAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    void addItem(QnAObject data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Header
        private ImageView userProfile;
        private TextView userName;
        private TextView date;
        private TextView question;
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
        private ConstraintLayout qnaChildLayout;


        ItemViewHolder(View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.imageView);
            userName = itemView.findViewById(R.id.textView21);
            date = itemView.findViewById(R.id.textView22);
            question = itemView.findViewById(R.id.editText);
            spreadbutton = itemView.findViewById(R.id.QnaSpread);
            spreadtext = itemView.findViewById(R.id.textView25);

            childuserProfile = itemView.findViewById(R.id.childimageView);
            childuserName = itemView.findViewById(R.id.childtextView21);
            childdate = itemView.findViewById(R.id.childtextView22);
            childquestion = itemView.findViewById(R.id.childeditText);

            qnaHeaderLayout = itemView.findViewById(R.id.qnaheader);
            qnaChildLayout = itemView.findViewById(R.id.qnachild);

        }

        public void onBind(QnAObject qnaObject, int position) {
            this.qnaObject = qnaObject;
            this.position = position;

            getUserInfo(qnaObject.getUserID());

            Picasso.get().load(userData.getuIMG()).into(userProfile);
            userName.setText(userData.getuName());
            date.setText(qnaObject.getFAQDate());
            question.setText(qnaObject.getFAQContent());

            Picasso.get().load(userData.getuIMG()).into(childuserProfile);
            childuserName.setText(userData.getuName());
            childdate.setText(qnaObject.getFAQDate());
            childquestion.setText(qnaObject.getFAQContent());

            changeVisibility(selectedItems.get(position));

            spreadbutton.setOnClickListener(this);
            spreadtext.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.QnaSpread:

                case R.id.textView25:
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectedItems.put(position, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                    break;
                // 해당 포지션의 변화를 알림
                // 클릭된 position 저장
            }

        }

        public void getUserInfo(int userID) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Call<UserObject> call = retroService.getUserInformation(userID);

            call.enqueue(new Callback<UserObject>() {
                @Override
                public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                    userData = response.body();
                }

                @Override
                public void onFailure(Call<UserObject> call, Throwable t) {
                    Log.e("Connection Error", "Bad Connection");
                }
            });
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용

            float d = context.getResources().getDisplayMetrics().density;

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(qnaHeaderLayout.getHeight(), qnaChildLayout.getHeight()) : ValueAnimator.ofInt(qnaChildLayout.getHeight(), qnaHeaderLayout.getHeight());
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
//                    int value = (int) animation.getAnimatedValue();
//                    imageView2.getLayoutParams().height = value;
//                    imageView2.requestLayout();
//                    imageView2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                    qnaChildLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                    qnaHeaderLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
                }
            });
            // Animation start
            va.start();
        }

    }
}