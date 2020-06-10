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

from Server_app.models import UserAccount, ManagerAccount

from fcm_django.models import FCMDevice
from django.shortcuts import get_object_or_404

class login(View):
    @csrf_exempt
    def post(self, request):
        data = json.loads(request.body)
        print(data)
        try:
            if UserAccount.objects.filter(uID = data['uID'], uPW = data['uPW']).exists():
                user = UserAccount.objects.get(uID = data['uID'], uPW = data['uPW'])
                updateUserDevice(data['uToken'], user.uSchoolID)
                return JsonResponse({'response' : 1, 'message' : str(user.uSchoolID)}, status=200)
            elif ManagerAccount.objects.filter(mID = data['uID'], mPW = data['uPW']).exists():
                manager = ManagerAccount.objects.get(mID = data['uID'], mPW = data['uPW'])
                updateUserDevice(data['uToken'], manager.clubID_id)
                return JsonResponse({'response' : 2, 'message' : str(manager.clubID_id)}, status=200)
            else:
                return JsonResponse({'response' : -1}, status = 402)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 401)

def updateUserDevice(token, uID):
    fcm = FCMDevice.objects.filter(registration_id = token)
    if not fcm:
        FCMDevice.objects.create(registration_id=token, name=uID, type="android").save()
    else:
        fcm.update(name=uID)

