package com.example.ajoudongfe;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static java.lang.Integer.parseInt;

public class UserEventAdapter extends RecyclerView.Adapter<UserEventAdapter.ItemViewHolder> {

    public static String BASE_URL= "http://10.0.2.2:8000";
    private List<EventObject> listData = new ArrayList<>();
    final  String TAG = getClass().getSimpleName();

    private List<Integer> eventIDs = new ArrayList<>();
    private List<Integer> clubIDs = new ArrayList<>();
    private List<String> Dday = new ArrayList<>();
    private int dday;
    private List<Integer> DdayColor = new ArrayList<>();

    public int getEventID(int position) {
        return eventIDs.get(position);
    }

    public void setEventID(int eventID, int position) {
        this.eventIDs.add(position, eventID);
    }

    public String getDday(int position) { return Dday.get(position); }

    public void setDday(String dday, int position) { this.Dday.add(position, dday); }

    public int getClubID(int position) {
        return clubIDs.get(position);
    }

    public void setClubID(int clubID, int position) {
        this.clubIDs.add(position, clubID);
    }

    public int getDdayColor(int position) {
        return DdayColor.get(position);
    }

    public void setDdayColor(int ddayColor, int position) {
        this.DdayColor.add(position, ddayColor);
    }


    public UserEventAdapter(List<EventObject> listData) {
        this.listData = listData;
    }



    @NonNull
    @Override
    public UserEventAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item_user, parent, false);
        UserEventAdapter.ItemViewHolder itemViewHolder = new UserEventAdapter.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserEventAdapter.ItemViewHolder holder, int position) {
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
        private TextView eventStatus;

        ItemViewHolder(final View itemView) {
            super(itemView);
            eventIMG = itemView.findViewById(R.id.userEventIMG);
            eventName = itemView.findViewById(R.id.userEventName);
            eventDate = itemView.findViewById(R.id.userEventDate);
            eventStatus = itemView.findViewById(R.id.eventStatus);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), UserEventDetailActivity.class);
                    intent.putExtra("eventID", getEventID(getLayoutPosition()));
                    intent.putExtra("clubID", getClubID(getLayoutPosition()));
                    intent.putExtra("dday",getDday(getLayoutPosition()));
                    intent.putExtra("ddayColor",getDdayColor(getLayoutPosition()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        void onBind(EventObject eventObject)
        {
            Log.d("EventName", eventObject.getEventName());
            setEventID(eventObject.getEventID(), getLayoutPosition());
            setClubID(eventObject.getClubID(),getLayoutPosition());
            eventName.setText(eventObject.getEventName());
            eventDate.setText(eventObject.getEventDate());
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
            Log.d(TAG, "dday는 "+dday);
            if(dday > 7){
                Dday = "D-"+dday;
                setDday(Dday,getLayoutPosition());
                eventStatus.setText(Dday);
                eventStatus.setTextColor(Color.BLUE);
                setDdayColor(Color.BLUE,getLayoutPosition());
            }
            else if(dday <= 7 && dday > 0){
                Dday = "D-"+dday;
                setDday(Dday,getLayoutPosition());
                eventStatus.setText(Dday);
                eventStatus.setTextColor(Color.RED);
                setDdayColor(Color.RED,getLayoutPosition());
            }
            else if(dday == 0){
                Dday = "D-Day";
                setDday(Dday,getLayoutPosition());
                eventStatus.setText(Dday);
                eventStatus.setTextColor(Color.BLACK);
                setDdayColor(Color.BLACK,getLayoutPosition());
            }
            else{
                dday = Math.abs(dday);
                Dday = "종료";
                setDday(Dday,getLayoutPosition());
                eventStatus.setText(Dday);
                eventStatus.setTextColor(Color.GRAY);
                setDdayColor(Color.GRAY,getLayoutPosition());
            }
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
