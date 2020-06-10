import json
import os
import sys
import datetime
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import get_object_or_404

from django.core import serializers
# Create your views here.
from rest_framework import viewsets, generics
from rest_framework.generics import ListAPIView
from .models import *
from Server_app.serializers import *

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

from rest_framework.response import Response

class userAccountViewset(viewsets.ModelViewSet):
    queryset = UserAccount.objects.all()
    serializer_class = UserAccountSerializer

class managerAccountViewset(viewsets.ModelViewSet):
    queryset = ManagerAccount.objects.all()
    serializer_class = ManagerAccountSerializer

class clubActivityViewSet(viewsets.ModelViewSet):
    queryset = ClubActivity.objects.all()
    serializer_class = clubActivitySerializer

    def get_queryset(self):
        gridclubID = self.kwargs['clubID']
        self.queryset = self.queryset.filter(clubID = gridclubID)
        return self.queryset.order_by('-clubActivityID')

class clubActivityDetailViewSet(viewsets.ModelViewSet):
    queryset = ClubActivity.objects.all()
    serializer_class = clubActivitySerializer

class promotionViewSet(viewsets.ModelViewSet):
    queryset = ClubPromotion.objects.all()
    serializer_class = clubPromotionSerializer
    
class MajorViewSet(viewsets.ModelViewSet):
    queryset= Major_Affiliation.objects.all()
    serializer_class=MajorSerializer

class UserInfoViewSet(viewsets.ViewSet):
    def retrieve(self, request, pk=None):
        queryset = UserAccount.objects.all()
        user = get_object_or_404(queryset, pk=pk)
        serializer = UserInfoSerializer(user)
        print(serializer.data)
        return Response(serializer.data)

class ClubQuestionViewSet(viewsets.ViewSet):
    def retrieve(self, request, pk=None):
        queryset = ClubPromotion.objects.all()
        question = get_object_or_404(queryset, pk=pk)
        serializer = ClubQuestionSerializer(question)
        return Response(serializer.data)


class ClubsViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class=ClubSerializer

class ClubViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    def get_queryset(self):
        club = self.kwargs.get('club')
        sort = self.kwargs.get('sort')
        category = self.kwargs.get('category')
        self.queryset = filter_club(club, self.queryset)
        self.queryset = filter_category(category, self.queryset)
        return sort_clublist(sort, self.queryset)

class ClubViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    def get_queryset(self):
        club = self.kwargs.get('club')
        sort = self.kwargs.get('sort')
        category = self.kwargs.get('category')
        self.queryset = filter_club(club, self.queryset)
        self.queryset = filter_category(category, self.queryset)
        return sort_clublist(sort, self.queryset)

class ClubSearchViewSet(viewsets.ModelViewSet):
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    def get_queryset(self):
        club = self.kwargs.get('club')
        sort = self.kwargs.get('sort')
        search = self.kwargs.get('search')
        category = self.kwargs.get('category')
        
        self.queryset = filter_club(club, self.queryset)
        self.queryset = filter_category(category, self.queryset)
        self.queryset = self.queryset.filter(clubName__icontains=search)
        queryset_serialized = serializers.serialize('json', sort_clublist(sort, self.queryset))
        return sort_clublist(sort, self.queryset)

class ClubFilter(generics.GenericAPIView):
    queryset = Club.objects.all()
    serializer_class = ClubSerializer

    @csrf_exempt
    def post(self, request, *args, **kwargs):
        tags = request.data["tags"]
        club = request.data["club"]
        sort = request.data["sort"]
        self.queryset = filter_taglist(tags, self.queryset)
        self.queryset = filter_club(club, self.queryset)
        queryset_serialized = self.serializer_class(sort_clublist(sort, self.queryset),many=True)
        return Response(queryset_serialized.data)


def sort_clublist(sort, queryset):
    if sort == 0:
            return queryset.order_by('?')
    elif sort == 1:
        return queryset.order_by('clubName')
    elif sort == 2:
        return queryset.order_by('-clubName')

def filter_category(category, queryset):
    if category == "전체":
        return queryset
    else:
        return queryset.filter(clubCategory__icontains=category)

def filter_club(club, queryset):
    if club == 13:
        return queryset.filter(clubMajor__range=(2,12))
    else:
        return queryset.filter(clubMajor=club)

