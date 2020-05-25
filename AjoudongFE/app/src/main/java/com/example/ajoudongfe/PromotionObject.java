package com.example.ajoudongfe;


import java.util.Date;

public class PromotionObject {
    private String posterIMG;
    private String clubInfo;
    private String clubFAQ;
    private String clubApply;
    private String clubContact;
    private String additionalApply;
    private String recruitStartDate;
    private String recruitEndDate;

    public String getAdditionalApply() {
        return additionalApply;
    }

    public void setAdditionalApply(String additionalApply) {
        this.additionalApply = additionalApply;
    }

    public String getRecruitStartDate() {
        return recruitStartDate;
    }

    public void setRecruitStartDate(String recruitStartDate) {
        this.recruitStartDate = recruitStartDate;
    }

    public String getRecruitEndDate() {
        return recruitEndDate;
    }

    public void setRecruitEndDate(String recruitEndDate) {
        this.recruitEndDate = recruitEndDate;
    }

    public String getClubInfo() {
        return clubInfo;
    }
    public String getClubFAQ() {
        return clubFAQ;
    }

    public String getClubApply() {
        return clubApply;
    }

    public String getClubContact() {
        return clubContact;
    }

    public String getPosterIMG() {
        return posterIMG;
    }

    public void setPosterIMG(String posterIMG) {
        this.posterIMG = posterIMG;
    }

    public void setClubInfo(String clubInfo) {
        this.clubInfo = clubInfo;
    }

    public void setClubFAQ(String clubFAQ) {
        this.clubFAQ = clubFAQ;
    }

    public void setClubApply(String clubApply) {
        this.clubApply = clubApply;
    }

    public void setClubContact(String clubContact) {
        this.clubContact = clubContact;
    }
}
