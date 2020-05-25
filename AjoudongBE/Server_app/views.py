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

from .models import UserAccount, ManagerAccount, Club, ClubPromotion, ClubActivity, Major_Affiliation, MarkedClubList
from Server_app.serializers import clubPromotionSerializer, clubActivitySerializer, MajorSerializer, ClubSerializer, BookmarkSerializer

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
    queryset = ClubActivity.objects.all()
    serializer_class = clubActivitySerializer

class promotionViewSet(viewsets.ModelViewSet):
    queryset = ClubPromotion.objects.all()
    serializer_class = clubPromotionSerializer
    
class MajorViewSet(viewsets.ModelViewSet):
    queryset= Major_Affiliation.objects.all()
    serializer_class=MajorSerializer

class ClubsViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class=ClubSerializer

class ClubViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    def get_queryset(self):
        club = self.kwargs.get('club')
        sort = self.kwargs.get('sort')
        if club == 13:
            self.queryset = self.queryset.filter(clubMajor__range=(2,12))
        else:
            self.queryset = self.queryset.filter(clubMajor=club)

        if sort == 0:
            return self.queryset.order_by('?')
        elif sort == 1:
            return self.queryset.order_by('clubName')
        elif sort == 2:
            return self.queryset.order_by('-clubName')

class ClubSearchViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    def get_queryset(self):
        club = self.kwargs.get('club')
        sort = self.kwargs.get('sort')
        search = self.kwargs.get('search')
        if club == 13:
            self.queryset = self.queryset.filter(clubMajor__range=(2,12))
        else:
            self.queryset = self.queryset.filter(clubMajor=club)

        self.queryset = self.queryset.filter(clubName__icontains=search)
        if sort == 0:
            return self.queryset.order_by('?')
        elif sort == 1:
            return self.queryset.order_by('clubName')
        elif sort == 2:
            return self.queryset.order_by('-clubName')

    
class BookmarkSearchViewSet(viewsets.ModelViewSet):
    queryset = MarkedClubList.objects.all()
    serializer_class=BookmarkSerializer

    #def get_queryset(self):
    #    schoolID=self.kwargs.get('schoolID')
    #    self.queryset=self.queryset.filter(uSchoolID_id=schoolID)

class PostBookmark(View):
    @csrf_exempt
    def post(self, request):
  
        data = json.loads(request.body)
        
        try:
            MarkedClubList.objects.create(
                clubID_id = data['clubID'],
                uSchoolID_id = data['uSchoolID']
            ).save()

            return HttpResponse(status = 200)

        except KeyError:
            return JsonResponse({'message' : 'Invalid Keys'}, status = 400)

class DeleteBookmark(View):
    @csrf_exempt
    def delete(self, request):

        clubID_id = self.kwargs.get('clubID')
        uSchoolID_id = self.kwargs.get('schoolID')
        
        MarkedClubList.objects.filter(clubID_id = clubID_id,
                uSchoolID_id = uSchoolID_id).delete()
        return HttpResponse(status = 200)