def filter_taglist(tags, queryset):
    clubqueryset = Club.objects.all()
    clubtaglist = TaggedClubList.objects.all()
    filter_list = []
    clubID_list = []
    category = ['레저', '종교', '사회', '창작전시', '학술', '과학기술', '체육', '연행예술', '준동아리', '음악', '예술', '기타']
    clubID_category = []
    print('사용자가 고른것')
    print(tags)
    for club in clubtaglist.values_list():
        for tag in tags:
            if club[2] in category and club[2] == tag:
                clubID_category.append(club[1])
            elif club[2] == tag:
                clubID_list.append(club[1])
                break

    if not clubID_category: # 카테고리를 고르지 않음
        filter_list = clubID_list
    elif not clubID_list: # 카테고리만 고름
        filter_list = clubID_category
    else: # 카테고리와 태그 둘다 고름
        for cid in clubID_list:
            if cid in clubID_category:
                filter_list.append(cid)
    clubqueryset = clubqueryset.filter(clubID__in=filter_list)
    return clubqueryset

class TagViewSet(viewsets.ModelViewSet):
    queryset = Tag.objects.all()
    serializer_class=TagSerializer

class ManagerFilterViewset(viewsets.ModelViewSet):
    queryset = TaggedClubList.objects.all()
    serializer_class=ManagerFilterSerializer

    def get_queryset(self):
        filterclubID = self.kwargs['clubID']
        self.queryset = self.queryset.filter(clubID = filterclubID)
        return self.queryset

class ClubMemberViewset(viewsets.ModelViewSet):
    queryset = ClubMember.objects.all()
    serializer_class=ClubMemberSerializer

    def get_queryset(self):
        filteruSchoolID = self.kwargs['uSchoolID']
        self.queryset = self.queryset.filter(uSchoolID = filteruSchoolID)
        return self.queryset

class PostFilter(View):
    @csrf_exempt
    def post(self, request):
  
        data = json.loads(request.body)
        
        try:
            for tag in data['tags']:
                TaggedClubList.objects.create(
                clubID_id = data['clubID'],
                clubTag_id = tag
                ).save()

            return HttpResponse(status = 200)

        except KeyError:
            return JsonResponse({'message' : 'Invalid Keys'}, status = 400)

class DeleteFilter(View):
    @csrf_exempt
    def delete(self, request, clubID):

        clubID_id = clubID
        
        TaggedClubList.objects.filter(clubID_id = clubID_id).delete()
        return HttpResponse(status = 200)

    
class BookmarkSearchViewSet(viewsets.ModelViewSet):
    queryset = MarkedClubList.objects.all()
    serializer_class=BookmarkSerializer

    def get_queryset(self):
        schoolID=self.kwargs.get('schoolID')
        self.queryset=self.queryset.filter(uSchoolID=schoolID)

        return self.queryset

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
    def post(self, request, clubID, schoolID):

        clubID_id = clubID
        uSchoolID_id = schoolID
        
        MarkedClubList.objects.filter(clubID_id = clubID_id,
                uSchoolID_id = uSchoolID_id).delete()
        return HttpResponse(status = 200)


class UserClubApply(View):
    @csrf_exempt
    def post(self, request):

        data = json.loads(request.body)

        try:
            print(data["clubID_id"])
            print(data["uSchoolID_id"])
            print(data["additionalApplyContent"])
            print(type(data["clubID_id"]))
            print(type(data["uSchoolID_id"]))
            print(type(data["additionalApplyContent"]))
            Apply.objects.create(
                clubID_id = data["clubID_id"],
                uSchoolID_id = data["uSchoolID_id"],
                additionalApplyContent = data["additionalApplyContent"],
            ).save()
            
            AppliedClubList.objects.create(
                clubID_id = data["clubID_id"],
                uSchoolID_id = data["uSchoolID_id"],
                memberState = 0,
                applyDate = "2020.06.02"
            ).save()

            return JsonResponse({'response' : 1}, status=200)
        except KeyError:
            return JsonResponse({'response' : -1}, status = 400)

class ClubStatisticsViewSet(viewsets.ViewSet):
    def retrieve(self, request, clubID):
        queryset=ClubStatistic.objects.all()
        statistic=get_object_or_404(queryset, clubID_id=clubID)
        serializer=ClubStatisticSerializer(statistic)
        return Response(serializer.data)


class StatisticsViewSet(viewsets.ModelViewSet):
    queryset = ClubStatistic.objects.all()
    serializer_class=ClubStatisticSerializer

class NRecruitViewSet(viewsets.ViewSet):

    def list(self, request):
        tagclublist = TaggedClubList.objects.all()
        nlist = []
        for tagData in tagclublist.values_list():
            if tagData[2] == "모집종료":
                nlist.append(tagData[1])
        return Response(nlist)

class EventListViewset(viewsets.ModelViewSet):
    queryset = Event.objects.all()
    serializer_class=EventSerializer

    def get_queryset(self):
        filterclubID = self.kwargs['clubID']
        self.queryset = self.queryset.filter(clubID = filterclubID)
        return self.queryset.order_by('-eventID')

class EventViewset(viewsets.ModelViewSet):
    queryset = Event.objects.all()
    serializer_class=EventSerializer
