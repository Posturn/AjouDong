import json
import os
import sys
import csv
import mimetypes
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club, ClubStatistic

from fcm_django.models import FCMDevice
from django.shortcuts import get_object_or_404

class memberlist(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            queryset = ClubMember.objects.filter(clubID_id=clubID).order_by('-id')
            memberList = list(queryset.values())
            print(memberList)
            memberInfoList = []
            for i in memberList:
                memberInfo = {}
                member = UserAccount.objects.get(uSchoolID = list(i.values())[2])
                memberInfo['uMajor'] = member.uMajor
                memberInfo['uSchoolID'] = member.uSchoolID
                memberInfo['uName'] = member.uName
                memberInfo['uIMG'] = member.uIMG
                memberInfoList.append(memberInfo)

            return JsonResponse({'response' : 1, 'content' : memberInfoList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)
        refreshStatistic(clubID)
        return JsonResponse({'response' : 1}, status = 200)

class applieduserlist(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            queryset = Apply.objects.filter(clubID_id=clubID)
            appliedUserList = list(queryset.values())
            appliedUserInfoList = []
            for i in appliedUserList:
                appliedUserInfo = {}
                appliedUser = UserAccount.objects.get(uSchoolID = list(i.values())[2])
                appliedUserInfo['uMajor'] = appliedUser.uMajor
                appliedUserInfo['uSchoolID'] = appliedUser.uSchoolID
                appliedUserInfo['uName'] = appliedUser.uName
                appliedUserInfo['additionalApplyContent'] = list(i.values())[3]
                appliedUserInfo['uIMG'] = appliedUser.uIMG
                appliedUserInfoList.append(appliedUserInfo)

            return JsonResponse({'response' : 1, 'content' : appliedUserInfoList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class newmember(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        try:
            ClubMember.objects.create(
                clubID_id = data['clubID'],
                uSchoolID_id = data['uSchoolID']
            ).save()
            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class deletemember(View):
    @csrf_exempt
    def post(self, request, clubID, uSchoolID):
        try :
            ClubMember.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
            return JsonResponse({'response' : 1}, status = 200)
        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class deleteAppliedUser(View):
    @csrf_exempt
    def post(self, request, clubID, uSchoolID):
        try:
            AppliedUser = AppliedClubList.objects.get(clubID_id = clubID, uSchoolID_id = uSchoolID)
            AppliedUser.memberState = -1
            AppliedUser.save()

            Apply.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
            applicationStateChange(clubID,uSchoolID,False)
            return JsonResponse({'reponse' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class newAppliedUser(View):
    @csrf_exempt
    def post(self, request, clubID, uSchoolID):
        try:
            ClubMember.objects.create(
                uSchoolID_id = uSchoolID,
                clubID_id = clubID
            ).save
            AppliedUser = AppliedClubList.objects.get(clubID_id = clubID, uSchoolID_id = uSchoolID)
            AppliedUser.memberState = 1
            AppliedUser.save()
            Apply.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
            applicationStateChange(clubID,uSchoolID,True)
            return JsonResponse({'reponse' : 1}, status = 200)
        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class csvupload(View):
    @csrf_exempt
    def post(self, request, clubID):
        
        print(type(request.FILES['members.csv']))

        uploadFile = request.FILES['members.csv']

        f = uploadFile.read().decode('utf-8-sig').splitlines()
        rdr = csv.reader(f) 
        lines = []
        for line in rdr:
            lines.append(line)
            
        lines.pop(0)
        for line in lines:
            if line != []:
                print("이름 : " + line[0])
                print("학번 : " + line[1])
                print("단과대 : " + line[2])
                print("학과 : " + line[3])
                ClubMember.objects.create(
                    clubID_id = clubID,
                    uSchoolID_id = line[1]
                ).save

        return JsonResponse({'reponse' : 1}, status = 200)


class appliedUserCSV(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            file_name = 'appliedUsers.csv'
            f = open('appliedUsers.csv', 'w', encoding='utf-8-sig', newline='')
            wr = csv.writer(f)
            wr.writerow(['이름','학번','단과대','학과', '전화번호'])
            queryset = Apply.objects.filter(clubID_id=clubID)
            appliedUserList = list(queryset.values())
            appliedUserInfoList = []
            for i in appliedUserList:
                appliedUser = UserAccount.objects.get(uSchoolID = list(i.values())[2])
                phoneNumber = "010" + str(appliedUser.uPhoneNumber)
                wr.writerow([appliedUser.uName, appliedUser.uSchoolID, appliedUser.uCollege, appliedUser.uMajor, phoneNumber])

            f.close()

            response = HttpResponse(open('appliedUsers.csv', 'rb'), content_type = 'text/csv')
            download_file_name = 'appliedUserList.csv'
            response['Content-Disposition'] = 'attachment; filename=' + download_file_name#한글 적용 유무 개선
            return response
        
        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class memberCSV(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            file_name = 'members.csv'
            f = open(file_name, 'w', encoding='utf-8-sig', newline='')
            wr = csv.writer(f)
            wr.writerow(['이름','학번','단과대','학과', '전화번호'])
            queryset = ClubMember.objects.filter(clubID_id=clubID)
            memberList = list(queryset.values())
            for i in memberList:
                member = UserAccount.objects.get(uSchoolID = list(i.values())[2])
                pnumber = str(member.uPhoneNumber)
                pfirst = pnumber[:4]
                psecond = pnumber[4:]
                phoneNumber = "010-" + pfirst +"-" + psecond

                wr.writerow([member.uName, member.uSchoolID, member.uCollege, member.uMajor, phoneNumber])

            f.close()

            response = HttpResponse(open('members.csv', 'rb'), content_type = 'text/csv')
            download_file_name = 'members.csv'
            response['Content-Disposition'] = 'attachment; filename=' + download_file_name#한글 적용 유무 개선
            return response
        
        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)


def applicationStateChange(clubID, uSchoolID, applyResult):
    queryset = Club.objects.all()
    club = get_object_or_404(queryset, clubID=clubID)

    queryset = UserAlarm.objects.all()
    alarmOn = get_object_or_404(queryset, uSchoolID_id=uSchoolID)
    if alarmOn.stateAlarm == False:
        return
    device = FCMDevice.objects.get(name=uSchoolID)
    
    message = str(club.clubName) + " 동아리 지원이 승인되었습니다."
    if applyResult == False:
        message = str(club.clubName) + " 동아리 지원이 거절되었습니다."
    print(message)
    device.send_message(title="동아리 지원 결과 업데이트!", body=message, icon="ic_notification",click_action="OPEN_USER_APPLY_RESULT_ACTIVITY", data={"pushed": "pushed", "message": message})

def refreshStatistic(clubID):
    queryset = ClubMember.objects.filter(clubID_id=clubID)
    memberList = list(queryset.values())
    # print(memberList)
    memberNumber = 0
    menNumber = 0
    womenNumber = 0
    overRatio12 = 0
    Ratio13 = 0
    Ratio14 = 0
    Ratio15 = 0
    Ratio16 = 0
    Ratio17 = 0
    Ratio18 = 0
    Ratio19 = 0
    Ratio20 = 0
    engineeringRatio = 0
    ITRatio = 0
    naturalscienceRatio = 0
    managementRatio = 0
    humanitiesRatio = 0
    socialscienceRatio = 0
    nurseRatio = 0
    InternationalRatio = 0#국제학부
    DasanRatio = 0#다산학부
    PharmacyRatio = 0#약대
    MedicalRatio = 0#의대
    clubStatistic = ClubStatistic.objects.get(clubID_id = clubID)
    for member in memberList:
        memberNumber = memberNumber + 1
        # print(member['uSchoolID_id'])
        User = UserAccount.objects.get(uSchoolID = member['uSchoolID_id'])
        #사용자 학번 체크
        UserRatio = User.uSchoolID / 100000
        if UserRatio < 2013 : overRatio12 = overRatio12 + 1
        elif UserRatio > 2013 and UserRatio < 2014 : Ratio13 = Ratio13 + 1
        elif UserRatio > 2014 and UserRatio < 2015 : Ratio14 = Ratio14 + 1
        elif UserRatio > 2015 and UserRatio < 2016 : Ratio15 = Ratio15 + 1
        elif UserRatio > 2016 and UserRatio < 2017 : Ratio16 = Ratio16 + 1
        elif UserRatio > 2017 and UserRatio < 2018 : Ratio17 = Ratio17 + 1
        elif UserRatio > 2018 and UserRatio < 2019 : Ratio18 = Ratio18 + 1
        elif UserRatio > 2019 and UserRatio < 2020 : Ratio19 = Ratio19 + 1
        elif UserRatio > 2020 : Ratio20 = Ratio20 + 1
        else : print("Not ratio")
        #사용자 성별체크
        if(User.uJender == 0 ) : menNumber = menNumber + 1
        else : womenNumber = womenNumber + 1
        #사용자 단과대 체크
        if(User.uCollege == '공과대학') : engineeringRatio = engineeringRatio + 1
        elif(User.uCollege == '정보통신대학') : ITRatio = ITRatio + 1
        elif(User.uCollege == '자연과학대학') : naturalscienceRatio = naturalscienceRatio + 1
        elif(User.uCollege == '경영대학') : managementRatio = managementRatio + 1
        elif(User.uCollege == '인문대학') : humanitiesRatio = humanitiesRatio + 1
        elif(User.uCollege == '사회과학대학') : socialscienceRatio = socialscienceRatio + 1
        elif(User.uCollege == '간호대학') : nurseRatio = nurseRatio + 1
        elif(User.uCollege == '다산학부대학') : DasanRatio = DasanRatio + 1
        elif(User.uCollege == '의과대학') : MedicalRatio = MedicalRatio + 1
        elif(User.uCollege == '약학대학') : PharmacyRatio = PharmacyRatio + 1
        elif(User.uCollege == '국제학부') : InternationalRatio = InternationalRatio + 1
        else : print("Not college")
    
    print("전체 : " + str(memberNumber))
    clubStatistic.memberNumber = memberNumber
    print("남성 : " + str(menNumber))
    clubStatistic.menNumber = menNumber
    print("여성 : " + str(womenNumber))
    clubStatistic.womenNumber = womenNumber
    print("12학번 : " + str(overRatio12))
    clubStatistic.overRatio12 = overRatio12
    print("13학번 : " + str(Ratio13))
    clubStatistic.Ratio13 = Ratio13
    print("14학번 : " + str(Ratio14))
    clubStatistic.Ratio14 = Ratio14
    print("15학번 : " + str(Ratio15))
    clubStatistic.Ratio15 = Ratio15
    print("16학번 : " + str(Ratio16))
    clubStatistic.Ratio16 = Ratio16
    print("17학번 : " + str(Ratio17))
    clubStatistic.Ratio17 = Ratio17
    print("18학번 : " + str(Ratio18))
    clubStatistic.Ratio18 = Ratio18
    print("19학번 : " + str(Ratio19))
    clubStatistic.Ratio19 = Ratio19
    print("20학번 : " + str(Ratio20))
    clubStatistic.Ratio20 = Ratio20
    print("공대 : " + str(engineeringRatio))
    clubStatistic.engineeringRatio = engineeringRatio
    print("정통대 : " + str(ITRatio))
    clubStatistic.ITRatio = ITRatio
    print("자연대 : " + str(naturalscienceRatio))
    clubStatistic.naturalscienceRatio = naturalscienceRatio
    print("경영대 : " + str(managementRatio))
    clubStatistic.managementRatio = managementRatio
    print("인문대 : " + str(humanitiesRatio))
    clubStatistic.humanitiesRatio = humanitiesRatio
    print("사회대 : " + str(socialscienceRatio))
    clubStatistic.socialscienceRatio = socialscienceRatio
    print("간호대 : " + str(nurseRatio))
    clubStatistic.nurseRatio = nurseRatio
    print("다산학부 : " + str(DasanRatio))
    clubStatistic.DasanRatio = DasanRatio
    print("약학대학 : " + str(PharmacyRatio))
    clubStatistic.PharmacyRatio = PharmacyRatio
    print("의과대학 : " + str(MedicalRatio))
    clubStatistic.MedicalRatio = MedicalRatio
    print("국제학부 : " + str(InternationalRatio))
    clubStatistic.InternationalRatio = InternationalRatio
    clubStatistic.save()
        
