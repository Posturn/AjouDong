package com.example.ajoudongfe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewMemberRecyclerAdapter extends RecyclerView.Adapter<NewMemberRecyclerAdapter.ItemViewHolder> {
    private List<MemberInfoObject> listData = new ArrayList<>();
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newember_recycler_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView uSchoolIDText;
        private TextView uMajorText;
        private TextView uNameText;
        private ImageView uIMG;

        ItemViewHolder(View itemView) {
            super(itemView);
            uSchoolIDText = itemView.findViewById(R.id.uSchoolIDText);
            uMajorText = itemView.findViewById(R.id.uMajorText);
            uNameText = itemView.findViewById(R.id.uNameText);
            uIMG = itemView.findViewById(R.id.uIMG);
        }

        void onBind(MemberInfoObject memberInfoObject)
        {
            uSchoolIDText.setText(memberInfoObject.getuSchoolID());
            uMajorText.setText(memberInfoObject.getuMajor());
            uNameText.setText(memberInfoObject.getuName());
            if(memberInfoObject.getuIMG() != null && memberInfoObject.getuName().length() > 0) {
                Picasso.get().load(memberInfoObject.getuIMG()).into(uIMG);
            }
            else
            {
                uIMG.setImageResource(R.drawable.icon);
            }

        }
    }

}
