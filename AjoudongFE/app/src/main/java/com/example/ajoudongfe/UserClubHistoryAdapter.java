package com.example.ajoudongfe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class UserClubHistoryAdapter extends BaseAdapter {
    private List<ClubActivityGridObject> clubmodels;
    private Context mContext;
    private String checkVideo;

    public UserClubHistoryAdapter(Context mContext, List<ClubActivityGridObject> clubmodels) {
        this.mContext = mContext;
        this.clubmodels = clubmodels;
    }

    public int getCount(){
        return clubmodels.size();
    }

    public Object getItem(int pos){
        return clubmodels.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();

        if(convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_shape, parent, false);
        }

        ImageView clubImage = convertView.findViewById(R.id.imageView1);
        TextView nameText = convertView.findViewById(R.id.textView1);

        final ClubActivityGridObject thisClubActivityObject = clubmodels.get(position);

            checkVideo = thisClubActivityObject.getClubActivityFile().substring(thisClubActivityObject.getClubActivityFile().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
            nameText.setText(thisClubActivityObject.getClubActivityInfo());

            if(thisClubActivityObject.getClubActivityFile() != null && thisClubActivityObject.getClubActivityFile().length()>0)
            {
                if(checkVideo.contains("mp4")) {
                    Glide.with(mContext.getApplicationContext()).load(thisClubActivityObject.getClubActivityFile()).thumbnail(0.1f).into(clubImage);
                } else{
                    Glide.with(mContext.getApplicationContext()).load(thisClubActivityObject.getClubActivityFile()).into(clubImage);
                }
            }else {
                //Toast.makeText(mContext, "Empty Image URL", Toast.LENGTH_LONG).show();
            }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserClubHistoryDetailActivity.class);
                intent.putExtra("activityID", thisClubActivityObject.getClubActivityID());
                // Toast.makeText(mContext, "활동 내용 추가", Toast.LENGTH_LONG).show();
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
