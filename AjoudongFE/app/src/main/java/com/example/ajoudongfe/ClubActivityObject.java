package com.example.ajoudongfe;

public class ClubActivityObject {
    private String clubActivityFile;
    private String clubActivityInfo;
    private int clubActivityID;
    private int clubID;
    private String clubActivityDetail;

    public int getClubID() {
        return clubID;
    }

    public void setClubID(int clubID) {
        this.clubID = clubID;
    }

    public String getClubActivityDetail() {
        return clubActivityDetail;
    }

    public void setClubActivityDetail(String activityDetail) {
        clubActivityDetail = activityDetail;
    }

    public String getClubActivityFile() {
        return clubActivityFile;
    }

    public void setClubActivityFile(String clubActivityFile) {
        this.clubActivityFile = clubActivityFile;
    }

    public String getClubActivityInfo() {
        return clubActivityInfo;
    }

    public void setClubActivityInfo(String clubActivityInfo) {
        this.clubActivityInfo = clubActivityInfo;
    }

    public int getClubActivityID() {
        return clubActivityID;
    }

    public void setClubActivityID(int clubActivityID) {
        this.clubActivityID = clubActivityID;
    }
}
