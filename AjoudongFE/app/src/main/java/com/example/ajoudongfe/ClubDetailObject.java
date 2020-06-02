package com.example.ajoudongfe;

public class ClubDetailObject {
    private int clubID;
    private String clubName;
    private String clubCategory;
    private String clubIMG;
    private int clubMajor;
    private double clubDues;

    public int getRealClubID() {
        return clubID;
    }

    public void setRealClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getClubIMG() {
        return clubIMG;
    }

    public void setClubIMG(String clubIMG) {
        this.clubIMG = clubIMG;
    }
}
