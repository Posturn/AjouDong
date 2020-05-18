package com.example.ajoudongfe;

import android.text.Editable;

public class PromotionObject {
    private String posterIMG;
    private String clubInfo;
    private String clubFAQ;
    private String clubApply;
    private String clubContact;

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
