package com.example.ajoudongfe;

public class MemberObject {
    int clubID;
    int uSchoolID;

    public MemberObject(int clubID, int uSchoolID) {
        this.clubID = clubID;
        this.uSchoolID = uSchoolID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }
}
