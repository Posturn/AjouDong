import json
from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import get_object_or_404

from django.core import serializers
# Create your views here.
from rest_framework import viewsets, generics
from django.views import View
from Server_app.serializers import *

from rest_framework.response import Response

from Server_app.models import UserAlarm

class ChangeStateAlarm(View):
    @csrf_exempt
    def post(self, request, clubID, uSchoolID):
        try:
            AppliedUser = applieduserlist.objects.get(clubID_id = clubID, uSchoolID_id = uSchoolID)
            AppliedUser.memberState = -1
            AppliedUser.save()

            Apply.objects.filter(clubID_id = clubID, uSchoolID_id = uSchoolID).delete()
            applicationStateChange(clubID,uSchoolID,False)
            return JsonResponse({'reponse' : 1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class ChangeEventAlarm(View):
    pass

class ChangeNewClubAlarm(View):
    pass