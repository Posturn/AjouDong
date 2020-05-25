package com.example.ajoudongfe;

public class BookmarkObject {
    private int clubID;
    private int uSchoolID;

    public BookmarkObject(int clubID, int uSchoolID) {
        this.clubID = clubID;
        this.uSchoolID = uSchoolID;
    }

    public int getClubID(){
        return clubID;
    }

    public void setClubID(){
        this.clubID=clubID;
    }

    public int getuSchoolID(){
        return uSchoolID;
    }

    public void setuSchoolID(){
        this.uSchoolID=uSchoolID;
    }
}
