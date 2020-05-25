package com.example.ajoudongfe;

public class ClubActivityGridObject {
    private String clubActivityFile;
    private String clubActivityInfo;
    private int clubActivityID;
    private int clubID;
    private String clubActivityDetail;

    public ClubActivityGridObject(int clubID, String clubActivityFile, String clubActivityInfo, String clubActivityDetail, int clubActivityID) {
        this.clubID = clubID;
        this.clubActivityInfo = clubActivityInfo;
        this.clubActivityDetail = clubActivityDetail;
        this.clubActivityFile = clubActivityFile;
        this.clubActivityID = clubActivityID;
    }

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
