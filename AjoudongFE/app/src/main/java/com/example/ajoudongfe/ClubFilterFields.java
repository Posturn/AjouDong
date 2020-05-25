package com.example.ajoudongfe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClubFilterFields {

    @SerializedName("clubName")
    @Expose
    private String clubName;
    @SerializedName("clubCategory")
    @Expose
    private String clubCategory;
    @SerializedName("clubIMG")
    @Expose
    private String clubIMG;
    @SerializedName("clubMajor")
    @Expose
    private Integer clubMajor;
    @SerializedName("clubDues")
    @Expose
    private Double clubDues;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubCategory() {
        return clubCategory;
    }
    public void setClubCategory(String clubCategory) {
        this.clubCategory = clubCategory;
    }

    public String getClubIMG() {
        return clubIMG;
    }

    public void setClubIMG(String clubIMG) {
        this.clubIMG = clubIMG;
    }

    public Integer getClubMajor() {
        return clubMajor;
    }

    public void setClubMajor(Integer clubMajor) {
        this.clubMajor = clubMajor;
    }

    public Double getClubDues() {
        return clubDues;
    }

    public void setClubDues(Double clubDues) {
        this.clubDues = clubDues;
    }

}
