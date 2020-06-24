import json
import os
import sys
import requests
import ssl
import urllib
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from fcm_django.models import FCMDevice
from Server_app.models import UserAccount, ManagerAccount

class getIDbyToken(View):
    def get(self, request, token):
        try:
            if FCMDevice.objects.filter(registration_id=token).exists():
                device = FCMDevice.objects.get(registration_id=token)
                print(device.name)
                return JsonResponse({'response' : int(device.name)}, status = 200)
            else:
                return JsonResponse({'response' : -1}, status = 200)
        except KeyError:
            return JsonResponse({'response' : -2}, status = 400)

class getmID(View):
    def get(self, request, clubID):
        manager = ManagerAccount.objects.get(clubID_id = clubID)
        print(manager.mID)
        return JsonResponse({'response' : 1, 'message' : manager.mID}, status=200)