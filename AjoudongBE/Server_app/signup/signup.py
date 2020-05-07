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

from Server_app.models import userAccount, managerAccount
class signup(View):
    @csrf_exempt
    def post(self, request):
  
        data = json.loads(request.body)
        
        try:
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
            return JsonResponse({'response' : -1}, status = 400)

class checkSameID(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)

        try:
            if userAccount.objects.filter(uID = data['uID']).exists():
                 return JsonResponse({'response' : 0}, status=400)
            return JsonResponse({'response' : 1}, status=200)

        except KeyError:
            return JsonResponse({'response' : -1}, status = 400)
            

