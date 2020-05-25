import json
import os
import sys
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt

from django.core import serializers
# Create your views here.
from rest_framework import viewsets, generics
from rest_framework.generics import ListAPIView

from .models import UserAccount, ManagerAccount, Club, ClubPromotion, ClubActivity, Major_Affiliation
from Server_app.serializers import clubPromotionSerializer, clubActivitySerializer, MajorSerializer, ClubSerializer

from rest_framework.response import Response

class clubActivityViewSet(viewsets.ModelViewSet):
    queryset = ClubActivity.objects.all()
    serializer_class = clubActivitySerializer

class promotionViewSet(viewsets.ModelViewSet):
    queryset = ClubPromotion.objects.all()
    serializer_class = clubPromotionSerializer
    
class MajorViewSet(viewsets.ModelViewSet):
    queryset= Major_Affiliation.objects.all()
    serializer_class=MajorSerializer

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
        print(tags)

        self.queryset = filter_club(club, self.queryset)
        queryset_serialized = self.serializer_class(sort_clublist(1, self.queryset),many=True)
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
    # id_list = []
    # for tag in tags:
    return queryset