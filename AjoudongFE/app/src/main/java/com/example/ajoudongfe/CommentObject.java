package com.example.ajoudongfe;

class CommentObject {
    private int userID;
    private int FAQCommentID;
    private String FAQCommentDate;
    private boolean isAnonymous;
    private String FAQCommentContent;
    private int FAQID;
    private int clubID;

    public CommentObject(int userID, int FAQCommentID, String FAQCommentDate, boolean isAnonymous, String FAQCommentContent, int FAQID, int clubID){
        this.userID=userID;
        this.FAQCommentID=FAQCommentID;
        this.FAQCommentDate=FAQCommentDate;
        this.isAnonymous=isAnonymous;
        this.FAQCommentContent=FAQCommentContent;
        this.FAQID=FAQID;
        this.clubID=clubID;
    }

    public int getUserID(){
        return userID;
    }
    public int getFAQCommentID(){
        return FAQCommentID;
    }

    public String getFAQCommentDate(){
        return FAQCommentDate;
    }

    public boolean getIsAnonymous(){
        return isAnonymous;
    }

    public String getFAQCommentContent(){
        return FAQCommentContent;
    }

    public int getFAQID(){
        return FAQID;
    }

    public int getClubID(){
        return clubID;
    }
}
