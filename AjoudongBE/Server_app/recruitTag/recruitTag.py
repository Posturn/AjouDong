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
from django.db.models import Q

from Server_app.models import TaggedClubList

class updateRecruitTags(View):
    @csrf_exempt
    def patch(self, request, clubID, mode):
        if mode == 1:
            TaggedClubList.objects.filter(clubID_id = clubID).filter(Q(clubTag_id="모집중") | Q(clubTag_id="모집종료")).update(clubTag_id = "모집중")
            return JsonResponse({'response' : 1}, status = 200)
        else:
            TaggedClubList.objects.filter(clubID_id = clubID).filter(Q(clubTag_id="모집중") | Q(clubTag_id="모집종료")).update(clubTag_id = "모집종료")
            return JsonResponse({'response' : 1}, status = 200)

