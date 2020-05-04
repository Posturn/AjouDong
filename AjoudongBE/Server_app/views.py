import json

from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse

# Create your views here.
from rest_framework import viewsets
from rest_framework.generics import ListAPIView

from Server_app.models import userAccount, managerAccount

class login(View):
    def post(self, request):
        data = json.loads(request.body)

        try:
            if managerAccount.object.filter(mID = data['mID']).exists():
                managerAccount.object
                return JsonResponse({'message' : 1}, status=200)

            return HttpResponse(status = 400)

        except KeyError:
            return JsonResponse({'message' : "Invalid Keys"}, status = 400)

class signup(View):
    def post(self, request):
        data = json.loads(request.body)

        try:
            if userAccount.object.filter(uID = data['uID']).exists():
                return JsonResponse({'message' : 'Exists email'}, status=400)

            userAccount.object.create(
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
            return JsonResponse({'message' : 'Invalid Keys'}, status = 400)