package com.example.ajoudongfe;

public class UserAccountObject {
    String uID;
    String uPW;
    String uIMG;
    String uName;
    Boolean uGender;
    int uSchoolID;
    String uMajor;
    int uPhoneNumber;
    String uCollege;

    public String getuIMG() {
        return uIMG;
    }

    public void setuIMG(String uIMG) {
        this.uIMG = uIMG;
    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }


    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

}
