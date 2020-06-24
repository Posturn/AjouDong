package com.example.ajoudongfe;

public class StatisticObject {
    private int clubID;
    private int memberNumber;
    private int menNumber;
    private int womenNumber;
    private int overRatio12;
    private int Ratio13;
    private int Ratio14;
    private int Ratio15;
    private int Ratio16;
    private int Ratio17;
    private int Ratio18;
    private int Ratio19;
    private int Ratio20;
    private int engineeringRatio;
    private int ITRatio;
    private int naturalscienceRatio;
    private int managementRatio;
    private int humanitiesRatio;
    private int socialscienceRatio;
    private int nurseRatio;
    private int DasanRatio;
    private int MedicalRatio;
    private int PharmacyRatio;
    private int InternationalRatio;


    public StatisticObject(int clubID, int memberNumber, int menNumber, int womenNumber,
                           int overRatio12, int Ratio13, int Ratio14, int Ratio15, int Ratio16,
                           int Ratio17, int Ratio18, int Ratio19, int Ratio20, int engineeringRatio,
                           int ITRatio, int naturalscienceRatio, int managementRatio,
                           int humanitiesRatio, int socialscienceRatio ,int nurseRatio,
                           int DasanRatio, int MedicalRatio, int PharmacyRatio, int InternationalRatio){
        this.clubID=clubID;
        this.memberNumber=memberNumber;
        this.menNumber=menNumber;
        this.womenNumber=womenNumber;
        this.overRatio12=overRatio12;
        this.Ratio13=Ratio13;
        this.Ratio14=Ratio14;
        this.Ratio15=Ratio15;
        this.Ratio16=Ratio16;
        this.Ratio17=Ratio17;
        this.Ratio18=Ratio18;
        this.Ratio19=Ratio19;
        this.Ratio20=Ratio20;
        this.engineeringRatio=engineeringRatio;
        this.ITRatio=ITRatio;
        this.naturalscienceRatio=naturalscienceRatio;
        this.managementRatio=managementRatio;
        this.humanitiesRatio=humanitiesRatio;
        this.socialscienceRatio=socialscienceRatio;
        this.nurseRatio=nurseRatio;
        this.DasanRatio=DasanRatio;
        this.PharmacyRatio=PharmacyRatio;
        this.MedicalRatio=MedicalRatio;
        this.InternationalRatio=InternationalRatio;
    }

    public int getclubID(){
        return clubID;
    }
    public int getMemberNumber(){
        return memberNumber;
    }
    public int getMenNumber(){
        return menNumber;
    }
    public int getWomenNumber(){
        return womenNumber;
    }
    public int getOverRatio12(){
        return overRatio12;
    }
    public int getRatio13(){
        return Ratio13;
    }
    public int getRatio14(){
        return Ratio14;
    }
    public int getRatio15(){
        return Ratio15;
    }
    public int getRatio16(){
        return Ratio16;
    }
    public int getRatio17(){
        return Ratio17;
    }
    public int getRatio18(){
        return Ratio18;
    }
    public int getRatio19(){
        return Ratio19;
    }
    public int getEngineeringRatio(){
        return engineeringRatio;
    }
    public int getITRatio(){
        return ITRatio;
    }
    public int getNaturalscienceRatio(){
        return naturalscienceRatio;
    }
    public int getManagementRatio(){
        return managementRatio;
    }
    public int getHumanitiesRatio(){
        return humanitiesRatio;
    }
    public int getSocialscienceRatio(){
        return socialscienceRatio;
    }
    public int getNurseRatio(){
        return nurseRatio;
    }

    public int getClubID() {
        return clubID;
    }

    public int getRatio20() {
        return Ratio20;
    }

    public int getDasanRatio() {
        return DasanRatio;
    }

    public int getMedicalRatio() {
        return MedicalRatio;
    }

    public int getPharmacyRatio() {
        return PharmacyRatio;
    }

    public int getInternationalRatio() {
        return InternationalRatio;
    }
}
