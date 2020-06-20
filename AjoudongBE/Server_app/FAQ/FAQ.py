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

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club, FAQ, FAQComment, ManagerAccount

class postFAQ(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        try:
            clubID = data['clubID']
            userID = data['uID']
            FAQDate = data['datetime']
            isAnonymous = data['isAnonymous']
            FAQContent = data['FAQContent']

            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class postFAQComment(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        try:
            clubID = data['clubID']
            userID = data['uID']
            FAQCommentDate = data['datetime']
            isAnonymous = data['isAnonymous']
            FAQCommentContent = data['FAQCommentContent']

            return JsonResponse({'response' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class getFAQ(View):
    @csrf_exempt
    def get(self, request, clubID):
        try:
            queryset = FAQ.objects.filter(clubID_id=clubID).order_by('FAQDate')
            FAQList = list(queryset.values())
            print(FAQList)
            FAQList = []
            for i in FAQist:
                FAQInfo = {}
                User = UserAccount.objects.get(uSchoolID = i.userID)
                FAQInfo['userID'] = i.userID
                FAQInfo['uName'] = user.uName
                FAQInfo['FAQContent'] = i.FAQContent
                FAQInfo['FAQDate'] = i.FAQDate
                FAQInfo['isAnonymous'] = i.isAnonymous
                CommentsQueryset = FAQComment.objects.filter(FAQID_id=i.FAQID).order_by('FAQDate')
                comments = list(CommentsQueryset.values())
                commentList = []

                for comment in comments:
                    commentInfo = {}
                    if(comment.userID > 9999):
                        commentUser = UserAccount.objects.get(uSchoolID = comment.userID)
                        commentInfo['userID'] = comment.userID
                        commentInfo['uName'] = commentUser.uName
                    else:
                        commentUser = Club.objects.get(clubID = comment.userID)
                        commentInfo['userID'] = comment.clubID
                        commentInfo['uName'] = commentUser.clubID

                    commentInfo['FAQCommentContent'] = comment.FAQCommentContent
                    commentInfo['FAQCommentDate'] = comment.FAQCommentDate
                    commentInfo['isAnonymous'] = comment.isAnonymous
                    commentList.append(commentInfo)

                FAQInfo['FAQComments'] = commentList
                FAQList.append(FAQInfo)

            
            return JsonResponse({'response' : 1, content : FAQList}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)