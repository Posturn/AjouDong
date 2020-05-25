package com.example.ajoudongfe;

public class MemberInfoObject {
    private int uSchoolID;
    private String uMajor;
    private String uName;
    private String uIMG;

    public MemberInfoObject(int uSchoolID, String uMajor, String uName, String uIMG) {
        this.uSchoolID = uSchoolID;
        this.uMajor = uMajor;
        this.uName = uName;
        this.uIMG = uIMG;
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

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuIMG() {
        return uIMG;
    }

    public void setuIMG(String uIMG) {
        this.uIMG = uIMG;
    }
}
