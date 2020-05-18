package com.example.ajoudongfe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class ClubGridAdapter extends BaseAdapter {

    private List<ClubObject> clubmodels;
    private Context mContext;

    public ClubGridAdapter(Context mContext, List<ClubObject> clubmodels) {
        this.mContext=mContext;
        this.clubmodels=clubmodels;
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
        mContext=parent.getContext();

        if(convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_shape, parent, false);
        }

        ImageView clubImage = convertView.findViewById(R.id.imageView1);
        TextView nameText = convertView.findViewById(R.id.textView1);

        final ClubObject thisClubObject = clubmodels.get(position);

        nameText.setText(thisClubObject.getName());

        if(thisClubObject.getIMG() != null && thisClubObject.getIMG().length()>0)
        {
            Picasso.get().load(thisClubObject.getIMG()).into(clubImage);
        }else {
            //Toast.makeText(mContext, "Empty Image URL", Toast.LENGTH_LONG).show();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClubInfomationActivity.class);
                intent.putExtra("clubName", thisClubObject.getName());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}