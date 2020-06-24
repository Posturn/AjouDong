package com.example.ajoudongfe;

import java.util.ArrayList;
import java.util.List;

public class QnAObject {
    private int userID;
    private int FAQID;
    private String FAQDate;
    private boolean isAnonymous;
    private String FAQContent;
    private int clubID;
    //private List<CommentObject> FAQComments;

    public QnAObject(int userID, int FAQID, String FAQDate, boolean isAnonymous, String FAQContent, int clubID){
        this.userID=userID;
        this.FAQID=FAQID;
        this.FAQDate=FAQDate;
        this.isAnonymous=isAnonymous;
        this.FAQContent=FAQContent;
        this.clubID=clubID;
    }

    public int getUserID(){
        return userID;
    }

    public int getFAQID(){
        return FAQID;
    }

    public String getFAQDate(){
        return FAQDate;
    }

    public boolean getIsAnonymous(){
        return isAnonymous;
    }

    public String getFAQContent(){
        return FAQContent;
    }

    public int getClubID(){
        return clubID;
    }

    //public List<CommentObject> getCommentObjects(){
    //    return FAQComments;
   // }
    public void setUserID(int userID){this.userID=userID;}
    public void setFAQDate(String FAQCommentDate){
        this.FAQDate=FAQDate;
    }
    public void setIsAnonymous(boolean isAnonymous){
        this.isAnonymous=isAnonymous;
    }
    public void setFAQContent(String FAQContent){
        this.FAQContent=FAQContent;
    }
    public void setFAQID(int FAQID){
        this.FAQID=FAQID;
    }
    public void setClubID(int clubID){this.clubID=clubID;}
}
