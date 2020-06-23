import json
import os
import sys
import csv
import mimetypes
from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse, FileResponse
from django.views.decorators.csrf import csrf_exempt
import requests
import random

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import Apply, ClubMember, UserAccount, AppliedClubList, UserAlarm, Club

class getTempPW(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        try :
            uName = data['uName']
            uSchoolID = data['uSchoolID']
            uID = data['uID']

            if UserAccount.objects.filter(uSchoolID = uSchoolID, uID = uID, uName = uName).exists():
                _LENGTH = 10 # 10자리 
                string_pool = 'abcdefghijklmnpqrstuvwxyz0123456789' # 소문자 
                result = "" # 결과 값 
                for i in range(_LENGTH) : 
                    result += random.choice(string_pool) # 랜덤한 문자열 하나 선택 print(result)​

                print(data)
                print(result)
                user = UserAccount.objects.get(uSchoolID = uSchoolID)
                user.uPW = result
                user.save()
                body = {
                    "templateSid" : 1607,
                    "recipients" : 
                        [
                            {
                                "address": uID,
                                "name" : data['uName'],
                                "type" : "R",
                                "parameters" : 
                                {
                                    "who_signup" : data['uName'],
                                    "tempPW" : result
                                }
                            }
                        ]
                    }

                header = {
                    'Content-Type' : 'application/json;charset=UTF-8',
                    'x-ncp-apigw-timestamp' : data['timeStamp'],
                    'x-ncp-iam-access-key' : data['accessKey'],
                    'x-ncp-apigw-signature-v2' : data['encryptedKey'],
                    'x-ncp-lang' : 'ko-KR'
                }

                print(body)


                r = requests.post('https://mail.apigw.ntruss.com/api/v1/mails', headers=header, data=str(body).replace('\'', "\"").encode('utf-8'))

                print(r)
                print(r.text)
                return JsonResponse({'response' : 1}, status = 200)

            else:
                return JsonResponse({'response' : -1, 'message' : 'No matching ID'})

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)