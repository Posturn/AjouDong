package com.example.ajoudongfe;

public class LoginObject {
    private String uID;
    private String uPW;
    private String uToken;

    public LoginObject(String uID, String uPW, String uToken) {
        this.uID = uID;
        this.uPW = uPW;
        this.uToken = uToken;
    }

    public LoginObject(String uID, String uPW) {
        this.uID = uID;
        this.uPW = uPW;
    }

    public String getuID() {
        return uID;
    }

    public String getuToken() {
        return uToken;
    }

    public void setuToken(String uToken) {
        this.uToken = uToken;
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
