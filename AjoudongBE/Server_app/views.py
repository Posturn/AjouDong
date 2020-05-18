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


from .models import userAccount, managerAccount, club, clubPromotion, clubActivity, major_Affiliation
from Server_app.serializers import clubPromotionSerializer, clubActivitySerializer, MajorSerializer, ClubSerializer
from .models import 
from .models import 


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
    
class MajorViewSet(viewsets.ModelViewSet):
    queryset=major_Affiliation.objects.all()
    serializer_class=MajorSerializer

class ClubViewSet(viewsets.ModelViewSet):
    queryset=club.objects.all()
    serializer_class=ClubSerializer
