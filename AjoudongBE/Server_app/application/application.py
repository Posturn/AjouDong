import json
import os
import sys
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, Club, ClubPromotion

class getApplicationResult(View):
    @csrf_exempt
    def get(self, request, uSchoolID):
        try:
            queryset = AppliedClubList.objects.filter(uSchoolID_id=uSchoolID)
            clubList = list(queryset.values())
            applicationList = []
            for i in clubList:
                application = {}
                club = Club.objects.get(clubID = list(i.values())[1])
                promotion = ClubPromotion.objects.get(clubID = list(i.values())[1])
                user = UserAccount.objects.get(uSchoolID = list(i.values())[2])
                application['clubID'] = list(i.values())[1]
                application['clubName'] = Club.objects.get(clubID = list(i.values())[1]).clubName
                application['clubContact'] = promotion.clubContact
                application['uSchoolID'] = list(i.values())[2]
                application['uName'] = user.uName
                application['uMajor'] = user.uMajor
                application['uPhoneNumber'] = user.uPhoneNumber
                application['uGender'] = user.uJender
                application['clubIMG'] = Club.objects.get(clubID = list(i.values())[1]).clubIMG
                application['status'] = AppliedClubList.objects.get(clubID_id = list(i.values())[1], uSchoolID_id = uSchoolID).memberState
                # date = AppliedClubList.objects.get(clubID_id = list(i.values())[1], uSchoolID_id = uSchoolID).applyDate
                # date_str = date.strftime("%Y.%m.%d")
                application['applyDate'] = AppliedClubList.objects.get(clubID_id = list(i.values())[1], uSchoolID_id = uSchoolID).applyDate
                applicationList.append(application)

            return JsonResponse({'response' : 1, 'content' : applicationList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class deleteApplication(View):
    @csrf_exempt
    def post(self, request, uSchoolID, clubID):
        try:
           
            AppliedClubList.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
            Apply.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
        

            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)


