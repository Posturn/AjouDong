package com.example.ajoudongfe;

import java.util.List;

public class ClubTagObject {
    private String clubTag;
    private int clubID;

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private List<String> tags;

    public String getClubTag() {
        return clubTag;
    }

    public void setClubTag(String clubTag) {
        this.clubTag = clubTag;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }
}
