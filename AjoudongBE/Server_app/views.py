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

from .models import userAccount, managerAccount, club, clubPromotion, clubActivity
from Server_app.serializers import clubPromotionSerializer, clubActivitySerializer

class login(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        print(data)
        try:
            if userAccount.objects.filter(uID = data['uID'], uPW = data['uPW']).exists():
                
                return JsonResponse({'response' : 1}, status=200)
            elif managerAccount.objects.filter(mID = data['uID'], mPW = data['uPW']).exists():
                
                return JsonResponse({'response' : 2}, status=200)

            return JsonResponse({'response' : -1}, status = 400)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 400)

class signup(View):
    @csrf_exempt
    def post(self, request):
  
        data = json.loads(request.body)
        
        try:
            if userAccount.objects.filter(uID = data['uID']).exists():
                 return JsonResponse({'message' : 'Exists email'}, status=400)
            userAccount.objects.create(
                uID = data['uID'],
                uPW = data['uPW'],
                uName = data['uName'],
                uJender = data['uJender'],
                uSchoolID = data['uSchoolID'],
                uMajor = data['uMajor'],
                uPhoneNumber = data['uPhoneNumber'],
                uCollege = data['uCollege']
            ).save()

            return HttpResponse(status = 200)

        except KeyError:
            return JsonResponse({'message' : 'Invalid Keys'}, status = 400)

class clubActivityViewSet(viewsets.ModelViewSet):
    queryset = clubActivity.objects.all()
    serializer_class = clubActivitySerializer


class promotionViewSet(viewsets.ModelViewSet):
    queryset = clubPromotion.objects.all()
    serializer_class = clubPromotionSerializer
"""
class getClubPromotion(View):
    @csrf_exempt
    def get(self, request, club_id):
        promotion_list = [{
            'posterIMG': promotion_data.posterIMG,
            'clubInfo': promotion_data.clubInfo,
            'clubApply': promotion_data.clubApply,
            'clubFAQ': promotion_data.clubFAQ,
            'clubContact': promotion_data.clubContact
        }for promotion_data in clubPromotion.objects.all().filter(clubID = club_id)]

        return JsonResponse({'clubPromotion_list' : promotion_list}, status = 200)

class updateClubPromotion(View):
    @csrf_exempt
    def post(self, request, club_id):
  
        data = json.loads(request.body)
        
        try:
            if clubPromotion.objects.filter(promotionID = club_id).exists():
            clubPromotion.objects.all().(
                uID = data['uID'],
                uPW = data['uPW'],
                uName = data['uName'],
                uJender = data['uJender'],
                uSchoolID = data['uSchoolID'],
                uMajor = data['uMajor'],
                uPhoneNumber = data['uPhoneNumber'],
                uCollege = data['uCollege']
            ).save()

            return HttpResponse(status = 200)

        except KeyError:
            return JsonResponse({'message' : 'Invalid Keys'}, status = 400)

class getClubActivity(View):


class updateClubActivity(View):

class postClubActivity(View):


class deleteClubActivity(View):
"""