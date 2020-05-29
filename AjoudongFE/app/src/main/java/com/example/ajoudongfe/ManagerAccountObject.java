package com.example.ajoudongfe;

public class ManagerAccountObject {
    String mID;
    String mPW;
    int clubID;
    String clubName;
    String clubIMG;
    boolean newbieAlarm;

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubIMG() {
        return clubIMG;
    }

    public void setClubIMG(String clubIMG) {
        this.clubIMG = clubIMG;
    }
}
