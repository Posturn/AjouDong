package com.example.ajoudongfe;

public class VerifyInfoObject {
    private int templateSid;
    private String address;
    private String name;
    private String type;
    private String who_signup;
    private String verify_code;
    private String timeStamp;
    private String accessKey;
    private String encryptedKey;

    public int getTemplateSid() {
        return templateSid;
    }

    public void setTemplateSid(int templateSid) {
        this.templateSid = templateSid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho_signup() {
        return who_signup;
    }

    public void setWho_signup(String who_signup) {
        this.who_signup = who_signup;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }
}
