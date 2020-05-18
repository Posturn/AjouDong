package com.example.ajoudongfe;

import android.graphics.drawable.Drawable;

public class GridListObject {
    private Drawable mThumbIds;
    private String name;

    public GridListObject(Drawable mThumbIds, String name)
    {
        this.mThumbIds = mThumbIds;
        this.name = name;

    }

    public Drawable getmThumbIds() {
        return mThumbIds;
    }

    public void setmThumbIds(Drawable mThumbIds) {
        this.mThumbIds = mThumbIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
