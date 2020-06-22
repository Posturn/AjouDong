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
                return JsonResponse({'response' : -1}, status = 200)

        except KeyError:
            return JsonResponse({'response' : -2}, status = 403)

def updateUserDevice(token, uID):
    fcm_device = FCMDevice.objects.filter(registration_id = token)
    fcm_id = FCMDevice.objects.filter(name = uID)

    fcm_check = FCMDevice.objects.filter(registration_id = token, name = uID)
    if fcm_check:
        return

    if not fcm_device and not fcm_id:
        FCMDevice.objects.create(registration_id=token, name=uID, type="android").save()
    elif not fcm_device and fcm_id:
        fcm_id.update(registration_id=token)
    elif fcm_device and not fcm_id:
        fcm_device.update(name=uID)
    else:
        fcm_id.delete()
        fcm_device.update(name=uID)

