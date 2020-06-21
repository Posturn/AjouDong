import json
import os
import sys
import csv
import mimetypes
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils import timezone
import datetime

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club, FAQ, FAQComment, ManagerAccount

class postFAQ(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        print(data)
        try:
            now = timezone.now()
            
            FAQ.objects.create(
                userID = data['uID'],
                clubID_id = data['clubID'],
                FAQDate = now,
                isAnonymous = data['isAnonymous'],
                FAQContent = data['FAQContent']
            ).save()

            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 400)

class postFAQComment(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        print(data)
        try:

            now = timezone.now()
            
            FAQComment.objects.create(
                userID = data['uID'],
                clubID_id = data['clubID'],
                FAQID_id = data['FAQID'],
                FAQCommentDate = now,
                isAnonymous = data['isAnonymous'],
                FAQCommentContent = data['FAQCommentContent']
            ).save()

            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 400)

class getFAQ(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            queryset = FAQ.objects.filter(clubID_id=clubID).order_by('FAQDate')
            FAQs = list(queryset.values())
            print(FAQs)
            FAQList = []
            for i in FAQs:
                print(i)
                FAQInfo = {}
                user = UserAccount.objects.get(uSchoolID = i['userID'])
                FAQInfo['userID'] = i['userID']
                FAQInfo['uName'] = user.uName
                FAQInfo['FAQContent'] = i['FAQContent']
                FAQInfo['FAQDate'] = i['FAQDate']
                FAQInfo['isAnonymous'] = i['isAnonymous']
                CommentsQueryset = FAQComment.objects.filter(FAQID_id=i['FAQID']).order_by('FAQCommentDate')
                comments = list(CommentsQueryset.values())
                commentList = []
                
                for comment in comments:
                    commentInfo = {}
                    if(comment['userID'] > 9999):
                        commentUser = UserAccount.objects.get(uSchoolID = comment['userID'])
                        commentInfo['userID'] = comment['userID']
                        commentInfo['uName'] = commentUser.uName
                    else:
                        commentUser = Club.objects.get(clubID = comment['userID'])
                        commentInfo['userID'] = comment['userID']
                        commentInfo['uName'] = commentUser.clubName

                    commentInfo['FAQCommentContent'] = comment['FAQCommentContent']
                    commentInfo['FAQCommentDate'] = comment['FAQCommentDate']
                    commentInfo['isAnonymous'] = comment['isAnonymous']
                    commentList.append(commentInfo)

                FAQInfo['FAQComments'] = commentList
                FAQList.append(FAQInfo)

            
            return JsonResponse({'response' : 1, 'content' : FAQList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)