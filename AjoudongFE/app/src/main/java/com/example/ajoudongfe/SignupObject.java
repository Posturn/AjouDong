package com.example.ajoudongfe;

public class SignupObject
{
    String uID;
    String uPW;
    String uName;
    int uJender;
    int uSchoolID;
    String uMajor;
    String uCollege;
    int uPhoneNumber;

    public SignupObject(String uID, String uPW, String uName, int uJender, int uSchoolID, String uMajor, String uCollege, int uPhoneNumber) {
        this.uID = uID;
        this.uPW = uPW;
        this.uName = uName;
        this.uJender = uJender;
        this.uSchoolID = uSchoolID;
        this.uMajor = uMajor;
        this.uCollege = uCollege;
        this.uPhoneNumber = uPhoneNumber;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getuPW() {
        return uPW;
    }

    public void setuPW(String uPW) {
        this.uPW = uPW;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getuJender() {
        return uJender;
    }

    public void setuJender(int uJender) {
        this.uJender = uJender;
    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }

    public String getuMajor() {
        return uMajor;
    }

    public void setuMajor(String uMajor) {
        this.uMajor = uMajor;
    }

    public String getuCollege() {
        return uCollege;
    }

    public void setuCollege(String uCollege) {
        this.uCollege = uCollege;
    }

    public int getuPhoneNumber() {
        return uPhoneNumber;
    }

    public void setuPhoneNumber(int uPhoneNumber) {
        this.uPhoneNumber = uPhoneNumber;
    }
}
