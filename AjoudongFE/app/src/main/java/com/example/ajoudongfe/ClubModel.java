package com.example.ajoudongfe;

import java.util.ArrayList;

public class ClubModel
{
    private int clubID;
    private String clubName;
    private String clubCategory;
    private String clubIMG;
    private int clubMajor;
    private double clubDues;

    public ClubModel(int clubID, String clubName, String clubCategory,String clubIMG, int clubMajor, double clubDues) {
        this.clubID = clubID;
        this.clubCategory = clubCategory;
        this.clubIMG = clubIMG;
        this.clubMajor = clubMajor;
        this.clubDues = clubDues;
    }

    public  String getName(){
        return clubName;
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

}