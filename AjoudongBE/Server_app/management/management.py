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

from Server_app.models import Apply, ClubMember, UserAccount

class memberlist(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            queryset = ClubMember.objects.filter(clubID_id=clubID)
            memberList = list(queryset.values())
            print(memberList)
            memberInfoList = []
            for i in memberList:
                memberInfo = {}
                member = UserAccount.objects.get(uSchoolID = list(i.values())[2])
                memberInfo['uMajor'] = member.uMajor
                memberInfo['uSchoolID'] = member.uSchoolID
                memberInfo['uName'] = member.uName
                appliedUserInfo['uIMG'] = member.uIMG
                memberInfoList.append(memberInfo)

            return JsonResponse({'response' : 1, 'content' : memberInfoList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

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
    def post(self, request):
        data = json.loads(request)
        try:
            applieduserlist.objects.filter(clubID_id = data['clubId'], uSchoolId_id = data['uSchoolID']).delete()

            return JsonResponse({'reponse' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class newAppliedUser(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request)
        try:
            ClubMember.objects.create(
                uSchoolID_id = data['uSchoolID'],
                clubID_id = data['clubID']
            ).save
            applieduserlist.objects.filter(clubID_id = data['clubId'], uSchoolId_id = data['uSchoolID']).delete()

            return JsonResponse({'reponse' : 1}, status = 200)
        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

        
