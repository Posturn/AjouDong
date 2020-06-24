package com.example.ajoudongfe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyAjoudongClubGridAdapter extends BaseAdapter implements View.OnClickListener  {

    private List<ClubObject> clubmodels;
    private Context mContext;
    private List<Integer> nRecruitClub;
    private int schoolID;
    final  String TAG = getClass().getSimpleName();
    public static String BASE_URL= Keys.getServerUrl();
    private RetroService retroService;
    private List<Integer> clubIDs = new ArrayList<>();
    private int uSchoolID;
    private int Position;

    public int getClubIDs(int position) {
        return clubIDs.get(position);
    }

    public void setClubIDs(int clubID, int position) {
        this.clubIDs.add(position, clubID);
    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public MyAjoudongClubGridAdapter(Context mContext, List<ClubObject> clubmodels, List<Integer> nRecruit, int schoolID) {
        this.mContext=mContext;
        this.clubmodels=clubmodels;
        this.nRecruitClub = nRecruit;
        this.schoolID=schoolID;
        setuSchoolID(schoolID);
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        mContext=parent.getContext();
        initMyAPI(BASE_URL);

        if(convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_shape, parent, false);
        }

        ImageView clubImage = convertView.findViewById(R.id.imageView1);
        ImageView blackImage = convertView.findViewById(R.id.blackImageView);
        TextView nameText = convertView.findViewById(R.id.textView1);

        final ClubObject thisClubObject = clubmodels.get(position);

        final int clubID = thisClubObject.getClubID();
        setClubIDs(clubID, position);
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
                setPosition(position);
                Intent intent = new Intent(mContext, MyAjoudongWithdrawPopupActivity.class);
                intent.putExtra("clubID", clubID);
                intent.putExtra("uSchoolID", getuSchoolID());
                ((Activity)view.getContext()).startActivityForResult(intent,1);
                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
    }

    private void initMyAPI(String baseUrl){     //레트로 핏 설정
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }

    public void onActivityResult(int requestCode, Intent i) {
        if(i.getIntExtra("result",3) == 0){
            notifyDataSetChanged();
            this.notifyDataSetChanged();
           /* Log.d(TAG,"DELETE");
            Log.d(TAG, "pos: "+getPosition());
            Log.d(TAG, "scholl: "+getuSchoolID());
            Call<ResponseObject> deleteCall = retroService.deleteClubMember(getClubIDs(getPosition()), getuSchoolID());
            deleteCall.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "삭제 완료");
                        clubmodels.remove(getPosition());
                        clubIDs.remove(getPosition());
                        notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Status Code : " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Log.d(TAG, "Fail msg : " + t.getMessage());
                }

            });
            notifyDataSetChanged();
            this.notifyDataSetChanged();*/
        }
    }
}
