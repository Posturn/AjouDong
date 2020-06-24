package com.example.ajoudongfe;

public class QnAObject {
    private int userID;
    private int FAQID;
    private String FAQDate;
    private int isAnonymous;
    private String FAQContent;
    private int clubID;

    public int getUserID(){
        return userID;
    }

    public int getFAQID(){
        return FAQID;
    }

    public String getFAQDate(){
        return FAQDate;
    }

    public int getIsAnonymous(){
        return isAnonymous;
    }

    public String getFAQContent(){
        return FAQContent;
    }

    public int getClubID(){
        return clubID;
    }

    public void setFAQDate(String FAQCommentDate){
        this.FAQDate=FAQDate;
    }
    public void setIsAnonymous(int isAnonymous){
        this.isAnonymous=isAnonymous;
    }
    public void setFAQContent(String FAQContent){
        this.FAQContent=FAQContent;
    }
    public void setFAQID(int FAQID){
        this.FAQID=FAQID;
    }
}
