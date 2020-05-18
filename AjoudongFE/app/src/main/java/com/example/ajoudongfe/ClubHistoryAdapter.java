package com.example.ajoudongfe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClubHistoryAdapter extends BaseAdapter {
    ArrayList<GridListObject> items = new ArrayList<GridListObject>();
    private Context mContext;

    public void addItem(GridListObject item)
    {
        items.add(item);
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext=parent.getContext();
        GridListObject listitem = items.get(position);

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_shape_activity, parent, false);

        }

        ImageView clubImage = convertView.findViewById(R.id.imageView1);
        TextView nameText = convertView.findViewById(R.id.textView1);

        clubImage.setImageDrawable(listitem.getmThumbIds());
        nameText.setText(listitem.getName());

        return convertView;
    }
}
