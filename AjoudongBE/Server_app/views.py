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

from .models import UserAccount, ManagerAccount, Club, ClubPromotion, ClubActivity, Major_Affiliation
from Server_app.serializers import clubPromotionSerializer, clubActivitySerializer, MajorSerializer, ClubSerializer

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
        sort = self.kwargs['sort']
        if sort == 0:
            return self.queryset.order_by('?')
        elif sort == 1:
            return self.queryset.order_by('clubName')
        elif sort == 2:
            return self.queryset.order_by('-clubName')

    # def get_queryset_sort_and_search(self, request, sort, search):
    #     if sort == 0:
    #         return queryset.order_by('?')
    #     elif sort == 1:
    #         return queryset.order_by('clubName')
    #     elif sort == 2:
    #         return queryset.order_by('-clubName')

    # Model.objects.filter(title__in=['happy cake', 'happi kake'])