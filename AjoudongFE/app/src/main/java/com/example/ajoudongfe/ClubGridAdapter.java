package com.example.ajoudongfe;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.List;

public class ClubGridAdapter extends BaseAdapter implements View.OnClickListener  {

    private List<ClubObject> clubmodels;
    private Context mContext;
    private List<Integer> nRecruitClub;
    private int schoolID;

    public ClubGridAdapter(Context mContext, List<ClubObject> clubmodels, List<Integer> nRecruit, int schoolID) {
        this.mContext=mContext;
        this.clubmodels=clubmodels;
        this.nRecruitClub = nRecruit;
        this.schoolID=schoolID;
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
        mContext=parent.getContext();

        if(convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_shape, parent, false);
        }

        ImageView clubImage = convertView.findViewById(R.id.imageView1);
        ImageView blackImage = convertView.findViewById(R.id.blackImageView);
        TextView nameText = convertView.findViewById(R.id.textView1);

        final ClubObject thisClubObject = clubmodels.get(position);

        int clubID = thisClubObject.getClubID();
        boolean white = true;
        if(nRecruitClub != null){
            for(int i=0; i<nRecruitClub.size(); i++){
                if (nRecruitClub.get(i) == clubID){
                    white = false;
                    break;
                }
            }
        }
        if(white == false) blackImage.setVisibility(View.VISIBLE);
        else blackImage.setVisibility(View.INVISIBLE);

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
                intent.putExtra("clubID", thisClubObject.getClubID());
                intent.putExtra("clubCategory", thisClubObject.getCategory());
                intent.putExtra("uSchoolID", schoolID);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}