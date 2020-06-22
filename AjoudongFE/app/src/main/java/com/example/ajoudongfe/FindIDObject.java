package com.example.ajoudongfe;

public class FindIDObject {
    private String uName;
    private int uSchoolID;

    public FindIDObject(String uName, int uSchoolID) {
        this.uName = uName;
        this.uSchoolID = uSchoolID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getuSchoolID() {
        return uSchoolID;
    }

    public void setuSchoolID(int uSchoolID) {
        this.uSchoolID = uSchoolID;
    }
}
