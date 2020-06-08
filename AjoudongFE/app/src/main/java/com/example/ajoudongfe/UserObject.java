package com.example.ajoudongfe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserObject {
    @SerializedName("uName")
    @Expose
    private String uName;
    @SerializedName("uPW")
    @Expose
    private String uPW;
    @SerializedName("uID")
    @Expose
    private String uID;
    @SerializedName("uIMG")
    @Expose
    private String uIMG;
    @SerializedName("uJender")
    @Expose
    private boolean uJender;
    @SerializedName("uSchoolID")
    @Expose
    private Integer uSchoolID;
    @SerializedName("uMajor")
    @Expose
    private String uMajor;
    @SerializedName("uPhoneNumber")
    @Expose
    private Integer uPhoneNumber;
    @SerializedName("uCollege")
    @Expose
    private String uCollege;

    public String getuName() {
        return uName;
    }

    public String getuCollege() {
        return uCollege;
    }

    public void setuCollege(String uCollege) {
        this.uCollege = uCollege;
    }

    public Integer getuPhoneNumber() {
        return uPhoneNumber;
    }

    public void setuPhoneNumber(Integer uPhoneNumber) {
        this.uPhoneNumber = uPhoneNumber;
    }

    public boolean isuJender() {
        return uJender;
    }

    public String getuMajor() {
        return uMajor;
    }

    public void setuMajor(String uMajor) {
        this.uMajor = uMajor;
    }

    public void setuJender(boolean uJender) {
        this.uJender = uJender;
    }

    public Integer getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(Integer uSchoolID) {
        this.uSchoolID = uSchoolID;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPW() { return uPW; }

    public void setuPW(String uPW) { this.uPW = uPW; }

    public String getuID() { return uID; }

    public void setuID(String uID) { this.uID = uID; }

    public String getuIMG() { return uIMG; }

    public void setuIMG(String uIMG) { this.uIMG = uIMG; }
}
