from django.shortcuts import render, redirect
from Server_app.models import ManagerAccount, Club, Ads, UserAlarm
from django.views import generic
from fcm_django.models import FCMDevice
from .keys import *
# Create your views here.
def login(request):
    return render(request,'adminLogin.html')
    
def management(request):
    manageraccounttable = ManagerAccount.objects.all()
    clublisttable = Club.objects.all()
    if request.method == "POST":
        aID = request.POST["id"]
        aPW = request.POST["pw"]
        if aID == keys.id:
            if aPW == keys.pw:
                return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})
            else:
                return render(request, 'adminLogin.html')
        else:
            return render(request, 'adminLogin.html')
    return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})

def advertisement(request):
    adstable = Ads.objects.all()
    if request.method == "POST":
        return render(request,'ajoudongAds.html',{'adstable':adstable})
    return render(request,'ajoudongAds.html',{'adstable':adstable})

def addads(request):
    if request.method == "POST":
        Ads.objects.create(
        advertiserID = request.POST["advertiserID"],
        adsSpace = request.POST["adsSpace"],
        adsIMG = request.POST["adsIMG"],
        adsView = request.POST["adsViews"],
        ).save()
        adstable = Ads.objects.all()
        return render(request,'ajoudongAds.html',{'adstable':adstable})

def deleteads(request):
    if request.method == "POST":
        deleteadsid = request.POST["deleteadsid"]
        Ads.objects.filter(adsID = deleteadsid).delete()
        adstable = Ads.objects.all()
        return render(request,'ajoudongAds.html',{'adstable':adstable})

def updateads(request):
    if request.method == "POST":
        updateadsid = request.POST["updateadsid"]
        updateadsIMG = request.POST["updateadsIMG"]
        Ads.objects.filter(adsID = updateadsid).update(adsIMG = updateadsIMG)
        adstable = Ads.objects.all()
        return render(request,'ajoudongAds.html',{'adstable':adstable})

def addmanageraccount(request):
    if request.method == "POST":
        ManagerAccount.objects.create(
        mID = request.POST["mID"],
        mPW = request.POST["mPW"],
        clubName = request.POST["managerclubname"],
        clubID_id = request.POST["managerclubid"],
        ).save()
        manageraccounttable = ManagerAccount.objects.all()
        clublisttable = Club.objects.all()
        return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})

def deletemanageraccount(request):
    if request.method == "POST":
        mID = request.POST["managerdelete"]
        ManagerAccount.objects.filter(mID = mID).delete()
        manageraccounttable = ManagerAccount.objects.all()
        clublisttable = Club.objects.all()
        return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})
        
def addclub(request):
    if request.method == "POST":
        clubName = request.POST["clubname"]
        Club.objects.create(
            clubName = clubName,
            clubCategory = request.POST["clubcategory"],
            clubMajor = request.POST["clubmajor"],
            clubDues = 1,
        ).save()

        userAlarmQuery = UserAlarm.objects.filter(newclubAlarm = True)
        userAlarmIDList = []
        for uid in userAlarmQuery.values_list():
            userAlarmIDList.append(uid[1])

        device = FCMDevice.objects.filter(name__in=userAlarmIDList)

        title = "신규 동아리 등록!"
        message = clubName+ " 동아리 신규 등록! 새로운 동아리를 만나보세요."
        device.send_message(title=title, body=message, icon="ic_notification", data={"title": title, "message": message})

        manageraccounttable = ManagerAccount.objects.all()
        clublisttable = Club.objects.all()
        return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})

def clubdelete(request):
    if request.method == "POST":
        clubName = request.POST["deleteclubid"]
        Club.objects.filter(clubName = clubName).delete()
        manageraccounttable = ManagerAccount.objects.all()
        clublisttable = Club.objects.all()
        return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})