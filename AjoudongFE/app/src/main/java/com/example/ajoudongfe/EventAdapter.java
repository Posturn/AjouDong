package com.example.ajoudongfe;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Integer.parseInt;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ItemViewHolder> {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private RetroService retroService;
    private List<EventObject> listData = new ArrayList<>();
    private int clubID;
    final  String TAG = getClass().getSimpleName();
    private List<Integer> eventIDs = new ArrayList<>();

    final String accessKey = Keys.getAccessKey();
    final String secretKey = Keys.getSecretKey();
    final String bucketName = "ajoudong";
    final String folderName = "events/";


    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);      //aws s3 클라이언트 객체 생성
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
    static private String nowImage2 = "";
    private int dday;

    public int getEventID(int position) {
        return eventIDs.get(position);
    }

    public void setEventID(int eventID, int position) {
        this.eventIDs.add(position, eventID);
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public EventAdapter(List<EventObject> listData, int clubID) {
        this.listData = listData;
        this.clubID = clubID;
    }



    @NonNull
    @Override
    public EventAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        EventAdapter.ItemViewHolder itemViewHolder = new EventAdapter.ItemViewHolder(view);
        initMyAPI(BASE_URL);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView eventName;
        private ImageView eventIMG;
        private TextView eventDate;
        private Button deleteBtn;
        private Button editBtn;

        ItemViewHolder(final View itemView) {
            super(itemView);
            eventIMG = itemView.findViewById(R.id.eventIMG);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            editBtn = itemView.findViewById(R.id.event_edit_btn);
            deleteBtn = itemView.findViewById(R.id.event_delete_btn);

            editBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ManagerNewEventActivity.class);
                    intent.putExtra("eventID", getEventID(getLayoutPosition()));
                    intent.putExtra("clubID",getClubID());
                    itemView.getContext().startActivity(intent);
                }
            });
            deleteBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "DELETE");
                    // pk 값은 임의로 변경가능
                    Call<EventObject> deleteCall = retroService.deleteEventObject(getEventID(getLayoutPosition()));
                    deleteCall.enqueue(new Callback<EventObject>() {
                        @Override
                        public void onResponse(Call<EventObject> call, Response<EventObject> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "삭제 완료");
                                nowImage2 = listData.get(getLayoutPosition()).getEventIMG().substring(listData.get(getLayoutPosition()).getEventIMG().lastIndexOf("/") + 1);
                                listData.remove(getLayoutPosition());
                                eventIDs.remove(getLayoutPosition());
                                notifyItemRemoved(getLayoutPosition());
                                new DeleteTask().execute();
                                Log.d(TAG,""+getLayoutPosition());
                            } else {
                                Log.d(TAG, "Status Code : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<EventObject> call, Throwable t) {
                            Log.d(TAG, "Fail msg : " + t.getMessage());
                        }
                    });
                }
            });
        }

        void onBind(EventObject eventObject)
        {
            Log.d("EventName", eventObject.getEventName());
            setEventID(eventObject.getEventID(), getLayoutPosition());
            setClubID(eventObject.getClubID());
            eventName.setText(eventObject.getEventName());
            if(eventObject.getEventIMG() != null && eventObject.getEventName().length() > 0) {
                Picasso.get().load(eventObject.getEventIMG()).into(eventIMG);
            }
            else
            {
                eventIMG.setImageResource(R.drawable.icon);
            }
            String[] ddayArray = eventObject.getEventDate().split("-");
            String Dday;
            Log.d(TAG,""+parseInt(ddayArray[0]) + parseInt(ddayArray[1])+parseInt(ddayArray[2]));
            dday = countdday(parseInt(ddayArray[0]),parseInt(ddayArray[1]),parseInt(ddayArray[2]));
            if(dday > 0){
                Dday = eventObject.getEventDate()+"(D-"+dday+")";
                eventDate.setText(Dday);
            }
            else if(dday == 0){
                Dday = eventObject.getEventDate()+"(D-Day)";
                eventDate.setText(Dday);
            }
            else{
                dday = Math.abs(dday);
                Dday = eventObject.getEventDate()+"(D+"+dday+")";
                eventDate.setText(Dday);
            }
        }
    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    private void deleteIMG(){       //원래 이미지 버킷에서 삭제
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderName+nowImage2));
        } catch (AmazonServiceException ase) {
            Log.e(TAG, ase.getErrorMessage());
        }
    }

    private class DeleteTask extends AsyncTask< Void, Void, String > {
        @Override
        protected String doInBackground(Void... voids) {
            deleteIMG();
            return null;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //result 값을 파싱하여 원하는 작업을 한다
        }
    }

    public int countdday(int myear, int mmonth, int mday) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar todaCal = Calendar.getInstance(); //오늘날짜 가져오기
            Calendar ddayCal = Calendar.getInstance(); //오늘날짜를 가져와 변경시킴

            mmonth -= 1; // 받아온날자에서 -1을 해줘야함.
            ddayCal.set(myear,mmonth,mday);// D-day의 날짜를 입력
            Log.e("테스트",simpleDateFormat.format(todaCal.getTime()) + "");
            Log.e("테스트",simpleDateFormat.format(ddayCal.getTime()) + "");

            long today = todaCal.getTimeInMillis()/86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
            long dday = ddayCal.getTimeInMillis()/86400000;
            long count = dday - today; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

}

