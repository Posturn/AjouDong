package com.example.ajoudongfe;

import java.util.ArrayList;

public class ClubObject
{
    private int clubID;
    private String clubName;
    private String clubCategory;
    private String clubIMG;
    private int clubMajor;
    private double clubDues;

    public ClubObject(int clubID, String clubName, String clubCategory, String clubIMG, int clubMajor, double clubDues) {
        this.clubID = clubID;
        this.clubName = clubName;
        this.clubCategory = clubCategory;
        this.clubIMG = clubIMG;
        this.clubMajor = clubMajor;
        this.clubDues = clubDues;
    }

    public  String getName(){
        return clubName;
    }

    public int getClubID() { return clubID;}

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getIMG(){
        return clubIMG;
    }
    public void setIMG(String clubIMG){
        this.clubIMG=clubIMG;
    }

    public double getDues() {
        return clubDues;
    }

    public void setDues(double clubDues) {
        this.clubDues = clubDues;
    }

    public String getCategory()
    {
        return clubCategory;
    }

    public int getClubMajor() {
        return clubMajor;
    }

}
