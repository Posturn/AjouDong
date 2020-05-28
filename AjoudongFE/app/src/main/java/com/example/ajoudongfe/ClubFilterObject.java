package com.example.ajoudongfe;

import java.util.ArrayList;

public class ClubFilterObject{
    private Integer club;
    private Integer sort;
    private ArrayList<String> tags;

    public ClubFilterObject(Integer club, Integer sort, ArrayList<String> tags){
        this.club = club;
        this.sort = sort;
        this.tags = tags;
    }
}
