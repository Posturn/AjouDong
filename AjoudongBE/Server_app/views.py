import json
import os
import sys
import datetime
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import get_object_or_404
from fcm_django.models import FCMDevice

from django.core import serializers
# Create your views here.
from rest_framework import viewsets, generics
from rest_framework.generics import ListAPIView
from .models import *
from Server_app.serializers import *

from rest_framework.response import Response


class adsViewset(viewsets.ModelViewSet):
    queryset = Ads.objects.all()
    serializer_class = AdsSerializer

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

    recruitmentTAG = ['모집중', '모집종료', '면접 없음', '면접 있음', '지원제한 없음']
    categoryTAG = ['레저', '종교', '사회', '창작전시', '학술', '과학기술', '체육', '연행예술', '준동아리', '음악', '예술', '기타']
    clubCultureTAG = ['공연','대회','정기전','공모전','OBYB','뒤풀이','스터디','친목']
    clubFeeTAG = ['5000원','10000원','15000원','20000원이상','최초 가입시 1회납부','없음']
    clubActivityDayTAG = ['월','화','수','목','금','토','일','상시활동']
    clubActivityTimeTAG = ['오전','오후','공강','야간','자율']

    recruitmentList =[]
    categoryList = []
    clubCultureList = []
    clubFeeList = []
    clubActivityDayList = []
    clubActivityTimeList = []
    
    filter_list = []
    for club in clubtaglist.values_list():
        filter_list.append(club[1])
        for tag in tags:
            if club[2] in recruitmentTAG and club[2] == tag:
                recruitmentList.append(club[1])
            elif club[2] in categoryTAG and club[2] == tag:
                categoryList.append(club[1])
            elif club[2] in clubCultureTAG and club[2] == tag:
                clubCultureList.append(club[1])
            elif club[2] in clubFeeTAG and club[2] == tag:
                clubFeeList.append(club[1])
            elif club[2] in clubActivityDayTAG and club[2] == tag:
                clubActivityDayList.append(club[1])
            elif club[2] in clubActivityTimeTAG and club[2] == tag:
                clubActivityTimeList.append(club[1])
            else:
                continue
            break
    
    filter_list = find_intersection(filter_list, recruitmentList)
    filter_list = find_intersection(filter_list, categoryList)
    filter_list = find_intersection(filter_list, clubCultureList)
    filter_list = find_intersection(filter_list, clubFeeList)
    filter_list = find_intersection(filter_list, clubActivityDayList)
    filter_list = find_intersection(filter_list, clubActivityTimeList)
    clubqueryset = clubqueryset.filter(clubID__in=filter_list)
    return clubqueryset

def find_intersection(list1, list2):
    if not list2:
        return list1
    return list(set(list1).intersection(set(list2)))

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

class DeleteClubMember(View):
    @csrf_exempt
    def delete(self, request, clubID, uSchoolID):
        ClubMember.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
        return HttpResponse(status = 200)

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
            clubID=data["clubID_id"]
            Apply.objects.create(
                clubID_id = clubID,
                uSchoolID_id = data["uSchoolID_id"],
                additionalApplyContent = data["additionalApplyContent"],
            ).save()

            
            AppliedClubList.objects.create(
                clubID_id = data["clubID_id"],
                uSchoolID_id = data["uSchoolID_id"],
                memberState = 0,
                applyDate = "2020.06.25"
            ).save()

            userApplytoClubAlarm(clubID)   

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

class UserFromDeviceViewset(viewsets.ModelViewSet):
    def retrieve(self, request, token):
        queryset = UserAccount.objects.all()
        user=get_object_or_404(queryset, uSchoolID=int(userFromDevice(token).name))
        serializer=UserAccountSerializer(user)
        return Response(serializer.data)
        
class IDFromDeviceViewset(viewsets.ModelViewSet):
    def retrieve(self, request, token):
        ID = userFromDevice(token).name
        return JsonResponse({'response' : 1, "message" : ID})
    
def userFromDevice(token):
    device = FCMDevice.objects.get(registration_id=token)
    return device
    
def userApplytoClubAlarm(clubID):
        queryset = ManagerAccount.objects.all()
        alarmOn = get_object_or_404(queryset, clubID_id=clubID)
        if alarmOn.newbieAlarm == False:
            return
        device = FCMDevice.objects.get(name=clubID)
    
        message = "동아리에 지원자가 도착했습니다!"
        device.send_message(title="지원자 알림", body=message, icon="ic_notification",  click_action="OPEN_MANAGER_MEMBER_MANAGEMENT_ACTIVITY", data={"pushed": "pushed", "Activity" : "OPEN_MANAGER_MEMBER_MANAGEMENT_ACTIVITY"})


class QnAViewset(viewsets.ModelViewSet):
    queryset = FAQ.objects.all()
    serializer_class=FAQSerializer

    def get_queryset(self):
        filterclubID = self.kwargs['clubID']
        self.queryset = self.queryset.filter(clubID = filterclubID)
        return self.queryset.order_by('-FAQID')

class CommentViewset(viewsets.ModelViewSet):
    queryset = FAQComment.objects.all()
    serializer_class=FAQCommentSerializer

    def get_queryset(self):
        filterfaqID = self.kwargs['FAQID']
        self.queryset = self.queryset.filter(FAQID_id = filterfaqID)
        return self.queryset.order_by('FAQCommentID')

class ManagerInfoViewSet(viewsets.ViewSet):
    def retrieve(self, request, clubID):
        queryset = ManagerAccount.objects.all()
        user = get_object_or_404(queryset, clubID_id=clubID)
        serializer = ManagerAccountSerializer(user)
        print(serializer.data)
        return Response(serializer.data)

