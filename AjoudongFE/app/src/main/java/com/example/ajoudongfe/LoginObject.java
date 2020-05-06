package com.example.ajoudongfe;

public class LoginObject {
    private String uID;
    private String uPW;

    public LoginObject(String uID, String uPW) {
        this.uID = uID;
        this.uPW = uPW;
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
}
