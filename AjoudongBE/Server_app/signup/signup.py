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

from Server_app.models import UserAccount, ManagerAccount
class signup(View):
    @csrf_exempt
    def post(self, request):
  
        data = json.loads(request.body)
        
        try:
            UserAccount.objects.create(
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
            if UserAccount.objects.filter(uID = data['checkedID']).exists():
                return JsonResponse({'response' : -1}, status=200)
            return JsonResponse({'response' : 1}, status=200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

class emailVerify(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)

        try :
            print(data)

            body = {
	            "templateSid" : 1419,
	            "recipients" : 
	                [
		                {
			                "address": data['address'],
			                "name" : data['name'],
			                "type" : "R",
			                "parameters" : 
			                {
    				            "who_signup" : data['who_signup'],
				                "verify_code" : data['verify_code']
			                }
		                }
                    ]
                }

            header = {
                'Content-Type' : 'application/json',
                'x-ncp-apigw-timestamp' : data['timeStamp'],
                'x-ncp-iam-access-key' : data['accessKey'],
                'x-ncp-apigw-signature-v2' : data['encryptedKey'],
                'x-ncp-lang' : 'ko-KR'
            }

            print(body)


            r = requests.post('https://mail.apigw.ntruss.com/api/v1/mails', headers=header, data=str(body).replace('\'', "\""))

            

            print(r)
            print(r.text)

            return JsonResponse({'response' : 1}, status = 200)
        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)
            

