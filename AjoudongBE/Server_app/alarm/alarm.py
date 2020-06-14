import json
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import JsonResponse
from django.shortcuts import get_object_or_404

from django.core import serializers
# Create your views here.
from rest_framework import viewsets
from django.views import View
from Server_app.serializers import ClubAlarmSerializer

from rest_framework.response import Response

from Server_app.models import UserAlarm

class UserAlarmState(viewsets.ViewSet):
    def retrieve(self, request, uSchoolID):
        queryset = UserAlarm.objects.all()
        alarm = get_object_or_404(queryset, uSchoolID_id=uSchoolID)
        serializer = ClubAlarmSerializer(alarm)
        print(serializer.data)
        return Response(serializer.data)

class ChangeUserAlarm(View):
    @csrf_exempt
    def post(self, request, uSchoolID, alarmType):
        try:
            changeAlarm = UserAlarm.objects.get(uSchoolID_id = uSchoolID)
            if alarmType == 1:
                changeAlarm.stateAlarm = not changeAlarm.stateAlarm
            elif alarmType == 2:
                changeAlarm.eventAlarm = not changeAlarm.eventAlarm
            else:
                changeAlarm.newclubAlarm = not changeAlarm.newclubAlarm
            changeAlarm.save()
            return JsonResponse({'response' : 1}, status=200)
        except KeyError:
            return JsonResponse({'response' : -1}, status = 400)