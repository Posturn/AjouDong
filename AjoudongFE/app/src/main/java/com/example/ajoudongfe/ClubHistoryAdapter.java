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

public class ClubHistoryAdapter extends BaseAdapter {
    private List<ClubActivityGridObject> clubmodels;
    private Context mContext;
    private int checkGrid = 0;
    private String checkVideo;

    public ClubHistoryAdapter(Context mContext, List<ClubActivityGridObject> clubmodels) {
        this.mContext = mContext;
        this.clubmodels = clubmodels;
        if(checkGrid == 0){
            clubmodels.add(0, new ClubActivityGridObject(0,"0","0","0",0));
        }
        checkGrid++;
    }
    //출력될 이미지 데이터셋(res/drawable 폴더)

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_shape_activity, parent, false);
        }

        ImageView clubImage = convertView.findViewById(R.id.imageView1);
        TextView nameText = convertView.findViewById(R.id.textView1);

        final ClubActivityGridObject thisClubActivityObject = clubmodels.get(position);

            if(thisClubActivityObject.getClubActivityID() == 0){
                nameText.setText("");
                clubImage.setImageResource(R.drawable.ic_add);
            }
            else{
                checkVideo = thisClubActivityObject.getClubActivityFile().substring(thisClubActivityObject.getClubActivityFile().lastIndexOf("/")+1);   //현재 이미지 파일 이름 가져오기
                nameText.setText(thisClubActivityObject.getClubActivityInfo());

                if(thisClubActivityObject.getClubActivityFile() != null && thisClubActivityObject.getClubActivityFile().length()>0)
                {
                    if(checkVideo.contains("mp4")) {
                        Glide.with(mContext.getApplicationContext()).load(thisClubActivityObject.getClubActivityFile()).thumbnail(0.1f).into(clubImage);
                    } else{
                        Glide.with(mContext.getApplicationContext()).load(thisClubActivityObject.getClubActivityFile()).into(clubImage);
                    }
                }
            }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ManagerClubHistoryEditActivity.class);
                intent.putExtra("activityID", thisClubActivityObject.getClubActivityID());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
