import json
import os
import sys
import csv
import mimetypes
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club

class getMaskedID(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        try:
            uName = data['uName']
            uSchoolID = data['uSchoolID']
            
            if UserAccount.objects.filter(uSchoolID = uSchoolID).exists():
                user = UserAccount.objects.get(uSchoolID = uSchoolID)
                uID = user.uID
                uID = uID[:len(uID)-11]
                if(len(uID) < 5):
                    mask = '*'*(len(uID) - 2)
                    maskedID = uID[:1] + mask + uID[len(uID) - 1:]
                    return JsonResponse({'response' : 1, 'content' : maskedID}, status = 200)
                elif(len(uID) < 3):
                    return JsonResponse({'response' : 2, 'content' : "Too Short ID"}, status = 200)

                else:
                    mask = '*'*(len(uID) - 4)
                    maskedID = uID[:2] + mask + uID[len(uID) - 2:]
                    return JsonResponse({'response' : 1, 'content' : maskedID}, status = 200)

            else:#아이디가 없을때
                return JsonResponse({'response' : -1, 'content' : "No matching ID"}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 400)