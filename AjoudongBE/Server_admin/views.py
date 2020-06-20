from django.shortcuts import render, redirect
from Server_app.models import ManagerAccount, Club, Ads
from django.views import generic

# Create your views here.
def login(request):
    nlist = [1, 3, 5, 7, 9, 8, 6, 4, 2]
    return render(request,'adminLogin.html', {'num1':2, 'hello': "안녕하세요", 'number': nlist})

def management(request):
    if request.method == "POST":
        aID = request.POST["id"]
        aPW = request.POST["pw"]
        manageraccounttable = ManagerAccount.objects.all()
        clublisttable = Club.objects.all()
        print(manageraccounttable)
        if aID == "hello":
            if aPW == "world":
                return render(request,'clubManagement.html',{'manageraccount':manageraccounttable,'clublisttable': clublisttable})
    else:
        return redirect('login')
    return redirect('login')

def advertisement(request):
    if request.method == "POST":
        adstable = Ads.objects.all()
        return render(request,'ajoudongAds.html',{'adstable':adstable})
    else:
        return render(request,'ajoudongAds.html')

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
        Club.objects.create(
            clubName = request.POST["clubname"],
            clubCategory = request.POST["clubcategory"],
            clubMajor = request.POST["clubmajor"],
            clubDues = 1,
        ).save()
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