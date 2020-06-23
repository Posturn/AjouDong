package com.example.ajoudongfe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationResultAdapter extends RecyclerView.Adapter<ApplicationResultAdapter.ItemViewHolder> {
    public static String BASE_URL= "http://10.0.2.2:8000";
    private Context context;
    private Retrofit retrofit;
    private List<ApplicationObject> listData = new ArrayList<>();
    private int uSchoolID;
    private RetroService retroService;

    public ApplicationResultAdapter(Context context, List<ApplicationObject> listData, int uSchoolID) {
        this.listData = listData;
        this.uSchoolID = uSchoolID;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_result_item, parent, false);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationResultAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));

        holder.resLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.resStatus.getText().toString().equals("심사 중"))
                {
                    showAlert(holder, uSchoolID);
                }

            }
        });
    }

    public void showAlert(final ApplicationResultAdapter.ItemViewHolder holder, final int uSchoolID)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("정말 신청취소하시겠습니까?");
        builder.setMessage("돌아가시려면 아니오를 누르십시오");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ResponseObject> call = retroService.deleteApplication(uSchoolID,holder.clubID);

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

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView resClubName;
        private ImageView resClubIMG;
        private TextView resStatus;
        private TextView applyDate;
        private ConstraintLayout resLayout;
        private int clubID;

        ItemViewHolder(View itemView) {
            super(itemView);
            resClubIMG = itemView.findViewById(R.id.resClubIMG);
            resClubName = itemView.findViewById(R.id.resClubName);
            resStatus = itemView.findViewById(R.id.resStauts);
            applyDate = itemView.findViewById(R.id.applyDate);
            resLayout = itemView.findViewById(R.id.resLayout);
        }

        void onBind(ApplicationObject applicationObject)
        {
            Log.d("ClubName", applicationObject.getClubName());
            resClubName.setText(applicationObject.getClubName());
            setStatus(resStatus, applicationObject.getStatus());
            if(applicationObject.getClubIMG() != null && applicationObject.getClubName().length() > 0) {
                Picasso.get().load(applicationObject.getClubIMG()).into(resClubIMG);

            }
            else
            {
                resClubIMG.setImageResource(R.drawable.icon);
            }
            applyDate.setText(applicationObject.getApplyDate());
            clubID = applicationObject.getClubID();
        }
    }

    private void setStatus(TextView resStatus, int status) {
        int color;
        if(status == 1)
        {
            resStatus.setText("승인");
            resStatus.setTextColor(Color.BLUE);
        }
        else if(status == 0)
        {
            resStatus.setText("심사 중");
            resStatus.setTextColor(Color.GRAY);
        }
        else
        {
            resStatus.setText("거절");
            resStatus.setTextColor(Color.RED);
        }
    }

}
