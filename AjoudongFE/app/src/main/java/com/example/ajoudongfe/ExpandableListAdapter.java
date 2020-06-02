package com.example.ajoudongfe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private Context mContext;

    private List<Item> data;

    public ExpandableListAdapter(List<Item> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        mContext=parent.getContext();
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.expandble_header_shape, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                LayoutInflater inflater1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater1.inflate(R.layout.expandable_child_shape, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.text);
                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.spread);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.spread);
                }

                //활동 내역 메뉴만 새 창으로 이동
                if(itemController.header_title.getText()=="활동 내역                   ") {
                    itemController.btn_expand_toggle.setVisibility(View.GONE);
                    itemController.header_title.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, UserClubHistoryActivity.class);
                            intent.putExtra("clubID", item.getClubID());
                            mContext.startActivity(intent);
                        }
                    });
                }

                //회원 비율 메뉴만 새 창으로 이동
                if(itemController.header_title.getText()=="회원 비율                   ") {
                    itemController.btn_expand_toggle.setVisibility(View.GONE);
                    itemController.header_title.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ClubMemberChartActivity.class);
                            intent.putExtra("clubID", item.getClubID());
                            mContext.startActivity(intent);
                        }
                    });
                }

                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.spread);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }

                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.close);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final ListChildViewHolder inside = (ListChildViewHolder) holder;
                inside.clubinfotext.setText(data.get(position).text);

                Picasso.get().load(item.getImg()).into(inside.clubinfoimg);

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    }

    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView clubinfotext;
        public ImageView clubinfoimg;
        public Item refferalItem;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            clubinfotext = (TextView) itemView.findViewById(R.id.clubinfotext);
            clubinfoimg = (ImageView) itemView.findViewById(R.id.clubinfoimage);
        }
    }

    public static class Item {
        public int type;
        public String img;
        public String text;
        public int clubID;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String img, int clubID,  String text) {
            this.type = type;
            this.img = img;
            this.clubID = clubID;
            this.text = text;
        }

        public String getImg() {
            return img;
        }
        public int getClubID(){return clubID;}
    }
}
